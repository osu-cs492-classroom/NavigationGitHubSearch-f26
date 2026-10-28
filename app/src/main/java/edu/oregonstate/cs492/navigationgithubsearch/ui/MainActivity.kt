package edu.oregonstate.cs492.navigationgithubsearch.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.navigationgithubsearch.R
import edu.oregonstate.cs492.navigationgithubsearch.data.LoadingStatus

class MainActivity : AppCompatActivity() {
    private val viewModel: GitHubSearchViewModel by viewModels()

    private val repoListAdapter = GitHubRepoListAdapter()

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchBoxET: EditText = findViewById(R.id.et_search_box)
        val searchBtn: Button = findViewById(R.id.btn_search)

        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        searchResultsListRV = findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(this)
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = repoListAdapter

        viewModel.searchResults.observe(this) { searchResult ->
            repoListAdapter.updateRepoList(searchResult)
        }

        viewModel.loadingStatus.observe(this) { loadingStatus ->
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

        viewModel.errorMessage.observe(this) { error ->
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
}