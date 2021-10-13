package com.example.openrss

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter(private val rv: ArrayList<Entry>, val cont: Context): RecyclerView.Adapter<RVAdapter.ItemViewHolder>()  {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ItemViewHolder {
        return RVAdapter.ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rvlist,parent,false )
        )
    }

    override fun onBindViewHolder(holder: RVAdapter.ItemViewHolder, position: Int) {
        val rvv = rv[position].title
        var rvvd= rv[position].author!!.name
        if(rv[position].thumbnail!=null){
            rvvd="[image] $rvvd"
        }
        holder.itemView.apply {
            var rvlisting= findViewById<CardView>(R.id.rvlisting)
            var ct= findViewById<TextView>(R.id.cardtitle)
            var cd= findViewById<TextView>(R.id.carddesc)
            findViewById<TextView>(R.id.date).text= "updated: ${(rv[position].updated)}"
            ct.text = rvv.toString()
            cd.text = rvvd.toString()
            rvlisting.setOnClickListener {
                val intent = Intent(cont,Fullview::class.java)
                var html =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(rv[position].content, Html.FROM_HTML_MODE_COMPACT) } else { Html.fromHtml(rv[position].content) }
                intent.putExtra("content",rv[position].content)
                intent.putExtra("author",rv[position].author!!.name)
                intent.putExtra("title",rv[position].title)
                intent.putExtra("update",rv[position].updated)
                if(rv[position].thumbnail!!.url!=null) {
                    intent.putExtra("thumb", rv[position].thumbnail!!.url)
                    intent.putExtra("key",1)
                }else intent.putExtra("key",0)
                context.startActivity(intent)
            }



        }
    }

    override fun getItemCount() = rv.size
}