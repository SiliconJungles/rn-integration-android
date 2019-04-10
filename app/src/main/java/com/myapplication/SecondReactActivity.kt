package com.myapplication

import android.app.Application
import com.facebook.react.ReactActivity
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.myapplication.reactnativelibrary.customtoast.MyToastPackage
import com.reactnative.ivpusic.imagepicker.PickerPackage
import java.util.*


class SecondReactActivity : MyReactActivity() {

    override fun createReactNativeHost(app: Application): ReactNativeHost {
        return object : ReactNativeHost(app) {
            override fun getUseDeveloperSupport(): Boolean {
                return true
            }

            override fun getPackages(): List<ReactPackage> {
                return Arrays.asList(
                    MainReactPackage(),
                    MyToastPackage(),
                    PickerPackage()
                )
            }

            override fun getJSMainModuleName(): String {
                return "index"
            }

            override fun getBundleAssetName(): String? {
                return "secondrn.android.bundle"
            }
        }
    }

    override fun getMainComponentName(): String = "HelloWorld"
}
