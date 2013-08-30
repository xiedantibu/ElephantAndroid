package com.elephant.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.elephant.R;

public class BaseProgressDialog extends Dialog {

	private static BaseProgressDialog mDialog;

	public BaseProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public BaseProgressDialog(Context context) {
		super(context);
	}

	public static BaseProgressDialog onCreateDialog(Context context, String msg) {
		mDialog = new BaseProgressDialog(context, R.style.CustomProgressDialog);
		mDialog.setContentView(R.layout.layout_loading_progress);
		mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		TextView mTextViewMsg = (TextView) mDialog
				.findViewById(R.id.loading_msg);
		mTextViewMsg.setText(msg);
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		return mDialog;
	}
}
