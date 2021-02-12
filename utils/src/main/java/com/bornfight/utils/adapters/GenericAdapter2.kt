package com.bornfight.utils.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bornfight.utils.adapters.GenericAdapter2.GenericViewHolder
import java.util.*

/**
 * This is a generic adapter class made for [RecyclerView].
 * It offers general functions to work with collections
 *  ([setItems], [addItems], [getItems], [getItem], [getItemPosition]).
 *
 * When extending, you just have to implement [getViewHolder] and [createBinding].
 * Also, you will need to set a type T for the data which this adapter will hold and type R for the corresponding binding class,
 * and implement a [GenericViewHolder] (for [onCreateViewHolder]).
 */

abstract class GenericAdapter2<T, R> : RecyclerView.Adapter<GenericViewHolder<T>>() {

    protected var listItems: MutableList<T> = ArrayList()

    open fun setItems(listItems: List<T>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }

    open fun addItems(listItems: List<T>) {
        val index = this.listItems.size
        this.listItems.addAll(listItems)
        notifyItemRangeInserted(index, listItems.size)
    }

    open fun addItem(listItem: T) {
        listItems.add(listItem)
        notifyItemInserted(listItems.indexOf(listItem))
    }

    fun getItemsList(): List<T> {
        return listItems
    }

    fun getItem(position: Int): T {
        return listItems[position]
    }

    fun getItemPosition(item: T): Int {
        return listItems.indexOf(item)
    }

    fun clearItems() {
        listItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val binding = createBinding(parent)
        return getViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        (holder).bind(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    protected abstract fun getViewHolder(binding: R): GenericViewHolder<T>

    protected abstract fun createBinding(parent: ViewGroup): R

    abstract class GenericViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(data: T)
    }
}