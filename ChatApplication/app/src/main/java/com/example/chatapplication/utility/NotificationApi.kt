package com.example.chatapplication.utility

import com.example.chatapplication.model.Constants.CONTENT_TYPE
import com.example.chatapplication.model.Constants.FCM_SERVER_KEY
import com.example.chatapplication.model.SendNotifications
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("Authorization: key=$FCM_SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    fun postNotification(@Body notification : String) : Call<String>
}