package com.blueray.kees.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.blueray.kees.model.CustomerData
import com.blueray.kees.model.DriverData
import com.blueray.kees.ui.activities.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

object HelperUtils {
const val PHONENUMEBR = "PHONE"
    const val FlagRegestr = "FlagRegestr"
    const val SHARED_PREF = "Montak_KEY"
    const val BASE_URL= "http://montek.com.dedi5536.your-server.de/app/"
    var FULL_NAME = ""
    var GENDER = "" //1 FOR MALE 2 FOR FEMALE
    var DATE_OF_BIRTH = "" //yyyy-mm-dd format
    var EMAIL = ""
    var LAT = ""
    var LONG = ""
    var AREA = ""
    var AREADetail = ""

    var CITY = ""
    var IMAGE : File? = null
    var LANG = "en"
    var PASSWORD = ""
    var CONFIRM_PASSWORD = ""
    var OTP = ""
    var FromLogin = false
    var LOCATION_IN_STRING = ""

    var NAME: String = ""
    var EMAIL2: String = ""

    fun String.toStringRequestBody():RequestBody{
        return toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

//    fun setLang(mContext:Context , lang:String) {
//        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString(LANG,lang)
//        editor.apply()
//    }

    fun saveUserData(mContext:Context , customerData: CustomerData) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
    }
    fun saveDriverData(mContext:Context , customerData: DriverData) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
    }
    fun saveUserToken(mContext:Context , token : String) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token",token)
        editor.apply()
    }
    fun saveUserName(mContext:Context , name : String) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name",name)
        editor.apply()
    }
    fun saveUserEmail(mContext:Context , email : String) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email",email)
        editor.apply()
    }
    fun logOutUser(mContext:Context){
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    fun getToken(mContext:Context):String {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString("token","").toString()
    }

    fun getName(mContext:Context):String {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString("name","").toString()
    }
    fun getEmail(mContext:Context):String {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString("email","").toString()
    }


    fun saveDriverStatus(mContext:Context , driver_status : String) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("driver_status",driver_status)
        editor.apply()
    }
    fun getDriverStatus(mContext:Context):String {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences.getString("driver_status","").toString()
    }


    fun getLang(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("lang", LANG)!!
    }

    fun setLang(mContext: Context?,lang: String){
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        LANG = lang
        editor?.putString("lang",lang)
        editor?.apply()
    }

    fun getUID(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("uid", "0")!!
    }

    fun isBranchSelected(mContext: Context?): Boolean {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getInt("branch_id", 0) != 0
    }

    fun isGuest(mContext: Context?): Boolean {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val isUserIDEmpty = sharedPreferences?.getString("uid", "0") == "0"
        if (isUserIDEmpty)
            openLoginActivity(mContext)
        return isUserIDEmpty
    }

    fun hideKeyBoard(activity: Activity) {
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        (activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } else false
    }

    //open login activity for guest user
    fun openLoginActivity(mContext: Context?) {
        Toast.makeText(mContext, "Context?.getString(R.string.guest_login)", Toast.LENGTH_LONG)
            .show()
        val splashIntent = Intent(mContext, MainActivity::class.java)
        splashIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext?.startActivity(splashIntent)
    }
    fun setDefaultLanguage(context: Context, lang: String?) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }
    fun showMessage(context: Context , message: String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    @SuppressLint("HardwareIds")
    fun getAndroidID(mContext: Context?): String =
        Settings.Secure.getString(mContext?.contentResolver, Settings.Secure.ANDROID_ID)

}