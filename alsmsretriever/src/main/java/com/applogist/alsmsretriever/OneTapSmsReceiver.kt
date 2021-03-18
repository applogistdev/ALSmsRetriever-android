package com.applogist.alsmsretriever

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/*
*  Created by Mustafa Ürgüplüoğlu on 11.02.2020.
*  Copyright © 2020 Mustafa Ürgüplüoğlu. All rights reserved.
*/

interface OneTapSmsListener {
    fun onSuccess(message: String)
    fun onFailure(errorCode: Int)
}

class OneTapSmsReceiver : BroadcastReceiver(), LifecycleObserver {

    companion object {

        var instance: OneTapSmsReceiver? = null
            get() {
                if (field == null) field = OneTapSmsReceiver()
                return field!!
            }

        const val REQUEST_CODE_ONE_TAP = 1299
        const val ERROR_INTENT_NULL = 12
        const val ERROR_INITIALIZE_FAILED = 99
    }

    private lateinit var activity: Activity
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var listener: OneTapSmsListener

    /** SMS from in contact list person not will be received
     * @param phoneNumber listen SMS only from this phone number (optional)
     */
    fun start(
        activity: Activity,
        lifecycleOwner: LifecycleOwner,
        listener: OneTapSmsListener,
        phoneNumber: String? = null
    ) {
        this.activity = activity
        this.lifecycleOwner = lifecycleOwner
        this.listener = listener
        lifecycleOwner.lifecycle.addObserver(instance!!)

        SmsRetriever.getClient(activity).also {
            it.startSmsUserConsent(phoneNumber)
                .addOnSuccessListener {
                    Log.d("OneTapSmsReceiver", "LISTENING_SUCCESS")
                }
                .addOnFailureListener {
                    listener.onFailure(ERROR_INITIALIZE_FAILED)
                }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {

            val extras = intent.extras!!
            val smsRetrieverStatus = extras.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        .also { readIntent ->
                            if (readIntent != null) {
                                startActivityForResult(
                                    activity,
                                    readIntent,
                                    REQUEST_CODE_ONE_TAP,
                                    null
                                )
                            } else {
                                listener.onFailure(ERROR_INTENT_NULL)
                            }
                        }
                }

                else -> {
                    listener.onFailure(smsRetrieverStatus.statusCode)
                }

            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        activity.registerReceiver(instance, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        activity.unregisterReceiver(instance)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == REQUEST_CODE_ONE_TAP) && (resultCode == Activity.RESULT_OK)
            && (data != null)
        ) {
            val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            if (message != null) {
                listener.onSuccess(message)
            } else {
                listener.onFailure(ERROR_INTENT_NULL)
            }
        }

    }
}