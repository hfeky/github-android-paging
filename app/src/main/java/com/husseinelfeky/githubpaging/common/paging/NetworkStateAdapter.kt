package com.husseinelfeky.githubpaging.common.paging

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.viewholder.NetworkStateViewHolder

class NetworkStateAdapter(
    private val retryAction: () -> Unit
) : RecyclerView.Adapter<NetworkStateViewHolder>() {

    var networkState: NetworkState = NetworkState.Loaded
        set(networkState) {
            if (field != networkState) {
                val displayOldItem = displayNetworkStateAsItem(field)
                val displayNewItem = displayNetworkStateAsItem(networkState)

                if (displayOldItem && !displayNewItem) {
                    notifyItemRemoved(0)
                } else if (displayNewItem && !displayOldItem) {
                    notifyItemInserted(0)
                } else if (displayOldItem && displayNewItem) {
                    notifyItemChanged(0)
                }
                field = networkState
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkStateViewHolder {
        return NetworkStateViewHolder.create(parent, retryAction)
    }

    override fun onBindViewHolder(holder: NetworkStateViewHolder, position: Int) {
        holder.bind(networkState)
    }

    override fun getItemViewType(position: Int): Int = 0

    override fun getItemCount(): Int = if (displayNetworkStateAsItem(networkState)) 1 else 0

    private fun displayNetworkStateAsItem(networkState: NetworkState): Boolean {
        return this.networkState is NetworkState.Loading || this.networkState is NetworkState.Error
    }
}