package com.otus.android_course.petrov.filmfinder.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.otus.android_course.petrov.filmfinder.R

class ExitDialog : DialogFragment() {

    // Интерфейс для обработки нажатий кнопок диалога
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    // Обработчик нажатия на кнопки
    private lateinit var mListener: NoticeDialogListener

    /**
    \brief Получение ссылки на обработчик нажатия на кнопки
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement NoticeDialogListener")
        }
    }

    /**
    \brief Создание диалога
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Завершение работы")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage("Выйти из приложения?")
                .setPositiveButton("Выход") { _, _ ->
                    // Send the positive button event back to the host activity
                    mListener.onDialogPositiveClick(this)
                }
                .setNegativeButton("Отмена", null)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}