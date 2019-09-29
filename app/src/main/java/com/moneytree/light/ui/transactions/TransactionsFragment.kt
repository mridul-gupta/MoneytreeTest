package com.moneytree.light.ui.transactions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moneytree.light.ACCOUNT_DASHBOARD_SCREEN
import com.moneytree.light.R
import com.moneytree.light.Status
import com.moneytree.light.obtainViewModel
import com.moneytree.light.ui.AccountsActivity
import kotlinx.android.synthetic.main.simple_toolbar.view.*
import kotlinx.android.synthetic.main.transactions_fragment.*


class TransactionsFragment : Fragment() {
    private lateinit var mViewModel: TransactionsViewModel
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionsAdapter: TransactionsAdapter


    companion object;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.transactions_fragment, container, false)
        mViewModel = obtainViewModel(TransactionsViewModel::class.java)

        mViewModel.selectedAccount = arguments?.getParcelable("ACCOUNT")!!
        mViewModel.getAccountData(mViewModel.selectedAccount.id, false)

        mViewModel.responseStatus.observe(
            this,
            Observer { consumeResponse(mViewModel.responseStatus.value) })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transactionsRecyclerView = rv_transactions
        transactionsAdapter = TransactionsAdapter(requireActivity(), mViewModel.getCombinedDataForAdaptor(), mViewModel)

        transactionsRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        transactionsRecyclerView.adapter = transactionsAdapter
    }

    private fun updateUI() {
        tv_account_name.text = mViewModel.selectedAccount.name

        tv_account_balance.text = String.format(
            requireActivity().resources.getString(R.string.currency_balance),
            mViewModel.selectedAccount.currency, mViewModel.selectedAccount.current_balance
        )

        if(mViewModel.transactionList.isEmpty())
            tv_no_transactions.visibility = View.VISIBLE
        else
            tv_no_transactions.visibility = View.GONE

        transactionsAdapter.refresh()
    }

    private fun setupToolbar() {
        val tbToolbar : Toolbar = toolbar as Toolbar
        tbToolbar.setNavigationIcon(R.drawable.ic_back)
        tbToolbar.setNavigationOnClickListener { onBackPressed() }
        tbToolbar.iv_add_account.visibility = View.GONE
        tbToolbar.iv_profile.visibility = View.GONE
        tbToolbar.toolbar_name.text = mViewModel.selectedAccount.institution
    }

    private fun consumeResponse(status: Status?) {
        when (status) {
            Status.LOADING -> {
                progressBar.visibility = View.VISIBLE
                toolbar.visibility = View.VISIBLE
                rv_transactions.visibility = View.GONE
            }

            Status.SUCCESS -> {
                progressBar.visibility = View.GONE
                toolbar.visibility = View.VISIBLE
                rv_transactions.visibility = View.VISIBLE
                updateUI()
                /*Toast.makeText(activity, "Refreshed data", Toast.LENGTH_SHORT).show()*/
            }

            Status.ERROR -> {
                progressBar.visibility = View.GONE
                toolbar.visibility = View.VISIBLE
                rv_transactions.visibility = View.VISIBLE
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()

                /* go back */
                onBackPressed()
            }

            else -> {
                Log.e("consumeResponse", "Status not set")
            }
        }
    }


    private fun onBackPressed() {
        mViewModel.responseStatus.value = Status.IDLE
        (activity as AccountsActivity).loadFragment(ACCOUNT_DASHBOARD_SCREEN, null)
    }
}
