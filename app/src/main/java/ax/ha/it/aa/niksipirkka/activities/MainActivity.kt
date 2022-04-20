package ax.ha.it.aa.niksipirkka.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.preference.PreferenceManager
import androidx.work.*
import ax.ha.it.aa.niksipirkka.AdviceWorker
import ax.ha.it.aa.niksipirkka.MyViewModel
import ax.ha.it.aa.niksipirkka.R
import ax.ha.it.aa.niksipirkka.databinding.ActivityMainBinding
import ax.ha.it.aa.niksipirkka.entities.AdviceWithCategory
import ax.ha.it.aa.niksipirkka.entities.Category
import ax.ha.it.aa.niksipirkka.entities.ResponseMsg
import ax.ha.it.aa.niksipirkka.services.AdviceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var model: MyViewModel
    private lateinit var listener: OnSharedPreferenceChangeListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)
        createNotificationChannel()
        model = ViewModelProvider(this)[MyViewModel::class.java]
        if (!isConnectedToNetwork(this)) {
            Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show()
        } else {
            getCategoriesFromServer()
            getAdviceFromServer()
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            val state = prefs.getBoolean("switch_refresh",false)
            val duration: Long = prefs.getInt("refreshInterval",15).toLong()
            val workName = "AUTO_REFRESH"
            val workManager = WorkManager.getInstance(this@MainActivity)
            if (state) {
                worker(duration, workManager, workName)
                println("pre started")
            }
            listener = OnSharedPreferenceChangeListener {prefs, key ->
                println("in func")
                if (key == "switch_refresh") {
                    val stateRefresh = prefs.getBoolean("switch_refresh", false)
                    if (stateRefresh) {
                        worker(duration, workManager, workName)
                        println("True")
                    } else {
                        workManager.cancelUniqueWork(workName)
                        println("False")
                    }
                }
            }
            prefs.registerOnSharedPreferenceChangeListener(listener)
        }
    }

    private fun worker(duration: Long, workManager: WorkManager, workName: String) {
        val constraints: Constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        //println("input: " + inputData.keyValueMap["model"])
        // create Data object with Data.Builder
        // Note: minimum period between repeats is 15 minutes
        val myPeriodicWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(AdviceWorker::class.java, duration, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(duration, TimeUnit.MINUTES)
                .build()

        // Unique work => can only exist one instance at a time
        workManager.enqueueUniquePeriodicWork(
            workName,  // unique name
            ExistingPeriodicWorkPolicy.REPLACE,  // Or KEEP => Do nothing if name exists
            myPeriodicWorkRequest
        )
    }

    private fun getAdviceFromServer( ) {
        val advCall: Call<List<AdviceWithCategory>> = getRetrofitAdviceCall()
        advCall.enqueue(object : Callback<List<AdviceWithCategory>> {
            override fun onResponse(
                call: Call<List<AdviceWithCategory>>,
                response: Response<List<AdviceWithCategory>>
            ) {
                if (response.isSuccessful) {
                    val data: List<AdviceWithCategory> = response.body()!!
                    data.forEach { item ->
                        model.addAdvice(AdviceWithCategory(item.category, item.content, item.author ))
                    }
                } else {
                    try {
                        val error = response.errorBody()!!.string()
                        println("error " + error)
                    } catch (e: IOException) {
                        Log.e("NIKSIPIRKKA", "Bad response: $e")
                    }
                }
            }

            override fun onFailure(call: Call<List<AdviceWithCategory>>, t: Throwable) {
                Log.e("NIKSIPIRKKA", "Failure: $t")
            }
        })
    }

    private fun getCategoriesFromServer() {
        val catCall: Call<List<Category>> = getRetrofitCategoryCall()
        catCall.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val data: List<Category> = response.body()!!
                    model.addCategory(*data.toTypedArray())
                } else {
                    try {
                        val error = response.errorBody()!!.string()
                        println("error " + error)
                    } catch (e: IOException) {
                        Log.e("NIKSIPIRKKA", "Bad response: $e")
                    }
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("NIKSIPIRKKA", "Failure: $t")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId: Int = item.itemId
        if (itemId == R.id.action_with_icon) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        } else if (itemId == R.id.refreshAdvice) {
            if (!isConnectedToNetwork(this)) {
                Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show()
            } else  {
                getAdviceFromServer()
            }
            return true
        }
        val navController = findNavController(this, R.id.fragmentContainerView)
        return (onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item))
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://niksipirkka.cloud-ha.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private val service: AdviceService = retrofit.create(AdviceService::class.java)

        private fun isConnectedToNetwork(context: Context): Boolean {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val isConnected: Boolean
            val activeNetwork = connectivityManager.activeNetworkInfo
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnected
            return isConnected
        }
        fun getRetrofitCategoryCall(): Call<List<Category>> {
            return service.loadCategories()
        }
        fun getRetrofitAdviceCall(): Call<List<AdviceWithCategory>> {
            return service.loadAdvice()
        }
        fun pushRetrofitAdviceCall(author: String, content: String, category: String): Call<ResponseMsg> {
            return service.pushAdvice(author,content,category)
        }

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support
        // library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MY CHANNEL NAME"
            val description = "MY CHANNEL DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_id", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change
            // the importance or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}
