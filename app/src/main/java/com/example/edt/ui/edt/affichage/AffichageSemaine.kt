package com.example.edt.ui.edt.affichage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.edt.R
import com.example.edt.data.local.entity.AbbreviationEntity
import com.example.edt.data.local.entity.CoursEntity
import com.example.edt.databinding.LayoutCoursBinding
import com.example.edt.databinding.LayoutEdtSemaineBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AffichageSemaine(
    inflater: LayoutInflater,
    parent: ViewGroup,
    lifecycleScope: LifecycleCoroutineScope
) : Affichage(lifecycleScope) {
    private var density = 0f
    private val rowHeight = 30
    private val columnWidth = 60
    private val heureColumnWidth = 20
    private val heureDebut = 8
    private val heureFin = 18
    override var binding = LayoutEdtSemaineBinding.inflate(inflater, parent, true).apply {
        val context = root.context
        density = context.resources.displayMetrics.density
        grid.children.forEach {
            it.layoutParams.width = (columnWidth.toFloat() * density).toInt()
        }

        for (i in heureDebut..heureFin) {
            val row = (i - heureDebut) * 2 + 1

            val textView = TextView(grid.context)
            textView.text = i.toString()
            textView.setTextColor(context.getColor(R.color.white))
            textView.layoutParams = LayoutParams().apply {
                rowSpec = GridLayout.spec(row, 1)
                columnSpec = GridLayout.spec(0, 1)
                width = (heureColumnWidth.toFloat() * density).toInt()
                height = (rowHeight.toFloat() * density).toInt()
            }

            grid.addView(textView)

            val linearLayout = LinearLayout(grid.context)
            linearLayout.background = context.getDrawable(R.color.darkBackground)
            linearLayout.layoutParams = LayoutParams().apply {
                rowSpec = GridLayout.spec(row + 1, 1)
                columnSpec = GridLayout.spec(0, 1)
                width = (heureColumnWidth.toFloat() * density).toInt()
                height = (rowHeight.toFloat() * density).toInt()
            }

            grid.addView(linearLayout)
        }
    }

    override fun afficherCours(cours: CoursEntity, abbreviations: List<AbbreviationEntity>) {
        val jour = cours.debut.day
        if (jour != 0) {
            val heureDebut = cours.debut.hours * 2 + if (cours.debut.minutes >= 15) 1 else 0
            val heureFin = cours.fin.hours * 2 + if (cours.fin.minutes >= 15) 1 else 0
            val rowSpan = heureFin - heureDebut
            val row = heureDebut - 11
            val param = LayoutParams().apply {
                rowSpec = GridLayout.spec(row, rowSpan)
                columnSpec = GridLayout.spec(jour, 1)
                width = (columnWidth.toFloat() * density).toInt()
                height = (rowHeight.toFloat() * density * rowSpan).toInt()
            }
            val titre = abbreviations.find { it.mod_lib == cours.titre }?.mod_code ?: cours.titre
            lifecycleScope.launch(Dispatchers.Main) {
                val coursBinding = LayoutCoursBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    binding.grid,
                    false
                ).apply {
                    tvSalle.text = cours.salle
                    tvTitre.text = titre
                    root.layoutParams = param
                }

                binding.grid.addView(coursBinding.root)
            }
        }
    }

    override fun effacer() {
        val casesAEnlever = mutableListOf<ConstraintLayout>()
        binding.grid.children.forEach {
            if (it is ConstraintLayout)
                casesAEnlever.add(it)
        }
        lifecycleScope.launch(Dispatchers.Main) {
            while (casesAEnlever.isNotEmpty()) {
                binding.grid.removeView(casesAEnlever.first())
                casesAEnlever.removeAt(0)
            }
        }
    }
}