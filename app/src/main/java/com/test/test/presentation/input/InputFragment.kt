package com.test.test.presentation.input

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.test.test.R
import com.test.test.model.DEFAULT_VALUE
import com.test.test.model.StateWithTowns
import com.test.test.model.Town
import com.test.test.model.User

/**
 * Captura de datos para poblar al modelo [User]
 */
open class InputFragment : PhotoFragment()
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stateWithTowns.observe(viewLifecycleOwner, Observer {
            it ?:return@Observer
            populateStateSpinners(it)
        })
        viewModel.msgs.observe(viewLifecycleOwner, Observer {
            it?:return@Observer
            MaterialAlertDialogBuilder(requireContext()).setTitle(
                R.string.alert
            ).setCancelable(true).setMessage(it).show()
            viewModel.clearMsgs()
        })

        binding.save.setOnClickListener {
            if (checkData())
            {
                loaderVm?.showLoader()
                val user = populateUser()
                savePhoto(){
                    user.apply { photoName = it }
                    viewModel.insertUser( user ){
                        loaderVm?.hideLoader()
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
    }

    protected fun populateUser(): User =
        User(
             nombre                =     binding.nombre.editText?.text?.toString() ?: ""
            ,edad                  =     binding.edad.editText?.text?.toString() ?: ""
            ,domicilio_calle       =     binding.domicilioCalle.editText?.text?.toString() ?: ""
            ,domicilio_no_exterior =     binding.domicilioNoExterior.editText?.text?.toString() ?: ""
            ,domicilio_no_interior =     binding.domicilioNoInterior.editText?.text?.toString() ?: ""
            ,domicilio_colonia     =     binding.domicilioColonia.editText?.text?.toString() ?: ""
            ,stateId               =     viewModel.currentStateId
            ,townId                =     viewModel.currentTownId
            ,photoName             =     ""
        )

    protected open fun preselectTownFromUser(states: List<StateWithTowns>){}

    protected open fun populateStateSpinners(states: List<StateWithTowns> )
    {
        binding.estado.adapter = ArrayAdapter<StateWithTowns>(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,states)
        binding.estado.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )
            {
                val towns =  states[position].towns
                viewModel.currentStateId = states[position].state.stateId
                binding.municipio.adapter = ArrayAdapter<Town>(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,towns)
                preselectTownFromUser(states)
            }

            override fun onNothingSelected(parent: AdapterView<*>?)
            {
            }
        }

        binding.municipio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )
            {
                viewModel.currentTownId = (binding.municipio.adapter.getItem(position) as? Town)?.townId ?: 0
            }
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
            }
        }
    }

    protected fun checkData(): Boolean
    {
        var pass = checkTIL(binding.nombre)
        pass = checkTIL(binding.edad) && pass
        pass = checkTIL(binding.domicilioCalle) && pass
        pass = checkTIL(binding.domicilioNoExterior) && pass
        pass = checkTIL(binding.domicilioNoInterior) && pass
        pass = viewModel.currentStateId  != DEFAULT_VALUE  && pass
        pass = viewModel.currentTownId   != DEFAULT_VALUE  && pass
        pass = checkTIL(binding.domicilioColonia) && pass
        return pass
    }

    private fun checkTIL(til: TextInputLayout): Boolean
    {
        val emptyError = getString(R.string.empty_error)
        var pass = true
        til.error = if (til.editText?.text?.isEmpty() == true)
        {
            pass = false
            emptyError
        } else
        {
            null
        }
        return pass
    }

}
