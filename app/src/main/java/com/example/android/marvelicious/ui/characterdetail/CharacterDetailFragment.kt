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
import com.google.android.material.snackbar.Snackbar
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
            binding.character = it
        })

        charactersViewModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.RUNNING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                }
                Status.FAILED -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(view!!, it.message.toString(), Snackbar.LENGTH_INDEFINITE).run {
                        setAction(getString(R.string.retry)) {
                            charactersViewModel.refresh()
                            dismiss()
                        }
                        show()
                    }
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