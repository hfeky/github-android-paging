package com.husseinelfeky.githubpaging.paging.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class DummyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, @LayoutRes layoutResource: Int): DummyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(layoutResource, parent, false)
            return DummyViewHolder(view)
        }
    }
}
