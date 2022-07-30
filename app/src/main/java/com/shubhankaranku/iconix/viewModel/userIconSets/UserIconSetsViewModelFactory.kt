package com.shubhankaranku.iconix.viewModel.userIconSets

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhankaranku.iconix.repository.IconifyRepository

class UserIconSetsViewModelFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserIconSetsViewModel(repository, app) as T
    }
}