package com.jibergroup.gitprofile.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jibergroup.domain.entities.User
import com.jibergroup.gitprofile.R

class SearchAdapter(private val users: MutableList<User>,
                    private val onClickListener: ((item: User) -> Unit)? = null
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_list, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val story = users[position]
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}