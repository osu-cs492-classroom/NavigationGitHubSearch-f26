package edu.oregonstate.cs492.navigationgithubsearch.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubRepoSearchResults(
    val items: List<GitHubRepo>
)
