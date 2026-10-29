package edu.oregonstate.cs492.navigationgithubsearch.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.oregonstate.cs492.navigationgithubsearch.R

class GitHubRepoDetailFragment : Fragment(R.layout.fragment_github_repo_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoNameTV = view.findViewById<TextView>(R.id.tv_repo_name)
        val repoStarsTV = view.findViewById<TextView>(R.id.tv_repo_stars)
        val repoDescriptionTV = view.findViewById<TextView>(R.id.tv_repo_description)

        repoNameTV.text = "Dummy repo name"
        repoStarsTV.text = "8192"
        repoDescriptionTV.text = "This is a description of the repo."
    }
}