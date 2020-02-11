package com.applogist.alsmsretriever_sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.applogist.alsmsretriever.OneTapSmsListener
import com.applogist.alsmsretriever.OneTapSmsReceiver
import kotlinx.android.synthetic.main.activity_onetap.*

class OneTapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onetap)

        OneTapSmsReceiver.instance?.start(this,this, object :
            OneTapSmsListener {
            override fun onSuccess(message: String) {
                Log.e("onSuccess", message)
                helloTextView?.text = message
            }

            override fun onFailure(errorCode: Int) {
                Log.e("onFailure", errorCode.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        OneTapSmsReceiver.instance?.handleOnActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
