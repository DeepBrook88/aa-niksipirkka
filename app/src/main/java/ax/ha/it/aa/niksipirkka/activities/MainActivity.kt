package ax.ha.it.aa.niksipirkka.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import ax.ha.it.aa.niksipirkka.MyViewModel
import ax.ha.it.aa.niksipirkka.R
import ax.ha.it.aa.niksipirkka.databinding.ActivityMainBinding
import ax.ha.it.aa.niksipirkka.entities.Category
import ax.ha.it.aa.niksipirkka.services.AdviceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var model: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)
        model = ViewModelProvider(this)[MyViewModel::class.java]
        if (!isConnectedToNetwork(this)) {
            Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show()
        }

        val call: Call<List<Category>> = getRetrofitCategoryCall()
        call.enqueue(object: Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val data: List<Category> = response.body()!!
                    val prevData = model.getCategories()
                    prevData.observeOnce(this@MainActivity) {
                        val insert: MutableList<Category> = mutableListOf()
                        val existing : MutableList<String> = mutableListOf()
                        it.toTypedArray().forEach { a-> existing.add(a.getCategory())  }
                        if (existing.isEmpty()) {
                            insert.addAll(data)
                        } else {
                            data.forEach { item ->
                                if (!existing.contains(item.getCategory())) {
                                    insert.add(item)
                                }
                            }
                        }
                        model.addCategory(*insert.toTypedArray())
                    }
                } else {
                    try {
                        val error = response.errorBody()!!.string()
                        println("error " + error)
                    } catch (e: IOException) {
                        Log.e("RETROFITDEMO", "Bad response: $e")
                    }
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("RETROFITDEMO", "Failure: $t")
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
        }
        val navController = findNavController(this, R.id.fragmentContainerView)
        return (onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item))
    }

    companion object {
        private fun isConnectedToNetwork(context: Context): Boolean {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            var isConnected = false
            if (connectivityManager != null) {
                val activeNetwork = connectivityManager.activeNetworkInfo
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnected
            }
            return isConnected
        }
        fun getRetrofitCategoryCall(): Call<List<Category>> {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://niksipirkka.cloud-ha.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: AdviceService = retrofit.create(AdviceService::class.java)
            return service.loadCategories()
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
