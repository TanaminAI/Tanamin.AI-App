package com.dicoding.tanaminai.view.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.databinding.FragmentSettingsBinding
import com.dicoding.tanaminai.view.factory.AuthViewModelFactory
import com.dicoding.tanaminai.view.main.MainViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.logoutSection.setOnClickListener {
            try {
                findNavController().navigate(R.id.act_settingsFrag_to_logoutFrag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvEmail.text = user.email
        }
        binding.rateSection.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.themeSection.setOnClickListener {
            findNavController().navigate(R.id.act_settingsFrag_to_themeFrag)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}