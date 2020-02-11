
# ALSmsRetriever

## Installation
[![](https://jitpack.io/v/applogistdev/ALSmsRetriever-android.svg)](https://jitpack.io/#applogistdev/ALSmsRetriever-android)
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.applogistdev:ALSmsRetriever-android:lastVersion'
}
```

## Usage with SMS User Consent API (https://developers.google.com/identity/sms-retriever/user-consent/overview)

Register your listener

```kotlin
        OneTapSmsReceiver.instance?.setLifeCycleOwner(this,this, object :
            OneTapSmsReceiver.OneTapSmsVerificationListener{
            override fun onSuccess(message: String) {
                Log.e("onSuccess", message)
            }

            override fun onFailure(errorCode: Int) {
                Log.e("onFailure", errorCode.toString())
            }
        })
```

Add onActivityResult

```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        OneTapSmsReceiver.instance?.handleOnActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }
```