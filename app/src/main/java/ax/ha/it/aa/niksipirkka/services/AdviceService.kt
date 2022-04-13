package ax.ha.it.aa.niksipirkka.services

import retrofit2.Call
import retrofit2.http.GET

import ax.ha.it.aa.niksipirkka.entities.AdviceWithCategory
import ax.ha.it.aa.niksipirkka.entities.Category
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AdviceService {

    @GET("getcategories.json")
    fun loadCategories(): Call<List<Category>>

    @GET("getadvice.json")
    fun loadAdvice(): Call<List<AdviceWithCategory>>

    /*@POST("addadvice")
    fun pushAdvice(advice: AdviceWithCategory): */

    /*@DELETE("")
    fun deleteAdvice():*/
}