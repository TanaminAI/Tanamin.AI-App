package com.dicoding.tanaminai.view.prediction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.data.dummy.DataDummy
import com.dicoding.tanaminai.data.local.entity.BookmarkEntity
import com.dicoding.tanaminai.databinding.ActivityResultBinding
import com.dicoding.tanaminai.utils.extractErrorMessage
import com.dicoding.tanaminai.view.bookmark.BookmarkViewModel
import com.dicoding.tanaminai.view.factory.MainViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var predictionResult: String
    private lateinit var inputN: String
    private lateinit var inputP: String
    private lateinit var inputK: String
    private lateinit var inputHum: String
    private lateinit var inputpH: String
    private lateinit var inputTemp: String
    private lateinit var predictedat: String
    private var plantImage: Int = 0

    private val bookmarkViewModel: BookmarkViewModel by viewModels {
        MainViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        predictionResult =
            intent.getStringExtra(EXTRA_RESULT) ?: getString(R.string.no_prediction_result)
        inputN = intent.getStringExtra(EXTRA_N) ?: getString(R.string.no_prediction_result)
        inputP = intent.getStringExtra(EXTRA_P) ?: getString(R.string.no_prediction_result)
        inputK = intent.getStringExtra(EXTRA_K) ?: getString(R.string.no_prediction_result)
        inputHum = intent.getStringExtra(EXTRA_HUM) ?: getString(R.string.no_prediction_result)
        inputpH = intent.getStringExtra(EXTRA_PH) ?: getString(R.string.no_prediction_result)
        inputTemp = intent.getStringExtra(EXTRA_TEMP) ?: getString(R.string.no_prediction_result)
        predictedat = intent.getStringExtra(EXTRA_TIME) ?: getString(R.string.no_prediction_result)

        binding.apply {
            tvNitrogenValue.text = inputN
            tvPhosphorusValue.text = inputP
            tvPotassiumValue.text = inputK
            tvHumidityValue.text = inputHum
            tvPHValue.text = inputpH
            tvTemperatureValue.text = inputTemp
            tvPlantResult.text = predictionResult
            tvDateTime.text = predictedat
        }

        val plantData = predictionResult.let { DataDummy.getPlantData(it) }
        plantImage = plantData.image
        Glide.with(binding.root)
            .load(plantImage)
            .into(binding.imgResultPrediction)

        val sectionPagerAdapter = SectionPagerAdapter(this, predictionResult)
        val viewPager = binding.viewPagerDetail
        viewPager.adapter = sectionPagerAdapter
        val tab = binding.tabResult
        TabLayoutMediator(tab, viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()

        checkBookmark(predictedat)
    }

    private fun checkBookmark(predictedAt: String) {
        bookmarkViewModel.checkBookmark(predictedAt).observe(this) { count: Int ->
            val isBookmarked = count > 0
            binding.fabBookmark.setImageResource(if (isBookmarked) R.drawable.icon_bookmark_added else R.drawable.icon_bookmark_add)
            binding.fabBookmark.setOnClickListener {
                try {
                    val bookmark = BookmarkEntity(
                        result = predictionResult,
                        n = inputN,
                        p = inputP,
                        k = inputK,
                        hum = inputHum,
                        ph = inputpH,
                        temp = inputTemp,
                        predictAt = predictedat,
                        image = plantImage
                    )
                    if (isBookmarked) {
                        delete(predictedAt)
                    } else {
                        insert(bookmark)
                    }
                } catch (e: Exception) {
                    val errorMessage = extractErrorMessage(e)
                    alertDialog(
                        getString(R.string.fetching_failed),
                        errorMessage,
                        getString(R.string.alert_close)
                    )
                }
            }
        }
    }

    private fun insert(bookmark: BookmarkEntity) {
        bookmarkViewModel.insert(bookmark)
        binding.fabBookmark.setImageResource(R.drawable.icon_bookmark_add)
    }

    private fun delete(predictedAt: String) {
        bookmarkViewModel.delete(predictedAt)
        binding.fabBookmark.setImageResource(R.drawable.icon_bookmark_added)
    }

    private fun alertDialog(title: String, message: String, positiveButtonText: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    companion object {
        const val EXTRA_RESULT = "extra_prediction_result"
        const val EXTRA_N = "extra_n"
        const val EXTRA_P = "extra_p"
        const val EXTRA_K = "extra_k"
        const val EXTRA_HUM = "extra_hum"
        const val EXTRA_PH = "extra_pH"
        const val EXTRA_TEMP = "extra_temp"
        const val EXTRA_TIME = "extra_time"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.description_result,
            R.string.maintenance_result
        )
    }


}