package com.applogist.alsmsretriever_sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applogist.alsmsretriever.OneTapSmsReceiver
import com.applogist.alsmsretriever.SmsRetrieverListener
import com.applogist.alsmsretriever.SmsRetrieverReceiver

/*
*  Created by Mustafa Ürgüplüoğlu on 11.02.2020.
*  Copyright © 2020 Mustafa Ürgüplüoğlu. All rights reserved.
*/

class SmsRetrieverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smsretriever)


        //TODO Send message with this signature
        // Sample: "Yumm! Pie à la Android mode! 123456 1Ouzi0b+Kxq"
        Log.e("Signatures", AppSignatureHelper(this).appSignatures.toString())


        SmsRetrieverReceiver.instance?.start(this, this, object : SmsRetrieverListener {
            override fun onReceive(message: String) {
                Log.e("message", message)
                findViewById<TextView>(R.id.helloTextView)?.text = message
            }

            override fun onFailure(errorCode: Int) {
                Log.e("onFailure", errorCode.toString())
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        OneTapSmsReceiver.instance?.handleOnActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}