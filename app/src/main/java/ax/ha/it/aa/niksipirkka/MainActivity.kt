package ax.ha.it.aa.niksipirkka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ax.ha.it.aa.niksipirkka.databinding.ActivityMainBinding
import ax.ha.it.aa.niksipirkka.databinding.ActivityMainBinding.inflate

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }
}