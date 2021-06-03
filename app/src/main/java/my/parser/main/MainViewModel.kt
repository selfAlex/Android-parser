package my.parser.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import my.parser.data.models.ParserRequest
import my.parser.data.models.Product
import my.parser.util.Resource
import java.util.*
import kotlin.collections.ArrayList

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

                    var dataShop = dataResponse.data?.shop
                    dataShop = dataShop?.sortedByDescending { it.cost }

                    var dataForcecom = dataResponse.data?.forcecom
                    dataForcecom = dataForcecom?.sortedByDescending { it.cost }

                    var dataTomas = dataResponse.data?.tomas
                    dataTomas = dataTomas?.sortedByDescending { it.cost }

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