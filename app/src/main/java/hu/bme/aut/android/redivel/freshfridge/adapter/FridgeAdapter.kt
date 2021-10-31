package hu.bme.aut.android.redivel.freshfridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.redivel.freshfridge.R
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ItemFridgeBinding

class FridgeAdapter(private val listener: FridgeItemClickListener) :
    RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder>() {

    private val items = mutableListOf<FridgeItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FridgeViewHolder(
        ItemFridgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FridgeViewHolder, position: Int) {
        val fridgeItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(fridgeItem.category))
        holder.binding.cbIsOpen.isChecked = fridgeItem.isOpen
        holder.binding.tvName.text = fridgeItem.name
        holder.binding.tvDescription.text = fridgeItem.description
        holder.binding.tvCategory.setText(getCategory(fridgeItem.category))
        holder.binding.tvExpDate.text = fridgeItem.expirationDate

        holder.binding.cbIsOpen.setOnCheckedChangeListener { buttonView, isChecked ->
            fridgeItem.isOpen = isChecked
            listener.onItemChanged(fridgeItem)
        }

        holder.binding.ibRemove.setOnClickListener { listener.onItemDeleted(fridgeItem, position) }
    }

    private fun getCategory(category: FridgeItem.Category): Int{
        return when (category){
            FridgeItem.Category.DAIRY -> R.string.DAIRY
            FridgeItem.Category.FRUITS_VEGETABLES -> R.string.FRUITS_VEGETABLES
            FridgeItem.Category.RAW_MEAT -> R.string.RAW_MEAT
            FridgeItem.Category.PROCESSED_MEAT -> R.string.PROCESSED_MEAT
            FridgeItem.Category.BAKED -> R.string.BAKED
            FridgeItem.Category.READY_MEAL -> R.string.READY_MEAL
            FridgeItem.Category.OTHER -> R.string.OTHER
        }
    }

    @DrawableRes()
    private fun getImageResource(category: FridgeItem.Category): Int {
        return when (category) {
            FridgeItem.Category.DAIRY -> R.drawable.groceries
            FridgeItem.Category.FRUITS_VEGETABLES -> R.drawable.groceries
            FridgeItem.Category.RAW_MEAT -> R.drawable.groceries
            FridgeItem.Category.PROCESSED_MEAT -> R.drawable.groceries
            FridgeItem.Category.BAKED -> R.drawable.groceries
            FridgeItem.Category.READY_MEAL -> R.drawable.groceries
            FridgeItem.Category.OTHER -> R.drawable.ic_pencil_grey600_48dp
        }
    }

    fun addItem(item: FridgeItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(fridgeItems: List<FridgeItem>) {
        items.clear()
        items.addAll(fridgeItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: FridgeItem, pos: Int) {
        items.remove(item)
        notifyItemRemoved(pos)
    }

    override fun getItemCount(): Int = items.size

    interface FridgeItemClickListener {
        fun onItemChanged(item: FridgeItem)
        fun onItemDeleted(item: FridgeItem, pos: Int)
    }

    inner class FridgeViewHolder(val binding: ItemFridgeBinding) : RecyclerView.ViewHolder(binding.root)
}