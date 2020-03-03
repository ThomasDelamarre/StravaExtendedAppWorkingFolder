package com.example.thomas.stravaappwidgetextended.api.authenticator;

import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Authenticator {
    @FormUrlEncoded
    @POST("token")
    Call <AuthTokens> getAllTokens(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("code") String authorization_code,
            @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @POST("token")
    Call <AuthTokens> refreshTokens(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh_token);
}
