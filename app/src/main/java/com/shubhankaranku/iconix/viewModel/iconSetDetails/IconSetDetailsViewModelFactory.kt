package com.shubhankaranku.iconix.viewModel.iconSetDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhankaranku.iconix.repository.IconifyRepository

class IconSetDetailsViewModelFactory(
    val app: Application,
    val repository: IconifyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IconSetDetailsViewModel(repository, app) as T
    }
}