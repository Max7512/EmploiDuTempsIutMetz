package com.iutmetz.edt.data.common;

class Promo(public var code: String, public var nom: String) { // cette classe sert à associer un objet promo et afficher le nom dans le spinner (liste déroulante)
    override fun toString(): String {
        return nom
    }
}
