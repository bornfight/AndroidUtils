package com.bornfight.utils

import io.reactivex.Observable
import java.net.UnknownHostException


/**
 * Created by tomislav on 01/03/2018.
 */
class PaginatedData<T>(val limit: Int, private val load: (limit: Int, page: Int) -> Observable<List<T>>) {

    private var page = 1
    private var loadingEnabled = true

    private val oldItems: MutableList<T> = mutableListOf()
    private val items: MutableList<T> = mutableListOf()

    fun loadMore(): Observable<List<T>> {
        if (loadingEnabled) {
            loadingEnabled = false
            return load(true)
        } else {
            return Observable.empty()
        }
    }

    private fun load(more: Boolean): Observable<List<T>> {
        return if (more) {
            load(limit, page)
        } else {
            load((page - 1) * limit, 1)
        }.doOnComplete {
            // if there was a change in items (new items loaded from server and the first call had at least #limit items
            // request was successful and pagination will continue
            // if the items remained the same or the first call had less than #limit items, all items were loaded and the
            // pagination will stop
            if (oldItems != items && items.size >= limit) {
                completed(true, true)
            } else {
                completed(false, false)
            }
            oldItems.clear()
            oldItems.addAll(items)
        }
            .doOnError {
                // if local error occurred, enable further pagination, but don't increase current page, try loading again
                if (it.cause is UnknownHostException || it is UnknownHostException) {
                    completed(true, true)
                } else {
                    completed(true, false)
                }
            }
            .doOnDispose {
                // if user cancelled request while loading from API, the oldItems and items will be unsynced.
                // This will cause PaginatedData to get stuck in doOnComplete next time it's called
                // To solve this, when subscription is disposed, oldItems & items are compared to check if they are synced.
                // If there is some difference because API call didn't complete, then the latest items which were loaded from database
                // are removed
                if (oldItems != items) {
                    val diffItems = items.filter { !oldItems.contains(it) }
                    oldItems.removeAll(diffItems)
                }
            }
            .map { newItems ->
                if (page == 1) {
                    items.clear()
                    items.addAll(newItems)
                } else {
                    items.addAll(newItems.toMutableList().apply { removeAll(items) })
                }
                items
            }
    }

    private fun completed(enableLoading: Boolean, increasePage: Boolean) {
        loadingEnabled = enableLoading

        if (increasePage) {
            page++
        }
    }

    fun reachedEnd(): Boolean {
        return !loadingEnabled
    }

    fun reset() {
        page = 1
        loadingEnabled = true
    }

    fun getData(): List<T> {
        return items
    }


    fun refreshData(): Observable<List<T>> {
        if (loadingEnabled) {
            loadingEnabled = false
            oldItems.clear()
            items.clear()
            return load(false)
        } else {
            return Observable.empty()
        }
    }

    fun replaceItem(oldItem: T, newItem: T): Observable<List<T>> {
        return Observable.fromCallable {
            val index = items.indexOf(oldItem)
            if (index != -1) {
                items.removeAt(index)
                items.add(index, newItem)
            }
            items
        }
    }

    fun removeItem(item: T): Observable<List<T>> {
        return Observable.fromCallable {
            val index = items.indexOf(item)
            if (index != -1) {
                items.remove(item)
            }
            items
        }
    }

    fun addItem(position: Int, item: T): Observable<List<T>> {
        return Observable.fromCallable {
            items.add(position, item)
            items
        }
    }

    val isLoadingEnabled: Boolean
        get() = !loadingEnabled


}