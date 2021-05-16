package my.parser

import my.parser.data.ParserApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://192.168.1.22:5000"

object AppModule {

    fun provideParserApi(): ParserApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParserApi::class.java)
}
