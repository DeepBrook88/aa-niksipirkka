package ax.ha.it.aa.niksipirkka

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Advice::class, Category::class], version = 3, exportSchema = false)
abstract class AdviceDatabase : RoomDatabase() {
    //abstract val adviceDao: AdviceDao
    //abstract val categoryDao: CategoryDao
    abstract fun adviceDao(): AdviceDao
    abstract fun categoryDao(): CategoryDao
    abstract fun adviceWithCat(): AdviceWithCategoryDao
    companion object {
        @Volatile
        private var dbInstance: AdviceDatabase? = null
        private const val SHARES_DB_NAME = "my_db"
        fun getInstance(context: Context): AdviceDatabase? {
            if (dbInstance == null) {
                synchronized(this) {
                    if (dbInstance == null) {
                        dbInstance = Room.databaseBuilder(
                            context.applicationContext,
                            AdviceDatabase::class.java,
                            SHARES_DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return dbInstance
        }
    }
}
