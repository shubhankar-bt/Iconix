package com.shubhankaranku.iconix.viewModel.iconDetails

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shubhankaranku.iconix.IconifyApplication
import com.shubhankaranku.iconix.model.iconDetails.IconDetails
import com.shubhankaranku.iconix.repository.IconifyRepository
import com.shubhankaranku.iconix.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class IconDetailsViewModel(
    private val repository: IconifyRepository,
    app: Application
) : AndroidViewModel(app) {

    val iconDetails: MutableLiveData<Resource<IconDetails>> = MutableLiveData()


    fun getIconDetails(icon_id: Int) = viewModelScope.launch {
        safeIconDetailsCall(icon_id)
    }

    private suspend fun safeIconDetailsCall(icon_id: Int) {
        iconDetails.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getIconDetails(icon_id)
                iconDetails.postValue(handleIconDetailsResponse(response))
            } else {
                iconDetails.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> iconDetails.postValue(Resource.Error("Network Failure"))
                else -> iconDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleIconDetailsResponse(response: Response<IconDetails>): Resource<IconDetails>? {
        if (response.isSuccessful) {
            return Resource.Success(response.body())
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<IconifyApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo.run {
                return when (this!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

}