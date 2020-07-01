package com.examples.android

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtils {
    companion object {

        fun readSharedPref(context: Context, key: String, defVal: Boolean): Boolean {
            val sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE)
            return sharedPref.getBoolean(key, defVal)
        }

        fun readSharedPref(context: Context, prefName: String): SharedPreferences {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        }


        fun saveSharedPref(context: Context, key: String, value: Boolean) {
            context.getSharedPreferences("LogIn", Context.MODE_PRIVATE).let {
                it.edit().let {edit->
                    edit.putBoolean(key, value)
                    edit.commit()
                }
            }
        }

        fun saveSharedPref(context: Context, prefName: String): SharedPreferences.Editor {
            val a = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return a.edit()
        }
    }
}