package com.iutmetz.edt.ui.parametres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iutmetz.edt.R
import com.iutmetz.edt.databinding.FragmentParametresBinding
import com.iutmetz.edt.ui.BaseFragment
import com.iutmetz.edt.ui.dialog.ConfirmationDialog
import com.iutmetz.edt.ui.parametres.parametre.Parametre
import com.iutmetz.edt.ui.parametres.parametre.ParametreCouleurs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class ParametresFragment : BaseFragment() { // ce fragment permet d'afficher l'emploi du temps de l'utilisateur et hérites des fonctions de base définies dans la classe BaseFragment
    private val viewModel: ParametresViewModel by viewModels() // on utilise un view model pour gérer les données de l'emploi du temps de l'utilisateur
    private var _binding: FragmentParametresBinding? = null // on utilise un binding pour accéder aux éléments de la vue
    val binding get() = _binding!! // on utilise un getteur pour accéder au binding tout en empechant d'en modifier la valeur
    private var parametreList: List<Parametre> = listOf() // on initialise une liste de paramètres vide

    override fun onCreateView( // cette fonction est appelée lorsque le fragment est créé et sert à initialiser la vue
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParametresBinding.inflate(inflater, container, false) // on initialise la vue de la page
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // cette fonction est appelée lorsque la vue est créée et sert à initialiser les interactions avec l'utilisateur et la logique du fragment
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.chargeSession()

            lifecycleScope.launch(Dispatchers.Main) {
                parametreList = listOf(
                    ParametreCouleurs(viewModel.session, binding.clContent, layoutInflater, binding.scrollParametres)
                )

                parametreList.forEach {
                    it.initView()
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.session == viewModel.sessionOriginal)
                    navigate(ParametresFragmentDirections.actionParametresFragmentToEdtFragment())
                else
                    context?.let {
                        ConfirmationDialog(
                            it,
                            R.string.quitter,
                            R.string.quitter_message,
                            onOkClicked = {
                                findNavController().popBackStack()
                            },
                            onCancelClicked = {

                            }).show()
                    }
            }
        })

        binding.apply {
            ibCancel.setOnClickListener {
                clContent.children.forEach {
                    it.visibility = View.GONE
                }
                clPopup.visibility = View.GONE
            }

            clPopup.setOnClickListener {
                clContent.children.forEach {
                    it.visibility = View.GONE
                }
                clPopup.visibility = View.GONE
            }

            ibSave.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.saveSession()

                    lifecycleScope.launch(Dispatchers.Main) {
                        navigate(ParametresFragmentDirections.actionParametresFragmentToEdtFragment())
                    }
                }
            }
        }
    }

    override fun onDestroyView() { // cette fonction est appelée lorsque la vue est détruite
        super.onDestroyView()
        _binding = null
    }
}