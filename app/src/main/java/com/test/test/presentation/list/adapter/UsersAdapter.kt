package com.test.test.presentation.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.test.test.databinding.ItemBinding
import com.test.test.model.User

/**
 * Adapta el listado de usuarios de la base de datos a views para mostrar su contenido
 * Al dar click en un usuario se muestra su detalle
 */
class UsersAdapter(val itemClickListener: (User)->Unit) : ListAdapter<User, UsersAdapter.UserViewHolder>(
    object : DiffUtil.ItemCallback<User>()
    {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }
)
{

    class UserViewHolder(val binding: ItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder
        = UserViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context))
        )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int)
    {
        holder.binding.user = getItem(position)
        holder.binding.entry.setOnClickListener {
            itemClickListener(getItem(position))
        }
        Log.i("User", getItem(position).toString())
        holder.binding.hasPendingBindings()
    }
}

