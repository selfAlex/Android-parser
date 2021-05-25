package my.parser.main

import my.parser.data.models.ParserRequest
import my.parser.data.models.ParserResponse
import my.parser.util.Resource

import retrofit2.http.Body
import retrofit2.http.POST

interface MainRepository {

    @POST("/")
    suspend fun loadData(@Body info: ParserRequest): Resource<ParserResponse>

}