package com.rosinvest.opros.hk.actis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rosinvest.opros.hk.R
import com.rosinvest.opros.hk.databinding.ActivityEndOprosBinding
import com.unity3d.ads.UnityAds

class EndOpros : AppCompatActivity() {
    lateinit var bind:ActivityEndOprosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityEndOprosBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.again.setOnClickListener {
            val inte= Intent(this@EndOpros,OprosRos::class.java)
            startActivity(inte)
        }
        bind.exit.setOnClickListener {
            finishAffinity()
        }
    }
}