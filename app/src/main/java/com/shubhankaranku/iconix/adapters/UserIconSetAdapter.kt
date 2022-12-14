package com.shubhankaranku.iconix.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shubhankaranku.iconix.R
import kotlinx.android.synthetic.main.recycler_view_layout_icon_set.view.*

class UserIconSetAdapter : RecyclerView.Adapter<UserIconSetAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtilCallback =
        object : DiffUtil.ItemCallback<com.shubhankaranku.iconix.model.userIconsSets.Iconset>() {
            override fun areItemsTheSame(
                oldItem: com.shubhankaranku.iconix.model.userIconsSets.Iconset,
                newItem: com.shubhankaranku.iconix.model.userIconsSets.Iconset
            ): Boolean {
                return oldItem.are_all_icons_glyph == newItem.are_all_icons_glyph &&
                        oldItem.categories == newItem.categories &&
                        oldItem.icons_count == newItem.icons_count &&
                        oldItem.iconset_id == newItem.iconset_id &&
                        oldItem.identifier == newItem.identifier &&
                        oldItem.is_premium == newItem.is_premium &&
                        oldItem.name == newItem.name &&
                        oldItem.published_at == newItem.published_at &&
                        oldItem.prices == newItem.prices &&
                        oldItem.styles == newItem.styles &&
                        oldItem.type == newItem.type
            }

            override fun areContentsTheSame(
                oldItem: com.shubhankaranku.iconix.model.userIconsSets.Iconset,
                newItem: com.shubhankaranku.iconix.model.userIconsSets.Iconset
            ): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_layout_icon_set, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val iconSet = differ.currentList[position]

        var license = "N/A"
        var price = 0
        if (differ.currentList[position].prices != null) {
            license = differ.currentList[position].prices[0].license.name
            price = differ.currentList[position].prices[0].price
        }


        holder.itemView.apply {
            tv_icon_set_author.visibility = View.INVISIBLE
            tv_icon_set_license.text = "License: $license"
            tv_icon_set_name.text = iconSet.name.toString()
            tv_icon_set_price.text = "$$price"
            tv_icon_set_type.text = "Type: ${iconSet.type}"

            if (iconSet.is_premium) {
                iv_premium.visibility = View.VISIBLE
            } else {
                iv_premium.visibility = View.INVISIBLE
            }


            //Handling Clicks
            setOnClickListener {
                onItemClickListener?.let {
                    it(iconSet)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.count()
    }

    private var onItemClickListener: ((com.shubhankaranku.iconix.model.userIconsSets.Iconset) -> Unit)? =
        null

    fun setOnItemClickListener(listener: (com.shubhankaranku.iconix.model.userIconsSets.Iconset) -> Unit) {
        onItemClickListener = listener
    }
}