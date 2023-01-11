package com.example.esdgspro

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SampleDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("確認")
        println(builder)
        //builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage("データを削除しても良いですか？")
        builder.setPositiveButton("はい") { dialog, id ->

        }
        builder.setNegativeButton("いいえ") { dialog, id ->
            //Cancel
        }
        return builder.create()
    }
}