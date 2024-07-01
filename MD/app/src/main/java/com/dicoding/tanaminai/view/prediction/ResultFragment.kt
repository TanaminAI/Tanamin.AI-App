package com.dicoding.tanaminai.view.prediction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.tanaminai.data.dummy.DataDummy
import com.dicoding.tanaminai.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var position: Int = 0
    private var predictionResult: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        position = arguments?.getInt(ARG_POSITION, 0) ?: 0
        predictionResult = arguments?.getString(ARG_PREDICTIONRESULT)
        val planData = predictionResult?.let { DataDummy.getPlantData(it) }

        planData?.let {
            when (position) {
                1 -> {
                    binding.tvDescription.text = it.description
                }

                2 -> {
                    binding.tvMaintenance.text = it.maintenance
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_PREDICTIONRESULT = "arg_predictionresult"
    }

}