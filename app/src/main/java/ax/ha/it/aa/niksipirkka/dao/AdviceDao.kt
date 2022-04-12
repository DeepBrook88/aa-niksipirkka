package ax.ha.it.aa.niksipirkka.dao

import androidx.room.*
import ax.ha.it.aa.niksipirkka.entities.Advice

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