package com.test.test.presentation.input

import android.os.Bundle
import android.view.View
import com.test.test.R
import com.test.test.model.StateWithTowns
import com.test.test.model.User

/**
 * Muestra el detalle de un [User] y permite eliminarlo o actualizar su información
 */
class EditDeleteFragment : InputFragment()
{
    var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        updateViewOrCreateView()
        user?.let {
            loaderVm?.showLoader()
            fetchImage(it.photoName)
        }
    }

    private fun updateViewOrCreateView()
    {
        val currentUser = user ?: return
        binding.user = currentUser
        binding.executePendingBindings()
        binding.save.text = getString(R.string.update)
        binding.save.setOnClickListener {
            if (checkData())
            {
                val user = populateUser().apply {
                    this.userId = currentUser.userId
                    this.photoName = currentUser.photoName
                }
                viewModel.updateUser( user ){
                    requireActivity().onBackPressed()
                }
            }
        }
        binding.delete.visibility = View.VISIBLE
        binding.delete.setOnClickListener {
            viewModel.deleteUser(currentUser) {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun populateStateSpinners(states: List<StateWithTowns>)
    {
        super.populateStateSpinners(states)
        preselectStateFromUser(states)
    }

    private fun preselectStateFromUser(states: List<StateWithTowns>)
    {
        user?.let { u ->
            binding.estado.setSelection(
                states.indexOfFirst {it.state.stateId == u.stateId  }.also { println(states[it]) }
            )
        }
    }

    override fun preselectTownFromUser(states: List<StateWithTowns>)
    {
        user?.let { u ->
            val state = states.find {it.state.stateId == u.stateId }
            state?.towns?.indexOfFirst { it.townId == u.townId }?.let {
                binding.municipio.setSelection(it)
            }
            binding.executePendingBindings()
        }
    }
}