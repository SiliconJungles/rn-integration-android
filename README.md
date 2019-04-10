
Decoupling React Native from existing Android application
=======


## Initialize React Native app

// Skip to step 7 if you already have an existing React Native app

1) Install react-native-cli
    - npm install -g react-native-cli

2) Initialize react-native project inside ReactNativeProject/
    - react-native init ReactNativeProject
    - npm install

3) Point SDK location
    - Create new file: ReactNativeProject/android/local.properties
  
      // Change path to correct path where Android SDK is located
   sdk.dir=/Users/<name>/Library/Android/sdk

4) Connect Android device or run emulator

5) Install and run react-native application
    - cd ReactNativeProject
    - react-native run-android
    - Wait for app to be installed on device then run app

6) Verification
    - You should see 'Welcome to React Native!'

7) Generate bundle.android.js. We will use this file later
    - mkdir bundle/assets
    - mkdir bundle/res
    - react-native bundle --platform android --dev false --entry-file index.js --bundle-output bundle/assets/index.android.bundle --assets-dest bundle/res


## Make app react-native compilable

1) Create new Android application in AndroidProject/ OR Open existing Android application 
    - Android Studio > New Project > Basic Activity

2) Create react-native library
    - File > New > New Module > Android Library
    - name: React Native Library, package: reactnativelibrary

3) Adding react-native library
    - Navigate to ReactNativeProject/node-modules/react-native/android/.../0.xx.x
    - Copy react-native-0.xx.x.aar to AndroidProject/reactnativelibrary/libs/ folder
    - Open build.gradle and enter the following:

          repositories {
              flatDir {
                  dirs 'libs'
              }
          }

          dependencies {
              ...

              compile(name: 'react-native-0.xx.x', ext: 'aar')

              ...
          }


4) Adding react-native dependencies

    - Navigate to ReactNativeProject/node-modules/react-native/android/.../0.xx.x
    - Open react-native-0.xx.x.pom in text file editor
    - Look for <dependency> tag and copy all dependencies to reactnativelibrary/build.gradle
    - Example:
  
          dependencies {

              ...

              //react-native
              compile 'com.facebook.infer.annotation:infer-annotation:0.11.2'
              compile 'javax.inject:javax.inject:1'
              compile 'com.android.support:appcompat-v7:28.0.0'
              compile 'com.facebook.fresco:fresco:1.10.0'
              compile 'com.facebook.fresco:imagepipeline-okhttp3:1.10.0'
              compile 'com.facebook.soloader:soloader:0.6.0'
              compile 'com.google.code.findbugs:jsr305:3.0.2'
              compile 'com.squareup.okhttp3:okhttp:3.12.1'
              compile 'com.squareup.okhttp3:okhttp-urlconnection:3.12.1'
              compile 'com.squareup.okio:okio:1.15.0'
          }


5) Modify app's build.gradle so that reactnativelibrary is visible

        dependencies {
            ...

            implementation project(':reactnativelibrary')
        }

        repositories {
            flatDir {
                dirs '../reactnativelibrary/libs'
            }
        }


6) Create a new ReactActivity


        package com.myapplication
        import com.facebook.react.ReactActivity


        class MainReactActivity : ReactActivity() {

            override fun getMainComponentName(): String = "HelloWorld"
        }


7) Add activity to AndroidManifest.xml

        <activity
                android:name=".MainReactActivity"
                android:label="ReactActivity"
                android:theme="@style/AppTheme.NoActionBar">
        </activity> 


8) Wire up MainReactActivity into your application flow. For testing purposes, we will launch MainReactActivity as the starting activity.

        <activity
                android:name=".MainActivity"
                android:label="@string/app_name"
                android:theme="@style/AppTheme.NoActionBar">
        </activity>

        <activity
                android:name=".MainReactActivity"
                android:label="ReactActivity"
                android:theme="@style/AppTheme.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>


9) Create MainReactApplication OR modify existing Application to extend ReactApplication

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

                override fun getJSMainModuleName(): String {
                    return "index"
                }
            }

            override fun getReactNativeHost(): ReactNativeHost {
                return mReactNativeHost
            }
        }


10) Modify AndroidManifest to use MainReactApplication

        <application
                ...
                android:name="com.myapplication.MainReactApplication">

11) Copy index.android.bundle generated earlier to AndroidProject/app/src/main/assets

12) Run the application
    - You should see 'Welcome to React Native' as your starting screen


## Enabling live reload

1) Enable Developer Support

        @Override
        public boolean getUseDeveloperSupport() {
            return true;
        }

2) May need the following permissions in AndroidManifest.xml

        <uses-permission android:name="android.permission.INTERNET"/>
        <application
            ...
            android:usesCleartextTraffic="true"
            ... >

3) Run Metro Bundler

    - npm run bridge
    - react-native run-android



## Writing Custom Library

https://facebook.github.io/react-native/docs/native-modules-android



## Building and importing custom libraries

**react-native-image-crop-picker**

1) Installation
    - npm install react-native-image-crop-picker

2) Open android project in ReactNativeProject/Android
    // We will only be using this project for compiling modules, but not for actual development

3) Add react-native-image-crop-picker to settings.gradle

        include ':react-native-image-crop-picker'
        project(':react-native-image-crop-picker').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-image-crop-picker/android')

4) Build Variants > Set react-native-image-crop-picker to 'Release'

5) Build
    - Select react-native-image-crop-picker project
    - Build > Build Module 'react-native-image-crop-picker'
    - File should be in react-native-image-crop-picker/build/outputs/aar/react-native-image-crop-picker-release.aar

6) Copy the file over to AndroidProject/reactnativelibraries/lib

7) Add dependencies to reactnativelibrary/build.gradle

        dependencies {
            ...
            compile(name: 'react-native-image-crop-picker-release', ext: 'aar')
        }

8) Copy dependencies from react-native-image-crop-picker/build.gradle to reactnativelibrary/build.gradle

        dependencies {
            ...
            //react-native-image-crop-picker
            implementation 'com.github.yalantis:ucrop:2.2.2-native'
        }

9) Import the library into PackageManager

        override fun getPackages(): List<ReactPackage> {
            return Arrays.asList<ReactPackage>(
                ...
                RNCameraPackage()
            )
        }

10) Add code to ReactNative 

https://github.com/ivpusic/react-native-image-crop-picker


# #Decoupling React Native Host from ReactApplication (Experimental)

Suppose your application contains multiple react-native applications and you would like to open different index.android.bundle files based on the activity started. Currently, all ReactActivities reference ReactApplication which can only serve a single index.android.bundle file. 

However there is a way to move the ReactNativeHost to the ReactActivity, allowing different activities to start different .android.bundle files. This also provides the advantage of being able to separate different react native applications from having different packages visible to them.

1) Create MyReactActivity

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

2) Extend MyReactActivity instead of ReactActivity

        class SecondReactActivity : MyReactActivity() {

            override fun createReactNativeHost(app: Application): ReactNativeHost {
                return object : ReactNativeHost(app) {
                    override fun getUseDeveloperSupport(): Boolean {
                        return false
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

3) Duplicate index.android.bundle and make some changes, name it secondrn.android.bundle

4) Run app
    - App should load secondrn.android.bundle when developerSupport is false
    - App should load from metro server when developerSupport is true


This change is still experimental and works so far to my knowledge. However it has not been tested extensively and there may be some bugs when using other libraries/functions.

