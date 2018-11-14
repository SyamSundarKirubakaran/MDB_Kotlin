package com.bugscript.moviesapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bugscript.moviesapp.R

class MoviesAdapter(val context : Context, val itemClick : () -> Unit) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,p0,false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return 20;
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindViews()
    }

    inner class ViewHolder(itemView : View, val itemClick : () -> Unit) : RecyclerView.ViewHolder(itemView){
        fun bindViews(){
            itemView.setOnClickListener {
                itemClick()
            }
        }
    }

}