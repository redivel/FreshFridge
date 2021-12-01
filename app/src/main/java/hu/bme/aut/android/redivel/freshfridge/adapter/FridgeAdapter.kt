package hu.bme.aut.android.redivel.freshfridge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.redivel.freshfridge.R
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ItemFridgeBinding

class FridgeAdapter(private val listener: FridgeItemClickListener, private val context: Context) :
    ListAdapter<FridgeItem, FridgeAdapter.FridgeViewHolder>(itemCallback) {

    private var fridgeItemList: MutableList<FridgeItem> = mutableListOf()

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
        FridgeViewHolder(ItemFridgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FridgeViewHolder, position: Int) {
        val fridgeItem = fridgeItemList[position]

        holder.cbIsOpen.isChecked = fridgeItem.isOpen
        holder.ivIcon.setImageResource(getImageResource(fridgeItem.category))
        holder.tvName.text = fridgeItem.name
        holder.tvDescription.text = fridgeItem.description
        holder.tvCategory.setText(getCategory(fridgeItem.category))
        holder.tvExpDate.text = fridgeItem.expirationDate
        val color = getBackgroundColor(fridgeItem.category)
        holder.cardView.setCardBackgroundColor(color)

        holder.cbIsOpen.setOnCheckedChangeListener { buttonView, isChecked ->
            fridgeItem.isOpen = isChecked
            listener.onItemChanged(fridgeItem, position)
        }

        holder.ibRemove.setOnClickListener { listener.onItemDeleted(fridgeItem, position) }
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

    private fun getBackgroundColor(category: FridgeItem.Category): Int{
        return when (category){
            FridgeItem.Category.DAIRY -> ContextCompat.getColor(context, R.color.DAIRY)
            FridgeItem.Category.FRUITS_VEGETABLES -> ContextCompat.getColor(context, R.color.FRUITS_VEGETABLES)
            FridgeItem.Category.RAW_MEAT -> ContextCompat.getColor(context, R.color.RAW_MEAT)
            FridgeItem.Category.PROCESSED_MEAT -> ContextCompat.getColor(context, R.color.PROCESSED_MEAT)
            FridgeItem.Category.BAKED -> ContextCompat.getColor(context, R.color.BAKED)
            FridgeItem.Category.READY_MEAL -> ContextCompat.getColor(context, R.color.READY_MEAL)
            FridgeItem.Category.OTHER -> ContextCompat.getColor(context, R.color.OTHER)
        }
    }

    fun addItem(item: FridgeItem) {
        fridgeItemList.add(item)
        notifyItemInserted(fridgeItemList.size-1)
    }

    fun update(item: FridgeItem, idx: Int) {
        fridgeItemList.remove(item)
        fridgeItemList.add(item)
        notifyItemChanged(idx)
    }

    fun removeItem(item: FridgeItem, idx: Int) {
        fridgeItemList.remove(item)
        notifyItemRemoved(idx)
    }

    override fun getItemCount(): Int = fridgeItemList.size

    interface FridgeItemClickListener {
        fun onItemChanged(item: FridgeItem, idx: Int)
        fun onItemDeleted(item: FridgeItem, idx: Int)
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
}