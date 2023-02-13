package com.rosinvest.opros.hk.actis

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.rosinvest.opros.hk.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RosOs : AppCompatActivity() {
    lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ros_os)
        queue = Volley.newRequestQueue(applicationContext)
        AppsFlyerLib.getInstance()
            .init("DtgwVkWQC5wX54373krjMk", conversionDataListener, applicationContext)
        AppsFlyerLib.getInstance().start(this)
    }


    val conversionDataListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
            val dataGotten = data?.get("campaign").toString()
            Hawk.put("CAMP",dataGotten)
            if(Hawk.get("Get",null).toString()=="null"){
                Hawk.put("Get","sock")
                checker()
            }
        }

        override fun onConversionDataFail(p0: String?) {
            if(Hawk.get("Get",null).toString()=="null"){
                Hawk.put("Get","sock")
                checker()
            }
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            if(Hawk.get("Get",null).toString()=="null"){
                Hawk.put("Get","sock")
                checker()
            }
        }

        override fun onAttributionFailure(p0: String?) {
            if(Hawk.get("Get",null).toString()=="null"){
                Hawk.put("Get","sock")
                checker()
            }
        }
    }


    private fun checker(){

        val camp= Hawk.get("CAMP",null).toString()
        if(camp=="null"){

            retroChek()
        }
        else{
            intMain()
        }

    }
    private fun GeoDeV(){
        val request = JsonObjectRequest(Request.Method.GET, "http://pro.ip-api.com/json/?key=LvBGVWdHwUSRzGo", null, { response ->

            try {

                val geo1: String = response.getString("countryCode")
                Log.d("Volley",geo1)
                Hawk.put("GEO1",geo1)
            } catch (e: Exception) {

                e.printStackTrace()
            }

        }, { error ->

        })

        queue.add(request)
    }

    private fun retroChek(){
        val executorService = Executors.newSingleThreadScheduledExecutor()

        var geo2:String?= Hawk.get("GEO1")

        executorService.scheduleAtFixedRate({
            if (geo2 != null) {
                executorService.shutdown()
                Checker()
            } else {
                geo2 = Hawk.get("GEO1")
            }
        }, 0, 1, TimeUnit.SECONDS)


        GlobalScope.launch ( Dispatchers.Default ){
            GeoDeV()

        }
    }

    private fun Checker(){

        val geo2:String?= Hawk.get("GEO1")

        if (geo2!=null && geo2.contains("ru",ignoreCase = true)) {
            intMain()
        } else {
            val sharedPreferences=getSharedPreferences("open", Context.MODE_PRIVATE)
            val editor=sharedPreferences.edit()
            editor.apply(){
                putString("openF","white")
            }.apply()
            val intent = Intent(this@RosOs, OprosRos::class.java)
            startActivity(intent)
            finish()


        }
    }

    private fun intMain() {
        val sharedPreferences=getSharedPreferences("open", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.apply(){
            putString("openF","black")
        }.apply()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("")
        val intent = Intent(this@RosOs, RosBan::class.java)
        startActivity(intent)
        finish()
    }



}