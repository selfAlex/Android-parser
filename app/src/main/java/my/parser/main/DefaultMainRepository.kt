package my.parser.main

import my.parser.AppModule
import my.parser.data.models.ParserRequest
import my.parser.data.models.ParserResponse
import my.parser.util.Resource
import java.lang.Exception

class DefaultMainRepository : MainRepository {

    override suspend fun loadData(info: ParserRequest): Resource<ParserResponse> {

        return try {
            val response = AppModule.provideParserApi().loadData(info)
            val result = response.body()

            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }

    }
}