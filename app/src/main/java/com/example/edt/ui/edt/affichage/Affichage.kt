package com.example.edt.ui.edt.affichage;

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class Affichage(
    protected val lifecycleScope: LifecycleCoroutineScope
) {
    protected abstract val binding: Any
    abstract fun effacer()
    abstract fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>)
    fun afficher(edt: List<CoursEntity>, abbreviations: List<AbbreviationEntity>, groupe: String) {
        effacer()
        filtrerCoursParGroupe(edt, groupe).forEach { cours ->
            afficherCours(cours, abbreviations)
        }
    }

    fun filtrerCoursParGroupe(edt: List<CoursEntity>, groupe: String): List<CoursEntity> {
        val tabGroupeSelect = groupe.split(".")
        return edt.filter {
            it.groupe.split(".").let { tabGroupeCours ->
                for (i in 0..<tabGroupeCours.size) {
                    if (tabGroupeCours[i].isNotEmpty() && tabGroupeCours[i] != tabGroupeSelect[i]) return@let false
                }
                return@let true
            }
        }
    }
}
