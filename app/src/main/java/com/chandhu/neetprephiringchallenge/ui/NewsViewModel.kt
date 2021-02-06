package com.chandhu.neetprephiringchallenge.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandhu.neetprephiringchallenge.datamodels.NewsResponse
import com.chandhu.neetprephiringchallenge.repository.NewsRepository
import com.chandhu.neetprephiringchallenge.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    val news : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1

    init {
        getNews("us")
    }

    private fun getNews(countryCode : String) = viewModelScope.launch {
        news.postValue(Resource.Loading())
        val response = newsRepository.getNews(countryCode,newsPage)
        news.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}