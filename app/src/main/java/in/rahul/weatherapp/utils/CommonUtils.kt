package `in`.rahul.weatherapp.utils

import `in`.rahul.weatherapp.BuildConfig
import android.content.Context
import android.util.Log
import android.widget.Toast

class CommonUtils {
    companion object {
        fun logMessage(tag:String, message:String){
            if (BuildConfig.DEBUG){
                Log.e(tag, message)
            }
        }
        fun toastShortMessage(context:Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}