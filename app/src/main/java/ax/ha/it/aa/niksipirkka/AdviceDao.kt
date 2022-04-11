package ax.ha.it.aa.niksipirkka

import androidx.room.*

@Dao
interface AdviceDao {
    @Insert
    fun insert(vararg advices: Advice?)

    @Update
    fun update(vararg advices: Advice?)

    @Query("DELETE FROM advices")
    fun deleteAllAdvices()

    @Query("SELECT * FROM advices")
    fun getAllAdvices() : List<Advice>

}