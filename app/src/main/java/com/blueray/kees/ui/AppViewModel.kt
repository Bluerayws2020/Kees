package com.blueray.kees.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueray.kees.helpers.HelperUtils.FULL_NAME
import com.blueray.kees.helpers.HelperUtils.LANG
import com.blueray.kees.helpers.HelperUtils.getToken
import com.blueray.kees.model.*
import com.blueray.kees.repository.Repository
import kotlinx.coroutines.launch
import java.io.File

class AppViewModel(application :Application ) : AndroidViewModel(application) {

    val repo = Repository
    val context = application.applicationContext
    private val registrationLiveData = MutableLiveData<NetworkResults<RegistrationModel>>()
    private val sendOtpLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val loginLiveData = MutableLiveData<NetworkResults<LoginResponse>>()
    private val getMainCategoriesLiveData = MutableLiveData<NetworkResults<GetMainCategories>>()
    private val getSubCategoriesLiveData = MutableLiveData<NetworkResults<GetMainCategories>>()
    private val getProductsLiveData = MutableLiveData<NetworkResults<GetProductsModel>>()

    fun retrieveRegistration(
        fullName: String,
        gender: String,//1 if male 2 if female
        date_of_birth: String,
        latitude: String,
        longitude: String,
        city_id: String,
        address: String,
        password: String,
        password_confirmation: String,
        image: File? = null,
        house_image: File? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            registrationLiveData.value = repo.register(
                LANG,
                fullName,
                gender,
                date_of_birth,
                latitude,
                longitude,
                city_id,
                address,
                password,
                password_confirmation,
                null,
                house_image,
                email
            )
        }
    }

    fun getRegistration() = registrationLiveData

    fun retrieveSendOtp(
        otp_code : String
    ){
        viewModelScope.launch {
            sendOtpLiveData.postValue(repo.sendOtpRequest(
                getToken(context), LANG, otp_code
            ))
        }
    }

    fun getSendOtp() = sendOtpLiveData
    fun retrieveLogin(
        username : String,
        password : String
    ){
        viewModelScope.launch {
            loginLiveData.postValue(repo.login(
                LANG,username, password
            ))
        }
    }

    fun getLogin() = loginLiveData
    fun retrieveMainCategories(
    ){
        viewModelScope.launch {
            getMainCategoriesLiveData.postValue(repo.getMainCategories(
                LANG
            ))
        }
    }

    fun getMainCategories() = getMainCategoriesLiveData

    fun retrieveSubCategories(
        categoryId: String
    ){
        viewModelScope.launch {
            getSubCategoriesLiveData.postValue(repo.getSubCategories(
                LANG, categoryId
            ))
        }
    }

    fun getSubCategories() = getSubCategoriesLiveData

    fun retrieveProducts(
        categoryId: String? = null,
        textSearch : String? = null
    ){
        viewModelScope.launch {
            getProductsLiveData.postValue(repo.getProducts(
                LANG, categoryId,textSearch
            ))
        }
    }

    fun getProducts() = getProductsLiveData

}