package com.moneytree.light.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.light.R
import com.moneytree.light.Status
import com.moneytree.light.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.dashboard_fragment.*

class DashboardFragment : Fragment() {
    private lateinit var mViewModel: DashboardViewModel
    private lateinit var dashboardRecyclerView: RecyclerView
    private lateinit var dashboardAdapter: DashboardAdapter

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dashboardRecyclerView = rv_dashboard
        dashboardAdapter = DashboardAdapter(context!!, mViewModel.accountsByInstitution)

        dashboardRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        dashboardRecyclerView.adapter = dashboardAdapter

        mViewModel.responseStatus.observe(
            this,
            Observer { consumeResponse(mViewModel.responseStatus.value) })
    }

    private fun updateUI() {
        /* update total card */
        var total = 0.0f
        for (i in mViewModel.accounts.indices) {
            total += mViewModel.accounts[i].current_balance_in_base
        }
        tv_total_value.text = String.format(
            requireActivity().resources.getString(R.string.currency_balance),
            "JPY", total
        )

        /* update recycler view */
        dashboardAdapter.updateData(mViewModel.accountsByInstitution)
    }


    private fun consumeResponse(status: Status?) {
        when (status) {
            Status.LOADING -> {
                progressBar.visibility = View.VISIBLE
                toolbar.visibility = View.VISIBLE
                rv_dashboard.visibility = View.GONE
            }

            Status.SUCCESS -> {
                progressBar.visibility = View.GONE
                toolbar.visibility = View.VISIBLE
                rv_dashboard.visibility = View.VISIBLE
                updateUI()
                /*Toast.makeText(activity, "Refreshed data", Toast.LENGTH_SHORT).show()*/
            }

            Status.ERROR -> {
                progressBar.visibility = View.GONE
                toolbar.visibility = View.VISIBLE
                rv_dashboard.visibility = View.VISIBLE
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.e("consumeResponse", "Status not set")
            }
        }
    }
}
