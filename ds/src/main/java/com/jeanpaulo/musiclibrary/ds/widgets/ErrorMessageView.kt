package com.jeanpaulo.musiclibrary.ds.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.jeanpaulo.musiclibrary.ds.R

class ErrorMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val iconView: ImageView
    private val messageView: TextView
    private val actionButton: MaterialButton

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error_message, this, true)
        iconView = findViewById(R.id.error_icon)
        messageView = findViewById(R.id.error_message)
        actionButton = findViewById(R.id.error_action)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ErrorMessageView)
            val message = a.getString(R.styleable.ErrorMessageView_errorMessage)
            val icon = a.getDrawable(R.styleable.ErrorMessageView_errorIcon)
            val actionText = a.getString(R.styleable.ErrorMessageView_errorActionText)
            a.recycle()

            message?.let { setMessage(it) }
            icon?.let { setIcon(it) }
            actionText?.let { setActionText(it) }
        }
        isClickable = false
        isFocusable = false
    }

    fun setMessage(text: CharSequence) {
        messageView.text = text
    }

    fun setIcon(drawable: Drawable?) {
        iconView.setImageDrawable(drawable)
    }

    fun setIcon(resId: Int) {
        iconView.setImageResource(resId)
    }

    fun setActionText(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            actionButton.text = ""
            actionButton.visibility = GONE
        } else {
            actionButton.text = text
            actionButton.visibility = VISIBLE
        }
    }

    fun setOnActionClickListener(listener: OnClickListener?) {
        if (listener == null) {
            actionButton.setOnClickListener(null)
            actionButton.isClickable = false
        } else {
            actionButton.setOnClickListener(listener)
            actionButton.isClickable = true
        }
    }
}
