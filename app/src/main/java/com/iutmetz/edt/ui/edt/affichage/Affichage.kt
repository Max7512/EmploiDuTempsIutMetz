package com.iutmetz.edt.ui.edt.affichage;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewbinding.ViewBinding
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.local.entity.CoursEntity

abstract class Affichage( // cette classe permet de créer un modèle pour afficher les cours de l'emploi du temps, elle est abstraite car elle ne peut pas être instanciée
    protected val inflater: LayoutInflater, // on utilise un inflater pour créer la vue de l'affichage
    protected val parent: ViewGroup, // on spécifie un parent pour ajouter la vue au parent
    protected val lifecycleScope: LifecycleCoroutineScope // on utilise un scope de coroutine pour gérer les tâches asynchrones ce qui est nécessaire pour afficher les cours de l'emploi du temps
) {
    protected abstract val binding: ViewBinding // on définit un binding qui est un objet qui permet de lier les éléments de la vue avec le code, ce binding peut être de n'importe quel layout
    abstract fun effacer() // cette fonction doit être implémentée dans les classes filles pour permettre d'effacer les cours
    abstract fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>) // cette fonction doit être implémentée dans les classes filles pour permettre d'afficher un cours
    fun afficher(edt: List<CoursEntity>, abbreviations: List<AbbreviationEntity>, groupe: String) { // cette fonction permet d'afficher les cours de l'emploi du temps en fcontion d'un groupe
        effacer() // on efface les cours
        filtrerCoursParGroupe(edt, groupe).forEach { cours -> // les cours sont filtrés par groupe et sont ensuite affichés
            afficherCours(cours, abbreviations)
        }
    }

    fun filtrerCoursParGroupe(edt: List<CoursEntity>, groupe: String): List<CoursEntity> { // cette fonction permet de filtrer les cours par groupe
        return edt.filter { // on filtre les cours selon une condition
                it.groupe.split("+").forEach { // on sépare les groupes dans le cas des SAE ou autres cours incluant plusieurs groupe précis
                    if (groupe.substring(0, it.length) == it) return@filter true // on vérifie si le groupe du cours inclus le groupe passé en paramètre
                }
            return@filter false
            }
    }
}
