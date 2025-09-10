package com.example.edt.ui.edt.affichage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.databinding.LayoutCoursBinding
import com.example.edt.databinding.LayoutEdtSemaineBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AffichageSemaine(inflater: LayoutInflater, parent: ViewGroup, lifecycleScope: LifecycleCoroutineScope): Affichage(lifecycleScope) {
    override var binding = LayoutEdtSemaineBinding.inflate(inflater, parent, true)

    override fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>) {
        val jour = cours.debut.day
        if (jour != 0) {
            val heureDebut = cours.debut.hours * 2 + if (cours.debut.minutes > 30) 1 else 0
            val heureFin = cours.fin.hours * 2 + if (cours.fin.minutes > 30) 1 else 0
            val rowSpan = heureFin - heureDebut
            val row = heureDebut - 7
            val param = LayoutParams()
            param.rowSpec = GridLayout.spec(row,rowSpan - 1)
            param.columnSpec = GridLayout.spec(jour,0)
            lifecycleScope.launch(Dispatchers.Main) {
                val coursBinding = LayoutCoursBinding.inflate(LayoutInflater.from(binding.root.context), binding.grid, false).apply {
                    tvSalle.text = cours.salle
                    tvTitre.text = cours.titre
                    root.layoutParams = param
                }

                binding.grid.addView(coursBinding.root)
            }
        }
    }

    override fun effacer() {
        binding.grid.allViews.forEach {
            if (it is ConstraintLayout)
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.grid.removeView(it)
                }
        }
    }
}