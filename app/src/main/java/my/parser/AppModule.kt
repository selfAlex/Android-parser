package my.parser

import my.parser.data.ParserApi
import my.parser.main.DefaultMainRepository
import my.parser.main.MainRepository
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://192.168.1.22:5000/"

object AppModule {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(6000, TimeUnit.SECONDS)
        .writeTimeout(6000, TimeUnit.SECONDS)
        .readTimeout(6000, TimeUnit.SECONDS)
        .build()

    fun provideParserApi(): ParserApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ParserApi::class.java)

}
