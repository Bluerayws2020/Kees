package com.blueray.kees.api

import com.blueray.kees.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("customer/register")
    suspend fun register(
        @Part("lang") Lang : RequestBody,
        @Part("full_name") fullName : RequestBody,
        @Part("gender") gender : RequestBody,//1 if male 2 if female
        @Part("date_of_birth") date_of_birth : RequestBody,
        @Part("latitude") latitude : RequestBody,
        @Part("longitude") longitude : RequestBody,
        @Part("city_id") city_id : RequestBody,
        @Part("address") address : RequestBody,
        @Part("password") password : RequestBody,
        @Part("password_confirmation") password_confirmation : RequestBody,
        @Part image : MultipartBody.Part? = null,
        @Part house_image : MultipartBody.Part? = null,
        @Part("email") email : RequestBody? = null
    ) : Response<RegistrationModel>

    @Multipart
    @POST("customer/sendOtpCodeRequest")
    suspend fun sendOtpCode(
        @Header("Authorization") auth: String,
        @Part("otp_code") otpCode : RequestBody,
        @Part("lang") lang :RequestBody
    ):Response<ErrorResponse>
    @Multipart
    @POST("customer/login")
    suspend fun login(
        @Part("lang") lang : RequestBody,
        @Part("email") email :RequestBody,
        @Part("password") password :RequestBody
    ):Response<LoginResponse>

    @Multipart
    @POST("frontend/getCategories")
    suspend fun getMainCategories(
        @Part("lang") lang : RequestBody
    ):Response<GetMainCategories>
    @Multipart
    @POST("frontend/getSubCategories")
    suspend fun getSubCategories(
        @Part("lang") lang : RequestBody,
        @Part("category_id") category_id : RequestBody
    ):Response<GetMainCategories>
    @Multipart
    @POST("frontend/getProducts")
    suspend fun getProducts(
        @Part("lang") lang : RequestBody,
        @Part("category_id") category_id : RequestBody?,
        @Part("text_search") text_search: RequestBody?
    ):Response<GetProductsModel>

}