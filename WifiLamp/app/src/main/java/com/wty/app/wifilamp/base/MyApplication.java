package com.wty.app.wifilamp.base;

import android.app.Application;

import com.wty.app.wifilamp.util.PreferenceUtil;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceUtil.init(this);
	}

}
