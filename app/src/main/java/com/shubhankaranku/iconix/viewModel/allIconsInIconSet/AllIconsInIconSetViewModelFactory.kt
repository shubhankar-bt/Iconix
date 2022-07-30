package com.shubhankaranku.iconix.viewModel.allIconsInIconSet

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhankaranku.iconix.repository.IconifyRepository

class AllIconsInIconSetViewModelFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllIconsInIconSetViewModel(repository, app) as T
    }
}