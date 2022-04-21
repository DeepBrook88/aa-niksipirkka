package ax.ha.it.aa.niksipirkka

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import androidx.work.Worker
import androidx.work.WorkerParameters
import ax.ha.it.aa.niksipirkka.activities.MainActivity
import ax.ha.it.aa.niksipirkka.activities.NotificationActivity
import ax.ha.it.aa.niksipirkka.entities.AdviceWithCategory
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import android.R
import ax.ha.it.aa.niksipirkka.entities.Advice
import ax.ha.it.aa.niksipirkka.entities.Category
import ax.ha.it.aa.niksipirkka.repository.NiksiPirkkaRepo

class AdviceWorker(context: Context, params: WorkerParameters) : Worker(context,params){
    override fun doWork(): Result {
        val call: Call<List<AdviceWithCategory>> = MainActivity.getRetrofitAdviceCall()
        val call2: Call<List<Category>> = MainActivity.getRetrofitCategoryCall()
        try {
            val response: Response<List<AdviceWithCategory>> = call.execute()
            val categoryRes: Response<List<Category>> = call2.execute()

            val db = AdviceDatabase.getInstance(applicationContext)
            val repo = NiksiPirkkaRepo(db?.adviceDao()!!,db.categoryDao(),db.adviceWithCat())

            categoryRes.body()?.let { repo.insertCategory(*it.toTypedArray()) }
            response.body()?.forEach { repo.insertAdvice(it) }

            // Create an explicit intent for an Activity in your app
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

            // Show notification
            val builder = NotificationCompat.Builder(
                applicationContext, "my_channel_id"
            )
                .setSmallIcon(R.drawable.star_on)
                .setContentTitle("LOADING FINISHED")
                .setContentText("FOUND ADVICES: " + response.body()!!.size)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
            val notificationManager = NotificationManagerCompat.from(applicationContext)
            // notificationId is a unique int for each notification that you
            // must define
            notificationManager.notify(1, builder.build())
        } catch (e: IOException) {
            Log.e("RETROFITDEMO", "Worker failed: $e")
            return Result.failure()
        }
        return Result.success()
    }

}