package com.moneytree.light.ui.accountlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.light.ACCOUNT_TRANSACTIONS_SCREEN
import com.moneytree.light.R
import com.moneytree.light.Status
import com.moneytree.light.obtainViewModel
import com.moneytree.light.ui.AccountsActivity
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.simple_toolbar.view.*

class DashboardFragment : Fragment() {
    private lateinit var mViewModel: DashboardViewModel
    private lateinit var dashboardRecyclerView: RecyclerView
    private lateinit var dashboardAdapter: DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = obtainViewModel(DashboardViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dashboardRecyclerView = rv_dashboard
        dashboardAdapter = DashboardAdapter(context!!, mViewModel.accountsByInstitution, mViewModel)

        dashboardRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        dashboardRecyclerView.adapter = dashboardAdapter

        mViewModel.responseStatus.observe(
            this,
            Observer { consumeResponse(mViewModel.responseStatus.value) })

        mViewModel.openAccountDetailEvent.observe(this, Observer { account ->
            val bundle = Bundle()
            bundle.putParcelable("ACCOUNT", account)
            (activity as AccountsActivity).loadFragment(ACCOUNT_TRANSACTIONS_SCREEN, bundle)
        })
    }

    private fun setupToolbar() {
        val tbToolbar: Toolbar = toolbar as Toolbar
        tbToolbar.iv_add_account.visibility = View.VISIBLE
        tbToolbar.iv_profile.visibility = View.VISIBLE
        tbToolbar.toolbar_name.text = getString(R.string.user_greeting)

        tbToolbar.iv_add_account.setOnClickListener {
            Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
        tbToolbar.iv_profile.setOnClickListener {
            Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        /* update total card */
        var total = 0.0f
        for (i in mViewModel.accounts.indices) {
            total += mViewModel.accounts[i].current_balance_in_base
        }
        tv_account_balance.text = String.format(
            requireActivity().resources.getString(R.string.currency_balance),
            getString(R.string.default_currency), total
        )

        if (mViewModel.accounts.isEmpty())
            tv_no_accounts.visibility = View.VISIBLE
        else
            tv_no_accounts.visibility = View.GONE

        /* update recycler view */
        dashboardAdapter.refresh(mViewModel.accountsByInstitution)
    }


    private fun consumeResponse(status: Status?) {
        when (status) {
            Status.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }

            Status.SUCCESS -> {
                progressBar.visibility = View.GONE
                updateUI()
                /*Toast.makeText(activity, "Refreshed data", Toast.LENGTH_SHORT).show()*/
            }

            Status.ERROR -> {
                progressBar.visibility = View.GONE
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.e("consumeResponse", "Status not set")
            }
        }
    }
}
