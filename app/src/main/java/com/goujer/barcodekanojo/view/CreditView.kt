package com.goujer.barcodekanojo.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.goujer.barcodekanojo.R

class CreditView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

	private var mIcon: ImageView
	private var mName: TextView
	private var mPosition: TextView
	private var mDescription: TextView

	init {
		LayoutInflater.from(context).inflate(R.layout.view_credit, this, true)
		val array = context.obtainStyledAttributes(attrs, R.styleable.CreditView, 0, 0)

		val iconDrawable = array.getDrawable(R.styleable.CreditView_creditIcon)
		mIcon = findViewById(R.id.credit_icon)
		if (iconDrawable != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				mIcon.background = iconDrawable
			} else {
				mIcon.setBackgroundDrawable(iconDrawable)
			}
		}

		val textName = array.getText(R.styleable.CreditView_creditName)
		mName = findViewById(R.id.credit_name)
		if (textName != null) {
			mName.text = textName
		}

		val textPosition = array.getText(R.styleable.CreditView_creditPosition)
		mPosition = findViewById(R.id.credit_position)
		if (textName != null) {
			mPosition.text = textPosition
		}

		val textDescription = array.getText(R.styleable.CreditView_creditDescription)
		mDescription = findViewById(R.id.credit_description)
		if (textDescription != null) {
			mDescription.text = textDescription
		} else {
			mDescription.visibility = View.GONE
		}
		array.recycle()
	}
}