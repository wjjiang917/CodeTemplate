package com.jiang.common.util.http;

import com.jiang.common.bean.Current;
import com.jiang.common.bean.GitHubUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Jiangwenjin on 16/4/12.
 */
public interface JService {
    // Note: in Retrofit 2.0, the GET path string should NOT start with “/”

    @GET("user")
    Call queryUser(@Query("id") Integer userId);

    @FormUrlEncoded
    @POST("login")
    Call login(@Field("account") String account, @Field("password") String password);

    @GET("weather")
    Observable<Current> getCurrent(@Query("q") String city);

    @GET("users/{user}")
    Observable<GitHubUser> getGitHubUser(@Path("user") String user);
}
