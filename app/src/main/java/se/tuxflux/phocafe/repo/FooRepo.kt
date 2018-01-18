package se.tuxflux.onyktert.repo

import com.squareup.moshi.Moshi
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import se.tuxflux.phocafe.BuildConfig
import java.util.concurrent.TimeUnit


class FooRepo : IFooRepo {

    private val foo: IFooRepo

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    private val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(BuildConfig.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

    init {
        val moshiAdapter = Moshi.Builder().build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshiAdapter).asLenient()
        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        val apiClient = Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL)
                .client(client)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(moshiConverterFactory)
                .build()

        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        foo = apiClient.create(IFooRepo::class.java)
    }


    override fun getNeverHaveIEver(): Observable<List<String>> = foo.getNeverHaveIEver()
}

