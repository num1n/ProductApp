package com.example.productapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.data.local.AppDatabase
import com.example.productapp.data.local.ProductEntity
import com.example.productapp.data.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).productDao()
    private val repository = ProductRepository(dao)

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val products: StateFlow<List<ProductEntity>> = _query
        .debounce(300)
        .flatMapLatest { q ->
            if (q.isBlank()) repository.getProducts()
            else repository.searchProducts(q)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch { repository.refreshCache() }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }
}