package my.parser.data.models

import java.io.Serializable

data class Product (
        val cost: String,
        val description: String,
        val image_url: String,
        val title: String,
        val url: String
        ) : Serializable
