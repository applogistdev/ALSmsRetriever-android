
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
        OneTapSmsReceiver.instance?.start(this,this, object :
            OneTapSmsListener {
            override fun onSuccess(message: String) {
                Log.d("onSuccess", message)
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

## Usage with SMS Retriever API (https://developers.google.com/identity/sms-retriever/overview)

Create your Debug or Release signature with AppSignatureHelper (IMPORTANT: When you release at Google Play with Google App Signing this Release signature won't work.)

```kotlin
AppSignatureHelper(this).appSignatures.toString()
```

You must send your message with this signature. Sample: "Yumm! Pie à la Android mode! 123456 1Ouzi0b+Kxq"

Register your listener

```kotlin
        SmsRetrieverReceiver.instance?.start(this,this, object : SmsRetrieverListener{
            override fun onReceive(message: String) {
                Log.d("message", message)
            }

            override fun onFailure(errorCode: Int) {
                Log.e("onFailure", errorCode.toString())
            }

        })
```

## Get Google Play Release Hash
https://stackoverflow.com/questions/51365778/how-to-generate-11-char-hash-key-for-sms-retriever-with-google-app-signing