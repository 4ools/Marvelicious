package com.example.android.marvelicious.ui.characterdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.marvelicious.R
import com.example.android.marvelicious.data.Status
import com.example.android.marvelicious.databinding.FragmentCharacterDetailBinding
import timber.log.Timber

class CharacterDetailFragment : Fragment() {
    private val args: CharacterDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentCharacterDetailBinding

    private val charactersViewModel: CharacterDetailViewModel by lazy {
        ViewModelProvider(
            this,
            CharacterDetailViewModel.Factory(
                requireActivity().application,
                args.characterId
            )
        ).get(CharacterDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentCharacterDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        charactersViewModel.character.observe(viewLifecycleOwner, Observer {
            Timber.d("something changed $it")
            binding.character = it
        })

        charactersViewModel.networkState.observe(viewLifecycleOwner, Observer {
            Timber.d("something happend $it")
            when (it.status) {
                Status.RUNNING -> {
                    binding.textViewNetwork.text = it.status.name
                }
                Status.SUCCESS -> {
                    binding.textViewNetwork.text = it.status.name
                }
                Status.FAILED -> {
                    binding.textViewNetwork.text = it.message
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.character_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                charactersViewModel.refresh()
                true
            }
            else -> false
        }
    }
}