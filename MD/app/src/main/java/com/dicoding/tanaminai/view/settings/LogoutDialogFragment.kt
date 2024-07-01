package com.dicoding.tanaminai.view.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.tanaminai.databinding.FragmentLogoutDialogBinding
import com.dicoding.tanaminai.view.factory.AuthViewModelFactory
import com.dicoding.tanaminai.view.main.MainViewModel
import com.dicoding.tanaminai.view.welcome.WelcomeActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LogoutDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLogoutDialogBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.buttonLogout.setOnClickListener {
            mainViewModel.logout()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}