package com.example.edt.ui.edt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.edt.data.common.Promo
import com.example.edt.databinding.FragmentEdtBinding
import com.example.edt.ui.BaseFragment
import com.example.edt.ui.edt.affichage.Affichage
import com.example.edt.ui.edt.affichage.AffichageSemaine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class EdtFragment : BaseFragment() {

    private val viewModel: EdtViewModel by viewModels()
    private var _binding: FragmentEdtBinding? = null
    val binding get() = _binding!!

    private var _affichage: Affichage? = null
    val affichage get() = _affichage!!

    private val promoOptions = listOf(
        Promo("but1", "BUT 1"),
        Promo("but2", "BUT 2"),
        Promo("butap", "BUT AP"),
        Promo("but3-dacs", "BUT 3 DACS"),
        Promo("but3-ra", "BUT 3 RA")
    )

    private lateinit var adapterPromo: ArrayAdapter<Promo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEdtBinding.inflate(inflater, container, false)
        _affichage = AffichageSemaine(inflater, binding.scrollEdt, viewLifecycleOwner.lifecycleScope)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterPromo = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, promoOptions)
        binding.spinnerPromo.apply {
            adapter = adapterPromo
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.promo = promoOptions[position].code
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.refresh(affichage)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            viewModel.promo = promoOptions[selectedItemPosition].code
        }

        showProgressIndicator(true)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.refresh(affichage)
        }.invokeOnCompletion {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                showProgressIndicator(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}