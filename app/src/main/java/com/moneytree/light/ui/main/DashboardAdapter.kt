package com.moneytree.light.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.light.R
import com.moneytree.light.pojo.Account
import kotlinx.android.synthetic.main.account_row.view.*
import kotlinx.android.synthetic.main.institution_card.view.*

class DashboardAdapter(
    var context: Context,
    private var accountsByInstitution: List<Pair<String, List<Account>>>
) : RecyclerView.Adapter<DashboardAdapter.InstitutionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.institution_card, parent, false)
        return InstitutionViewHolder(view, context)
    }

    fun updateData(newData: List<Pair<String, List<Account>>>) {
        accountsByInstitution = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return accountsByInstitution.size
    }


    override fun onBindViewHolder(holder: InstitutionViewHolder, position: Int) {
        holder.bind(accountsByInstitution[position])
    }

    class InstitutionViewHolder(private val view: View, private val context: Context) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: Pair<String, List<Account>>) {
            view.tv_institution_name.text = item.first
            view.ll_list.removeAllViews()

            if (item.second.isNotEmpty()) {
                for (i in 0 until item.second.size) {
                    val newEntry =
                        LayoutInflater.from(context)
                            .inflate(R.layout.account_row, view.ll_list, false)

                    /* update fields */
                    newEntry.tv_account_name.text = item.second[i].name
                    newEntry.tv_account_balance.text = String.format(
                        context.resources.getString(R.string.currency_balance),
                        item.second[i].currency, item.second[i].current_balance
                    )

                    view.ll_list.addView(newEntry)

                    /* add separator */
                    if (i < item.second.size - 1) {
                        view.ll_list.addView(
                            LayoutInflater.from(context)
                                .inflate(R.layout.horizontal_separator, view.ll_list, false)
                        )
                    }
                }
            }
        }
    }
}
