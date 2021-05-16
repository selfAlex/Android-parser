package my.parser.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParserRequest (
    val hardware: String,

    val useShop: Boolean,
    val useForcecom: Boolean,
    val useTomas: Boolean
): Parcelable