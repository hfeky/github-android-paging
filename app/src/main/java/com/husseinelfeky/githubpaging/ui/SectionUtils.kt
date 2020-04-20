package com.husseinelfeky.githubpaging.ui

import androidx.annotation.LayoutRes
import com.husseinelfeky.githubpaging.R
import com.husseinelfeky.githubpaging.sectionedRecyclerView.SectionParameters

@Suppress("SameParameterValue")
object SectionUtils {

    fun getSectionParams(): SectionParameters {
        return getSectionWithParamsBuilderWith(headerLayout = R.layout.item_user, childLayout = R.layout.item_repo)
               // .emptyResourceId()
                .build()
    }

    private fun getSectionWithParamsBuilderWith(@LayoutRes headerLayout: Int, @LayoutRes childLayout: Int): SectionParameters.Builder {
        return SectionParameters.builder()
                .headerResourceId(headerLayout)
                .itemResourceId(childLayout)
    }
}