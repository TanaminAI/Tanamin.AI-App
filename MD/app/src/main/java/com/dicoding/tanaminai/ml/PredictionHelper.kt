package com.dicoding.tanaminai.ml

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.dicoding.tanaminai.R
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.gpu.support.TfLiteGpu
import com.google.android.gms.tflite.java.TfLite
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.gpu.GpuDelegateFactory
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class PredictionHelper(
    private val modelName: String = "converted_model.tflite",
    val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
) {
    private var interpreter: InterpreterApi? = null
    private var isGPUSupported: Boolean = false

    init {
        TfLiteGpu.isGpuDelegateAvailable(context).onSuccessTask { gpuAvailable ->
            val optionsBuilder = TfLiteInitializationOptions.builder()
            if (gpuAvailable) {
                optionsBuilder.setEnableGpuDelegateSupport(true)
                isGPUSupported = true
            }
            TfLite.initialize(context, optionsBuilder.build())
        }.addOnSuccessListener {
            loadLocalModel()
        }.addOnFailureListener {
            onError(context.getString(R.string.tflite_is_not_initialized_yet))
        }
    }

    private fun loadLocalModel() {
        try {
            val buffer: ByteBuffer = loadModelFile(context.assets, modelName)
            initializeInterpreter(buffer)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    private fun initializeInterpreter(model: Any) {
        interpreter?.close()
        try {
            val options = InterpreterApi.Options()
                .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
            if (isGPUSupported) {
                options.addDelegateFactory(GpuDelegateFactory())
            }
            if (model is ByteBuffer) {
                interpreter = InterpreterApi.create(model, options)
            }
        } catch (e: Exception) {
            onError(e.message.toString())
            Log.e(TAG, e.message.toString())
        }
    }

    fun predict(
        N_val: String,
        P_val: String,
        K_val: String,
        hum_val: String,
        ph_val: String,
        temp_val: String
    ) {
        if (interpreter == null) {
            onError(context.getString(R.string.no_tflite_interpreter_loaded))
            return
        }

        val inputArray = Array(1) { FloatArray(6) }  // Reshape to [1, 6]
        try {
            inputArray[0][0] = N_val.toFloat()
            inputArray[0][1] = P_val.toFloat()
            inputArray[0][2] = K_val.toFloat()
            inputArray[0][3] = hum_val.toFloat()
            inputArray[0][4] = ph_val.toFloat()
            inputArray[0][5] = temp_val.toFloat()

            val outputShape = interpreter?.getOutputTensor(0)?.shape()
            val outputArray =
                Array(outputShape!![0]) { FloatArray(outputShape[1]) }  // Assuming single float output

            interpreter?.run(inputArray, outputArray)
            val predictedIndex = outputArray[0].indices.maxByOrNull { outputArray[0][it] } ?: -1
            val cropNames = arrayOf(
                "apple", "banana", "blackgram", "chickpea", "coconut", "coffee", "cotton", "grapes",
                "jute", "kidneybeans", "lentil", "maize", "mango", "mothbeans", "mungbean",
                "muskmelon", "orange", "papaya", "pigeonpeas", "pomegranate", "rice", "watermelon"
            )
            if (predictedIndex in cropNames.indices) {
                onResult(cropNames[predictedIndex])
                Log.d(TAG, "Predicted crop: ${cropNames[predictedIndex]}")
            } else {
                onResult("Unknown")
            }

        } catch (e: Exception) {
            val errorMessage = "Error during prediction: ${e.message}"
            Log.e(TAG, errorMessage)
            onError(errorMessage)
        }
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        assetManager.openFd(modelPath).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            }
        }
    }

    fun close() {
        interpreter?.close()
    }

    companion object {
        private const val TAG = "PredictionHelper"
    }

}