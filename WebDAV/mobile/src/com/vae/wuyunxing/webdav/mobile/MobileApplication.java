package com.vae.wuyunxing.webdav.mobile;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.vae.wuyunxing.webdav.library.config.LibraryConfig;
import com.vae.wuyunxing.webdav.library.log.CustomLogger;
import com.vae.wuyunxing.webdav.library.log.MKLog;
import com.vae.wuyunxing.webdav.mobile.config.MobileConfig;

import de.greenrobot.event.EventBus;

public class MobileApplication extends Application implements Application.ActivityLifecycleCallbacks {


	@Override
	public void onCreate() {
		super.onCreate();

		LibraryConfig.initialize(MobileApplication.this);
		MobileConfig.initialize(MobileApplication.this);

		if (MobileConfig.getInstance().getBoolean(MobileConfig.DEBUG, false)) {
			MKLog.setCustomLogger(new MobileLogger());
		}
	}

	private int mActivityCount = 0;

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		if (mActivityCount == 0) {
			/** first run the app */
			initializeApplication();
		}
		mActivityCount++;
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		mActivityCount--;
		if (mActivityCount == 0) {
			/** exit the app */
			deinitializeApplication();
		}
	}

	protected void initializeApplication() {

		EventBus.getDefault().postSticky(new APPStartupEvent());
	}

	protected void deinitializeApplication() {
		EventBus.getDefault().postSticky(new APPTerminateEvent());
	}


	public static final class MobileLogger implements CustomLogger {

		@Override
		public boolean isDebugEnabled() {
			return true;
		}

		@Override
		public void d(Class<?> clazz, String format, Object... args) {
			Log.d(clazz.getSimpleName(), String.format(format, args));
		}

		@Override
		public void e(Class<?> clazz, String format, Object... args) {
			Log.e(clazz.getSimpleName(), String.format(format, args));
		}

		@Override
		public void e(Class<?> clazz, Throwable t, String format, Object... args) {
			Log.e(clazz.getSimpleName(), String.format(format, args), t);
		}
	}
}
