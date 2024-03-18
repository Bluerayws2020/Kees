package com.blueray.kees.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blueray.kees.helpers.HelperUtils.LANG
import com.blueray.kees.helpers.HelperUtils.getEmail
import com.blueray.kees.helpers.HelperUtils.getLang
import com.blueray.kees.helpers.HelperUtils.getName
import com.blueray.kees.helpers.HelperUtils.getToken
import com.blueray.kees.model.AboutUsModel
import com.blueray.kees.model.CustomerGetAddressesModel
import com.blueray.kees.model.CustomerPastOrdersResponse
import com.blueray.kees.model.CustomerProfileModel
import com.blueray.kees.model.DeleteNotificationsResponse
import com.blueray.kees.model.DriverLoginResponse
import com.blueray.kees.model.DriverOrderDetailsResponse
import com.blueray.kees.model.DriverOrdersResponse
import com.blueray.kees.model.ErrorResponse
import com.blueray.kees.model.FinishedOrderDetailsResponse
import com.blueray.kees.model.FinishedOrdersRespose
import com.blueray.kees.model.GetDriverProfileResponse
import com.blueray.kees.model.GetMainCategories
import com.blueray.kees.model.GetMyProfileModel
import com.blueray.kees.model.GetProductDetailsResponse
import com.blueray.kees.model.GetProductsModel
import com.blueray.kees.model.GetWeeklyCartModel
import com.blueray.kees.model.LoginResponse
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.NotificationsResponse
import com.blueray.kees.model.PastOrderDetailsResponse
import com.blueray.kees.model.PrivacyPolicyModel
import com.blueray.kees.model.RegistrationModel
import com.blueray.kees.model.ShiftsModel
import com.blueray.kees.model.UpdateDeliveryStatusResponse
import com.blueray.kees.model.WalletTransactionResponse
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
    private val getProductDetailsLiveData =
        MutableLiveData<NetworkResults<GetProductDetailsResponse>>()
    private val addRemoveWishlistProductLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val getFavoriteProductsLiveData = MutableLiveData<NetworkResults<GetProductsModel>>()
    private val getCustomerProfileLiveData = MutableLiveData<NetworkResults<CustomerProfileModel>>()
    private val changePhoneNumberLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val customerAddressesLiveData =
        MutableLiveData<NetworkResults<CustomerGetAddressesModel>>()
    private val customerUpdateAddressLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val customerAddNewAddressLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val customerDeleteAddressLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val getAboutUsLiveData = MutableLiveData<NetworkResults<AboutUsModel>>()
    private val getPrivacyPoliciesLiveData = MutableLiveData<NetworkResults<PrivacyPolicyModel>>()
    private val retrieveMyProfileLiveData = MutableLiveData<NetworkResults<GetMyProfileModel>>()
    private val changePasswordLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val driverLoginLiveData = MutableLiveData<NetworkResults<DriverLoginResponse>>()
    private val driverProfileLiveData = MutableLiveData<NetworkResults<GetDriverProfileResponse>>()
    private val driverOrdersLiveData = MutableLiveData<NetworkResults<DriverOrdersResponse>>()
    private val driverOrderDetailsLiveData =
        MutableLiveData<NetworkResults<DriverOrderDetailsResponse>>()
    private val updateDeliveryStatusLiveData =
        MutableLiveData<NetworkResults<UpdateDeliveryStatusResponse>>()
    private val finishOrderLiveData =
        MutableLiveData<NetworkResults<UpdateDeliveryStatusResponse>>()
    private val getFinishedOrdersLiveData = MutableLiveData<NetworkResults<FinishedOrdersRespose>>()
    private val contactUsLiveData = MutableLiveData<NetworkResults<UpdateDeliveryStatusResponse>>()
    private val checkOutSingleBasketLiveData =
        MutableLiveData<NetworkResults<UpdateDeliveryStatusResponse>>()
    private val getNotificationsLiveData = MutableLiveData<NetworkResults<NotificationsResponse>>()
    private val deleteNotificationByIdLiveData =
        MutableLiveData<NetworkResults<DeleteNotificationsResponse>>()
    private val deleteAllNotificationsLiveData =
        MutableLiveData<NetworkResults<DeleteNotificationsResponse>>()
    private val customerPastOrdersLiveData =
        MutableLiveData<NetworkResults<CustomerPastOrdersResponse>>()
    private val driverFinishedOrderDetailsLiveData =
        MutableLiveData<NetworkResults<FinishedOrderDetailsResponse>>()
    private val cancelBasketPaymentLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val pastOrderDetailsLiveData =
        MutableLiveData<NetworkResults<PastOrderDetailsResponse>>()
    private val resetPasswordLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val resetPasswordRequestLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val addWeeklyBasketLiveData = MutableLiveData<NetworkResults<ErrorResponse>>()
    private val walletTransactionLiveData =
        MutableLiveData<NetworkResults<WalletTransactionResponse>>()

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
                getLang(context),
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
        otp_code: String,
        phone: String
    ) {
        viewModelScope.launch {
            sendOtpLiveData.postValue(
                repo.sendOtpRequest(
                    lang = getLang(context),
                    otp = otp_code,
                    phone = phone
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
                    getLang(context), username, password
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
                    getLang(context)
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
                    getLang(context), categoryId
                )
            )
        }
    }

    fun getSubCategories() = getSubCategoriesLiveData

    fun retrieveProducts(
        categoryId: String? = null,
        subCategoryId: String? = null,
        textSearch: String? = null
    ) {
        viewModelScope.launch {
            getProductsLiveData.postValue(
                repo.getProducts(
                    getToken(context),
                    getLang(context), categoryId, subCategoryId, textSearch
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
                    getLang(context)
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
                    getLang(context), weeks, endTime, startTime, day, getToken(context)
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
                    getLang(context),
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
        feature_ids: List<String>? = null
    ) {
        viewModelScope.launch {
            addToCartLiveData.postValue(
                repo.addProductToWeeklyBaskets(
                    getLang(context),
                    weeks,
                    productId,
                    quantity,
                    color_id,
                    size_id,
                    unit_id,
                    weight_id,
                    getToken(context),
                    feature_ids
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
                    getToken(context),
                    getLang(context),
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
                repo.addRemoveWishlistProduct(LANG, getToken(context), productId)
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

    fun retrieveCustomerProfile(
    ) {
        viewModelScope.launch {
            getCustomerProfileLiveData.postValue(
                repo.getCustomerProfile(getToken(context))
            )
        }
    }

    fun getCustomerProfile() = getCustomerProfileLiveData

    fun retrieveChangePhoneNumber(
        newPhone: String
    ) {
        viewModelScope.launch {
            changePhoneNumberLiveData.postValue(
                repo.changePhoneNumber(getToken(context), newPhone)
            )
        }
    }

    fun getChangePhoneNumber() = changePhoneNumberLiveData

    fun retrieveCustomerAddresses(
    ) {
        viewModelScope.launch {
            customerAddressesLiveData.postValue(
                repo.customerGetMyAddresses(getToken(context), LANG)
            )
        }
    }

    fun getCustomerAddresses() = customerAddressesLiveData

    fun retrieveCustomerUpdateAddress(
        addressId: String,
        title: String,
        lat: String,
        long: String,
        cityId: String,
        area: String,
        address: String
    ) {
        viewModelScope.launch {
            customerUpdateAddressLiveData.postValue(
                repo.customerUpdateMyAddress(
                    getToken(context),
                    getLang(context),
                    addressId,
                    title,
                    lat,
                    long,
                    cityId,
                    area,
                    address
                )
            )
        }
    }

    fun getCustomerUpdateAddress() = customerUpdateAddressLiveData

    fun retrieveCustomerAddNewAddress(
        title: String,
        lat: String,
        long: String,
        cityId: String,
        area: String,
        address: String
    ) {
        viewModelScope.launch {
            customerAddNewAddressLiveData.postValue(
                repo.customerAddNewAddress(
                    getToken(context),
                    getLang(context),
                    title,
                    lat,
                    long,
                    cityId,
                    area,
                    address
                )
            )
        }
    }

    fun getCustomerAddNewAddress() = customerAddNewAddressLiveData
    fun retrieveCustomerDeleteAddress(
        addressId: String,
    ) {
        viewModelScope.launch {
            customerDeleteAddressLiveData.postValue(
                repo.customerDeleteMyAddress(
                    getToken(context),
                    getLang(context), addressId
                )
            )
        }
    }

    fun getCustomerDeleteAddress() = customerDeleteAddressLiveData

    fun retrieveAboutUs(
    ) {
        viewModelScope.launch {
            getAboutUsLiveData.postValue(
                repo.getAboutUs(
                    getLang(context)
                )
            )
        }
    }

    fun getAboutUs() = getAboutUsLiveData
    fun retrievePrivacyPolicies(
    ) {
        viewModelScope.launch {
            getPrivacyPoliciesLiveData.postValue(
                repo.getPrivacyPolicies(
                    getLang(context)
                )
            )
        }
    }

    fun getPrivacyPolicies() = getPrivacyPoliciesLiveData
    fun retrieveMyProfile(
    ) {
        viewModelScope.launch {
            retrieveMyProfileLiveData.postValue(
                repo.getMyProfile(
                    getToken(context),
                    getLang(context)
                )
            )
        }
    }

    fun getMyProfile() = retrieveMyProfileLiveData
    fun retrieveChangePassword(
        old: String,
        new: String,
        confirm: String
    ) {
        viewModelScope.launch {
            changePasswordLiveData.postValue(
                repo.changePassword(
                    getToken(context),
                    getLang(context),
                    old, new, confirm
                )
            )
        }
    }

    fun getChangePassword() = changePasswordLiveData

    fun retrieveDriverLogin(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            driverLoginLiveData.postValue(
                repo.driverLogin(
                    getLang(context),
                    email,
                    password
                )
            )
        }
    }

    fun getDriverLogin() = driverLoginLiveData
    fun retrieveDriverProfile() {
        viewModelScope.launch {
            driverProfileLiveData.postValue(
                repo.getDriverProfile(
                    getToken(context)
                )
            )
        }
    }

    fun getDriverProfile() = driverProfileLiveData

    fun retrieveDriverOrders() {
        viewModelScope.launch {
            driverOrdersLiveData.postValue(
                repo.getDriverOrders(
                    getToken(context)
                )
            )
        }
    }

    fun getDriverOrders() = driverOrdersLiveData

    fun retrieveDriverOrderDetails(
        basket_id: String
    ) {
        viewModelScope.launch {
            driverOrderDetailsLiveData.postValue(
                repo.getDriverOrderDetails(
                    getToken(context),
                    basket_id,
                    getLang(context)
                )
            )
        }
    }

    fun getDriverOrderDetails() = driverOrderDetailsLiveData

    fun retrieveUpdateDeliveryStatus(
        basket_id: String
    ) {
        viewModelScope.launch {
            updateDeliveryStatusLiveData.postValue(
                repo.updateDeliveryStatus(
                    getToken(context),
                    basket_id
                )
            )
        }
    }

    fun getUpdateDeliveryStatus() = updateDeliveryStatusLiveData

    fun retrieveFinishOrder(
        basket_id: String
    ) {
        viewModelScope.launch {
            finishOrderLiveData.postValue(
                repo.finishOrder(
                    getToken(context), basket_id
                )
            )
        }
    }

    fun getFinishOrder() = finishOrderLiveData

    fun retrieveFinishedOrders() {
        viewModelScope.launch {
            getFinishedOrdersLiveData.postValue(
                repo.getFinishedOrders(
                    getToken(context)
                )
            )
        }
    }

    fun getFinishedOrders() = getFinishedOrdersLiveData

    fun retrieveContactUs(

        phone: String,
        subject: String,
        message: String
    ) {
        viewModelScope.launch {
            contactUsLiveData.postValue(
                repo.contactUsRequest(
                    getLang(context),
                    getName(context), getEmail(context), phone, subject, message
                )
            )
        }
    }

    fun getContactUs() = contactUsLiveData

    fun retrieveNotifications() {
        viewModelScope.launch {
            getNotificationsLiveData.postValue(
                repo.getNotifications(
                    getToken(context)
                )
            )
        }
    }

    fun getNotifications() = getNotificationsLiveData

    fun retrieveCheckOutSingleBasket(
        weekly_basket_id: String,
        coupon_code: String,
        address_id: String,
        title: String,
        latitude: String,
        longitude: String,
        city_id: String,
        area: String,
        address: String,
        note: String
    ) {
        viewModelScope.launch {
            checkOutSingleBasketLiveData.postValue(
                repo.checkOutSingleBasket(
                    token = getToken(context),
                    lang = getLang(context),
                    weekly_basket_id = weekly_basket_id,
                    coupon_code = coupon_code,
                    address_id = address_id,
                    title = title,
                    latitude = latitude,
                    longitude = longitude,
                    city_id = city_id,
                    area = area,
                    address = address,
                    note = note
                )
            )
        }
    }

    fun getCheckOutSingleBasket() = checkOutSingleBasketLiveData


    fun retrieveDeleteNotificationById(
        notificationId: String
    ) {
        viewModelScope.launch {
            deleteNotificationByIdLiveData.postValue(
                repo.deleteNotificationById(
                    getToken(context),
                    notificationId
                )
            )
        }
    }

    fun getDeleteNotificationById() = deleteNotificationByIdLiveData

    fun retrieveDeleteAllNotifications() {
        viewModelScope.launch {
            deleteAllNotificationsLiveData.postValue(
                repo.deleteAllNotifications(
                    getToken(context)
                )
            )
        }
    }

    fun getDeleteAllNotifications() = deleteAllNotificationsLiveData

    fun retrieveCustomerPastOrders() {
        viewModelScope.launch {
            customerPastOrdersLiveData.postValue(
                repo.customerPastOrders(
                    getToken(context)
                )
            )
        }
    }

    fun getCustomerPastOrders() = customerPastOrdersLiveData

    fun retrieveDriverFinishedOrderDetails(
        orderId: String
    ) {
        viewModelScope.launch {
            driverFinishedOrderDetailsLiveData.postValue(
                repo.driverFinishedOrderDetails(
                    getToken(context),
                    orderId,
                    getLang(context)
                )
            )
        }
    }

    fun getDriverFinishedOrderDetails() = driverFinishedOrderDetailsLiveData

    fun retrieveCancelBasketPayment(
        weekly_basket_id: String
    ) {
        viewModelScope.launch {
            cancelBasketPaymentLiveData.postValue(
                repo.cancelBasketPayment(
                    getToken(context),
                    getLang(context),
                    weekly_basket_id
                )
            )
        }
    }

    fun getCancelBasketPayment() = cancelBasketPaymentLiveData

    fun retrievePastOrderDetails(
        orderId: String
    ) {
        viewModelScope.launch {
            pastOrderDetailsLiveData.postValue(
                repo.pastOrderDetails(
                    getToken(context),
                    orderId,
                    getLang(context)
                )
            )
        }
    }

    fun getPastOrderDetails() = pastOrderDetailsLiveData

    fun retrieveResetPassword(
        phone: String
    ) {
        viewModelScope.launch {
            resetPasswordLiveData.postValue(
                repo.resetPassword(
                    getToken(context),
                    phone
                )
            )
        }
    }

    fun getResetPassword() = resetPasswordLiveData

    fun retrieveResetPasswordRequest(
        phone: String,
        otp_code: String,
        password: String,
        password_confirmation: String
    ) {
        viewModelScope.launch {
            resetPasswordRequestLiveData.postValue(
                repo.resetPasswordRequest(
                    getToken(context),
                    phone,
                    otp_code, password, password_confirmation
                )
            )
        }
    }

    fun getResetPasswordRequest() = resetPasswordRequestLiveData

    fun retrieveAddWeeklyBasket(
        number_of_week: String,
        start_time: String,
        end_time: String,
        day: String
    ) {
        viewModelScope.launch {
            addWeeklyBasketLiveData.postValue(
                repo.addWeeklyBasket(
                    getToken(context),
                    number_of_week, start_time, end_time, day
                )
            )
        }
    }

    fun getAddWeeklyBasket() = addWeeklyBasketLiveData

    fun retrieveWalletTransaction() {
        viewModelScope.launch {
            walletTransactionLiveData.postValue(
                repo.getMyWalletTransactions(
                    getToken(context),
                    getLang(context)
                )
            )
        }
    }

    fun getWalletTransaction() = walletTransactionLiveData
}