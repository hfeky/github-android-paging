package com.husseinelfeky.githubpaging.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class PlaceholderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, @LayoutRes layoutResource: Int): PlaceholderViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(layoutResource, parent, false)
            return PlaceholderViewHolder(view)
        }
    }
}
