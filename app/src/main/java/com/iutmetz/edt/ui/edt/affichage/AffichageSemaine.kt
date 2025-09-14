package com.iutmetz.edt.ui.edt.affichage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.LifecycleCoroutineScope
import com.iutmetz.edt.R
import com.iutmetz.edt.data.local.entity.AbbreviationEntity
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.databinding.LayoutCoursBinding
import com.iutmetz.edt.databinding.LayoutEdtSemaineBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AffichageSemaine( // cette classe permet d'afficher les cours de l'emploi du temps par semaine
    inflater: LayoutInflater,
    parent: ViewGroup, // on définit les arguments nécessaires pour l'affichage
    lifecycleScope: LifecycleCoroutineScope
) : Affichage(inflater, parent, lifecycleScope) {
    private var density = 0f // on utilise une densité pour calculer des tailles en pixels
    private val rowHeight = 30
    private val columnWidth = 60
    private val heureColumnWidth = 20 // des constantes sont définies pour gérer l'affichage du tableau
    private val heureDebut = 8
    private val heureFin = 18
    override var binding = LayoutEdtSemaineBinding.inflate(inflater, parent, true).apply { // une vue est initialisée et est immédiatement traitée pour afficher en utilisant les constantes définies
        val context = root.context // on récupère le contexte de la vue
        density = context.resources.displayMetrics.density // on récupère la densité du contexte, ce qui sert au calcul des tailles en pixels
        grid.children.forEach {
            it.layoutParams.width = (columnWidth.toFloat() * density).toInt() // on définit la largeur de chaque colonne qui sont les intitulés de chaque jour
        }

        for (i in heureDebut..heureFin) { // on parcourt les heures du début à la fin
            val row = (i - heureDebut) * 2 + 1 // la ligne ciblé est calculée en fonction de l'heure, on multiplie par 2 car il y a deux lignes par heure, puis on ajoute 1 pour la ligne des jours

            val textView = TextView(grid.context) // on crée un TextView pour afficher l'heure, un TextView sert à afficher du texte
            textView.text = i.toString() // l'heure est affichée
            textView.setTextColor(context.getColor(R.color.white)) // la couleur du texte est blanche
            textView.layoutParams = LayoutParams().apply { // on définit les paramètres de la vue
                rowSpec = GridLayout.spec(row, 1) // on définit la ligne et la colonne de la vue
                columnSpec = GridLayout.spec(0, 1)
                width = (heureColumnWidth.toFloat() * density).toInt() // on définit la largeur et la hauteur de la vue
                height = (rowHeight.toFloat() * density).toInt()
            }

            grid.addView(textView) // on ajoute la vue au tableau

            val linearLayout = LinearLayout(grid.context) // on crée un LinearLayout pour signaler une demi heure, un LinearLayout est un peu comme un div en HTML
            linearLayout.background = context.getDrawable(R.color.darkBackground) // on définit la couleur de fond du LinearLayout
            linearLayout.layoutParams = LayoutParams().apply { // on définit les paramètres de la vue
                rowSpec = GridLayout.spec(row + 1, 1) // on définit la ligne et la colonne de la vue
                columnSpec = GridLayout.spec(0, 1)
                width = (heureColumnWidth.toFloat() * density).toInt() // on définit la largeur et la hauteur de la vue
                height = (rowHeight.toFloat() * density).toInt()
            }

            grid.addView(linearLayout) // on ajoute la vue au tableau
        }
    }

    override fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>) { // cette fonction permet de définir comment afficher un cours
        val jour = cours.debut.day // on récupère le jour de la semaine du cours
        if (jour != 0) { // si le jour n'est pas de 0 (dimanche)
            val heureDebut = cours.debut.hours * 2 + if (cours.debut.minutes >= 15) 1 else 0 // on calcule l'heure de début et de fin en comptant une heure comme 2 et si il y a encore plus de 15 minutes, on ajoute 1
            val heureFin = cours.fin.hours * 2 + if (cours.fin.minutes >= 15) 1 else 0
            val rowSpan = heureFin - heureDebut // on calcule le nombre de lignes nécessaires pour afficher le cours
            val row = heureDebut - ((this.heureDebut + (cours.debut.timezoneOffset / 60)) * 2) + 1 // on calcule la ligne de début du cours de façon à bien aligner les cours sur la bonne heure de début
            val param = LayoutParams().apply { // on définit les paramètres de la vue
                rowSpec = GridLayout.spec(row, rowSpan) // on définit la ligne et la colonne de la vue
                columnSpec = GridLayout.spec(jour, 1)
                width = (columnWidth.toFloat() * density).toInt() // on définit la largeur et la hauteur de la vue
                height = (rowHeight.toFloat() * density * rowSpan).toInt()
            }
            val titre = abbreviations.find { it.mod_lib == cours.titre }?.mod_code ?: cours.titre // on récupère le titre du cours en utilisant les abbréviations si possible
            lifecycleScope.launch(Dispatchers.Main) { // on lance une coroutine sur le thread principal pour afficher le cours
                val coursBinding = LayoutCoursBinding.inflate( // on crée un binding pour afficher le cours
                    LayoutInflater.from(binding.root.context),
                    binding.grid,
                    false
                ).apply {
                    tvSalle.text = cours.salle // on définit le texte de la salle de la vue
                    tvTitre.text = titre // on définit le texte du titre de la vue
                    root.layoutParams = param // on applique les paramètres à la vue
                }

                binding.grid.addView(coursBinding.root) // on ajoute la vue au tableau
            }
        }
    }

    override fun effacer() { // cette fonction permet d'effacer les cours
        val casesAEnlever = mutableListOf<ConstraintLayout>() // on crée une liste de ConstraintLayout (comme un div en HTML) qui seront effacées qui correspondent au cases de cours
        binding.grid.children.forEach { // on parcourt toutes les vues du tableau
            if (it is ConstraintLayout) // si la vue est un ConstraintLayout on l'ajoute à la liste
                casesAEnlever.add(it)
        }
        lifecycleScope.launch(Dispatchers.Main) { // on lance une coroutine sur le thread principal pour effacer les cours
            while (casesAEnlever.isNotEmpty()) { // tant que la liste n'est pas vide on enlève la première vue de la grille et du tableau
                binding.grid.removeView(casesAEnlever.first())
                casesAEnlever.removeAt(0)
            }
        }
    }
}