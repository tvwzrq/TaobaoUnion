package com.example.taobaounion.uitl


import android.util.Log


private const val Verbose=0
private const val Debug=1
private const val Info=2
private const val Warn=3
private const val Error=4
class LogUtil {

    companion object {
        private const val currentLevel=0

        fun v(label: String, msg: String) {
            if (currentLevel <= Verbose) {
                Log.v(label, msg)
            }
        }

        fun d(label: String, msg: String) {
            if (currentLevel <= Debug) {
                Log.d(label, msg)
            }
        }

        fun i(label: String, msg: String) {
            if (currentLevel <= Info) {
                Log.i(label, msg)
            }
        }

        fun w(label: String, msg: String) {
            if (currentLevel <= Warn) {
                Log.w(label, msg)
            }
        }

        fun e(label: String, msg: String) {
            if (currentLevel <= Error) {
                Log.e(label, msg)
            }
        }
    }
}