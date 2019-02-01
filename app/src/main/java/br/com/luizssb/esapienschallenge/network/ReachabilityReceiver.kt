package br.com.luizssb.esapienschallenge.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReachabilityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        print("DUDE")
        println(intent)
    }
}
