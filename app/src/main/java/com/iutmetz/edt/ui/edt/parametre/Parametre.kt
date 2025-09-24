package com.iutmetz.edt.ui.edt.parametre;

import androidx.viewbinding.ViewBinding
import com.iutmetz.edt.data.local.entity.SessionEntity

abstract class Parametre( // cette classe permet de créer un modèle pour afficher les cours de l'emploi du temps, elle est abstraite car elle ne peut pas être instanciée
    protected val session: SessionEntity
) {
    protected abstract val binding: ViewBinding // on définit un binding qui est un objet qui permet de lier les éléments de la vue avec le code, ce binding peut être de n'importe quel layout

    abstract fun initView()
}
