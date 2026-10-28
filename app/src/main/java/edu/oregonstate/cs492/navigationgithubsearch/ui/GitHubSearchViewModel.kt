package edu.oregonstate.cs492.navigationgithubsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.navigationgithubsearch.data.GitHubRepo
import edu.oregonstate.cs492.navigationgithubsearch.data.GitHubReposRepository
import edu.oregonstate.cs492.navigationgithubsearch.data.GitHubService
import edu.oregonstate.cs492.navigationgithubsearch.data.LoadingStatus
import kotlinx.coroutines.launch

class GitHubSearchViewModel: ViewModel() {
    private val repository = GitHubReposRepository(GitHubService.create())
    private val _searchResults = MutableLiveData<List<GitHubRepo>?>(null)
    val searchResults: LiveData<List<GitHubRepo>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadRepositorySearch(query: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadRepositorySearch(query)
            _searchResults.value = result.getOrNull()
            _errorMessage.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}