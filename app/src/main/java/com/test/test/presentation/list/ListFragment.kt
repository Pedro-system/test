package com.test.test.presentation.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.test.data.db.DB
import com.test.test.databinding.FragmentListBinding
import com.test.test.main.LoaderViewModel
import com.test.test.main.MainActivity
import com.test.test.presentation.list.adapter.UsersAdapter
import com.test.test.model.User

/**
 * Muestra un listado de usuarios almacenados en base local.
 */
class ListFragment : Fragment()
{
    private lateinit var viewModel: ListViewModel
    protected var loaderVm : LoaderViewModel? = null
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentListBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        loaderVm = ViewModelProvider(requireActivity())[LoaderViewModel::class.java]
        val factory = ListViewModelFactory( DB.getInstance(requireContext()).userDAO() )
        viewModel = ViewModelProvider(requireActivity(),factory )[ListViewModel::class.java]
        binding.addUser.setOnClickListener {
            (requireActivity() as? MainActivity )?.navigate()
        }
        binding.list.adapter = UsersAdapter {
            showUser(it)
        }
        binding.list.addItemDecoration(
            DividerItemDecoration(requireContext(),
            DividerItemDecoration.VERTICAL )
        )
        loaderVm?.showLoader()
        viewModel.users.observe(viewLifecycleOwner, Observer {
            it?:return@Observer
            (binding.list.adapter as? UsersAdapter)?.submitList(it)
            loaderVm?.hideLoader()
        })
    }

    private fun showUser(user: User) = (requireActivity() as? MainActivity )?.navigate(user)
}