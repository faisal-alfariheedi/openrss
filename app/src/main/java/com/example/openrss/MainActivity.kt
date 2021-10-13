package com.example.openrss

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var rv: RecyclerView
    lateinit var pos : ArrayList<Entry>
    lateinit var sp: SharedPreferences
    var apif =client().getClient()?.create(API::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        saverestore(true)


    }
    fun init(){
        rv=findViewById(R.id.rv)
        pos=arrayListOf()
        rv.adapter=RVAdapter(pos,this)
        rv.layoutManager= LinearLayoutManager(this)
    }
    /////////////////////menu////////////////////////////


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item1 = menu!!.getItem(0)
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.m1 -> {
                CoroutineScope(Dispatchers.IO).launch {

                    apif!!.getfeed!!.enqueue(object : Callback<Feed?> {
                        override fun onResponse(call: Call<Feed?>, response: Response<Feed?>) {
                    Log.d("feed", "onResponse: feed: " + response.body().toString())
                    Log.d("response", "onResponse: Server Response: $response")
                            val entries = response.body()!!.entrys
                            for (entry in entries!!) {
                              pos.add(entry)
                            }
                            rv.adapter?.notifyDataSetChanged()
                            saverestore(false)
                            Toast.makeText(applicationContext, "done refreshing",Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<Feed?>, t: Throwable) {
                            Log.d("failurerere", "$t")
                            Toast.makeText(
                                this@MainActivity,
                                "An Error Occured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun saverestore(s:Boolean) {
        var size: Int
        sp = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        size = sp.getInt("size", 0)
        if (s) {
            if (size != 0) {
                for (i in 0..size) {
                    var a=Author(sp.getString("his $i nam",""),sp.getString("his $i uri",""))
                    var ent=Entry(sp.getString("his $i cont",""),a,sp.getString("his $i id",""),Thumbnail(sp.getString("his $i thumb","")),sp.getString("his $i tit","")
                    ,sp.getString("his $i up",""))
                    pos.add(ent)
                }
            }
            rv.adapter=RVAdapter(pos,this)
            rv.adapter?.notifyDataSetChanged()

        } else {
            if (pos.size > 0) {
                for (i in pos) {
                    with(sp.edit()) {
                        putString("his ${pos.indexOf(i)} cont", i.content)
                        putString("his ${pos.indexOf(i)} nam", i.author!!.name)
                        putString("his ${pos.indexOf(i)} uri", i.author!!.uri)
                        putString("his ${pos.indexOf(i)} id", i.id)
                        if(i.thumbnail!=null) {

                            putString("his ${pos.indexOf(i)} thumb", i.thumbnail!!.url)
                        }
                        putString("his ${pos.indexOf(i)} tit", i.title)
                        putString("his ${pos.indexOf(i)} up", i.updated)
                        apply()
                    }
                }
            }
            with(sp.edit()) {
                putInt("size", pos.size - 1)

                apply()
            }
        }
    }

}