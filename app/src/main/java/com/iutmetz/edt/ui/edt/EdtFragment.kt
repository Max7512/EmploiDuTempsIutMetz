package com.iutmetz.edt.ui.edt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iutmetz.edt.R
import com.iutmetz.edt.data.common.Promo
import com.iutmetz.edt.data.common.Result
import com.iutmetz.edt.data.local.entity.CoursEntity
import com.iutmetz.edt.databinding.FragmentEdtBinding
import com.iutmetz.edt.ui.BaseFragment
import com.iutmetz.edt.ui.edt.affichage.Affichage
import com.iutmetz.edt.ui.edt.affichage.AffichageSemaine
import com.iutmetz.edt.util.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.getValue

class EdtFragment : BaseFragment() { // ce fragment permet d'afficher l'emploi du temps de l'utilisateur et hérites des fonctions de base définies dans la classe BaseFragment

    private val viewModel: EdtViewModel by viewModels() // on utilise un view model pour gérer les données de l'emploi du temps de l'utilisateur
    private var _binding: FragmentEdtBinding? = null // on utilise un binding pour accéder aux éléments de la vue
    val binding get() = _binding!! // on utilise un getteur pour accéder au binding tout en empechant d'en modifier la valeur

    private lateinit var _affichage: Affichage // on utilise un objet Affichage pour afficher l'emploi du temps de l'utilisateur, ces affichages sont définis dans le package ui.edt.affichage et pourront être interchangés
    val affichage get() = _affichage // on utilise un getteur pour accéder à l'affichage tout en empechant d'en modifier la valeur

    private val promoOptions = listOf( // on définit les options de promo
        Promo("but1", "BUT 1"),
        Promo("but2", "BUT 2"),
        Promo("butap", "BUT AP"),
        Promo("but3-dacs", "BUT 3 DACS"),
        Promo("but3-ra", "BUT 3 RA")
    )

    private lateinit var adapterPromo: ArrayAdapter<Promo> // on utilise des adapters pour afficher les options de promo et de groupe dans les listes déroulantes, les spinners
    private lateinit var adapterGroupe: ArrayAdapter<String>

    private var spinnerTrigger = false // cette variable permet de désactiver le rafraîchissement lors d'un changement de promo ou de groupe

    override fun onCreateView( // cette fonction est appelée lorsque le fragment est créé et sert à initialiser la vue
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEdtBinding.inflate(inflater, container, false) // on initialise la vue de la page
        _affichage = AffichageSemaine(inflater, binding.scrollEdt, viewLifecycleOwner.lifecycleScope) // on initialise l'affichage de l'emploi du temps à un affichage par semaine
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // cette fonction est appelée lorsque la vue est créée et sert à initialiser les interactions avec l'utilisateur et la logique du fragment
        super.onViewCreated(view, savedInstanceState)

        adapterPromo = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, promoOptions) // on initialise les adapters
        adapterGroupe = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.groupes)

        binding.spinnerPromo.apply { // on initialise le spinner promo, la méthode apply permet d'éviter de répéter binding.spinnerPromo car tout le code est effectué avec binding.spinnerPromo comme this,
            // en kotlin il n'est pas nécessaire d'écrire this, il s'agit d'une méthode d'écriture simple pour bien regroupper le code et éviter les répétitions
            adapter = adapterPromo // on initialise l'adapter du spinner
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener { // on initialise un listener pour le spinner pour définir le code à éxecuter lors d'un changement de promo
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (spinnerTrigger) { // si le spinner peut être déclenché
                        viewModel.promo = promoOptions[position].code // on met à jour le code de la promo dans le ViewMdel
                        viewModel.groupe = "" // on met à jour le groupe dans le ViewModel
                        refreshPage() // la page est rafraîchie
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.spinnerGroupe.apply { // idem que pour le spinner promo
            adapter = adapterGroupe
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (viewModel.groupes[position] != viewModel.groupe) {
                        if (spinnerTrigger) {
                            viewModel.groupe = viewModel.groupes[position]
                            refreshPage()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.ibLeft.setOnClickListener { // on initialise les boutons pour naviguer entre les semaines
            viewModel.previousWeek() // on change la date dans le ViewModel à la semaine dernière
            refreshPage() // la page est rafraîchie
        }

        binding.ibRight.setOnClickListener {
            viewModel.nextWeek() // on change la date dans le ViewModel à la semaine prochaine
            refreshPage() // la page est rafraîchie
        }

        binding.ibCalendar.setOnClickListener { // on initialise le bouton pour afficher le calendrier
            binding.clCalendar.visibility = View.VISIBLE // on affiche le calendrier
        }

        binding.ibCancel.setOnClickListener { // on initialise le bouton pour masquer le calendrier
            binding.clCalendar.visibility = View.GONE // on cache le calendrier
        }

        binding.clCalendar.setOnClickListener { // l'arrière plan du calendrier est initalisé pour permettre de masquer le calendrier également
            binding.clCalendar.visibility = View.GONE // on cache le calendrier
        }

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth -> // on définit le code à éxecuter lors d'un changement de date dans le calendrier
            viewModel.date = Date(year - 1900, month, dayOfMonth) // on met à jour la date dans le ViewModel
            changeDate() // la date est mise à jour dans la vue
            binding.clCalendar.visibility = View.GONE // on cache le calendrier
            refreshPage() // la page est rafraîchie
        }

        showProgressIndicator(true) // on affiche un indicateur de chargement
        lifecycleScope.launch(Dispatchers.IO) { // on lance une coroutine qui est un thread séparé du thread principal pour charger les données de l'emploi du temps,
            // à noter que dans kotlin les fonctions asynchrone sont des fonctions suspend, elles bloquent complètement le thread courant et doivent donc être lancées dans une coroutine
            val session = viewModel.chargeSession() // on charge la session de l'utilisateur

            if (session == null) {
                viewModel.promo = promoOptions[binding.spinnerPromo.selectedItemPosition].code // le code de promo est mis par défaut au premier élément du spinner si la session n'existe pas
            }
        }.invokeOnCompletion { // on attend que la coroutine soit terminée pour exécuter le code suivant
            binding.spinnerPromo.setSelection(promoOptions.indexOfFirst { it.code == viewModel.promo }) // on sélectionne la promo spécifiée dans le ViewModel dans le spinner pour le cas où la session est récupérée
            refreshPage() // la page est rafraîchie
        }
    }

    override fun onDestroyView() { // cette fonction est appelée lorsque la vue est détruite
        super.onDestroyView()
        _binding = null
    }

    fun refreshPage() { // cette fonction permet de rafraîchir la page
        showProgressIndicator(true) // on affiche un indicateur de chargement

        val groupeVide = viewModel.groupe.isEmpty() // on vérifie si le groupe est vide au début de la requête

        var result: Result<List<CoursEntity>>? = null // on initialise une variable pour stocker le résultat de la requête

        changeDate() // la date est mise à jour dans la vue

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) { // on lance une coroutine pour charger les données de l'emploi du temps
            result = viewModel.refresh(affichage) // on charge les données de l'emploi du temps dans le ViewModel en lui donnant l'affichage à utiliser
        }.invokeOnCompletion { // on attend que la coroutine soit terminée pour exécuter le code suivant
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) { // on lance une coroutine sur le thread principal pour mettre à jour la vue
                if (result?.error != null) {
                    showMessage(resources.getString(R.string.err_chargement)) // on affiche un message d'erreur si la requête a échoué
                }

                adapterGroupe.notifyDataSetChanged() // on met à jour l'adapter du spinner de groupe

                if (groupeVide) binding.spinnerGroupe.setSelection(0) // si le groupe est vide, on met le premier élément du spinner par défaut
                else binding.spinnerGroupe.setSelection(viewModel.groupes.indexOf(viewModel.groupe)) // sinon on met le groupe actuel dans le spinner

                binding.spinnerGroupe.visibility = if (adapterGroupe.isEmpty) View.GONE // on cache le spinner si il n'y a pas de groupe
                    else View.VISIBLE
                showProgressIndicator(false) // on cache l'indicateur de chargement
                spinnerTrigger = true // on réactive le rafraîchissement par les spinners
            }

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) { // on lance une coroutine pour vérifier si la version du projet est à jour
                if (!viewModel.estAJour()) viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    showMessage(resources.getString(R.string.mise_a_jour)) // on affiche un message si la version du projet n'est pas à jour
                }
            }
        }
    }

    fun changeDate() { // cette fonction permet de mettre à jour la date dans la vue
        binding.tvRange.text = DateConverter.weekToString(viewModel.date) // on met à jour la date dans la vue grâce à la fonction weekToString de la classe DateConverter
    }
}