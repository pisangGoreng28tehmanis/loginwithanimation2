package com.dicoding.picodiploma.loginwithanimation.view.fragment.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.api.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStory2Binding


class StoryAdapter(private var storyList: List<ListStoryItem> = emptyList()) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStory2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = storyList.size

    fun updateList(newList: List<ListStoryItem>) {
        storyList = newList
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(private val binding: ItemStory2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            // Update this to bind the correct view from your layout, such as tvName
            binding.tvName.text = story.name  // Corrected: Use tvName instead of tvStoryTitle
            // Bind other data as needed, such as tvDescription, etc.
        }
    }
}
