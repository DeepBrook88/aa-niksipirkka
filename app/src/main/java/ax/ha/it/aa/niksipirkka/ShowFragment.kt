package ax.ha.it.aa.niksipirkka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ax.ha.it.aa.niksipirkka.databinding.FragmentShowBinding

class ShowFragment : Fragment() {
    private lateinit var binding : FragmentShowBinding
    private lateinit var recyclerViewAdapter: AdviceAdapter
    private lateinit var model: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowBinding.inflate(inflater, container, false)
        val recyclerView : RecyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        // Create a ViewModel tied to this activity
        model = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        model.getAdvices().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter = AdviceAdapter(it)
            recyclerView.adapter = recyclerViewAdapter
        })

        binding.floatingActionButton.setOnClickListener { view ->
            findNavController(view).navigate(
                R.id.action_showFrag_to_addFrag
            )
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}
