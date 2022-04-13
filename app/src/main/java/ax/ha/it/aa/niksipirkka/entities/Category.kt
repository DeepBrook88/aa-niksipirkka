package ax.ha.it.aa.niksipirkka.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "categories", indices = [Index(value = arrayOf("category"), unique = true)])
class Category(@SerializedName("name") private var category: String): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private var categoryId: Int = 0

    fun getCategoryId(): Int {
        return categoryId
    }

    fun setCategoryId(id: Int) {
        this.categoryId = id
    }
    fun getCategory() : String{
        return category
    }

    override fun toString(): String {
        return this.category
    }

}