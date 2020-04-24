package com.otus.android_course.petrov.filmfinder

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment

class CustomDialog : DialogFragment() {

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    private lateinit var mListener: NoticeDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement NoticeDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Завершение работы")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage("Выйти из приложения?")
                .setPositiveButton("Выход", DialogInterface.OnClickListener { dialog, id ->
                    // Send the positive button event back to the host activity
                    mListener.onDialogPositiveClick(this)
                })
                .setNegativeButton("Отмена", null)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}