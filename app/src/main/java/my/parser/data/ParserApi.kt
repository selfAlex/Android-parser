package my.parser.data

import my.parser.data.models.ParserRequest
import my.parser.data.models.ParserResponse

import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call

interface ParserApi {

    @POST("/")

    fun getData(@Body info: ParserRequest): Call<ParserResponse>

}