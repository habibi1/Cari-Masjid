package com.project.masjid.ui.search_mosque

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.masjid.R
import com.project.masjid.database.MosqueEntity

class SearchMosqueAdapter (private val context: Context, private val items : List<MosqueEntity>)
    : RecyclerView.Adapter<SearchMosqueAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_mosque, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val nameMosque = view.findViewById<TextView>(R.id.tv_name_mosque)
        private val imageMosque = view.findViewById<ImageView>(R.id.img_mosque)
        private val mapsMosque = view.findViewById<TextView>(R.id.tv_maps)
        private val cvItemMosque = view.findViewById<CardView>(R.id.cv_item_mosque)
        private lateinit var mosque : MosqueEntity
        private lateinit var context: Context

        fun bindItem(mosqueEntity: MosqueEntity, context: Context) {
            nameMosque.text = mosqueEntity.name
            Glide.with(context)
                    .load(mosqueEntity.downloadImage)
                    .into(imageMosque)

            mosque = mosqueEntity
            this.context = context

            mapsMosque.setOnClickListener(this)
            cvItemMosque.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.cv_item_mosque -> {

                }
                R.id.tv_maps -> {
                    val intent = Intent(context, SearchMapsActivity::class.java)
                    intent.putExtra(context.getString(R.string.latitude), mosque.latitude)
                    intent.putExtra(context.getString(R.string.longitude), mosque.longitude)
                    intent.putExtra(context.getString(R.string.nama_masjid), mosque.name)
                    context.startActivity(intent)
                }
            }
        }
    }
}