package com.example.edt.ui.edt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.edt.databinding.FragmentEdtBinding
import com.example.edt.databinding.LayoutEdtSemaineBinding
import com.example.edt.ui.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class EdtFragment : BaseFragment() {

    private val viewModel: EdtViewModel by viewModels()
    private var _binding: FragmentEdtBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEdtBinding.inflate(inflater, container, false)

        LayoutEdtSemaineBinding.inflate(LayoutInflater.from(context), binding.clEdt, true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}