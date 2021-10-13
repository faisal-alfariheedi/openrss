package com.example.openrss

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.URL

class Fullview : AppCompatActivity() {
    lateinit var tit: TextView
    lateinit var cont:TextView
    lateinit var img: ImageView
    lateinit var bkbut: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullview)
        init()
        bkbut.setOnClickListener {
            intent= Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }
    fun init() {
        tit = findViewById<TextView>(R.id.tvtitle)
        cont = findViewById<TextView>(R.id.tvcontent)
        img = findViewById(R.id.image)
        bkbut = findViewById(R.id.back)
        tit.text = intent.getStringExtra("title")
        cont.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(intent.getStringExtra("content"), Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(intent.getStringExtra("content"))
        }
        Log.d("imagekey", "${intent.getIntExtra("key",0)}")
        if (intent.getIntExtra("key",0) == 1) {

            Toast.makeText(applicationContext, "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(intent.getStringExtra("thumb"))
                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//                img.setImageURI(intent.getStringExtra("thumb")!!.toUri())
                Log.d("thumbcheck", "${intent.getStringExtra("thumb")}")
//                img.setImageBitmap(doInBackground(intent.getStringExtra("thumb")!!))
                runOnUiThread { img.setImageBitmap(bmp) }
            }


        }
    }

    }

    fun doInBackground( urls: String): Bitmap? {
        val imageURL = urls
        var image: Bitmap? = null
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return image
    }
