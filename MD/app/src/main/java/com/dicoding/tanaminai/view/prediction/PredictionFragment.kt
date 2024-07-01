package com.dicoding.tanaminai.view.prediction

import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.data.remote.soil.SoilResponse
import com.dicoding.tanaminai.databinding.FragmentPredictionBinding
import com.dicoding.tanaminai.ml.PredictionHelper
import com.dicoding.tanaminai.utils.DateFormatter
import com.dicoding.tanaminai.utils.ResultState
import com.dicoding.tanaminai.utils.extractErrorMessage
import com.dicoding.tanaminai.view.factory.MainViewModelFactory
import com.dicoding.tanaminai.view.home.HomeViewModel
import retrofit2.HttpException
import java.util.Locale


class PredictionFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    private lateinit var predictionHelper: PredictionHelper
    private lateinit var predictionResult: String
    private val homeViewModel: HomeViewModel by viewModels {
        MainViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var inputN: String
    private lateinit var inputP: String
    private lateinit var inputK: String
    private lateinit var inputHum: String
    private lateinit var inputpH: String
    private lateinit var inputTemp: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSoilData()
        val currentTime = System.currentTimeMillis()
        val timeStamp = DateFormatter.parseTimestamp(currentTime)

        predictionHelper = PredictionHelper(
            context = requireActivity(),
            onResult = { result ->
                predictionResult = result
            },
            onError = { errorMessage ->
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )

        binding.btnStartPrediction.setOnClickListener {
            try {
                predictionHelper.predict(inputN, inputP, inputK, inputHum, inputpH, inputTemp)
                val intent = Intent(requireActivity(), ResultActivity::class.java).apply {
                    putExtra(ResultActivity.EXTRA_RESULT, predictionResult)
                    putExtra(ResultActivity.EXTRA_N, inputN)
                    putExtra(ResultActivity.EXTRA_P, inputP)
                    putExtra(ResultActivity.EXTRA_K, inputK)
                    putExtra(ResultActivity.EXTRA_HUM, inputHum)
                    putExtra(ResultActivity.EXTRA_PH, inputpH)
                    putExtra(ResultActivity.EXTRA_TEMP, inputTemp)
                    putExtra(ResultActivity.EXTRA_TIME, timeStamp)
                }
                startActivity(intent)
            } catch (e: HttpException) {
                val errorMessage = extractErrorMessage(e)
                alertDialog(getString(R.string.fetching_failed), errorMessage, getString(R.string.alert_close))
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                alertDialog(getString(R.string.fetching_failed), errorMessage, getString(R.string.alert_close))
            }
        }

    }

    private fun getSoilData() {
        homeViewModel.getSoilData()
            .observe(viewLifecycleOwner) { resultState: ResultState<SoilResponse> ->
                when (resultState) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val data = resultState.data
                        val decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
                        val decimalFormat = DecimalFormat("##.#", decimalFormatSymbols)
                        val n = decimalFormat.format(data.n)
                        val p = decimalFormat.format(data.p)
                        val k = decimalFormat.format(data.k)
                        val hum = decimalFormat.format(data.hum)
                        val ph = decimalFormat.format(data.ph)
                        val temp = decimalFormat.format(data.temp)
                        inputN = n
                        inputP = p
                        inputK = k
                        inputHum = hum
                        inputpH = ph
                        inputTemp = temp
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        val errorMessage = resultState.error
                        Log.e(TAG, "Error: $errorMessage")
                        alertDialog(getString(R.string.fetching_failed), errorMessage, getString(R.string.alert_close))
                    }
                }
            }
    }

    private fun alertDialog(title: String, message: String, positiveButtonText: String) {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        predictionHelper.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "PredictionFragment"
    }

}