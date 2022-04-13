package ax.ha.it.aa.niksipirkka.services

import retrofit2.Call

import ax.ha.it.aa.niksipirkka.entities.AdviceWithCategory
import ax.ha.it.aa.niksipirkka.entities.Category
import ax.ha.it.aa.niksipirkka.entities.ResponseMsg
import retrofit2.http.*

interface AdviceService {

    @GET("getcategories")
    fun loadCategories(): Call<List<Category>>

    @GET("getadvice")
    fun loadAdvice(): Call<List<AdviceWithCategory>>

    @POST("addadvice")
    fun pushAdvice(@Query("author") author: String, @Query("advice")content: String, @Query("category")category: String): Call<ResponseMsg>

    /*@DELETE("")
    fun deleteAdvice():*/
}