package com.bornfight.utils

import io.reactivex.Observable
import java.net.UnknownHostException


/**
 * Created by tomislav on 01/03/2018.
 * Updated by tomislav on 24/05/2019.
 */
class PaginatedData<T>(val limit: Int, private val load: (limit: Int, page: Int) -> Observable<List<T>>) {

    private var page = 1
    var reachedEnd = false
        private set

    var loadingEnabled = true
        private set
        get() = field && !reachedEnd

    private val oldItems: MutableList<T> = mutableListOf()
    private val items: MutableList<T> = mutableListOf()

    /**
     * Call to load next page of data
     */
    fun loadMore(): Observable<List<T>> {
        if (loadingEnabled) {
            loadingEnabled = false
            return load(true)
        } else {
            return Observable.empty()
        }
    }

    /**
     * Refresh only existing data (all loaded pages)
     */
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
                completed(enableLoading = true, increasePage = more)
            } else {
                completed(enableLoading = false, increasePage = false, reachedEnd = true)
            }
            oldItems.clear()
            oldItems.addAll(items)
        }
            .doOnError {
                // if local error occurred, enable further pagination, but don't increase current page, try loading again
                if (it.cause is UnknownHostException || it is UnknownHostException) {
                    completed(enableLoading = true, increasePage = more)
                } else {
                    completed(enableLoading = true, increasePage = false)
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
                completed(enableLoading = true, increasePage = false)
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

    private fun completed(enableLoading: Boolean, increasePage: Boolean, reachedEnd: Boolean = this.reachedEnd) {
        this.loadingEnabled = enableLoading
        this.reachedEnd = reachedEnd
        if (increasePage) {
            page++
        }
    }

    @Deprecated(
        message = "This method should not be used to check if feed reached end.",
        replaceWith = ReplaceWith("reachedEnd")
    )
    fun reachedEnd(): Boolean {
        return reachedEnd
    }

    fun reset() {
        page = 1
        loadingEnabled = true
    }

    fun getData(): List<T> {
        return items
    }

    /**
     * Replaces one item and returns new Observable with updated list.
     * If the oldItem doesn't exists, the list will not be updated
     */
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

    /**
     * Removes one item and returns new Observable with updated list
     */
    fun removeItem(item: T): Observable<List<T>> {
        return Observable.fromCallable {
            val index = items.indexOf(item)
            if (index != -1) {
                items.remove(item)
            }
            items
        }
    }

    /**
     * Adds one item and returns new Observable with updated list
     */
    fun addItem(position: Int, item: T): Observable<List<T>> {
        return Observable.fromCallable {
            items.add(position, item)
            items
        }
    }
}