package jp.co.cybird.app.android.lib.applauncher;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import com.google.android.gcm.GCMRegistrar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import jp.co.cybird.app.android.lib.commons.file.FileUtil;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.http.AsyncHttpClient;
import jp.co.cybird.app.android.lib.commons.http.AsyncHttpResponseHandler;
import jp.co.cybird.app.android.lib.commons.http.JsonHttpResponseHandler;
import jp.co.cybird.app.android.lib.commons.http.RequestParams;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.misc.PackageUtil;
import jp.co.cybird.app.android.lib.commons.net.NetworkUtil;
import jp.co.cybird.app.android.lib.commons.tracker.TrackerWrapper;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppLauncher implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
	private static final int SCHEME_DATA_EXISTS = 1;
	private static final int SCHEME_DATA_NOT_EXISTS = 0;
	private static final int SCHEME_DATA_OLD = -1;
	private static boolean isShown = false;
	private String mCategory;
	private Context mContext;
	private AppLauncherAdapter mCyAdapter = null;
	private Dialog mDialog;
	private List<Scheme> mSchemeList = null;
	private SlidingDrawer mSlidingDrawer;
	private AppLauncherConsts.LAUNCHER_TYPE mType;

	AppLauncher(Context context, AppLauncherConsts.LAUNCHER_TYPE type, String category) {
		this.mContext = context;
		this.mType = type;
		this.mCategory = category;
		setUserAgent(context);
		createDialog(context);
		TrackerWrapper.init(context);
	}

	static void setUserAgent(Context context) {
		if (AppLauncherConsts.getUserAgent() == null) {
			AppLauncherConsts.setUserAgent(new WebView(context.getApplicationContext()).getSettings().getUserAgentString());
		}
	}

	private void createDialog(final Context context) {
		this.mDialog = new Dialog(context, ParamLoader.getResourceIdForType("Launcher.Dialog", "style", context)) {

			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(ParamLoader.getResourceIdForType("lib_launcher_main", "layout", AppLauncher.this.mContext));
				AppLauncher.this.mSlidingDrawer = (SlidingDrawer) findViewById(ParamLoader.getResourceIdForType("lib_launcher_slidingdrawer", "id", AppLauncher.this.mContext));
				AppLauncher.this.mSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
					public void onDrawerClosed() {
						dismiss();
					}
				});
				((Button) findViewById(ParamLoader.getResourceIdForType("lib_launcher_close_button", "id", AppLauncher.this.mContext))).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						dismiss();
					}
				});
				ListView listview = (ListView) findViewById(ParamLoader.getResourceIdForType("lib_launcher_listview", "id", AppLauncher.this.mContext));
				TextView blankView = (TextView) findViewById(ParamLoader.getResourceIdForType("lib_launcher_blank", "id", AppLauncher.this.mContext));
				if (AppLauncher.this.mCyAdapter != null) {
					DLog.d(AppLauncherConsts.TAG, "mCyAdapter is not null");
					listview.setVisibility(View.VISIBLE);
					blankView.setVisibility(View.GONE);
					listview.setAdapter(AppLauncher.this.mCyAdapter);
					listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Scheme item = (Scheme) parent.getItemAtPosition(position);
							AppLauncher.this.track(context, item.getProduct_id(), "&referrer=utm_source%3Dlauncher%26utm_content%3Dlist");
							if (PackageUtil.goApp(context, item.getProduct_id(), "&referrer=utm_source%3Dlauncher%26utm_content%3Dlist")) {
								TrackerWrapper.sendEvent(AppLauncherConsts.GA_LAUNCHER_CATEGORY, AppLauncherConsts.GA_LAUNCHER_TAP_INSTALLED_APP_ACTION, item.getTitle(), 1);
							} else {
								TrackerWrapper.sendEvent(AppLauncherConsts.GA_LAUNCHER_CATEGORY, AppLauncherConsts.GA_LAUNCHER_TAP_NEW_APP_ACTION, item.getTitle(), 1);
							}
						}
					});
					return;
				}
				DLog.d(AppLauncherConsts.TAG, "mCyAdapter is null");
				blankView.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				blankView.setText(ParamLoader.getString("lib_launcher_blank_message", AppLauncher.this.mContext));
			}
		};
		this.mDialog.setOnShowListener(this);
		this.mDialog.setOnCancelListener(this);
		this.mDialog.setOnDismissListener(this);
	}

	private void loadSchemeData(Context context) {
		TrackerWrapper.sendEvent(AppLauncherConsts.GA_LAUNCHER_CATEGORY, AppLauncherConsts.GA_LAUNCHER_INITIATION_ACTION, AppLauncherConsts.GA_LAUNCHER_LOAD_SCHEME_LABEL, 1);
		this.mSchemeList = parseScheme(context);
		if (this.mSchemeList == null) {
			prepareSchemeData(context, this.mType, this.mCategory);
			this.mSchemeList = parseScheme(context);
		}
		if (this.mSchemeList != null) {
			DLog.d(AppLauncherConsts.TAG, "schemeList.LENGTH:".concat(String.valueOf(this.mSchemeList.size())));
			this.mCyAdapter = new AppLauncherAdapter(context, this.mSchemeList);
		}
	}

	public void onPause() {
		if (this.mDialog != null && this.mDialog.isShowing()) {
			isShown = true;
		}
	}

	public void onResume() {
		if (isShown && !this.mDialog.isShowing()) {
			this.mDialog.show();
			this.mSlidingDrawer.open();
		}
	}

	public void showLauncher() {
		if (this.mDialog != null) {
			if (this.mSchemeList == null || this.mCyAdapter == null) {
				Log.d("SISSI", "start loadSchemeData()");
				loadSchemeData(this.mContext);
			}
			this.mDialog.show();
			this.mSlidingDrawer.open();
			TrackerWrapper.sendEvent(AppLauncherConsts.GA_LAUNCHER_CATEGORY, AppLauncherConsts.GA_LAUNCHER_SHOW_LAUNCHER_ACTION, AppLauncherConsts.GA_LAUNCHER_SHOW_LAUNCHER_LABEL, 1);
		}
	}

	public void onCancel(DialogInterface dialog) {
		isShown = false;
	}

	public void onDismiss(DialogInterface dialog) {
		isShown = false;
	}

	public void onShow(DialogInterface dialog) {
		isShown = true;
	}

	public static void prepareSchemeData(final Context context, AppLauncherConsts.LAUNCHER_TYPE type, String category) {
		setUserAgent(context);
		DLog.setDebuggable(false);
		if (category == null || category.equals("")) {
			category = AppLauncherConsts.DEFAULT_SCHEME_DATA_CATEGORY;
		}
		final String schemeFileName = String.format(AppLauncherConsts.FORMAT_FILENAME_SCHEME_DATA, new Object[]{category});
		int schemeState = existsSchemeData(context, schemeFileName);
		DLog.d(AppLauncherConsts.TAG, "schemeState:".concat(String.valueOf(schemeState)));
		if (schemeState <= 0 && NetworkUtil.isNetworkConnected(context)) {
			DLog.i(AppLauncherConsts.TAG, "--- getting scheme data file ---");
			RequestParams params = new RequestParams();
			params.put("v", GreeDefs.KANOJO_NAME);
			params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL, AppLauncherCommons.getGeneralParamsString(context, type, category));
			AsyncHttpClient httpClient = new AsyncHttpClient();
			httpClient.setUserAgent(AppLauncherConsts.getUserAgent());
			DLog.d(AppLauncherConsts.TAG, "ua:" + AppLauncherConsts.getUserAgent());
			httpClient.get("http://app.sf.cybird.ne.jp/scheme", params, (AsyncHttpResponseHandler) new JsonHttpResponseHandler() {
				public void onSuccess(JSONArray response) {
					DLog.i(AppLauncherConsts.TAG, "got scheme file. saving to ".concat(schemeFileName));
					try {
						FileUtil.saveFileInAppDirectory(context, response.toString(), schemeFileName, true);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}

				public void onFailure(Throwable e, JSONObject errorResponse) {
					DLog.i(AppLauncherConsts.TAG, "failure get json...");
				}
			});
		}
	}

	private static int existsSchemeData(Context context, String schemeFileName) {
		File schemeFile = new File(String.valueOf(context.getFilesDir().getPath()) + File.separator + schemeFileName);
		if (schemeFile.exists()) {
			return schemeFile.lastModified() < System.currentTimeMillis() - GCMRegistrar.DEFAULT_ON_SERVER_LIFESPAN_MS ? -1 : 1;
		}
		return 0;
	}

	private List<Scheme> parseScheme(Context context) {
		DLog.i(AppLauncherConsts.TAG, "--- parse scheme file to object. --->");
		Object[] objArr = new Object[1];
		objArr[0] = this.mCategory != null ? this.mCategory : AppLauncherConsts.DEFAULT_SCHEME_DATA_CATEGORY;
		String schemeFileName = String.format(AppLauncherConsts.FORMAT_FILENAME_SCHEME_DATA, objArr);
		DLog.i(AppLauncherConsts.TAG, "schemeFileName: " + schemeFileName);
		try {
			FileInputStream is = context.openFileInput(schemeFileName);
			StringBuilder sb = new StringBuilder();
			BufferedReader buffR = new BufferedReader(new InputStreamReader(is));
			while (true) {
				try {
					String lineText = buffR.readLine();
					if (lineText == null) {
						break;
					}
					sb.append(lineText);
				} catch (IOException e) {
					e.printStackTrace();
					try {
						buffR.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					return null;
				} catch (Throwable th) {
					try {
						buffR.close();
					} catch (IOException e3) {
						e3.printStackTrace();
					}
					throw th;
				}
			}
			Scheme[] schemeArray = (Scheme[]) new JSON().parse((CharSequence) sb, Scheme[].class);
			DLog.d(AppLauncherConsts.TAG, "parse ".concat(String.valueOf(schemeArray.length)).concat(" schemes."));
			List<Scheme> list = Arrays.asList(schemeArray);
			try {
				buffR.close();
			} catch (IOException e4) {
				e4.printStackTrace();
			}
			return list;
		} catch (FileNotFoundException e5) {
			DLog.e(AppLauncherConsts.TAG, "FileNotFoundException");
			return null;
		}
	}

	private void track(Context context, String product_id, String param) {
		RequestParams params = new RequestParams();
		params.put("v", GreeDefs.KANOJO_NAME);
		params.put(AppLauncherConsts.REQUEST_PARAM_GENERAL, AppLauncherCommons.getGeneralParamsString(context, this.mType, this.mCategory, product_id));
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.setUserAgent(AppLauncherConsts.getUserAgent());
		DLog.d(AppLauncherConsts.TAG, "ua:" + AppLauncherConsts.getUserAgent());
		httpClient.get("http://app.sf.cybird.ne.jp/track", params, new AsyncHttpResponseHandler() {
		});
	}
}
