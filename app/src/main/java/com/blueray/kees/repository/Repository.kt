package com.blueray.kees.repository

import android.util.Log.e
import com.blueray.kees.api.ApiClient
import com.blueray.kees.helpers.HelperUtils.toStringRequestBody
import com.blueray.kees.model.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object Repository {

    suspend fun register(
        Lang: String,
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
    ): NetworkResults<RegistrationModel> {

        val langRequest = Lang.toStringRequestBody()
        val fullNameRequest = fullName.toStringRequestBody()
        val genderRequest = gender.toStringRequestBody()
        val dateOfBirthRequest = date_of_birth.toStringRequestBody()
        val latitudeRequest = latitude.toStringRequestBody()
        val longitudeRequest = longitude.toStringRequestBody()
        val cityIdRequest = city_id.toStringRequestBody()
        val areaRequest = area.toStringRequestBody()
        val addressRequest = address.toStringRequestBody()
        val passwordRequest = password.toStringRequestBody()
        val passwordConfirmationRequest = password_confirmation.toStringRequestBody()
        val emailRequest = email?.toStringRequestBody()
        val phoneRequest = phone?.toStringRequestBody()

        var imgPart: MultipartBody.Part? = null
        if (house_image != null) {
            val requestBodyimg =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), house_image)
            imgPart =
                MultipartBody.Part.createFormData("house_image", house_image.name, requestBodyimg)
        }
        try {
            val result = ApiClient.retrofitService.register(
                langRequest,
                fullNameRequest,
                genderRequest,
                dateOfBirthRequest,
                latitudeRequest,
                longitudeRequest,
                cityIdRequest,
                areaRequest,
                addressRequest,
                passwordRequest,
                passwordConfirmationRequest,
                null,
                emailRequest,
                phoneRequest,
                imgPart,
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun sendOtpRequest(
        token: String,
        lang: String,
        otp: String
    ): NetworkResults<ErrorResponse> {
        val langRequest = lang.toStringRequestBody()
        val otpRequest = otp.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.sendOtpCode(
                "Bearer $token",
                otpRequest,
                langRequest
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun login(
        lang: String,
        username: String,
        password: String
    ): NetworkResults<LoginResponse> {
        val langRequest = lang.toStringRequestBody()
        val usernameRequest = username.toStringRequestBody()
        val passwordRequest = password.toStringRequestBody()

        try {
            val result = ApiClient.retrofitService.login(
                langRequest,
                usernameRequest,
                passwordRequest
            )

            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun getMainCategories(
        lang: String
    ): NetworkResults<GetMainCategories> {
        val langRequest = lang.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getMainCategories(
                langRequest
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun getSubCategories(
        lang: String,
        categoryId: String
    ): NetworkResults<GetMainCategories> {
        val langRequest = lang.toStringRequestBody()
        val categoryIdRequest = categoryId.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getSubCategories(
                langRequest, categoryIdRequest
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun getProducts(
        lang: String,
        categoryId: String?,
        textSearch: String?
    ): NetworkResults<GetProductsModel> {
        val langRequest = lang.toStringRequestBody()
        val categoryIdRequest = categoryId?.toStringRequestBody()
        val textSearchRequest = textSearch?.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getProducts(
                langRequest, categoryIdRequest, textSearchRequest
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun numberOfWeeksRegistration(
        lang: String,
        weeks: List<Int>,
        endTime: String,
        startTime: String,
        day: String,
        token: String

    ): NetworkResults<ErrorResponse> {
        val body = NumberOfWeeksModel(lang, weeks, startTime, endTime, day)
        try {
            val result = ApiClient.retrofitService.chooseNumberOfWeeklyBasketsAfterRegister(
                "Bearer $token",
                body
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun getShifts(
        lang: String,
    ): NetworkResults<ShiftsModel> {
        val langRequest = lang.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getShifts(
                langRequest
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun getWeeklyCart(
        lang: String,
        token:String
    ): NetworkResults<GetWeeklyCartModel> {
        val langRequest = lang.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getWeeklyCart(
                langRequest,
                "Bearer $token",
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun addProductToWeeklyBaskets(
        lang: String,
        weeks: List<Int>,
        productId: String,
        quantity: String,
        color_id: String,
        size_id: String,
        unit_id: String,
        weight_id: String,
        token: String

    ): NetworkResults<ErrorResponse> {
        val body = AddToBasketRequestBody(
            lang,
            weeks,
            productId,
            quantity,
            color_id,
            size_id,
            unit_id,
            weight_id
        )
        try {
            val result = ApiClient.retrofitService.addProductToWeeklyBaskets(
                "Bearer $token",
                body
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun getProductDetails(
        lang: String,
        productId: String
    ): NetworkResults<GetProductDetailsResponse> {
        val langRequest = lang.toStringRequestBody()
        val productIdBody = productId.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getProductDetails(
                langRequest,
                productIdBody
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

    suspend fun addRemoveWishlistProduct(
        lang: String,
        token:String,
        productId: String
    ): NetworkResults<ErrorResponse> {
        val langRequest = lang.toStringRequestBody()
        val productIdRequest = productId.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.addRemoveWishlistProduct(
                "Bearer $token",
                productIdRequest, langRequest
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }
    suspend fun getFavoriteProducts(
        token:String
    ): NetworkResults<GetProductsModel> {
        try {
            val result = ApiClient.retrofitService.getWishlistProducts(
                "Bearer $token"
            )
            return if (result.isSuccessful) {
                NetworkResults.Success(result.body()!!)
            } else {
                val errorBody = result.errorBody()?.string()

                errorBody?.let {
                    e("Repository Error Message", it)

                    try {
                        // Convert the error response JSON to a common Error Model
                        val apiResponse: ErrorResponse =
                            Gson().fromJson(it, ErrorResponse::class.java)
                        NetworkResults.ErrorMessage(apiResponse)
                    } catch (e: JsonSyntaxException) {
                        // Handle the case where the error response is not a valid JSON
                        NetworkResults.Error(e)
                    }
                } ?: NetworkResults.Error(Exception("Error body is null"))
            }
        } catch (e: Exception) {
            return NetworkResults.Error(e)
        }
    }

}