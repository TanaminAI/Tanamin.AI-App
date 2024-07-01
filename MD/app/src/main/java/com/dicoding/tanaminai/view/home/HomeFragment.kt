package com.dicoding.tanaminai.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.tanaminai.BuildConfig
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.data.remote.soil.SoilResponse
import com.dicoding.tanaminai.data.remote.weather.WeatherResponse
import com.dicoding.tanaminai.databinding.FragmentHomeBinding
import com.dicoding.tanaminai.utils.ResultState
import com.dicoding.tanaminai.utils.extractErrorMessage
import com.dicoding.tanaminai.view.factory.MainViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.HttpException
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        MainViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncherLocation =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyWeather()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyWeather()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyWeather()
        binding.linearMain.setOnClickListener {
            try {
                getSoilData()
            } catch (e: HttpException) {
                val errorMessage = extractErrorMessage(e)
                alertDialog(
                    getString(R.string.fetching_failed),
                    errorMessage,
                    getString(R.string.alert_close)
                )
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                alertDialog(
                    getString(R.string.fetching_failed),
                    errorMessage,
                    getString(R.string.alert_close)
                )
            }
        }
        binding.buttonInfo.setOnClickListener {
            findNavController().navigate(R.id.act_homeFrag_to_infoFrag)
        }
        getSoilData()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyWeather() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
                currentLocation = location
                val lat = currentLocation.latitude
                val lon = currentLocation.longitude
                homeViewModel.getCurrentWeather(lat, lon, apiKey = APPID)
                    .observe(viewLifecycleOwner) { resultState: ResultState<WeatherResponse> ->
                        when (resultState) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                showLoading(false)
                                val weatherData = resultState.data
                                val kalvin = weatherData.main?.temp
                                val celcius = kalvin?.minus(273.15)
                                val temperature = celcius?.toInt()
                                val condition = weatherData.weather?.get(0)?.description
                                val city = weatherData.name
                                val country = weatherData.sys?.country
                                iconWeather(condition)
                                binding.apply {
                                    tvLocation.text = getString(R.string.location_format, city, country)
                                    tvWeatherTemp.text = "$temperature"
                                    tvWeatherCondition.text = condition
                                }
                            }

                            is ResultState.Error -> {
                                showLoading(false)
                                val errorMessage = resultState.error
                                Log.e(TAG, "Error: $errorMessage")
                                showToast(getString(R.string.weather_data_is_not_available))
                            }
                        }

                    }
            }
        } else {
            requestPermissionLauncherLocation.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
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

                        binding.apply {
                            tvNitrogenValue.text = n
                            tvPhosphorusValue.text = p
                            tvPotassiumValue.text = k
                            tvHumidityValue.text = hum
                            tvPhValue.text = ph
                            tvTempValue.text = temp
                        }
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        val errorMessage = resultState.error
                        Log.e(SOIL_TAG, "Error: $errorMessage")
                        alertDialog(
                            getString(R.string.fetching_failed),
                            errorMessage,
                            getString(R.string.alert_close)
                        )
                    }
                }
            }
    }

    private fun iconWeather(condition: String?) {
        if (condition != null) {
            when {
                condition.contains("clear") -> {
                    binding.imgWeather.setImageResource(R.drawable.sun)
                }

                condition.contains("thunder") -> {
                    binding.imgWeather.setImageResource(R.drawable.thunder)
                }

                condition.contains("rain") -> {
                    binding.imgWeather.setImageResource(R.drawable.rain)
                }

                condition.contains("snow") -> {
                    binding.imgWeather.setImageResource(R.drawable.snow)
                }

                condition.contains("clouds") && condition.contains("overcast") -> {
                    binding.imgWeather.setImageResource(R.drawable.overcast)
                }

                condition.contains("clouds") -> {
                    binding.imgWeather.setImageResource(R.drawable.clouds)
                }

                else -> {
                    binding.imgWeather.setImageResource(R.drawable.def)
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

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val APPID = BuildConfig.KEY_WEATHER
        const val TAG = "CURRENT LOCATION"
        const val SOIL_TAG = "GET SOIL DATA"
    }
}