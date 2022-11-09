package com.compose.myipl.utils

import android.util.Log
import com.compose.myipl.BuildConfig

/** @author Himanshu Mistri
 *  Print log into android logcat console
 */
object LogUtils {

    /**
     * Information in Blue Color
     * @param mTag  Tag for filter log
     * @param mValue Log value to be print in console
     */
    @JvmStatic
    fun i(mTag: String, mValue: String) {
        if (BuildConfig.DEBUG) {
            Log.i(mTag, mValue)
        }
    }


    /**
     * Error messages
     * @param mTag  Tag value for filter
     * @param mValue Log value to be print in console
     */
    @JvmStatic
    fun e(mTag: String, mValue: String) {
        if (BuildConfig.DEBUG) {
            Log.e(mTag, mValue)
        }
    }

    /**
     * Warning messages
     * @param mTag Tag value for filter
     * @param mValue Log value to be print in console
     */
    @JvmStatic
    fun w(mTag: String, mValue: String) {
        if (BuildConfig.DEBUG) {
            Log.w(mTag, mValue)
        }
    }

    /**
     * Verbose messages
     * @param mTag Tag value for filter
     * @param mValue Log value to be print in console
     */
    @JvmStatic
    fun v(mTag: String, mValue: String) {

        if (BuildConfig.DEBUG) {
            Log.v(mTag, mValue)
        }
    }


    /**
     * Debug messages  in Black Color
     * @param mTag Tag value for filter
     * @param mValue Log value to be print in console
     */
    @JvmStatic
    fun d(mTag: String, mValue: String) {
        if (BuildConfig.DEBUG) {
            Log.d(mTag, mValue)
        }
    }
}