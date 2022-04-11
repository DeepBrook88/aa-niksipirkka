package ax.ha.it.aa.niksipirkka

import android.content.Context
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
import ax.ha.it.aa.niksipirkka.databinding.FragmentAddBinding
import java.util.stream.Collectors

class AddFragment : Fragment() {
    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        setupUI(binding.root)
        val model = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        //val dao : CategoryDao = AdviceDatabase.getInstance(requireActivity())!!.categoryDao()
        /*Thread{
            dao.deleteAllCategories()
            dao.insert(Category("Test"),Category("Test2"))
        }.start()*/
        model.getCategories().observe(viewLifecycleOwner) {
            // TODO: make this compatible with API 21
            val list: Array<String> = it.stream().map { a -> a.getCategory() }.collect(Collectors.toList()).toTypedArray()
            val list2: Array<Category> = it.toTypedArray()
            println(it)
            val spinnerAdapter : ArrayAdapter<Category> = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                list2
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = spinnerAdapter
        }
        /*dao.getAllCategories().observe(requireActivity()) {

            val list: Array<String> = it.stream().map { a -> a.getCategory() }.collect(Collectors.toList()).toTypedArray()
            println(it)
            val spinnerAdapter : ArrayAdapter<String> = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_item,
                list
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = spinnerAdapter
        }*/
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
                model.addAdvice(Advice("test Author", content, category))
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
