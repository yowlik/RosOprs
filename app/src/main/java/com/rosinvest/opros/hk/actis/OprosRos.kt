package com.rosinvest.opros.hk.actis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rosinvest.opros.hk.R
import com.rosinvest.opros.hk.databinding.ActivityOprosRosBinding

class OprosRos : AppCompatActivity() {
    lateinit var bind:ActivityOprosRosBinding

    val liste= mutableListOf<String>("Довольны ли вы экономическим состоянием государства?", "Вашей зарплаты хватает для комфортной жизни и всех ваших забот?",
    "Есть ли у вас возможность откладывать средства для дальнейших инвистиций?", "Если бы вы получили деньги, на которые вы не рассчитывали, вы бы их инвестировали?")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityOprosRosBinding.inflate(layoutInflater)
        setContentView(bind.root)
        var i=0
        bind.quest.text=liste[i]
        bind.daBut.setOnClickListener {
            i++
            if(i<4){
                bind.quest.text=liste[i]
            }
            else{
                val ends= Intent(this@OprosRos,EndOpros::class.java)
                startActivity(ends)
            }
        }
        bind.noBut.setOnClickListener {
            i++
            if(i<4){
                bind.quest.text=liste[i]
            }
            else{
                val ends= Intent(this@OprosRos,EndOpros::class.java)
                startActivity(ends)
            }
        }
    }
}