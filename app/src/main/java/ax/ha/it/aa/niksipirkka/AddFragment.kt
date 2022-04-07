package ax.ha.it.aa.niksipirkka

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
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

class AddFragment : Fragment() {
    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        setupUI(binding.root)
        val model = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val spinnerAdapter : ArrayAdapter<String> = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories)
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter
        // Setting up button listener in landscape mode will crash the app as
        // we have no NavHost in that layout
        if (activity!!.resources.configuration.orientation ==
            Configuration.ORIENTATION_PORTRAIT
        ) {
            binding.button2.setOnClickListener { view ->
                val content: String = binding.contentData.text.toString()
                val category: String = binding.spinner.selectedItem.toString()
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
