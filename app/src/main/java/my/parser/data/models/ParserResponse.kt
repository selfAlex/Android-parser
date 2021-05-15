package my.parser.data.models

data class ParserResponse(
    val forcecom: List<Forcecom>,
    val shop: List<Shop>,
    val tomas: List<Tomas>
)