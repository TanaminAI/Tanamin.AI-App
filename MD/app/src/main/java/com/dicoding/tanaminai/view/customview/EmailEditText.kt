package com.dicoding.tanaminai.view.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.tanaminai.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            error = null
        } else {
            setError(context.getString(R.string.email_error), null)
        }
    }
}