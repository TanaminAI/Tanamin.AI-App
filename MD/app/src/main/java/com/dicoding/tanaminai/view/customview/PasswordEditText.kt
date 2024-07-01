package com.dicoding.tanaminai.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.tanaminai.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        if (s.toString().length < 8) {
            setError(context.getString(R.string.password_error), null)
        } else {
            error = null
        }
    }
}