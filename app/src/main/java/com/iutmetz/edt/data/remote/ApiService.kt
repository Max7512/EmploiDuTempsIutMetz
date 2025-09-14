package com.iutmetz.edt.data.remote

import com.iutmetz.edt.data.remote.model.Abbreviation
import com.iutmetz.edt.data.remote.model.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService { // cette interface permet de définir les différents endpoints de l'API, une instance sera automatiquement créé par le plugin retrofit et les fonctions seront définies également

    @GET("charge_edt.php") // l'annotation GET permet de définir le type de requête et le chemin de l'endpoint de l'API
    suspend fun getEdtTxt(
        @Query("promo") promo: String, // l'annotation Query permet de définir les paramètres de la requête par exemple charge_edt?promo=but3-ra
        @Query("date") date: String
    ): Response<String> // le type de réponse sera toujours sous la forme Reponse<Objet> où l'objet doit correspondre au modèle de la réponse serveur

    @GET("charge_abbreviations.php")
    suspend fun getAbbrevation(): Response<List<Data<List<Abbreviation>>>>
}