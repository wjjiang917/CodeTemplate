package jiang.com.codetemplate.util.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Jiangwenjin on 16/4/12.
 */
public interface JService {
    @GET("/user")
    Call queryUser(@Query("id") Integer userId);

    @FormUrlEncoded
    @POST("login")
    Call login(@Field("account") String account, @Field("password") String password);
}
