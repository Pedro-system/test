package com.test.test.presentation.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.test.domain.repository.dao.StateDAO
import com.test.test.domain.repository.dao.UserDAO
import com.test.test.model.DEFAULT_VALUE
import com.test.test.model.StateWithTowns
import kotlinx.coroutines.Job
import java.lang.IllegalArgumentException
import com.test.test.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import java.io.InputStream

class InputViewModelFactory(val userDAO: UserDAO, val stateDAO: StateDAO) :
    ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(InputViewModel::class.java))
        {
            return InputViewModel(userDAO, stateDAO) as T
        } else
        {
            throw IllegalArgumentException()
        }
    }
}

class InputViewModel(
    val userDAO: UserDAO,
    stateDAO: StateDAO
) : ViewModel()
{

    var currentTownId: Long = DEFAULT_VALUE
    var currentStateId: Long = DEFAULT_VALUE
    val stateWithTowns: LiveData<List<StateWithTowns>> = stateDAO.fetchStates()

    private val _msg = MutableLiveData<String>()
    val delegate = FireStoreDecorator(_msg)

    val img: LiveData<ByteArray>
        get() = delegate.img

    val msgs: LiveData<String>
        get() = delegate.msgs

    var jobVM: CoroutineContext? = null
    var job = Job()

    override fun onCleared()
    {
        job.cancel()
        (jobVM as? Job)?.cancel()
        delegate.job.cancel()
        (delegate.jobVM as? Job)?.cancel()
        super.onCleared()
    }

    fun insertUser(user: User, callback: () -> Unit)
    {
        job.cancel()
        job = Job()
        jobVM = CoroutineScope(Dispatchers.Main + job).launch {
            userDAO.insertUser(user)
            callback()
        }
    }

    fun updateUser(user: User, callback: () -> Unit)
    {
        job.cancel()
        job = Job()
        jobVM = CoroutineScope(Dispatchers.Main + job).launch {
            userDAO.updateUser(user)
            callback()
        }
    }

    fun deleteUser(currentUser: User, callback: () -> Unit)
    {
        job.cancel()
        job = Job()
        jobVM = CoroutineScope(Dispatchers.Main + job).launch {
            userDAO.deleteUser(currentUser)
            callback()
        }
    }

    fun uploadPhoto(inputStream: InputStream, callback: (String) -> Unit) = delegate.uploadPhoto(inputStream, callback)

    fun fetchImage(photoName: String) = delegate.fetchImage(photoName)

    fun clearMsgs() = delegate.clearMsgs()

    fun onError(error:String)
    {
        _msg.postValue(error)
    }
}