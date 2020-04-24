package com.husseinelfeky.githubpaging.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.NetworkState
import com.husseinelfeky.githubpaging.common.Status
import kotlinx.android.synthetic.main.item_loading.view.*

class LoadingViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bindTo(networkState: NetworkState?) {
        itemView.progress_bar.visibility = toVisibility(networkState?.status == Status.RUNNING)
    }

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(view)
        }

        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
