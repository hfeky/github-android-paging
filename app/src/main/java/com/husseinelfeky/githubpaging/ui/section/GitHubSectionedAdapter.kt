package com.husseinelfeky.githubpaging.ui.section

import com.husseinelfeky.githubpaging.sectionedRecyclerView.Section
import com.husseinelfeky.githubpaging.sectionedRecyclerView.bases.BaseSectionAdapter
import com.husseinelfeky.githubpaging.ui.UserWithReposSection

class GitHubSectionedAdapter: BaseSectionAdapter() {

    private var reposSections = mutableListOf<UserWithReposSection>()

    override fun addSection(section: Section?): String {
        val tempSection = section as UserWithReposSection
        // Section not exist
        if (!reposSections.contains(section)) {
            reposSections.add(section)
            return super.addSection(section)
        }
        else {
            reposSections.map {
                // Section exists -> Get section tag
                if (it.id == tempSection.id) {
                    // Add the items of the passed section to the already existing one
                    it.addItemsToSection(tempSection.items)
                    updateSection(it)
                    return it.id
                }
            }
        }
        throw IllegalArgumentException("Invalid section")
    }
}
