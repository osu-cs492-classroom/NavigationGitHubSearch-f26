package edu.oregonstate.cs492.navigationgithubsearch.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import edu.oregonstate.cs492.navigationgithubsearch.R

class GitHubRepoDetailFragment : Fragment(R.layout.fragment_github_repo_detail) {
    private val args: GitHubRepoDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoNameTV = view.findViewById<TextView>(R.id.tv_repo_name)
        val repoStarsTV = view.findViewById<TextView>(R.id.tv_repo_stars)
        val repoDescriptionTV = view.findViewById<TextView>(R.id.tv_repo_description)

        repoNameTV.text = args.repo.name
        repoStarsTV.text = args.repo.stars.toString()
        repoDescriptionTV.text = args.repo.description

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.github_repo_detail_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_open_in_browser -> {
                            openInBrowser()
                            true
                        }
                        R.id.action_share -> {
                            share()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )
    }

    private fun openInBrowser() {
        val url = Uri.parse(args.repo.url)
        val intent = Intent(Intent.ACTION_VIEW, url)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("GitHubRepoDetailFragment", "No browser found to open the repo URL.")
            e.printStackTrace()
        }
    }

    private fun share() {
        val shareText = getString(
            R.string.share_text,
            args.repo.name,
            args.repo.url
        )
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }
}