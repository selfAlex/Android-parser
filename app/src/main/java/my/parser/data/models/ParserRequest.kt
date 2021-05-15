package my.parser.data.models

data class ParserRequest (
    val hardware: String,

    val useShop: Boolean,
    val useForcecom: Boolean,
    val useTomas: Boolean
)