package com.myapplication

import android.app.Application
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import java.util.*

class MainReactApplication : Application(), ReactApplication {

    private val mReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            // return false or buildconfig.debug for release
            // return BuildConfig.DEBUG;
            // return true for debugging
            return true
        }

        override fun getPackages(): List<ReactPackage> {
            return Arrays.asList<ReactPackage>(
                MainReactPackage()
            )
        }

        override fun getBundleAssetName(): String {
            return "index.android.bundle"
        }
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }
}
