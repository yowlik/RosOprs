package com.rosinvest.opros.hk.actis

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.rosinvest.opros.hk.R
import com.rosinvest.opros.hk.databinding.ActivityRosBanBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class RosBan : AppCompatActivity() {
    private val FILECHOOSERRESULTCODE = 1

    private lateinit var builde: AlertDialog.Builder






    var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String? = null
    lateinit var webView1: WebView
    lateinit var bind: ActivityRosBanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRosBanBinding.inflate(layoutInflater)
        setContentView(bind.root)
        webView1 = bind.rosopros
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView1, true)
        webSettings(webView1)
        val activity: Activity = this
        webView1.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)

                return false
            }
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                saveUrl(url)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }


        }
        webView1.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                mFilePathCallback?.onReceiveValue(null)
                mFilePathCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(packageManager) != null) {


                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {

                    }

                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent?> =
                    takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(
                    chooserIntent, FILECHOOSERRESULTCODE
                )
                return true
            }


            @Throws(IOException::class)
            private fun createImageFile(): File {
                var imageStorageDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "DirectoryNameHere"
                )
                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs()
                }


                imageStorageDir =
                    File(imageStorageDir.toString() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg")
                return imageStorageDir
            }

        }
        val shar=getSharedPreferences("Link", MODE_PRIVATE)
        val size=shar.getInt("Size",-100)
        val lins=shar.getString((size-1).toString(),null)

        Hawk.put("Size",(size-1))
        Log.d("Size",size.toString())
        if(size<1){
            webView1.loadUrl(getUrl())
            Log.d("Size", "getUrl")
        }
        else{
            webView1.loadUrl(lins.toString())
            Log.d("Size", "lins")
            Log.d("Size",lins.toString())

        }

    }


    private fun saveForwardList2(){
        val webBackHist=webView1.copyBackForwardList()
        val size=webBackHist.size
        val shar=getSharedPreferences("Link", MODE_PRIVATE)
        val editor=shar.edit()
        editor.putInt("Size",(size-1))

        for ( i in 0 until (size-1)){
            val item=webBackHist.getItemAtIndex(i)
            val url=item.url
            Log.d("Size", "$i :  $url")
            editor.putString(i.toString(),url)
        }
        editor.apply()


    }









    var firstUrl = ""
    fun saveUrl(url: String?) {
        if (firstUrl == "") {
            firstUrl = getSharedPreferences(
                "SP_WEBVIEW_PREFS",
                AppCompatActivity.MODE_PRIVATE
            ).getString(
                "SAVED_URL",
                url
            ).toString()
            val sp = getSharedPreferences("SP_WEBVIEW_PREFS", AppCompatActivity.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("SAVED_URL", url)
            editor.apply()

        }
    }

    private fun getUrl(): String {

        val spoon = getSharedPreferences("SP_WEBVIEW_PREFS", AppCompatActivity.MODE_PRIVATE)
        val name= Hawk.get("CAMP",null).toString()
        val link=""
        val afId = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        AppsFlyerLib.getInstance().setCollectAndroidID(true)
        var after=""
        if(name=="null"){
            after="$link"
        }
        else{
            after="$link?$name"
        }

        OneSignal.setExternalUserId(afId.toString())
        return spoon.getString("SAVED_URL", after).toString()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FILECHOOSERRESULTCODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results: Array<Uri>? = null


        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (data == null || data.data == null) {

                results = arrayOf(Uri.parse(mCameraPhotoPath))
            } else {
                val dataString = data.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mFilePathCallback?.onReceiveValue(results)
        mFilePathCallback = null
    }

    override fun onStop() {
        super.onStop()
        saveForwardList2()
    }



    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val shar=getSharedPreferences("Link", MODE_PRIVATE)
        val size= Hawk.get("Size",0)

        if (webView1.canGoBack()) {
            Hawk.put("Exit",true)
            if (doubleBackToExitPressedOnce) {
                webView1.stopLoading()
                webView1.loadUrl(firstUrl)
            }
            this.doubleBackToExitPressedOnce = true
            webView1.goBack()
        }

        else if(size>0){
            val lins=shar.getString(size.toString(),null)

            Hawk.put("Size",(size-1))
            webView1.stopLoading()
            webView1.loadUrl(lins.toString())
        }
        else {
            builde= AlertDialog.Builder(this)
            builde.setTitle("Предупреждение")
                .setMessage("Вы уверены, что хотите выйти?")
                .setCancelable(true)
                .setPositiveButton("Остаться"){dialogInterface,it->
                    webView1.loadUrl(firstUrl)
                }
                .setNegativeButton("Выйти"){dialogInterface,it->
                    finish()
                }
                .show()

        }
    }

    fun webSettings(vv: WebView) {
        val webSettings = vv.settings
        webSettings.javaScriptEnabled = true

        webSettings.useWideViewPort = true

        webSettings.loadWithOverviewMode = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.userAgentString = webSettings.userAgentString.replace("; wv", "")

        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(false)

        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)

        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.setAppCacheEnabled(true)

        webSettings.allowContentAccess = true
    }



}