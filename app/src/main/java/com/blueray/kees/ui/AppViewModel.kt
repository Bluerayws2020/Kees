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

class AppViewModel(application: Application) : AndroidViewModel(application) {

    val repo = Repository
    val context = application.applicationContext
    private val registrationLiveData = MutableLiveData<NetworkResults<RegistrationModel>>()
    private val sendOtpLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val loginLiveData = MutableLiveData<NetworkResults<LoginResponse>>()
    private val getMainCategoriesLiveData = MutableLiveData<NetworkResults<GetMainCategories>>()
    private val getSubCategoriesLiveData = MutableLiveData<NetworkResults<GetMainCategories>>()
    private val getProductsLiveData = MutableLiveData<NetworkResults<GetProductsModel>>()
    private val shiftsLiveData = MutableLiveData<NetworkResults<ShiftsModel>>()
    private val weeksAfterRegistrationLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val getWeeklyCartLiveData = MutableLiveData<NetworkResults<GetWeeklyCartModel>>()
    private val addToCartLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val getProductDetailsLiveData = MutableLiveData<NetworkResults<GetProductDetailsResponse>>()
    private val addRemoveWishlistProductLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val getFavoriteProductsLiveData = MutableLiveData<NetworkResults<GetProductsModel>>()

    fun retrieveRegistration(
        fullName: String,
        gender: String,//1 if male 2 if female
        date_of_birth: String,
        latitude: String,
        longitude: String,
        city_id: String,
        area: String,
        address: String,
        password: String,
        password_confirmation: String,
        image: File? = null,
        house_image: File? = null,
        email: String? = null,
        phone: String? = null
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
                area,
                address,
                password,
                password_confirmation,
                null,
                house_image,
                email,
                phone
            )
        }
    }

    fun getRegistration() = registrationLiveData

    fun retrieveSendOtp(
        otp_code: String
    ) {
        viewModelScope.launch {
            sendOtpLiveData.postValue(
                repo.sendOtpRequest(
                    getToken(context), LANG, otp_code
                )
            )
        }
    }

    fun getSendOtp() = sendOtpLiveData
    fun retrieveLogin(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            loginLiveData.postValue(
                repo.login(
                    LANG, username, password
                )
            )
        }
    }

    fun getLogin() = loginLiveData
    fun retrieveMainCategories(
    ) {
        viewModelScope.launch {
            getMainCategoriesLiveData.postValue(
                repo.getMainCategories(
                    LANG
                )
            )
        }
    }

    fun getMainCategories() = getMainCategoriesLiveData

    fun retrieveSubCategories(
        categoryId: String
    ) {
        viewModelScope.launch {
            getSubCategoriesLiveData.postValue(
                repo.getSubCategories(
                    LANG, categoryId
                )
            )
        }
    }

    fun getSubCategories() = getSubCategoriesLiveData

    fun retrieveProducts(
        categoryId: String? = null,
        textSearch: String? = null
    ) {
        viewModelScope.launch {
            getProductsLiveData.postValue(
                repo.getProducts(
                    LANG, categoryId, textSearch
                )
            )
        }
    }

    fun getProducts() = getProductsLiveData
    fun retrieveShifts(
    ) {
        viewModelScope.launch {
            shiftsLiveData.postValue(
                repo.getShifts(
                    LANG
                )
            )
        }
    }

    fun getShifts() = shiftsLiveData
    fun retrieveWeeksAfterRegistration(
        weeks: List<Int>,
        endTime: String,
        startTime: String,
        day: String
    ) {
        viewModelScope.launch {
            weeksAfterRegistrationLiveData.postValue(
                repo.numberOfWeeksRegistration(
                    LANG, weeks, endTime, startTime, day, getToken(context)
                )
            )
        }
    }

    fun getWeeksAfterRegistration() = weeksAfterRegistrationLiveData
    fun retrieveWeeklyCart(
    ) {
        viewModelScope.launch {
            getWeeklyCartLiveData.postValue(
                repo.getWeeklyCart(
                    LANG,
                    getToken(context)
                )
            )
        }
    }

    fun getWeeklyCart() = getWeeklyCartLiveData
    fun retrieveAddToBasket(
        weeks: List<Int>,
        productId: String,
        quantity: String,
        color_id: String,
        size_id: String,
        unit_id: String,
        weight_id: String,
    ) {
        viewModelScope.launch {
            addToCartLiveData.postValue(
                repo.addProductToWeeklyBaskets(
                    LANG, weeks, productId, quantity, color_id, size_id, unit_id, weight_id,
                    getToken(context)
                )
            )
        }
    }

    fun getAddToBasket() = addToCartLiveData

    fun retrieveProductDetails(
        productId: String
    ) {
        viewModelScope.launch {
            getProductDetailsLiveData.postValue(
                repo.getProductDetails(
                    LANG,
                    productId
                )
            )
        }
    }

    fun getProductDetails() = getProductDetailsLiveData

    fun retrieveAddRemoveWishlistProduct(
        productId: String
    ) {
        viewModelScope.launch {
            addRemoveWishlistProductLiveData.postValue(
                repo.addRemoveWishlistProduct(LANG, getToken(context),productId)
            )
        }
    }

    fun getAddRemoveWishlistProduct() = addRemoveWishlistProductLiveData
    fun retrieveFavoriteProducts(
    ) {
        viewModelScope.launch {
            getFavoriteProductsLiveData.postValue(
                repo.getFavoriteProducts(getToken(context))
            )
        }
    }

    fun getFavoriteProducts() = getFavoriteProductsLiveData

}