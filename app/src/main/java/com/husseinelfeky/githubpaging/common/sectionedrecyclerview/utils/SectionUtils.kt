package com.husseinelfeky.githubpaging.common.sectionedrecyclerview.utils

import androidx.annotation.LayoutRes
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.SectionParameters

object SectionUtils {

    fun getSectionWithParamsBuilderWith(@LayoutRes headerLayout: Int, @LayoutRes childLayout: Int): SectionParameters.Builder {
        return SectionParameters.builder()
            .headerResourceId(headerLayout)
            .itemResourceId(childLayout)
    }
}
