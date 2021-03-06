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
import hu.bme.aut.android.redivel.freshfridge.data.ShoppingItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ItemShoppingBinding
import hu.bme.aut.android.redivel.freshfridge.ui.AddToFridgeDialogFragment

class ShoppingAdapter(private val listener: ShoppingItemClickListener, private val context: Context, private val fm: FragmentManager):
    ListAdapter<ShoppingItem, ShoppingAdapter.ShoppingViewHolder>(itemCallback), BaseAdapter {

    private var shoppingList: MutableList<ShoppingItem> = mutableListOf()
    
    class ShoppingViewHolder(binding: ItemShoppingBinding): RecyclerView.ViewHolder(binding.root){
        val cbBought: CheckBox = binding.cbBought
        val ivIconSh: ImageView = binding.ivIconSh
        val tvNameSh: TextView = binding.tvNameSh
        val tvCategorySh: TextView = binding.tvCategorySh
        val cardViewSh: CardView = binding.cardViewSh
        val ibRemoveSh: ImageButton = binding.ibRemoveSh
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ShoppingViewHolder(
            ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val shoppingItem = shoppingList[position]

        holder.cbBought.isChecked = shoppingItem.bought
        holder.ivIconSh.setImageResource(getImageResource(shoppingItem.category))
        holder.tvNameSh.text = shoppingItem.name
        holder.tvCategorySh.setText(getCategory(shoppingItem.category))
        val color = getBackgroundColor(shoppingItem.category, context)
        holder.cardViewSh.setCardBackgroundColor(color)

        holder.cbBought.setOnCheckedChangeListener { buttonView, isChecked ->
            shoppingList[position].bought = isChecked
            listener.onItemChanged(shoppingList[position])
        }

        holder.ibRemoveSh.setOnClickListener {
            AddToFridgeDialogFragment(shoppingItem).show(fm,AddToFridgeDialogFragment.TAG)
            listener.onItemDeleted(shoppingItem)
        }
    }

    fun addItem(item: ShoppingItem) {
        shoppingList.add(item)
        notifyItemInserted(shoppingList.size-1)
        shoppingList.sortBy { it.name }
        notifyDataSetChanged()
    }

    fun update(item: ShoppingItem) {
        if(shoppingList.contains(item))
            for (it in shoppingList){
                if (it == item){
                    it.bought = item.bought
                }
            }
        else shoppingList.add(item)
        notifyDataSetChanged()
    }

    fun removeItem(item: ShoppingItem) {
        shoppingList.remove(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = shoppingList.size

    interface ShoppingItemClickListener {
        fun onItemChanged(item: ShoppingItem)
        fun onItemDeleted(item: ShoppingItem)
    }

    companion object {
        object itemCallback: DiffUtil.ItemCallback<ShoppingItem>() {
            override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}