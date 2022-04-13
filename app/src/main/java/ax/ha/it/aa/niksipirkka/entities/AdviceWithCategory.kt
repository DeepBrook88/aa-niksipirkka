package ax.ha.it.aa.niksipirkka.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AdviceWithCategory(
    val category: String,
    @SerializedName("advice")
    val content: String,
    val author: String
): Serializable {
}
