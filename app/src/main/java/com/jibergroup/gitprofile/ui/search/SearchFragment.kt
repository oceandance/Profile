package com.jibergroup.gitprofile.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jibergroup.gitprofile.R
import com.jibergroup.gitprofile.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(R.layout.fragment_search){

    private val mViewModel: SearchViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        //deleted logs
        mViewModel.onSearch("mukhit")
        mViewModel.usersLiveData.observe(viewLifecycleOwner, Observer {

        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = SearchView(
            (activity as MainActivity).supportActionBar!!.themedContext
        )

        item.actionView = searchView
        searchView.queryHint = "Input type..."
        searchView.isIconified = false
        searchView.isIconifiedByDefault = false
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView.setOnClickListener{

        }
    }
}