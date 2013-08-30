package com.elephant.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.elephant.widget.dialog.BaseProgressDialog;

public class BaseActivity extends Activity {

	private BaseProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void showProgressDialog(String msg) {
		mProgressDialog = BaseProgressDialog.onCreateDialog(BaseActivity.this,
				msg);
		mProgressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}
