package com.iutmetz.edt.data.remote

import com.iutmetz.edt.data.remote.model.Abbreviation
import com.iutmetz.edt.data.remote.model.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("charge_edt.php")
    suspend fun getEdtTxt(
        @Query("promo") promo: String,
        @Query("date") date: String
    ): Response<String>

    @GET("charge_abbreviations.php")
    suspend fun getAbbrevation(): Response<List<Data<List<Abbreviation>>>>
}