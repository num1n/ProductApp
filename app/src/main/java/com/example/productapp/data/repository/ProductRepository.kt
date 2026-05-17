package com.example.productapp.data.repository

import com.example.productapp.data.local.ProductDao
import com.example.productapp.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {

    fun getProducts(): Flow<List<ProductEntity>> = dao.getAllProducts()

    fun searchProducts(query: String): Flow<List<ProductEntity>> =
        dao.searchProducts(query)

    suspend fun refreshCache() {
        val newProducts = listOf(
            ProductEntity(name = "Laptop", category = "Electronics", price = 1500.0),
            ProductEntity(name = "Mouse", category = "Accessories", price = 25.0),
            ProductEntity(name = "Keyboard", category = "Accessories", price = 50.0),
            ProductEntity(name = "Monitor", category = "Electronics", price = 800.0),
            ProductEntity(name = "Headphones", category = "Accessories", price = 120.0)
        )
        dao.clearAll()
        dao.insertAll(newProducts)
    }
}