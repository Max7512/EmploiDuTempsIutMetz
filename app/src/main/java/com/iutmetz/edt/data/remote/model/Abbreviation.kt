package com.iutmetz.edt.data.remote.model

import com.google.gson.annotations.SerializedName

class Abbreviation( // cette classe correspond au modèle de la réponse serveur d'une abbréviation
    @SerializedName("mod_lib") val mod_lib: String, // l'annotation SerializedName permet de spécifier le nom de la clé dans la réponse serveur, ça peut être utile pour définir un nom différent localement
    @SerializedName("mod_code") val mod_code: String,
) {
}