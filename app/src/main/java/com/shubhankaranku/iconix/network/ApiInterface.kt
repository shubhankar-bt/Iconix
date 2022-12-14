package com.shubhankaranku.iconix.network

import com.shubhankaranku.iconix.model.allIconsInIconSet.AllIconsInIconsSet
import com.shubhankaranku.iconix.model.iconDetails.IconDetails
import com.shubhankaranku.iconix.model.iconSetDetails.IconSetDetail
import com.shubhankaranku.iconix.model.publicIconSets.iconSets
import com.shubhankaranku.iconix.model.searchIcons.SearchIcons
import com.shubhankaranku.iconix.model.userDetails.UserDetails
import com.shubhankaranku.iconix.model.userIconsSets.UserIconSets
import com.shubhankaranku.iconix.utils.Constants.Companion.API_KEY
import com.shubhankaranku.iconix.utils.Constants.Companion.ICON_PER_PAGE
import com.shubhankaranku.iconix.utils.Constants.Companion.INITIAL_SEARCH_QUERY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {


    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/iconsets")
    suspend fun getPublicIconSets(
        @Query("count") iconSetPerPage: Int = ICON_PER_PAGE
    ): Response<iconSets>


    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/icons/search")
    suspend fun getSearchedIcons(
        @Query("query") searchQuery: String = INITIAL_SEARCH_QUERY,
        @Query("count") iconsPerPage: Int = ICON_PER_PAGE
    ): Response<SearchIcons>


    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/iconsets")
    suspend fun getIconSetDetails(
        @Query("iconset_id") iconSetID: Int
    ): Response<IconSetDetail>

    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/iconsets/{iconset_id}/icons")
    suspend fun getAllIconsInIconSet(
        @Path("iconset_id") iconSetID: Int,
        @Query("count") iconsPerPage: Int = ICON_PER_PAGE
    ): Response<AllIconsInIconsSet>

    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/users/{user_id}")
    suspend fun getUserDetails(
        @Path("user_id") user_id: Int
    ): Response<UserDetails>

    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/users/{user_id}/iconsets")
    suspend fun getUserIconSets(
        @Path("user_id") user_id: Int,
        @Query("count") iconsPerPage: Int = ICON_PER_PAGE
    ): Response<UserIconSets>

    @Headers("Authorization: Bearer $API_KEY")
    @GET("v4/icons/{icon_id}")
    suspend fun getIconDetails(
        @Path("icon_id") icon_id: Int,
    ): Response<IconDetails>


}