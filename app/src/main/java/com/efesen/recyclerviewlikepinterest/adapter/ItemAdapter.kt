package com.efesen.recyclerviewlikepinterest.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.efesen.recyclerviewlikepinterest.databinding.ItemHolderBinding
import com.efesen.recyclerviewlikepinterest.model.ImageItem

/**
 * Created by Efe Şen on 24.10.2023.
 */
class ItemAdapter(private val imageItemList: List<ImageItem>, private val context: Context) :
    RecyclerView.Adapter<ItemAdapter.CardItemHolder>() {

    inner class CardItemHolder(var binding: ItemHolderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemHolder {
        val binding = ItemHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardItemHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageItemList.size
    }

    override fun onBindViewHolder(holder: CardItemHolder, position: Int) {
        val item = imageItemList[position]

        Glide.with(context)
            .asBitmap()
            .load(item.largeImageURL)
            .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    holder.binding.itemImageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Image cancellation, changing adjustment operations
                }

            })
    }
}