package com.husseinelfeky.githubpaging.ui.adapter

import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.Section
import com.husseinelfeky.githubpaging.common.sectionedrecyclerview.bases.BaseSectionAdapter
import com.husseinelfeky.githubpaging.persistence.entities.UserWithRepos

class UserWithReposAdapter : BaseSectionAdapter() {

    private var reposSections = mutableListOf<UserWithReposSection>()

    override fun addSection(section: Section?): String {
        val tempSection = section as UserWithReposSection
        // Section does not exist.
        if (!reposSections.contains(section)) {
            reposSections.add(section)
            return super.addSection(section)
        } else {
            reposSections.map {
                // Section exists -> Get section tag
                if (it.id == tempSection.id) {
                    // Add the items of the passed section to the already existing one.
                    it.addItemsToSection(tempSection.items)
                    updateSection(it)
                    return it.id
                }
            }
        }
        throw IllegalArgumentException("Invalid section")
    }

    fun appendList(usersWithRepos: List<UserWithRepos>) {
        usersWithRepos.forEach { userWithRepos ->
            addSection(UserWithReposSection(userWithRepos))
        }
        notifyDataSetChanged()
    }
}
