package com.test.test.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.test.domain.repository.dao.UserDAO
import com.test.test.model.User
import java.lang.IllegalArgumentException


class ListViewModelFactory(val userDAO: UserDAO) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(ListViewModel::class.java))
        {
            return ListViewModel(userDAO) as T
        } else
        {
            throw IllegalArgumentException()
        }
    }
}

class ListViewModel(private val userDAO: UserDAO) : ViewModel()
{
    val users: LiveData<List<User>> = userDAO.fetchUsers()
}