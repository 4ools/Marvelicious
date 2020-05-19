package com.example.android.marvelicious.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marvelicious.R
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.databinding.CharacterItemBinding
import com.example.android.marvelicious.domain.Models

class CharactersDataAdapter(
    private val click: CharacterClick,
    private val retry: NetworkStateViewHolder.OnClick
) :
    PagedListAdapter<Models.Character, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    private var networkState: NetworkState? = null

    fun setResultState(resultState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = resultState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != resultState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.character_item -> CharactersViewHolder.from(parent)
            R.layout.network_state_item -> NetworkStateViewHolder.from(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.character_item -> (holder as CharactersViewHolder).bind(
                getItem(position)!!,
                click
            )
            R.layout.network_state_item -> (holder as NetworkStateViewHolder).bind(
                networkState,
                retry
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.character_item
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }


    private fun hasExtraRow() =
        networkState != null && networkState != NetworkState.LOADED

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

    fun bind(
        item: Models.Character,
        click: CharacterClick
    ) {
        binding.character = item
        binding.executePendingBindings()
        binding.characterClickCallback = click
    }
}

class CharacterClick(val click: (Models.Character) -> Unit) {
    /**
     * Called when a video is clicked
     *
     * @param video the video that was clicked
     */
    fun onClick(character: Models.Character) = click(character)
}
