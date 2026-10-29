package edu.oregonstate.cs492.navigationgithubsearch.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.navigationgithubsearch.R
import edu.oregonstate.cs492.navigationgithubsearch.data.GitHubRepo
import edu.oregonstate.cs492.navigationgithubsearch.data.LoadingStatus

class GitHubSearchFragment : Fragment(R.layout.fragment_github_search) {
    private val viewModel: GitHubSearchViewModel by viewModels()

    private val repoListAdapter = GitHubRepoListAdapter(::onGitHubRepoClick)

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)
        val searchBtn: Button = view.findViewById(R.id.btn_search)

        searchErrorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = repoListAdapter

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResult ->
            repoListAdapter.updateRepoList(searchResult)
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) { loadingStatus ->
            loadingIndicator.visibility = View.INVISIBLE
            searchResultsListRV.visibility = View.INVISIBLE
            searchErrorTV.visibility = View.INVISIBLE
            when (loadingStatus) {
                LoadingStatus.SUCCESS -> {
                    searchResultsListRV.visibility = View.VISIBLE
                }
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                }

                LoadingStatus.ERROR -> {
                    searchErrorTV.visibility = View.VISIBLE
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                searchErrorTV.text = getString(
                    R.string.search_error,
                    error
                )
            }
        }

        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                viewModel.loadRepositorySearch(query)
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }

    private fun onGitHubRepoClick(repo: GitHubRepo) {
        Log.d("GitHubSearchFragment", "Repo clicked: ${repo.name}")
    }
}