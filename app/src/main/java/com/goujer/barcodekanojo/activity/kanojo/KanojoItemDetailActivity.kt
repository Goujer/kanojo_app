package com.goujer.barcodekanojo.activity.kanojo

import android.app.ProgressDialog
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

import com.goujer.barcodekanojo.core.cache.DynamicImageCache

import com.goujer.barcodekanojo.BarcodeKanojoApp
import com.goujer.barcodekanojo.core.model.Kanojo
import com.goujer.barcodekanojo.core.model.User
import com.goujer.barcodekanojo.R
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity
import jp.co.cybird.barcodekanojoForGAM.activity.kanojo.KanojoItemsActivity
import jp.co.cybird.barcodekanojoForGAM.billing.util.Inventory
import jp.co.cybird.barcodekanojoForGAM.billing.util.Purchase
import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi
import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi.OnPurchaseListener
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException
import jp.co.cybird.barcodekanojoForGAM.core.model.*
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import java.io.IOException
import java.util.*

class KanojoItemDetailActivity : BaseActivity(), View.OnClickListener {
	private var btnOk: Button? = null
	private var imgView: ImageView? = null
	private val lstProductId: List<String>? = null
	private var mBuyTicketTask: BuyTicketTask? = null
	private var mInventory: Inventory? = null
	private var mKanojo: Kanojo? = null
	private var mKanojoItem: KanojoItem? = null
	private var mListener: OnPurchaseListener? = null
	private var mLoadingDone = false
	private var mLoadingView: CustomLoadingView? = null
	private var mLoveIncrement: LoveIncrement? = null
	private lateinit var mPurchaseAPI: PurchaseApi
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
		mPurchaseAPI = (application as BarcodeKanojoApp).mPurchaseApi
		mListener = object : OnPurchaseListener {
			override fun onSetUpFailed(message: String) {
				Log.d(TAG, "setUp Purchase failed $message")
			}

			override fun onSetUpDone(inventory: Inventory) {
				mInventory = inventory
			}

			override fun onPurchaseDone(purchase: Purchase) {
				mReceiptData = purchase.orderId
				Log.d("Purchase", "start to consume item " + purchase.sku)
				mPurchaseAPI.consumeItem(purchase)
			}

			override fun onPurchaseFailed(message: String) {
				Log.d(TAG, "Error purchasing: $message")
				this@KanojoItemDetailActivity.showNoticeDialog(message)
				clearQueue()
			}

			override fun onConsumeDone(purchase: Purchase, message: String) {
				if (!isQueueEmpty) {
					mTaskEndHandler.sendEmptyMessage(0)
				}
			}

			override fun onConsumeFail(purchase: Purchase, message: String) {
				Log.d(TAG, "Purchase onConsumeDone failed $message")
				this@KanojoItemDetailActivity.showNoticeDialog(message)
				clearQueue()
				dismissProgressDialog()
			}
		}
		mPurchaseAPI.setListener(mListener)
	}

	override fun onResume() {
		super.onResume()
		if (mLoadingDone || !((mode == 4 || mode == 5) && mKanojoItem!!.has_units == null)) {
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

	override  fun onStop() {
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
		if (mode == 3) {
			btnOk!!.text = resources.getString(R.string.kanojo_shop_buy)
		} else if (mode == 2 || mode == 1 || mKanojoItem!!.has_units != null) {
			if (mode == 2 || mode == 5) {
				btnOk!!.text = resources.getString(R.string.item_detail_button_ok_text)
			} else if (mode == 1 || mode == 4) {
				btnOk!!.text = resources.getString(R.string.item_detail_date_button_ok_text)
			}
		}
		if (btnTextRes > 0) {
			btnOk!!.setText(btnTextRes)
		}
	}

	fun hideContent() {
		imgView!!.visibility = View.INVISIBLE
		txtDescription!!.visibility = View.INVISIBLE
		btnOk!!.visibility = View.INVISIBLE
	}

	public override fun getClientView(): View {
		val leyout = layoutInflater.inflate(R.layout.activity_kanojo_item_detail, null as ViewGroup?)
		val appLayoutRoot = LinearLayout(this)
		appLayoutRoot.addView(leyout)
		return appLayoutRoot
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		if (mPurchaseAPI!!.helper == null || !mPurchaseAPI!!.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data)
			if (requestCode == 1009 && resultCode == 207) {
				close()
			} else if (requestCode == 1106) {
				hideContent()
				mLoadingDone = false
			}
		} else {
			Log.d(TAG, "resume from google play")
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
			200 -> {
				if (mode == 3) {
					setResult(RESULT_BUY_TICKET_SUCCESS)
					close()
					return
				}
				val data = Intent()
				data.putExtra(EXTRA_KANOJO, mKanojo)
				data.putExtra(EXTRA_LOVE_INCREMENT, mLoveIncrement)
				setResult(RESULT_KANOJO_ITEM_USED, data)
				close()
				return
			}
			403 -> {
				if (mode != 3) {
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
		return if (status.loading) {
			true
		} else false
	}

	class StatusHolder {
		var key = 0
		var loading = false

		companion object {
			const val CHECK_TICKET_TASK = 3
			const val COMPLETE_PURCHASE_TASK = 2
			const val GET_TRANSACTION_ID_TASK = 0
			const val KANOJOITEM_TASK = 4
			const val REQUEST_PURCHASE_TASK = 1
		}
	}

	private val queue: Queue<StatusHolder?>?
		private get() {
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
		private get() = mTaskQueue!!.isEmpty()

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
		mKanojoHolder.key = 4
		val mGetTransactionHolder = StatusHolder()
		mGetTransactionHolder.key = 0
		val mRequestHolder = StatusHolder()
		mRequestHolder.key = 1
		val mCompleteHolder = StatusHolder()
		mCompleteHolder.key = 2
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
		} else if (list.key != 1) {
			mBuyTicketTask = BuyTicketTask()
			mBuyTicketTask!!.setList(list)
			showProgressDialog()
			mBuyTicketTask!!.execute(*arrayOfNulls<Void>(0))
		} else {
			dismissProgressDialog()
			try {
				mPurchaseAPI!!.BuyProduct(this, mKanojoItem!!.item_purchase_product_id, mKanojoItem!!.item_id.toString() + "-" + mTransactionId)
			} catch (e: Exception) {
			}
		}
	}

	internal inner class BuyTicketTask : AsyncTask<Void?, Void?, Response<*>?>() {
		private var mList: StatusHolder? = null
		private var mReason: Exception? = null
		fun setList(list: StatusHolder?) {
			mList = list
		}

		public override fun onPreExecute() {
			mList!!.loading = true
		}

		override fun doInBackground(vararg params: Void?): Response<*>? {
			return try {
				process(mList)
			} catch (e: Exception) {
				mReason = e
				null
			}
		}

		public override fun onPostExecute(response: Response<*>?) {
			val code: Int
			try {
				if (mReason != null) {
				}
				if (response == null) {
					throw BarcodeKanojoException("""response is null! \n${mReason}""".trimIndent())
				}
				if (mList!!.key == 4) {
					val kanojo = response[Kanojo::class.java] as Kanojo
					if (kanojo != null) {
						mKanojo = kanojo
					}
					val loveIncrement = response[LoveIncrement::class.java] as LoveIncrement
					if (loveIncrement != null) {
						mLoveIncrement = loveIncrement
					}
					clearQueue()
				}
				code = if (isQueueEmpty) {
					this@KanojoItemDetailActivity.getCodeAndShowAlert(response, mReason)
				} else {
					response.code
				}
				when (code) {
					200 -> {
						if (mList!!.key == 0) {
							mTransactionId = (response[PurchaseItem::class.java] as PurchaseItem).transactiontId
						} else if (mList!!.key == 3) {
							loadContent(R.string.item_detail_buy_ticket_text)
							if (!mLoadingDone) {
								clearQueue()
								dismissProgressDialog()
								mLoadingDone = true
							}
						}
						if (!isQueueEmpty) {
							mTaskEndHandler.sendEmptyMessage(0)
							return
						}
						return
					}
					Response.CODE_ERROR_NOT_ENOUGH_TICKET -> {
						if (mList!!.key == 3) {
							if (!mLoadingDone) {
								mLoadingDone = true
								loadContent(R.string.item_detail_go_to_ticket_screen_text)
							} else {
								val ticket = Intent(this@KanojoItemDetailActivity, KanojoItemsActivity::class.java)
								if (mKanojo != null) {
									ticket.putExtra(EXTRA_KANOJO, mKanojo)
								}
								val item = KanojoItem(3)
								item.title = this@KanojoItemDetailActivity.resources.getString(R.string.kanojo_items_store)
								ticket.putExtra(EXTRA_KANOJO_ITEM, item)
								ticket.putExtra(EXTRA_KANOJO_ITEM_MODE, 3)
								ticket.putExtra(EXTRA_REQUEST_CODE, REQUEST_BUY_TICKET)
								this@KanojoItemDetailActivity.startActivityForResult(ticket, REQUEST_BUY_TICKET)
							}
							clearQueue()
							dismissProgressDialog()
							return
						}
						return
					}
					400, 401, 404, 500, 503 -> return
					403 -> {
						if (mList!!.key == 2) {
							dismissProgressDialog()
							return
						}
						return
					}
					else -> return
				}
			} catch (e: BarcodeKanojoException) {
				clearQueue()
				dismissProgressDialog()
				showToast(this@KanojoItemDetailActivity.getString(R.string.error_internet))
			}
		}

		override fun onCancelled() {
			dismissProgressDialog()
		}

		@Throws(BarcodeKanojoException::class, IllegalStateException::class, IOException::class)
		fun process(list: StatusHolder?): Response<*>? {
			val barcodeKanojo = (this@KanojoItemDetailActivity.application as BarcodeKanojoApp).barcodeKanojo
			if (list == null) {
				throw BarcodeKanojoException("process:StatusHolder is null!")
			}
			return when (list.key) {
				0 -> barcodeKanojo.android_get_transaction_id(mKanojoItem!!.item_id)
				2 -> barcodeKanojo.android_verify_purchased(mKanojoItem!!.item_id, mTransactionId, mReceiptData)
				3 -> barcodeKanojo.android_check_ticket(getPriceFromString(mKanojoItem!!.price), mKanojoItem!!.item_id)
				4 -> when (mode) {
					1 -> {
						if (mKanojo == null || mKanojoItem == null) {
							null
						} else barcodeKanojo.do_date(mKanojo!!.id, mKanojoItem!!.item_id)
					}
					2 -> {
						if (mKanojo == null || mKanojoItem == null) {
							null
						} else barcodeKanojo.do_gift(mKanojo!!.id, mKanojoItem!!.item_id)
					}
					3 -> {
						if (mKanojo == null || mKanojoItem == null) {
							null
						} else barcodeKanojo.do_ticket(mKanojoItem!!.item_id, getPriceFromString(mKanojoItem!!.price))
					}
					4 -> {
						if (mKanojo == null || mKanojoItem == null) {
							return null
						}
						if (mKanojoItem!!.has_units == null) {
							barcodeKanojo.do_ticket(mKanojoItem!!.item_id, getPriceFromString(mKanojoItem!!.price))
						} else barcodeKanojo.do_extend_date(mKanojo!!.id, mKanojoItem!!.item_id)
					}
					5 -> {
						if (mKanojo == null || mKanojoItem == null) {
							return null
						}
						if (mKanojoItem!!.has_units == null) {
							barcodeKanojo.do_ticket(mKanojoItem!!.item_id, getPriceFromString(mKanojoItem!!.price))
						} else barcodeKanojo.do_extend_gift(mKanojo!!.id, mKanojoItem!!.item_id)
					}
					else -> null
				}
				else -> null
			}
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
		if (keyCode != 4 || !mLoadingView!!.isShow) {
			if (mode == 3) {
				setResult(RESULT_BUY_TICKET_FAIL)
			}
			close()
			return super.onKeyDown(keyCode, event)
		}
		mLoadingView!!.setMessage(getString(R.string.requesting_cant_cancel))
		return true
	}

	public override fun showProgressDialog(): ProgressDialog {
		mLoadingView!!.show()
		return ProgressDialog(this)
	}

	override fun dismissProgressDialog() {
		mLoadingView!!.dismiss()
	}

	companion object {
		private const val DEBUG_PURCHASE = false
		const val MODE_DATE = 1
		const val MODE_EXTEND_DATE = 4
		const val MODE_EXTEND_GIFT = 5
		const val MODE_GIFT = 2
		const val MODE_TICKET = 3
		private const val TAG = "KanojoItemDetailActivit"
	}
}