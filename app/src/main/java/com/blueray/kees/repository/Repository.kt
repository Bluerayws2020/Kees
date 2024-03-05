package com.blueray.kees.repository

import android.util.Log.e
import com.blueray.kees.api.ApiClient
import com.blueray.kees.helpers.HelperUtils.toStringRequestBody
import com.blueray.kees.model.AboutUsModel
import com.blueray.kees.model.AddToBasketRequestBody
import com.blueray.kees.model.CustomerGetAddressesModel
import com.blueray.kees.model.CustomerProfileModel
import com.blueray.kees.model.DriverLoginResponse
import com.blueray.kees.model.DriverOrderDetailsResponse
import com.blueray.kees.model.DriverOrdersResponse
import com.blueray.kees.model.ErrorResponse
import com.blueray.kees.model.FinishedOrdersRespose
import com.blueray.kees.model.GetDriverProfileResponse
import com.blueray.kees.model.GetMainCategories
import com.blueray.kees.model.GetMyProfileModel
import com.blueray.kees.model.GetProductDetailsResponse
import com.blueray.kees.model.GetProductsModel
import com.blueray.kees.model.GetWeeklyCartModel
import com.blueray.kees.model.LoginResponse
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.NumberOfWeeksModel
import com.blueray.kees.model.PrivacyPolicyModel
import com.blueray.kees.model.RegistrationModel
import com.blueray.kees.model.ShiftsModel
import com.blueray.kees.model.UpdateDeliveryStatusResponse
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
        phone: String,
        lang: String,
        otp: String
    ): NetworkResults<ErrorResponse> {
        val langRequest = lang.toStringRequestBody()
        val otpRequest = otp.toStringRequestBody()
        val phoneRequest = phone.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.sendOtpCode(

                otpCode = otpRequest,
                lang = langRequest,
                phone = phoneRequest
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
        token: String,
        lang: String,
        categoryId: String?,
        textSearch: String?,
    ): NetworkResults<GetProductsModel> {
        val langRequest = lang.toStringRequestBody()
        val categoryIdRequest = categoryId?.toStringRequestBody()
        val textSearchRequest = textSearch?.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getProducts(
                "Bearer $token",
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
        token: String
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
        token: String,
        feature_ids: List<String>?

    ): NetworkResults<ErrorResponse> {
        val body = AddToBasketRequestBody(
            lang,
            weeks,
            productId,
            quantity,
            color_id,
            size_id,
            unit_id,
            weight_id,
            feature_ids
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
        token: String,
        lang: String,
        productId: String
    ): NetworkResults<GetProductDetailsResponse> {
        val langRequest = lang.toStringRequestBody()
        val productIdBody = productId.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getProductDetails(
                "Bearer $token",
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
        token: String,
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
        token: String
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

    suspend fun getCustomerProfile(
        token: String
    ): NetworkResults<CustomerProfileModel> {
        try {
            val result = ApiClient.retrofitService.getCustomerProfile(
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

    suspend fun changePhoneNumber(
        token: String,
        newPhone: String
    ): NetworkResults<ErrorResponse> {
        try {
            val phoneRequestBody = newPhone.toStringRequestBody()
            val result = ApiClient.retrofitService.customerChangePhoneNumber(
                "Bearer $token",
                phoneRequestBody
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

    suspend fun customerGetMyAddresses(
        token: String,
        lang: String
    ): NetworkResults<CustomerGetAddressesModel> {
        try {
            val langBody = lang.toStringRequestBody()
            val result = ApiClient.retrofitService.customerGetMyAddresses(
                "Bearer $token",
                langBody
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

    suspend fun customerUpdateMyAddress(
        token: String,
        lang: String,
        addressId: String,
        title: String,
        lat: String,
        long: String,
        cityId: String,
        area: String,
        address: String
    ): NetworkResults<ErrorResponse> {
        try {
            val langBody = lang.toStringRequestBody()
            val addressIdBody = addressId.toStringRequestBody()
            val titleBody = title.toStringRequestBody()
            val latBody = lat.toStringRequestBody()
            val longBody = long.toStringRequestBody()
            val cityIdBody = cityId.toStringRequestBody()
            val areaBody = area.toStringRequestBody()
            val addressBody = address.toStringRequestBody()
            val result = ApiClient.retrofitService.customerUpdateMyAddress(
                "Bearer $token",
                langBody,
                addressIdBody,
                titleBody,
                latBody,
                longBody,
                cityIdBody,
                areaBody,
                addressBody
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

    suspend fun customerAddNewAddress(
        token: String,
        lang: String,
        title: String,
        lat: String,
        long: String,
        cityId: String,
        area: String,
        address: String
    ): NetworkResults<ErrorResponse> {
        try {
            val langBody = lang.toStringRequestBody()
            val titleBody = title.toStringRequestBody()
            val latBody = lat.toStringRequestBody()
            val longBody = long.toStringRequestBody()
            val cityIdBody = cityId.toStringRequestBody()
            val areaBody = area.toStringRequestBody()
            val addressBody = address.toStringRequestBody()
            val result = ApiClient.retrofitService.customerAddNewAddress(
                "Bearer $token",
                langBody,
                titleBody,
                latBody,
                longBody,
                cityIdBody,
                areaBody,
                addressBody
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

    suspend fun customerDeleteMyAddress(
        token: String,
        lang: String,
        addressId: String
    ): NetworkResults<ErrorResponse> {
        try {
            val langBody = lang.toStringRequestBody()
            val addressIdBody = addressId.toStringRequestBody()

            val result = ApiClient.retrofitService.customerDeleteMyAddress(
                "Bearer $token",
                langBody,
                addressIdBody
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

    suspend fun getAboutUs(
        lang: String,
    ): NetworkResults<AboutUsModel> {
        try {
            val langBody = lang.toStringRequestBody()

            val result = ApiClient.retrofitService.getAboutUs(
                langBody
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

    suspend fun getPrivacyPolicies(
        lang: String,
    ): NetworkResults<PrivacyPolicyModel> {
        try {
            val langBody = lang.toStringRequestBody()

            val result = ApiClient.retrofitService.getPrivacyPolicy(
                langBody
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

    suspend fun getMyProfile(
        token: String,
        lang: String,
    ): NetworkResults<GetMyProfileModel> {
        try {
            val langBody = lang.toStringRequestBody()

            val result = ApiClient.retrofitService.getMyProfile(
                "Bearer $token",
                langBody
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

    suspend fun changePassword(
        token: String,
        lang: String,
        old: String,
        new: String,
        confirm: String
    ): NetworkResults<ErrorResponse> {
        try {
            val langBody = lang.toStringRequestBody()
            val oldBody = old.toStringRequestBody()
            val newBody = new.toStringRequestBody()
            val confirmBody = confirm.toStringRequestBody()

            val result = ApiClient.retrofitService.changeMyPassword(
                "Bearer $token",
                langBody,
                oldBody,
                newBody,
                confirmBody
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

    suspend fun driverLogin(
        lang: String,
        email: String,
        password: String
    ): NetworkResults<DriverLoginResponse> {
        val langRequest = lang.toStringRequestBody()
        val emailRequest = email.toStringRequestBody()
        val passwordRequest = password.toStringRequestBody()

        try {
            val result = ApiClient.retrofitService.driverLogin(
                langRequest,
                emailRequest,
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

    suspend fun getDriverProfile(
        token: String
    ): NetworkResults<GetDriverProfileResponse> {
        try {
            val result = ApiClient.retrofitService.getDriverProfile(
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

    suspend fun getDriverOrders(
        token: String

    ): NetworkResults<DriverOrdersResponse> {
        try {
            val result = ApiClient.retrofitService.getDriverOrders(
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

    suspend fun getDriverOrderDetails(
        token: String,
        basket_id: String
    ): NetworkResults<DriverOrderDetailsResponse> {
        val basketRequest = basket_id.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.getDriverOrderDetails(
                "Bearer $token",
                basketRequest
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

    suspend fun updateDeliveryStatus(
        token: String,
        basket_id: String
    ): NetworkResults<UpdateDeliveryStatusResponse> {
        val basketRequest = basket_id.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.updateDeliveryStatus(
                "Bearer $token",
                basketRequest
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

    suspend fun finishOrder(
        token: String,
        basket_id: String
    ): NetworkResults<UpdateDeliveryStatusResponse> {
        val basketRequest = basket_id.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.finishOrder(
                "Bearer $token",
                basketRequest
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

    suspend fun getFinishedOrders(
        token: String,
    ): NetworkResults<FinishedOrdersRespose> {
        try {
            val result = ApiClient.retrofitService.getFinishedOrders(
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

    suspend fun contactUsRequest(
        lang: String,
        name: String,
        email: String,
        phone: String,
        subject: String,
        message: String
    ): NetworkResults<UpdateDeliveryStatusResponse> {
        val langRequest = lang.toStringRequestBody()
        val nameRequest = name.toStringRequestBody()
        val emailRequest = email.toStringRequestBody()
        val phoneRequest = phone.toStringRequestBody()
        val subjectRequest = subject.toStringRequestBody()
        val messageRequest = message.toStringRequestBody()
        try {
            val result = ApiClient.retrofitService.contactUsRequest(
                langRequest,
                nameRequest,
                emailRequest,
                phoneRequest,
                subjectRequest,
                messageRequest

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
