package com.dicoding.tanaminai.view.bookmark

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.tanaminai.data.local.entity.BookmarkEntity
import com.dicoding.tanaminai.databinding.ItemBookmarkBinding

class BookmarkAdapter : ListAdapter<BookmarkEntity, BookmarkAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: BookmarkEntity) {
            binding.tvPlant.text = bookmark.result
            binding.tvDate.text = bookmark.predictAt
            Glide.with(binding.root)
                .load(bookmark.image)
                .into(binding.imgPlant)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bookmark = getItem(position)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(bookmark) }
        holder.bind(bookmark)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BookmarkEntity> =
            object : DiffUtil.ItemCallback<BookmarkEntity>() {
                override fun areItemsTheSame(
                    oldItem: BookmarkEntity,
                    newItem: BookmarkEntity
                ): Boolean {
                    return oldItem.predictAt == newItem.predictAt
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: BookmarkEntity,
                    newItem: BookmarkEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: BookmarkEntity)
    }

}