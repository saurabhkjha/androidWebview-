package com.androidexample.webview;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.Toast;

public class ShowWebView extends Activity {

	//private Button button;
	private WebView webView;
	private static final String TAG = "Main";
	final Activity activity = this;
	private ProgressDialog progressBar;
	public static String barcode = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_web_view);
		String webViewUrl = "https://54.154.224.145/webview.html";
		webView = (WebView) findViewById(R.id.webView1);    //you might need to change webView1

		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		progressBar = ProgressDialog.show(ShowWebView.this, "WebView Example", "Loading...");

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i(TAG, "Finished loading URL: " + url);
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Log.e(TAG, "Error: " + description);
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
				alertDialog.setTitle("Error");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				alertDialog.show();
			}
		});
		webView.loadUrl(webViewUrl);
		
	}
	public class JavaScriptInterface {
		Context mContext;

		// Instantiate the interface and set the context
		JavaScriptInterface(Context c) {
			mContext = c;
		}

		// using Javascript to call the finish activity
		public void closeMyActivity() {
			finish();
		}

		public void scanBarcode() {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.setPackage("com.google.zxing.client.android");
			startActivityForResult(intent, 0);
		}
		public String getBarcode(){
			return barcode;
		}
	}   //JavascriptInterface

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				//here is where you get your result
				barcode = intent.getStringExtra("SCAN_RESULT");
			}
			else {
				barcode = "nothing";
			}
		}else {
			barcode = "nothing";
		}
	}



}