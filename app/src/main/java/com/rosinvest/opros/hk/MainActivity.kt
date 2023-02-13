package com.rosinvest.opros.hk

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.opengl.GLES20
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import com.orhanobut.hawk.Hawk
import com.rosinvest.opros.hk.actis.OprosRos
import com.rosinvest.opros.hk.actis.RosBan
import com.rosinvest.opros.hk.actis.RosOs
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.UnityAds
import com.unity3d.services.core.connectivity.ConnectivityMonitor.addListener
import java.io.File

class MainActivity : AppCompatActivity(){


    var rating=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Hawk.init(this).build()
        UnityAds.initialize(applicationContext,"")

        Lopaaaas()
    }
    private fun chekkkkks(): Boolean {
        var newRating = 0
        if (rating < 0) {
            if (Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("Andy") ||
                Build.PRODUCT.contains("ttVM_Hdragon") ||
                Build.PRODUCT.contains("google_sdk") ||
                Build.PRODUCT.contains("Droid4X") ||
                Build.PRODUCT.contains("nox") ||
                Build.PRODUCT.contains("sdk_x86") ||
                Build.PRODUCT.contains("sdk_google") ||
                Build.PRODUCT.contains("vbox86p")
            ) {
                newRating++
            }
            if (Build.MANUFACTURER == "unknown" || Build.MANUFACTURER == "Genymotion" ||
                Build.MANUFACTURER.contains("Andy") ||
                Build.MANUFACTURER.contains("MIT") ||
                Build.MANUFACTURER.contains("nox") ||
                Build.MANUFACTURER.contains("TiantianVM")
            ) {
                newRating++
            }
            if (Build.BRAND == "generic" || Build.BRAND == "generic_x86" || Build.BRAND == "TTVM" ||
                Build.BRAND.contains("Andy")
            ) {
                newRating++
            }
            if (Build.DEVICE.contains("generic") ||
                Build.DEVICE.contains("generic_x86") ||
                Build.DEVICE.contains("Andy") ||
                Build.DEVICE.contains("ttVM_Hdragon") ||
                Build.DEVICE.contains("Droid4X") ||
                Build.DEVICE.contains("nox") ||
                Build.DEVICE.contains("generic_x86_64") ||
                Build.DEVICE.contains("vbox86p")
            ) {
                newRating++
            }
            if (Build.MODEL == "sdk" || Build.MODEL == "google_sdk" ||
                Build.MODEL.contains("Droid4X") ||
                Build.MODEL.contains("TiantianVM") ||
                Build.MODEL.contains("Andy") || Build.MODEL == "Android SDK built for x86_64" || Build.MODEL == "Android SDK built for x86"
            ) {
                newRating++
            }
            if (Build.HARDWARE == "goldfish" || Build.HARDWARE == "vbox86" ||
                Build.HARDWARE.contains("nox") ||
                Build.HARDWARE.contains("ttVM_x86")
            ) {
                newRating++
            }
            if (Build.FINGERPRINT.contains("generic/sdk/generic") ||
                Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86") ||
                Build.FINGERPRINT.contains("Andy") ||
                Build.FINGERPRINT.contains("ttVM_Hdragon") ||
                Build.FINGERPRINT.contains("generic_x86_64") ||
                Build.FINGERPRINT.contains("generic/google_sdk/generic") ||
                Build.FINGERPRINT.contains("vbox86p") ||
                Build.FINGERPRINT.contains("generic/vbox86p/vbox86p")
            ) {
                newRating++
            }
            try {
                val opengl = GLES20.glGetString(GLES20.GL_RENDERER)
                if (opengl != null) {
                    if (opengl.contains("Bluestacks") ||
                        opengl.contains("Translator")
                    ) newRating += 10
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val sharedFolder = File(
                    (Environment
                        .getExternalStorageDirectory().toString()
                            + File.separatorChar
                            ) + "windows"
                            + File.separatorChar
                        .toString() + "BstSharedFolder"
                )
                if (sharedFolder.exists()) {
                    newRating += 10
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            rating = newRating

        }
        return rating >= 2
    }

    private fun Lopaaaas(){
        val sharedPreferences = getSharedPreferences("open", Context.MODE_PRIVATE)
        val open = sharedPreferences.getString("openF", null)
        if(open==null){
            if(chekkkkks()==true) {
                val editor=sharedPreferences.edit()
                editor.apply(){
                        putString("openF","white")
                }.apply()
                    val ints= Intent(this@MainActivity, OprosRos::class.java)
                    startActivity(ints)
                    finish()
                }
                else{
                val ints= Intent(this@MainActivity, RosOs::class.java)
                startActivity(ints)
                finish()
                }
            }
            else if(open=="white"){
                val ints= Intent(this@MainActivity,OprosRos::class.java)
                startActivity(ints)
                finish()
            }
            else if(open=="black"){
                val ints= Intent(this@MainActivity, RosBan::class.java)
                startActivity(ints)
                finish()
            }
        }

}