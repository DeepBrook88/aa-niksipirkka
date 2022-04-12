package ax.ha.it.aa.niksipirkka.entities

import java.io.Serializable

data class AdviceWithCategory(
    val category: String,
    val content: String,
    val author: String
): Serializable {
}
