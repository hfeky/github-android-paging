package com.husseinelfeky.githubpaging.common.paging.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import kotlinx.android.synthetic.main.item_paged_loading.view.*

class NetworkStateViewHolder(
    view: View,
    retryAction: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        itemView.btn_retry.setOnClickListener { retryAction.invoke() }
    }

    fun bind(networkState: NetworkState) {
        with(itemView) {
            if (networkState is NetworkState.Error) {
                tv_error.text = networkState.error.localizedMessage
            }
            progress_bar.visibility = toVisibility(networkState == NetworkState.Loading)
            tv_error.visibility = toVisibility(networkState != NetworkState.Loading)
            btn_retry.visibility = toVisibility(networkState != NetworkState.Loading)
        }
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }

    companion object {
        fun create(parent: ViewGroup, retryAction: () -> Unit): NetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_paged_loading, parent, false)
            return NetworkStateViewHolder(
                view,
                retryAction
            )
        }
    }
} 
