package my.parser.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import my.parser.data.models.ParserRequest
import my.parser.data.models.Product
import my.parser.util.DispatcherProvider
import my.parser.util.Resource
import okhttp3.Dispatcher

class MainViewModel(application: Application): AndroidViewModel(application) {

    sealed class ParserEvent {
        class Success(val resultText: ArrayList<Product>): ParserEvent()
        class Failure(val errorText: String): ParserEvent()

        object Loading: ParserEvent()
        object Empty: ParserEvent()
    }

    private val repository = DefaultMainRepository()

    private val _state = MutableStateFlow<ParserEvent> (ParserEvent.Empty)
    val state: StateFlow<ParserEvent> = _state

    fun loadData(
        hardware: String,

        useShop: Boolean,
        useForcecom: Boolean,
        useTomas: Boolean
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ParserEvent.Loading

            when(val dataResponse = repository.loadData(ParserRequest(hardware, useShop, useForcecom, useTomas))) {

                is Resource.Error -> {
                    _state.value = ParserEvent.Failure(dataResponse.message!!)
                    Log.i("INFO_DATA", dataResponse.message)
                }

                is Resource.Success -> {


                    val dataShop = dataResponse.data!!.shop
                    val dataForcecom = dataResponse.data.forcecom
                    val dataTomas = dataResponse.data.tomas

                    val data = ArrayList<Product>()

                    if (dataShop != null) { data.addAll(dataShop) }
                    if (dataForcecom != null) { data.addAll(dataForcecom) }
                    if (dataTomas != null) { data.addAll(dataTomas) }

                    _state.value = ParserEvent.Success(data)
                }

            }
        }

    }


}