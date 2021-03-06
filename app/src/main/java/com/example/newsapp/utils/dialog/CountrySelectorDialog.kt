package com.example.newsapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.newsapp.R

/**
 * This DialogFragment is used to select the country
 *
 * @author SandeepK
 * @version 1.0
 */

class CountrySelectorDialog : DialogFragment() {

    private lateinit var listener: SelectionDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.title_select_country))
                .setItems(R.array.country_list) { dialog, which ->
                    val items = resources.getStringArray(R.array.country_list)
                    listener.onCountryClick(items[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /**
     * This interface is used to return selected country to host activity
     *
     * */
    interface SelectionDialogListener {
        fun onCountryClick(country:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            listener = context as SelectionDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement SelectionDialogListener"))
        }
    }
}