package com.myapplication;

import android.app.Application;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;

public abstract class MyReactActivity extends ReactActivity {
    private ReactNativeHost mReactNativeHost;
    protected abstract String getMainComponentName();
    protected abstract ReactNativeHost createReactNativeHost(Application app);

    protected ReactActivityDelegate createReactActivityDelegate() {

        return new ReactActivityDelegate(this, getMainComponentName()) {
            @Override
            protected ReactNativeHost getReactNativeHost() {
                if (mReactNativeHost==null) {
                    mReactNativeHost = createReactNativeHost(getApplication());
                }
                return mReactNativeHost;
            }
        };
    }
}