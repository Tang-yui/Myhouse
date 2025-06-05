package com.example.teacherconsultapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
        holder.itemView.setOnClickListener { onItemClick(contact) }
    }

    override fun getItemCount(): Int = contacts.size

    fun update(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.contact_name)
        private val numberView: TextView = itemView.findViewById(R.id.contact_number)

        fun bind(contact: Contact) {
            nameView.text = contact.name
            numberView.text = contact.phoneNumber
        }
    }
}
