package com.applogist.alsmsretriever

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/*
*  Created by Mustafa Ürgüplüoğlu on 11.02.2020.
*  Copyright © 2020 Mustafa Ürgüplüoğlu. All rights reserved.
*/

interface SmsRetrieverListener {
    fun onReceive(message: String)
    fun onFailure(errorCode: Int)
}

class SmsRetrieverReceiver : BroadcastReceiver(), LifecycleObserver {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: SmsRetrieverReceiver? = null
            get() {
                if (field == null) field = SmsRetrieverReceiver()
                return field!!
            }
    }

    private var smsRetrieverClient: SmsRetrieverClient? = null
    private lateinit var activity: Activity
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var listener: SmsRetrieverListener

    fun start(activity: Activity, lifecycleOwner: LifecycleOwner, listener: SmsRetrieverListener) {
        this.smsRetrieverClient = SmsRetriever.getClient(activity)
        this.smsRetrieverClient?.startSmsRetriever()
        this.activity = activity
        this.lifecycleOwner = lifecycleOwner
        this.listener = listener
        lifecycleOwner.lifecycle.addObserver(instance!!)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null && SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val status = intent.extras!!.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val smsMessage = intent.extras!!.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    listener.onReceive(smsMessage)
                }
                else -> {
                    listener.onFailure(status.statusCode)
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
}