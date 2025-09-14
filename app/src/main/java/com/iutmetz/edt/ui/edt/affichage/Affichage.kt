package com.iutmetz.edt.ui.edt.affichage;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.local.entity.CoursEntity

abstract class Affichage( // cette classe permet de créer un modèle pour afficher les cours de l'emploi du temps, elle est abstraite car elle ne peut pas être instanciée
    protected val inflater: LayoutInflater, // on utilise un inflater pour créer la vue de l'affichage
    protected val parent: ViewGroup, // on spécifie un parent pour ajouter la vue au parent
    protected val lifecycleScope: LifecycleCoroutineScope // on utilise un scope de coroutine pour gérer les tâches asynchrones ce qui est nécessaire pour afficher les cours de l'emploi du temps
) {
    protected abstract val binding: Any // on définit un binding qui est un objet qui permet de lier les éléments de la vue avec le code, ce binding peut être de n'importe quel type
    abstract fun effacer() // cette fonction doit être implémentée dans les classes filles pour permettre d'effacer les cours
    abstract fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>) // cette fonction doit être implémentée dans les classes filles pour permettre d'afficher un cours
    fun afficher(edt: List<CoursEntity>, abbreviations: List<AbbreviationEntity>, groupe: String) { // cette fonction permet d'afficher les cours de l'emploi du temps en fcontion d'un groupe
        effacer() // on efface les cours
        filtrerCoursParGroupe(edt, groupe).forEach { cours -> // les cours sont filtrés par groupe et sont ensuite affichés
            afficherCours(cours, abbreviations)
        }
    }

    fun filtrerCoursParGroupe(edt: List<CoursEntity>, groupe: String): List<CoursEntity> { // cette fonction permet de filtrer les cours par groupe
        val tabGroupeSelect = groupe.split(".") // on sépare le groupe en paramètre en tableau de groupe, par exemple "1.2" devient ["1", "2"]
        return edt.filter { // on filtre les cours selon une condition
            it.groupe.split(".").let { tabGroupeCours -> // on sépare le groupe du cours en tableau de groupe, par exemple "3.1" devient ["3", "1"]
                for (i in 0..<tabGroupeCours.size) { // on parcourt les deux tableaux de groupe en s'arrêtant à la taille du tableau de groupe
                    if (tabGroupeCours[i].isNotEmpty() && tabGroupeCours[i] != tabGroupeSelect[i]) return@let false // si le groupe du cours n'est pas "" (il s'agit d'un cours pour toute la promotion en théorie)
                // on vérifie que les groupe ne correspondent pas au fur et à mesure, une différence veut dire que le cours n'est pas pour ce groupe et donc il n'est pas inclus dans le tableau
                }
                return@let true // si les groupes correspondent ou il est destiné à toute la promotion, le cours est inclus dans le tableau
            }
        }
    }
}
