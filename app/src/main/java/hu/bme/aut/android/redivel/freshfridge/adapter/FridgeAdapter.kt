package hu.bme.aut.android.redivel.freshfridge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ItemFridgeBinding
import hu.bme.aut.android.redivel.freshfridge.ui.AddToShoppingDialogFragment
import java.util.*

class FridgeAdapter(private val listener: FridgeItemClickListener, private val context: Context, private val fm: FragmentManager) :
    ListAdapter<FridgeItem, FridgeAdapter.FridgeViewHolder>(itemCallback), BaseAdapter, Filterable {

    private var fridgeItemListFull: MutableList<FridgeItem> = mutableListOf()
    private var fridgeItemList: MutableList<FridgeItem> = mutableListOf()

    init {
        fridgeItemList = fridgeItemListFull
    }

    class FridgeViewHolder(binding: ItemFridgeBinding) : RecyclerView.ViewHolder(binding.root){
        val cbIsOpen: CheckBox = binding.cbIsOpen
        val ivIcon: ImageView = binding.ivIcon
        val tvName: TextView = binding.tvName
        val tvDescription: TextView = binding.tvDescription
        val tvCategory: TextView = binding.tvCategory
        val tvExpDate: TextView = binding.tvExpDate
        val cardView: CardView = binding.cardView
        val ibRemove: ImageButton = binding.ibRemove
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FridgeViewHolder(
            ItemFridgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FridgeViewHolder, position: Int) {
        val fridgeItem = fridgeItemList[position]

        holder.cbIsOpen.isChecked = fridgeItem.isOpen
        holder.ivIcon.setImageResource(getImageResource(fridgeItem.category))
        holder.tvName.text = fridgeItem.name
        holder.tvDescription.text = fridgeItem.description
        holder.tvCategory.setText(getCategory(fridgeItem.category))
        holder.tvExpDate.text = fridgeItem.expirationDate
        val color = getBackgroundColor(fridgeItem.category, context)
        holder.cardView.setCardBackgroundColor(color)

        holder.cbIsOpen.setOnCheckedChangeListener { buttonView, isChecked ->
            fridgeItemList[position].isOpen = isChecked
            listener.onItemChanged(fridgeItemList[position])
        }

        holder.ibRemove.setOnClickListener {
            AddToShoppingDialogFragment(fridgeItem).show(
                fm,
                AddToShoppingDialogFragment.TAG)
            listener.onItemDeleted(fridgeItem)
        }
    }

    fun addItem(item: FridgeItem) {
        fridgeItemList.add(item)
        notifyItemInserted(fridgeItemList.size-1)
        fridgeItemList.sortBy { it.expirationDate }
        notifyDataSetChanged()
    }

    fun update(item: FridgeItem) {
        if(fridgeItemList.contains(item))
            for (it in fridgeItemList){
                if (it == item){
                    it.isOpen = item.isOpen
                }
            }
        else fridgeItemList.add(item)
        notifyDataSetChanged()
    }

    fun removeItem(item: FridgeItem) {
        fridgeItemList.remove(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fridgeItemList.size

    interface FridgeItemClickListener {
        fun onItemChanged(item: FridgeItem)
        fun onItemDeleted(item: FridgeItem)
    }

    companion object {
        object itemCallback : DiffUtil.ItemCallback<FridgeItem>() {
            override fun areItemsTheSame(oldItem: FridgeItem, newItem: FridgeItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FridgeItem, newItem: FridgeItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    fridgeItemList = fridgeItemListFull
                } else {
                    val resultList: MutableList<FridgeItem> = mutableListOf()
                    for (row in fridgeItemListFull) {
                        if (row.name!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    fridgeItemList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = fridgeItemList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                fridgeItemList = results?.values as MutableList<FridgeItem>
                notifyDataSetChanged()
            }

        }
    }
}