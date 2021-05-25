package my.parser.data

import my.parser.data.models.ParserRequest
import my.parser.data.models.ParserResponse

import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Response

interface ParserApi {

    @POST("/")
    suspend fun loadData(@Body info: ParserRequest): Response<ParserResponse>

}