package com.example.edt.ui.edt.affichage;

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity

abstract class Affichage(
    protected val lifecycleScope: LifecycleCoroutineScope) {
    protected abstract val binding: Any

    abstract fun effacer()
    abstract fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>)

    fun afficher(edt: List<CoursEntity>, abbreviations: List<AbbreviationEntity>) {
        effacer()
        edt.forEach { cours ->
            afficherCours(cours, abbreviations)
        }
    }
}
