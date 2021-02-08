package com.bornfight.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes

/**
 * Created by lleopoldovic on 14/01/2019.
 *
 *
 * Generic array adapter for creating dropdown/autocomplete items, holding custom set layout and custom set type.
 *
 * If you are using this with Spinner (dropdown), pass android.R.layout.simple_dropdown_item_1line as
 * resource Int in the constructor as well, otherwise, ignore this constructor param and pass your
 * custom layout in the bind method.
 */
abstract class GenericArrayAdapter<T> @JvmOverloads constructor(context: Context, @LayoutRes resource: Int = 0) :
    ArrayAdapter<T>(context, resource), Filterable {

    protected val listItems: MutableList<T> = ArrayList()

    private val valueFilter: ValueFilter by lazy { ValueFilter() }
    private val listItemsFiltered: MutableList<T> = ArrayList()
    private var query: String? = ""

    open fun setItems(listItems: List<T>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        this.listItemsFiltered.clear()
        this.listItemsFiltered.addAll(listItems)
        notifyDataSetChanged()
    }

    open fun addItem(listItem: T) {
        listItems.add(listItem)
    }

    open fun removeItem(listItem: T) {
        if (listItems.contains(listItem)) listItems.remove(listItem)
    }

    open fun getAllItems(): List<T> {
        return listItems
    }

    override fun getItem(position: Int): T {
        return listItemsFiltered[position]
    }

    override fun getCount(): Int {
        return listItemsFiltered.size
    }

    override fun getFilter(): Filter {
        return valueFilter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      var listItemView = convertView
      if (listItemView == null)
        listItemView = LayoutInflater
          .from(context)
          .inflate(getLayoutId(getItemViewType(position)), parent, false)

      listItemView?.setOnClickListener { onItemSelected(it, listItemsFiltered[position]) }

      return listItemView!!.apply { bind(this, listItemsFiltered[position], query) }
    }

    private inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            query = constraint.toString()

            if (constraint != null && constraint.isNotEmpty()) {
                val filterList = ArrayList<T>()
                for (i in 0 until listItems.size) {
                    if (filterBy(listItems[i], constraint.toString()) == true)
                        filterList.add(listItems[i])
                }
                results.count = filterList.size
                results.values = sort(filterList)
            } else {
                synchronized(this) {
                    results.count = listItemsFiltered.size
                    results.values = listOf(listItemsFiltered)
                }
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            listItemsFiltered.clear()
            listItemsFiltered.addAll(results?.values as List<T>)

            notifyDataSetChanged()
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun bind(itemView: View, data: T, query: String?)

    protected abstract fun filterBy(data: T, query: String?): Boolean?

    protected abstract fun sort(dataList: ArrayList<T>): List<T>

    protected abstract fun onItemSelected(view: View, data: T)
}
