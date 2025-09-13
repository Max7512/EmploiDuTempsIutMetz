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
import com.example.edt.util.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.getValue

class EdtFragment : BaseFragment() {

    private val viewModel: EdtViewModel by viewModels()
    private var _binding: FragmentEdtBinding? = null
    val binding get() = _binding!!

    private lateinit var _affichage: Affichage
    val affichage get() = _affichage

    private val promoOptions = listOf(
        Promo("but1", "BUT 1"),
        Promo("but2", "BUT 2"),
        Promo("butap", "BUT AP"),
        Promo("but3-dacs", "BUT 3 DACS"),
        Promo("but3-ra", "BUT 3 RA")
    )

    private lateinit var adapterPromo: ArrayAdapter<Promo>
    private lateinit var adapterGroupe: ArrayAdapter<String>

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
        adapterGroupe = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.groupes)
        binding.spinnerPromo.apply {
            adapter = adapterPromo
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.promo = promoOptions[position].code
                    refreshPage()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            viewModel.promo = promoOptions[selectedItemPosition].code
        }

        binding.spinnerGroupe.apply {
            adapter = adapterGroupe
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.groupe = viewModel.groupes[position]
                    refreshPage()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.ibLeft.setOnClickListener {
            viewModel.previousWeek()
            refreshPage()
        }

        binding.ibRight.setOnClickListener {
            viewModel.nextWeek()
            refreshPage()
        }

        binding.ibCalendar.setOnClickListener {
            binding.clCalendar.visibility = View.VISIBLE
        }

        binding.ibCancel.setOnClickListener {
            binding.clCalendar.visibility = View.GONE
        }

        binding.clCalendar.setOnClickListener {
            binding.clCalendar.visibility = View.GONE
        }

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            viewModel.date = Date(year - 1900, month, dayOfMonth)
            changeDate()
            binding.clCalendar.visibility = View.GONE
            refreshPage()
        }

        refreshPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refreshPage() {
        changeDate()
        showProgressIndicator(true)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.refresh(affichage)
        }.invokeOnCompletion {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                adapterGroupe.notifyDataSetChanged()
                binding.spinnerGroupe.visibility = if (adapterGroupe.isEmpty) View.GONE
                    else View.VISIBLE
                showProgressIndicator(false)
            }
        }
    }

    fun changeDate() {
        binding.tvRange.text = DateConverter.weekToString(viewModel.date)
    }
}