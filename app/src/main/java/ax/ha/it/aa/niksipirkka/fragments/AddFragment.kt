package ax.ha.it.aa.niksipirkka.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.preference.PreferenceManager
import ax.ha.it.aa.niksipirkka.MyViewModel
import ax.ha.it.aa.niksipirkka.R
import ax.ha.it.aa.niksipirkka.databinding.FragmentAddBinding
import ax.ha.it.aa.niksipirkka.entities.Advice
import ax.ha.it.aa.niksipirkka.entities.Category

class AddFragment : Fragment() {
    private lateinit var binding : FragmentAddBinding
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)
        setupUI(binding.root)
        val model = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        model.getCategories().observe(viewLifecycleOwner) {
            val list: Array<Category> = it.toTypedArray()
            val spinnerAdapter : ArrayAdapter<Category> = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                list
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = spinnerAdapter
            val cate: String = sharedPreferences.getString("default_category","1")!!
            println("cate: $cate")
            binding.spinner.setSelection(cate.toInt())
        }
        Thread {
            try {
                Thread.sleep(1000)
            } catch (ex: InterruptedException) {
                Log.e("ROOM", "INTERRUPT")
            }
        }

        // Setting up button listener in landscape mode will crash the app as
        // we have no NavHost in that layout
        if (activity!!.resources.configuration.orientation ==
            Configuration.ORIENTATION_PORTRAIT
        ) {
            binding.button2.setOnClickListener { view ->
                val content: String = binding.contentData.text.toString()
                val cat: Category = binding.spinner.selectedItem as Category
                val category: Int = cat.getCategoryId()
                val curVal : String? = sharedPreferences.getString("author_name", "Anonymous")
                model.addAdvice(Advice(curVal!!, content, category))
                findNavController(view).navigate(
                    R.id.action_addFrag_to_showFrag
                )
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, _ ->
                //v.performClick()
                hideKeyboard(this.context, v)
                v.clearFocus()
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }
    private fun hideKeyboard(context: Context?, view: View) {
        val inputMethodManager: InputMethodManager =
            context!!.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
