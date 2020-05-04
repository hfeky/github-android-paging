package com.husseinelfeky.githubpaging.common.paging.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import kotlinx.android.synthetic.main.item_network_state.view.*

class NetworkStateViewHolder(
    view: View,
    retryAction: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        itemView.btn_retry.setOnClickListener { retryAction.invoke() }
    }

    fun bind(networkState: NetworkState) {
        with(itemView) {
            tv_error.text = if (networkState is NetworkState.Error) {
                networkState.error.localizedMessage
            } else {
                null
            }
            progress_bar.visibility = toVisibility(networkState == NetworkState.Loading)
            group_error.visibility = toVisibility(networkState != NetworkState.Loading)
        }
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }

    companion object {
        fun create(parent: ViewGroup, retryAction: () -> Unit): NetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)
            return NetworkStateViewHolder(
                view,
                retryAction
            )
        }
    }
} 
