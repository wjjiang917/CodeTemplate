package com.jiang.common.util.http;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jiangwenjin on 16/4/12.
 */
public class JHttpApi {
    private static JHttpApi instance = null;
    private Map<String, String> headers = new HashMap<>();
    private JService service;
    private Context context;

    // openweathermap.org    appid 112cef24d554ee64cff677f7bb34b612

    private JHttpApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        // set portalType
                        HttpUrl url = null;
//                        if ("GET".equalsIgnoreCase(original.method())) {
                        url = originalHttpUrl.newBuilder()
//                                .addQueryParameter("portalType", PhoneUtil.getPortalType(context) + "")
                                .addQueryParameter("r", Math.random() + "")
                                .addQueryParameter("appid", "b1b15e88fa797225412429c1c50c122a")
                                .build();
//                        } else {
//                        }

                        Request.Builder builder = original.newBuilder()
                                .url(url)
                                .method(original.method(), original.body());

                        // custom header
                        for (Map.Entry<String, String> entry : headers.entrySet()) {
                            builder.addHeader(entry.getKey(), entry.getValue());
                        }

                        // token to keep log in
//                        builder.addHeader("app", SPUtil.getCookieSessionId(context));
//                        builder.addHeader("Cookie", "JSESSIONID=" + SPUtil.getCookieSessionId(context));

                        Response response = chain.proceed(builder.build());

//                        String reqUrl = chain.request().url().toString();
//                        if (reqUrl.startsWith(Constant.USER_LOGIN_URL) || reqUrl.startsWith(Constant.USER_OTHERLOGIN_URL) || reqUrl.startsWith(Constant.USER_REGIST_URL)) {
//                            MediaType contentType = response.body().contentType();
//                            String content = response.body().string();
//
//                            if (StringUtils.isNotBlank(content)) {
//                                SimpResp<LoginResult> resp = new Gson().fromJson(content, new TypeToken<SimpResp<LoginResult>>() {
//                                }.getType());
//                                if (HttpRequestClient.HTTP_SUCCESS.equals(resp.getReturnCode())) {
//                                    SPUtil.saveCookieApp(context, getSessionId(response), resp.getRecord().getUserInfo().getId());
//                                }
//                            }
//
//                            ResponseBody body = ResponseBody.create(contentType, content);
//                            response = response.newBuilder().body(body).build();
//                        }
//                        SPUtil.saveCookieSessionId(context, getSessionId(response));

                        return response;
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                // .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(JService.class);
    }

    private String getSessionId(Response response) {
        String sessionId = "";
        sessionId = response.header("Set-Cookie", sessionId);
        if (sessionId.contains("JSESSIONID")) {
            sessionId = sessionId.substring(sessionId.indexOf("JSESSIONID") + 11);
            if (sessionId.contains(";")) {
                sessionId = sessionId.substring(0, sessionId.indexOf(";"));
            }
        }

        return sessionId;
    }

    /**
     * Get the HttpApi singleton instance
     */
    public static JHttpApi getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new JHttpApi();
        instance.context = context;
    }

    /**
     * Get the API service to execute calls with
     */
    public JService getService() {
        return service;
    }

    /**
     * Add a header which is added to every API request
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Add multiple headers
     */
    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    /**
     * Remove a header
     */
    public void removeHeader(String key) {
        headers.remove(key);
    }

    /**
     * Remove all headers
     */
    public void clearHeaders() {
        headers.clear();
    }

    /**
     * Get all headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
}
