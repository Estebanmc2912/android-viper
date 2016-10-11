package cz.helmisek.android.androidrxvipersample.core.interactor;

import android.content.Context;

import java.io.IOException;

import cz.helmisek.android.androidrxviper.core.interactor.RetrofitApiInteractor;
import cz.helmisek.android.androidrxvipersample.api.ApiConfig;
import cz.helmisek.android.androidrxvipersample.api.WeatherAPIEndpoint;
import cz.helmisek.android.androidrxvipersample.core.entity.api.CurrentWeatherEntity;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by Jirka Helmich on 11.10.16.
 */

public class WeatherInteractor extends RetrofitApiInteractor
{

	private static final String UNITS_METRIC = "metric";
	private static final String QUERY_PARAM_APP_ID = "appid";


	@Override
	public Retrofit.Builder getRetrofitBuilder()
	{
		final Retrofit.Builder retrofitBuilder = getDefaultRetrofitBuilder();
		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(new Interceptor()
		{
			@Override
			public okhttp3.Response intercept(Chain chain) throws IOException
			{
				Request original = chain.request();
				HttpUrl httpUrl = original.url().newBuilder()
						.addQueryParameter(QUERY_PARAM_APP_ID, ApiConfig.OPENWEATHER_API_KEY)
						.build();
				Request request = original.newBuilder().url(httpUrl).build();
				return chain.proceed(request);
			}
		});
		retrofitBuilder.client(httpClient.build());
		return retrofitBuilder;
	}


	@Override
	public String getBaseUrl()
	{
		return ApiConfig.OPENWEATHER_API_BASE;
	}


	public WeatherInteractor(Context context)
	{
		super(context);
	}


	public Observable<Response<CurrentWeatherEntity>> loadCurrentWeather(String location)
	{
		return getEndpoint(WeatherAPIEndpoint.class)
				.getCurrentWeather(location, UNITS_METRIC)
				.observeOn(AndroidSchedulers.mainThread());
	}


}
