package com.test.test.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.test.R
import com.test.test.presentation.input.InputFragment
import com.test.test.presentation.list.ListFragment
import com.test.test.model.User
import com.test.test.presentation.input.EditDeleteFragment

/**
Inicio de flujo de aplicacion muestra un listado con los
 usuarios capturados en la base de datos al cargar al fragmento
 [ListFragment]
 */
class MainActivity : AppCompatActivity()
{

    var vm : LoaderViewModel? = null
    var loader : View? = null
    init
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        supportFragmentManager.beginTransaction().replace(R.id.main_container, ListFragment())
            .commit()
        loader = findViewById(R.id.loader)
        vm = ViewModelProvider(this)[LoaderViewModel::class.java]
        vm?.loader?.observe(this, Observer {
           it?:return@Observer
           if(it)
           {
              loader?.visibility = View.VISIBLE
           }
           else
           {
               loader?.visibility = View.GONE
           }
        })
    }

    /**
     * Navega a solicitar los datos de un usuario para almacenarlo
     */
    fun navigate()
    {
        val fragment = supportFragmentManager.findFragmentByTag(InputFragment::class.java.name)
            ?: InputFragment()
        supportFragmentManager.beginTransaction().addToBackStack(InputFragment::class.java.name)
            .add(R.id.main_container, fragment, InputFragment::class.java.name).commit()
    }

    /**
     * Navega a  mostrar el detalle de un usuario capturado
     */
    fun navigate(user:User)
    {
        val fragment = supportFragmentManager.findFragmentByTag(EditDeleteFragment::class.java.name)
            ?: EditDeleteFragment()
        (fragment as EditDeleteFragment).user = user
        supportFragmentManager.beginTransaction().addToBackStack(EditDeleteFragment::class.java.name)
            .add(R.id.main_container, fragment, EditDeleteFragment::class.java.name).commit()
    }

}
