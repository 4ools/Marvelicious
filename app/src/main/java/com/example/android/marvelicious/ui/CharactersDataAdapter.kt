package com.example.android.marvelicious.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marvelicious.R
import com.example.android.marvelicious.databinding.CharacterItemBinding
import com.example.android.marvelicious.domain.Models

class CharactersDataAdapter : ListAdapter<Models.Character, CharactersViewHolder>(POST_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Models.Character>() {
            override fun areContentsTheSame(
                oldItem: Models.Character,
                newItem: Models.Character
            ): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(
                oldItem: Models.Character,
                newItem: Models.Character
            ): Boolean =
                oldItem.id == newItem.id
        }
    }
}

class CharactersViewHolder(val binding: CharacterItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): CharactersViewHolder {
            val dataBinding: CharacterItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.character_item,
                parent,
                false
            )
            return CharactersViewHolder(dataBinding)
        }
    }

    fun bind(item: Models.Character) {
        binding.character = item
        binding.executePendingBindings()
    }
}