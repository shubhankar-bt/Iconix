package com.shubhankaranku.iconix.repository

import com.shubhankaranku.iconix.network.RetrofitInstance


class IconifyRepository {

    suspend fun getAllPublicIconSets() =
        RetrofitInstance.apiInterface.getPublicIconSets()

    suspend fun getAllSearchedIcons(searchQuery: String) =
        RetrofitInstance.apiInterface.getSearchedIcons(searchQuery)

    suspend fun getIconSetDetails(iconSetId: Int) =
        RetrofitInstance.apiInterface.getIconSetDetails(iconSetId)

    suspend fun getAllIconsInIconSet(iconSetId: Int) =
        RetrofitInstance.apiInterface.getAllIconsInIconSet(iconSetId)

    suspend fun getUserDetails(user_id: Int) =
        RetrofitInstance.apiInterface.getUserDetails(user_id)

    suspend fun getUserIconSets(user_id: Int) =
        RetrofitInstance.apiInterface.getUserIconSets(user_id)

    suspend fun getIconDetails(icon_id: Int) =
        RetrofitInstance.apiInterface.getIconDetails(icon_id)


}