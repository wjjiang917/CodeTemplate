package com.jiang.common;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiang.common.bean.GitHubUser;
import com.jiang.common.util.Logger;
import com.jiang.common.util.http.JService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.main_content)
    TextView mainContent;

    @Bind(R.id.button)
    Button button;

    private Subscriber<String> subscriber;
    private Observable<String> observable;
    private Action1<String> action1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.clear();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //        subscriber = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                Logger.i("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Logger.e("", e);
//            }
//
//            @Override
//            public void onNext(String s) {
//                Logger.i("onNext: " + s);
//                mainContent.setText(s);
//            }
//        };

        action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                mainContent.setText(s);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("r", Math.random() + "")
                                .addQueryParameter("appid", "b1b15e88fa797225412429c1c50c122a")
                                .build();

                        Request.Builder builder = original.newBuilder()
                                .url(url)
                                .method(original.method(), original.body());

                        return chain.proceed(builder.build());
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                // .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .baseUrl("https://api.github.com/")
                .client(client)
                .build();

        JService jService = retrofit.create(JService.class);
//        Observable<Current> islamabad = jService.getCurrent("Islamabad");
//
//        islamabad.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Current>() {
//                    @Override
//                    public void onCompleted() {
//                        Logger.d("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.e("", e);
//                    }
//
//                    @Override
//                    public void onNext(Current current) {
//                        Logger.d("Current Weather" + current.getWeather().get(0).getDescription());
//                    }
//                });

        Observable<GitHubUser> gitHubUserObservable = jService.getGitHubUser("wjjiang917");
//        gitHubUserObservable.subscribeOn(Schedulers.newThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<GitHubUser>() {
//                    @Override
//                    public void onCompleted() {
//                        Logger.d("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.e("", e);
//                    }
//
//                    @Override
//                    public void onNext(GitHubUser gitHubUser) {
//                        Logger.d("gitHubUser: " + gitHubUser);
//                    }
//                });
        gitHubUserObservable.subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GitHubUser, Object>() {
                    @Override
                    public Object call(GitHubUser gitHubUser) {
                        Logger.d(gitHubUser.toString());
                        return null;
                    }
                })
                .subscribe();
    }

    @OnClick(R.id.button)
    public void clickButton(View view) {
//        observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello RxAndroid~");
//                subscriber.onCompleted();
//            }
//        });

//        observable = Observable.just("Hello RxAndroid~");

        Observable.just("Hello RxAndroid~").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + " by jiang";
            }
        }).subscribe(action1);

//        observable.subscribe(subscriber);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
