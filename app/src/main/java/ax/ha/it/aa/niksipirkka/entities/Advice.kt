package ax.ha.it.aa.niksipirkka.entities

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "advices",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        childColumns = ["category_id"],
        parentColumns = ["category_id"]
    )], indices = [Index(value = arrayOf("category_id","content"), unique = true)])
class Advice(
    private var author: String,
    private var content: String,
    @ColumnInfo(name = "category_id")
    private var categoryId: Int
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "advice_id")
    private var adviceId: Int = 0

    fun getAuthor() : String {
        return author
    }
    fun getContent() : String {
        return content
    }
    fun getCategoryId() : Int {
        return categoryId
    }
    fun setCategoryId(id: Int) {
        this.categoryId = id
    }
    fun getAdviceId(): Int {
        return adviceId
    }
    fun setAdviceId(id: Int) {
        this.adviceId = id
    }
    /*fun getCategory(): Int {
        return adviceId
    }
    fun setAdviceId(id: Int) {
        this.adviceId = id
    }*/
}
