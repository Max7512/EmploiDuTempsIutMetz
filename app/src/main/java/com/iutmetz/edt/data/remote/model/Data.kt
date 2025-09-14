package com.iutmetz.edt.data.remote.model

import com.google.gson.annotations.SerializedName

class Data<T>( // cette classe est utilisé dans la réponse des abbréviations qui contient un nom et une donnée variable, cette classe pourrait être réutilisé dans le futur si les résultats utilisent cette forme
    @SerializedName("name") val name: String, // l'annotation SerializedName permet de spécifier le nom de la clé dans la réponse serveur, ça peut être utile pour définir un nom différent localement
    @SerializedName("data") val data: T,
)