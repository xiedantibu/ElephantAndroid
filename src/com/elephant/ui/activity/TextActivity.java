package com.elephant.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.elephant.R;

public class TextActivity extends BaseActivity {

	private Button mButtonClick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text);
		mButtonClick = (Button) findViewById(R.id.text_bt_click);
		mButtonClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgressDialog("加载中...");
				mButtonClick.setVisibility(View.GONE);
			}
		});
	}
}
