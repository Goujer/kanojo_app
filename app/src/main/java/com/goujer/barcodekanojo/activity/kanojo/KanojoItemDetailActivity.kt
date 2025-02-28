package com.goujer.barcodekanojo.activity.kanojo

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.R
import com.goujer.barcodekanojo.core.cache.DynamicImageCache
import com.goujer.barcodekanojo.core.model.Kanojo
import com.goujer.barcodekanojo.core.model.User
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface
import jp.co.cybird.barcodekanojoForGAM.activity.kanojo.KanojoItemsActivity
import jp.co.cybird.barcodekanojoForGAM.billing.util.Inventory
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.*
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.*

class KanojoItemDetailActivity : BaseActivity(), View.OnClickListener {
	private var btnOk: Button? = null
	private var imgView: ImageView? = null
	private val lstProductId: List<String>? = null
	private var mBuyTicketTask: BuyTicketTask? = null
	private var mInventory: Inventory? = null
	private var mKanojo: Kanojo? = null
	private var mKanojoItem: KanojoItem? = null
	private var mLoadingDone = false
	private var mLoadingView: CustomLoadingView? = null
	private var mLoveIncrement: LoveIncrement? = null
	private var mKanojoMessage: KanojoMessage? = null
	private var mReceiptData: String? = null
	private lateinit var mDic: DynamicImageCache
	val mTaskEndHandler: Handler = object : Handler() {
		override fun handleMessage(msg: Message) {
			val next = queue!!.poll()
			if (next != null) {
				executePurchaseTask(next)
			}
		}
	}
	private var mTaskQueue: Queue<StatusHolder?>? = null
	private var mTransactionId = 0
	private var mUser: User? = null
	private var mode = 0
	private var txtDescription: TextView? = null
	private var txtTitle: TextView? = null
	private val mScope = MainScope()
	private var mJob: Job? = null

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_kanojo_item_detail)
		mDic = (application as BarcodeKanojoApp).imageCache
		(findViewById<View>(R.id.kanojo_item_detail_close) as Button).setOnClickListener(this)
		txtTitle = findViewById<View>(R.id.kanojo_item_detail_title) as TextView
		imgView = findViewById<View>(R.id.kanojo_item_detail_img) as ImageView
		imgView!!.visibility = View.INVISIBLE
		txtDescription = findViewById<View>(R.id.kanojo_item_detail_description) as TextView
		txtDescription!!.visibility = View.INVISIBLE
		btnOk = findViewById<View>(R.id.kanojo_item_detail_btn_01) as Button
		btnOk!!.visibility = View.INVISIBLE
		btnOk!!.setOnClickListener(this)
		val btnCancel = findViewById<View>(R.id.kanojo_item_detail_btn_02) as Button
		btnCancel.setOnClickListener(this)
		mLoadingView = findViewById<View>(R.id.loadingView) as CustomLoadingView
		mUser = (application as BarcodeKanojoApp).barcodeKanojo.user
		val bundle = intent.extras
		if (bundle != null) {
			mKanojo = bundle[EXTRA_KANOJO] as Kanojo?
			mKanojoItem = bundle[EXTRA_KANOJO_ITEM] as KanojoItem?
			mode = bundle.getInt(EXTRA_KANOJO_ITEM_MODE)
		}
		btnCancel.visibility = View.GONE
	}

	override fun onResume() {
		super.onResume()
		if (mLoadingDone || !((mode == MODE_EXTEND_DATE || mode == MODE_EXTEND_GIFT) && mKanojoItem!!.has_units == null)) {
			loadContent(0)
		} else {
			executeCheckPriceTask()
		}
	}

	override fun onPause() {
		super.onPause()
		if (mBuyTicketTask != null) {
			mBuyTicketTask!!.cancel(true)
			mBuyTicketTask = null
		}
	}

	override fun onStop() {
		mJob?.cancel()
		super.onStop()
	}

	override fun onDestroy() {
		mScope.cancel()
		super.onDestroy()
	}

	fun loadContent(btnTextRes: Int) {
		if (mKanojoItem != null) {
			imgView!!.visibility = View.VISIBLE
			txtDescription!!.visibility = View.VISIBLE
			btnOk!!.visibility = View.VISIBLE
			txtTitle!!.text = mKanojoItem!!.title
			mJob = mScope.launch { mDic.loadBitmap(imgView!!, mKanojoItem!!.image_url, R.drawable.common_noimage_product, null) }
			if (mKanojoItem!!.isHas) {
				txtDescription!!.text = mKanojoItem!!.description
			} else {
				txtDescription!!.text = """
					${mKanojoItem!!.price}
					${mKanojoItem!!.description}
					""".trimIndent()
			}
		}

		// Set the text of the buy button
		if (mode == MODE_TICKET) {
			btnOk!!.text = resources.getString(R.string.kanojo_shop_buy)
		} else if (mode == MODE_GIFT || mode == MODE_DATE || mKanojoItem!!.has_units != null) {
			if (mode == MODE_GIFT || mode == MODE_EXTEND_GIFT) {
				btnOk!!.text = resources.getString(R.string.item_detail_button_ok_text)
			} else if (mode == MODE_DATE || mode == MODE_EXTEND_DATE) {
				btnOk!!.text = resources.getString(R.string.item_detail_date_button_ok_text)
			}
		}

		if (btnTextRes > 0) {
			btnOk!!.setText(btnTextRes)
		}
	}

	private fun hideContent() {
		imgView!!.visibility = View.INVISIBLE
		txtDescription!!.visibility = View.INVISIBLE
		btnOk!!.visibility = View.INVISIBLE
	}

	public override fun getClientView(): View {
		val layout = layoutInflater.inflate(R.layout.activity_kanojo_item_detail, null as ViewGroup?)
		val appLayoutRoot = LinearLayout(this)
		appLayoutRoot.addView(layout)
		return appLayoutRoot
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == BaseInterface.REQUEST_KANOJO_TICKETS && resultCode == BaseInterface.RESULT_KANOJO_ITEM_PAYMENT_DONE) {
			close()
		} else if (requestCode == BaseInterface.REQUEST_BUY_TICKET) {
			hideContent()
			mLoadingDone = false
		}
	}

	override fun onClick(v: View) {
		val id = v.id
		if (id == R.id.kanojo_item_detail_close) {
			if (mode == 3) {
				setResult(RESULT_BUY_TICKET_FAIL)
			}
			close()
		} else if (id == R.id.kanojo_item_detail_btn_01) {
			executePurchaseListTask()
		}
	}

	override fun onDismiss(dialog: DialogInterface, code: Int) {
		when (code) {
			Response.CODE_SUCCESS -> {
				if (mode == MODE_TICKET) {
					setResult(RESULT_BUY_TICKET_SUCCESS)
					close()
					return
				}
				val data = Intent()
				data.putExtra(BaseInterface.EXTRA_KANOJO, mKanojo)
				data.putExtra(BaseInterface.EXTRA_LOVE_INCREMENT, mLoveIncrement)
				data.putExtra(BaseInterface.EXTRA_MESSAGES, mKanojoMessage)
				setResult(RESULT_KANOJO_ITEM_USED, data)
				close()
				return
			}
			403 -> {
				if (mode != MODE_TICKET) {
					val data2 = Intent()
					data2.putExtra(EXTRA_KANOJO, mKanojo)
					setResult(RESULT_KANOJO_ITEM_USED, data2)
					close()
					return
				}
				dismissProgressDialog()
				clearQueue()
				return
			}
			else -> return
		}
	}

	private fun isLoading(status: StatusHolder): Boolean {
		return status.loading
	}

	class StatusHolder {
		var key = 0
		var loading = false

		companion object {
			const val GET_TRANSACTION_ID_TASK = 0
			const val REQUEST_PURCHASE_TASK = 1
			const val COMPLETE_PURCHASE_TASK = 2
			const val CHECK_TICKET_TASK = 3
			const val KANOJOITEM_TASK = 4
		}
	}

	private val queue: Queue<StatusHolder?>?
		get() {
			if (mTaskQueue == null) {
				mTaskQueue = LinkedList<StatusHolder?>()
			}
			return mTaskQueue
		}

	@Synchronized
	private fun clearQueue() {
		queue!!.clear()
	}

	@get:Synchronized
	private val isQueueEmpty: Boolean
		get() = mTaskQueue!!.isEmpty()

	@Synchronized
	private fun executeCheckPriceTask() {
		clearQueue()
		val mCheckTicketHolder = StatusHolder()
		mCheckTicketHolder.key = StatusHolder.CHECK_TICKET_TASK
		queue!!.offer(mCheckTicketHolder)
		mTaskEndHandler.sendEmptyMessage(0)
	}

	@Synchronized
	private fun executePurchaseListTask() {
		clearQueue()
		val mCheckTicketHolder = StatusHolder()
		mCheckTicketHolder.key = StatusHolder.CHECK_TICKET_TASK
		val mKanojoHolder = StatusHolder()
		mKanojoHolder.key = StatusHolder.KANOJOITEM_TASK
		val mGetTransactionHolder = StatusHolder()
		mGetTransactionHolder.key = StatusHolder.GET_TRANSACTION_ID_TASK
		val mRequestHolder = StatusHolder()
		mRequestHolder.key = StatusHolder.REQUEST_PURCHASE_TASK
		val mCompleteHolder = StatusHolder()
		mCompleteHolder.key = StatusHolder.COMPLETE_PURCHASE_TASK
		if (mode != 3) {
			if ((mode == 4 || mode == 5) && mKanojoItem!!.has_units == null) {
				queue!!.offer(mCheckTicketHolder)
			}
			queue!!.offer(mKanojoHolder)
		} else {
			queue!!.offer(mGetTransactionHolder)
			queue!!.offer(mRequestHolder)
			queue!!.offer(mCompleteHolder)
		}
		mTaskEndHandler.sendEmptyMessage(0)
	}

	private fun executePurchaseTask(list: StatusHolder) {
		if (isLoading(list)) {
			Log.d(TAG, "task " + list.key + " is running ")
		} else if (list.key != StatusHolder.REQUEST_PURCHASE_TASK) {
			mBuyTicketTask = BuyTicketTask(this, list)
			showProgressDialog()
			mBuyTicketTask!!.execute()
		} else {
			dismissProgressDialog()
		}
	}

	fun nextScreen(list: StatusHolder) {
		val i = list.key
		dismissProgressDialog()
	}

	fun getPriceFromString(price: String?): Int {
		return Scanner(price).useDelimiter("[^0-9]+").nextInt()
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode != KeyEvent.KEYCODE_BACK || !mLoadingView!!.isShow) {
			if (mode == 3) {
				setResult(RESULT_BUY_TICKET_FAIL)
			}
			close()
			return super.onKeyDown(keyCode, event)
		}
		mLoadingView!!.setMessage(getString(R.string.requesting_cant_cancel))
		return true
	}

	public override fun showProgressDialog() {
		mLoadingView!!.show()
	}

	override fun dismissProgressDialog() {
		mLoadingView!!.dismiss()
	}

	companion object {
		private const val DEBUG_PURCHASE = false
		const val MODE_DATE = 1
		const val MODE_GIFT = 2
		const val MODE_TICKET = 3
		const val MODE_EXTEND_DATE = 4
		const val MODE_EXTEND_GIFT = 5

		internal class BuyTicketTask(activity: KanojoItemDetailActivity, list: StatusHolder) : AsyncTask<Void?, Void?, Response<*>?>() {
			private var mList: StatusHolder = list
			private var mReason: Exception? = null
			private val activityRef: WeakReference<KanojoItemDetailActivity>

			init {
				activityRef = WeakReference<KanojoItemDetailActivity>(activity)
			}

			public override fun onPreExecute() {
				val activity: KanojoItemDetailActivity? = activityRef.get()
				if (activity == null || activity.isFinishing) {
					return
				}

				mList.loading = true
			}

			override fun doInBackground(vararg params: Void?): Response<*>? {
				val activity: KanojoItemDetailActivity? = activityRef.get()
				if (activity == null || activity.isFinishing) {
					return null
				}

				return try {
					val barcodeKanojo = (activity.application as BarcodeKanojoApp).barcodeKanojo
					//if (mList == null) {
					//	throw BarcodeKanojoException("process:StatusHolder is null!")
					//}
					return when (mList.key) {
						StatusHolder.GET_TRANSACTION_ID_TASK -> barcodeKanojo.android_get_transaction_id(activity.mKanojoItem!!.item_id)
						StatusHolder.COMPLETE_PURCHASE_TASK -> barcodeKanojo.android_verify_purchased(activity.mKanojoItem!!.item_id, activity.mTransactionId, activity.mReceiptData)
						StatusHolder.CHECK_TICKET_TASK -> barcodeKanojo.android_check_ticket(activity.getPriceFromString(activity.mKanojoItem!!.price), activity.mKanojoItem!!.item_id)
						StatusHolder.KANOJOITEM_TASK -> when (activity.mode) {
							MODE_DATE -> {
								if (activity.mKanojo == null || activity.mKanojoItem == null) {
									null
								} else barcodeKanojo.do_date(activity.mKanojo!!.id, activity.mKanojoItem!!.item_id)
							}

							MODE_GIFT -> {
								if (activity.mKanojo == null || activity.mKanojoItem == null) {
									null
								} else barcodeKanojo.do_gift(activity.mKanojo!!.id, activity.mKanojoItem!!.item_id)
							}

							MODE_TICKET -> {
								if (activity.mKanojo == null || activity.mKanojoItem == null) {
									null
								} else barcodeKanojo.do_ticket(activity.mKanojoItem!!.item_id, activity.getPriceFromString(activity.mKanojoItem!!.price))
							}

							MODE_EXTEND_DATE -> {
								if (activity.mKanojo == null || activity.mKanojoItem == null) {
									return null
								}
								if (activity.mKanojoItem!!.has_units == null) {
									barcodeKanojo.do_ticket(activity.mKanojoItem!!.item_id, activity.getPriceFromString(activity.mKanojoItem!!.price))
								} else barcodeKanojo.do_extend_date(activity.mKanojo!!.id, activity.mKanojoItem!!.item_id)
							}

							MODE_EXTEND_GIFT -> {
								if (activity.mKanojo == null || activity.mKanojoItem == null) {
									return null
								}
								if (activity.mKanojoItem!!.has_units == null) {
									barcodeKanojo.do_ticket(activity.mKanojoItem!!.item_id, activity.getPriceFromString(activity.mKanojoItem!!.price))
								} else barcodeKanojo.do_extend_gift(activity.mKanojo!!.id, activity.mKanojoItem!!.item_id)
							}
							else -> null
						}
						else -> null
					}
				} catch (e: Exception) {
					mReason = e
					null
				}
			}

			public override fun onPostExecute(response: Response<*>?) {
				val activity: KanojoItemDetailActivity? = activityRef.get()
				if (activity == null || activity.isFinishing) {
					return
				}

				try {
					if (response == null) {
						throw BarcodeKanojoException("""response is null! \n${mReason}""".trimIndent())
					}
					if (mList.key == StatusHolder.KANOJOITEM_TASK) {
						val kanojo = response[Kanojo::class.java] as Kanojo?
						if (kanojo != null) {
							activity.mKanojo = kanojo
						}

						val loveIncrement = response[LoveIncrement::class.java] as LoveIncrement?
						if (loveIncrement != null) {
							activity.mLoveIncrement = loveIncrement
						}

						val kanojoMessage = response[KanojoMessage::class.java] as KanojoMessage?
						if (kanojoMessage != null) {
							activity.mKanojoMessage = kanojoMessage
						}

						activity.clearQueue()
					}

					val code = if (activity.isQueueEmpty) {
						activity.getCodeAndShowAlert(response, mReason)
					} else {
						response.code
					}
					when (code) {
						Response.CODE_SUCCESS -> {
							if (mList.key == StatusHolder.GET_TRANSACTION_ID_TASK) {
								activity.mTransactionId = (response[PurchaseItem::class.java] as PurchaseItem).transactiontId
							} else if (mList.key == StatusHolder.CHECK_TICKET_TASK) {
								activity.loadContent(R.string.item_detail_buy_ticket_text)
								if (!activity.mLoadingDone) {
									activity.clearQueue()
									activity.dismissProgressDialog()
									activity.mLoadingDone = true
								}
							}
							if (!activity.isQueueEmpty) {
								activity.mTaskEndHandler.sendEmptyMessage(0)
								return
							}
							return
						}
						Response.CODE_ERROR_NOT_ENOUGH_TICKET -> {
							if (mList.key == StatusHolder.CHECK_TICKET_TASK) {
								if (!activity.mLoadingDone) {
									activity.mLoadingDone = true
									activity.loadContent(R.string.item_detail_go_to_ticket_screen_text)
								} else {
									val ticket = Intent(activity, KanojoItemsActivity::class.java)
									if (activity.mKanojo != null) {
										ticket.putExtra(EXTRA_KANOJO, activity.mKanojo)
									}
									val item = KanojoItem(KanojoItem.TICKET_ITEM_CLASS)
									item.title = activity.resources.getString(R.string.kanojo_items_store)
									ticket.putExtra(EXTRA_KANOJO_ITEM, item)
									ticket.putExtra(EXTRA_KANOJO_ITEM_MODE, MODE_TICKET)
									ticket.putExtra(EXTRA_REQUEST_CODE, REQUEST_BUY_TICKET)
									activity.startActivityForResult(ticket, REQUEST_BUY_TICKET)
								}
								activity.clearQueue()
								activity.dismissProgressDialog()
								return
							}
							return
						}
						Response.CODE_ERROR_BAD_REQUEST, Response.CODE_ERROR_UNAUTHORIZED, Response.CODE_ERROR_NOT_FOUND, Response.CODE_ERROR_SERVER, Response.CODE_ERROR_SERVICE_UNAVAILABLE -> return
						Response.CODE_ERROR_FORBIDDEN -> {
							if (mList.key == StatusHolder.COMPLETE_PURCHASE_TASK) {
								activity.dismissProgressDialog()
								return
							}
							return
						}
						else -> return
					}
				} catch (e: BarcodeKanojoException) {
					activity.clearQueue()
					activity.dismissProgressDialog()
					activity.showToast(activity.getString(R.string.error_internet))
				}
			}

			override fun onCancelled() {
				val activity: KanojoItemDetailActivity? = activityRef.get()
				if (activity == null || activity.isFinishing) {
					return
				}

				activity.dismissProgressDialog()
			}
		}
	}
}