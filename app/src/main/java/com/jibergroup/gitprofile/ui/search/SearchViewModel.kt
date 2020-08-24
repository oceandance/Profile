package com.jibergroup.gitprofile.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jibergroup.domain.entities.User
import com.jibergroup.domain.usecases.GetUsersUseCase
import com.jibergroup.domain.utils.RequestResult
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel(){

    private val _usersLiveData = MutableLiveData<List<User>>()
    val usersLiveData : LiveData<List<User>>
    get() = _usersLiveData
    fun onSearch(searchText: String){
        viewModelScope.launch {
            when(val requestResult = getUsersUseCase.invoke(q = searchText)){
                is RequestResult.Success ->{
                    _usersLiveData.value = requestResult.result
                }
                is RequestResult.Error -> {
                }
            }
        }
    }
}