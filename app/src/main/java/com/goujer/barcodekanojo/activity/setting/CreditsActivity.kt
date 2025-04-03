package com.goujer.barcodekanojo.activity.setting

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.goujer.barcodekanojo.databinding.ActivityCreditsBinding

class CreditsActivity: Activity() {
	private lateinit var binding: ActivityCreditsBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCreditsBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.creditsBack.setOnClickListener {
			finish()
		}
	}
}