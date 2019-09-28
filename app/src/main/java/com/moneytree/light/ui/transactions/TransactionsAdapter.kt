package com.moneytree.light.ui.transactions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.light.R
import com.moneytree.light.data.Transaction
import kotlinx.android.synthetic.main.transaction_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionsAdapter(
    var context: Context,
    private val combinedList: MutableList<Comparable<*>>,
    private var mViewModel: TransactionsViewModel
) : RecyclerView.Adapter<TransactionsAdapter.BaseViewHolder<*>>() {

    companion object {
        const val TYPE_HEADER = 1001
        const val TYPE_TRANSACTION = 1002
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        context = parent.context


        return when (viewType) {
            TYPE_HEADER -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.transaction_header, parent, false)
                HeaderViewHolder(view, context)
            }
            TYPE_TRANSACTION -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.transaction_row, parent, false)
                TransactionViewHolder(view, context, mViewModel)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun refresh() {
        combinedList.clear()
        combinedList.addAll(mViewModel.getCombinedDataForAdaptor())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return combinedList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (combinedList[position]) {
            is String -> TYPE_HEADER
            is Transaction -> TYPE_TRANSACTION
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = combinedList[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind(element as String)
            is TransactionViewHolder -> holder.bind(element as Transaction)
            else -> throw IllegalArgumentException()
        }
    }


    /**
     * View holders
     */
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class HeaderViewHolder(private val view: View, private val context: Context) :
        BaseViewHolder<String>(view) {

        override fun bind(item: String) {
            view.tv_month_title.text = item
        }
    }

    class TransactionViewHolder(
        private val view: View,
        private val context: Context,
        private val viewModel: TransactionsViewModel
    ) :
        BaseViewHolder<Transaction>(view) {

        override fun bind(item: Transaction) {

            val calendar = Calendar.getInstance()
            val sdf =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            calendar.time = sdf.parse(item.date)!!

            view.tv_month_title.text = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
            view.iv_category.setImageResource(getCategoryResource(item.category_id))
            view.tv_transaction_info.text = item.description
            view.tv_transaction_value.text = String.format(
                context.resources.getString(R.string.currency_balance),
                viewModel.selectedAccount.currency, item.amount
            )
        }

        private fun getCategoryResource(category: Int): Int {
            return when (category) {
                111 -> R.drawable.ic_coffee
                112 -> R.drawable.ic_restaurant
                192 -> R.drawable.ic_groceries
                else -> R.drawable.ic_groceries /* default to grocery */
            }
        }
    }
}
