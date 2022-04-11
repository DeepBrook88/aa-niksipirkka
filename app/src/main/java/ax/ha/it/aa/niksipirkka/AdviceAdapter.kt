package ax.ha.it.aa.niksipirkka

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ax.ha.it.aa.niksipirkka.databinding.SingleAdviceBinding

class AdviceAdapter(private val advices:List<AdviceWithCategory>): RecyclerView.Adapter<AdviceAdapter.AdapterViewHolder>() {
    // A ViewHolder represents an item view within the RecyclerView
    class AdapterViewHolder(binding: SingleAdviceBinding) : RecyclerView.ViewHolder(binding.root) {
        // The binding is used in Adapter.onBindViewHolder to access the
        // views in the row (only a single TextView in this example)
        val binding: SingleAdviceBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding: SingleAdviceBinding = SingleAdviceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val advice : AdviceWithCategory = advices[position]
        holder.binding.adviceAuthor.text = advice.author
        holder.binding.adviceCategory.text = advice.category
        holder.binding.adviceContent.text = advice.content
    }

    override fun getItemCount(): Int {
        return advices.size
    }
}