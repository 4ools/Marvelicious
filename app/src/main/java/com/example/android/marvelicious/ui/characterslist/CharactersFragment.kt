package com.example.android.marvelicious.ui.characterslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.marvelicious.R
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.databinding.FragmentCharactersBinding
import timber.log.Timber

class CharactersFragment : Fragment() {

    private val charactersViewModel: CharactersViewModel by lazy {
        ViewModelProvider(
            this,
            CharactersViewModel.Factory(
                requireActivity().application
            )
        ).get(CharactersViewModel::class.java)
    }

    private lateinit var adapter: CharactersDataAdapter
    private lateinit var binding: FragmentCharactersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_characters,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = charactersViewModel

        adapter =
            CharactersDataAdapter(
                CharacterClick {
                    Toast.makeText(activity, "character called ${it.name}", Toast.LENGTH_SHORT)
                        .show()
                    val action =
                        CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
                            it.id!!
                        )
                    findNavController().navigate(action)
                },
                NetworkStateViewHolder.OnClick {
//            charactersViewModel.refresh()
                })

        binding.charactersList.adapter = adapter
        binding.charactersList.layoutManager = LinearLayoutManager(context)

        binding.swipeRefresh.setOnRefreshListener {
            charactersViewModel.refresh()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        charactersViewModel.characters.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        charactersViewModel.networkState.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
            adapter.setResultState(it)
        })

    }
}