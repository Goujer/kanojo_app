package com.goujer.barcodekanojo.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import jp.co.cybird.barcodekanojoForGAM.R
import com.goujer.barcodekanojo.core.model.User
import kotlinx.coroutines.*

class UserProfileView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
	private val imgPhoto: ImageView
	private val txtBcoin: TextView
	private val txtKanojos: TextView
	private val txtLevel: TextView
	private val txtName: TextView
	private val txtStamina: TextView
	private val txtTickets: TextView
	private val scope = MainScope()

	fun destroy() {
		imgPhoto.setImageDrawable(null)
		scope.cancel()
	}

	fun setUser(user: User?, dic: DynamicImageCache) {
		if (user != null) {
			scope.launch { dic.loadBitmap(imgPhoto, user.profile_image_url, R.drawable.common_noimage, null) }
			txtLevel.text = ":Lv." + user.level
			if (user.name != null && user.name != "null") {
				txtName.text = user.name + txtLevel.text.toString()
			} else {
				txtName.text = resources.getString(R.string.blank_name) + txtLevel.text.toString()
			}
			txtStamina.text = user.stamina.toString()
			txtKanojos.text = user.kanojo_count.toString()
			txtBcoin.text = user.money.toString()
			txtTickets.text = user.tickets.toString()
		}
	}

	init {
		LayoutInflater.from(context).inflate(R.layout.view_user_profile, this, true)
		imgPhoto = findViewById(R.id.common_profile_img)
		txtLevel = findViewById(R.id.common_profile_level)
		txtName = findViewById(R.id.common_profile_name)
		txtStamina = findViewById(R.id.common_profile_stamina)
		txtKanojos = findViewById(R.id.common_profile_kanojos)
		txtBcoin = findViewById(R.id.common_profile_b_coin)
		txtTickets = findViewById(R.id.common_profile_ticket)
		txtLevel.visibility = GONE
		txtName.setSingleLine()
		txtName.ellipsize = TextUtils.TruncateAt.END
	}
}