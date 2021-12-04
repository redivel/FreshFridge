package hu.bme.aut.android.redivel.freshfridge

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.android.redivel.freshfridge.adapter.ShoppingAdapter
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.data.ShoppingItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivityShoppingBinding
import hu.bme.aut.android.redivel.freshfridge.ui.AddToFridgeDialogFragment
import hu.bme.aut.android.redivel.freshfridge.ui.NewFridgeItemDialogFragment
import kotlin.concurrent.thread

class ShoppingActivity : BaseActivity(),
    ShoppingAdapter.ShoppingItemClickListener,
    AddToFridgeDialogFragment.AddToFridgeDialogListener,
    NewFridgeItemDialogFragment.NewFridgeItemDialogListener
{
    private lateinit var binding: ActivityShoppingBinding
    private lateinit var adapter: ShoppingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initShoppingItemsListener()
    }

    private fun initRecyclerView() {
        adapter = ShoppingAdapter(this@ShoppingActivity, this@ShoppingActivity, supportFragmentManager)
        binding.rvShopping.layoutManager = LinearLayoutManager(this@ShoppingActivity)
        binding.rvShopping.adapter = adapter
        binding.rvShopping.itemAnimator = null;
    }

    override fun onItemChanged(item: ShoppingItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("shoppinglist-$uid")
            .document(item.id).update("bought",item.bought)
            .addOnSuccessListener {
                toast("Item updated")
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    override fun onItemDeleted(item: ShoppingItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("shoppinglist-$uid")
            .document(item.id).delete()
            .addOnSuccessListener {
                toast("Item removed")
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    override fun onFridgeItemCreated(newItem: FridgeItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems-$uid")
            .add(newItem)
            .addOnSuccessListener {
                toast("Item created")
                it.update("id",it.id)
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    private fun initShoppingItemsListener() {
        val db = FirebaseFirestore.getInstance()
        db.collection("shoppinglist-$uid")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    toast(e.toString())
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val item = ShoppingItem(dc.document.toObject(ShoppingItem::class.java))
                            if (item.id != "")
                                thread{
                                    runOnUiThread{
                                        adapter.addItem(item)
                                        toast(adapter.itemCount.toString())
                                    }
                                }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val item = ShoppingItem(dc.document.toObject(ShoppingItem::class.java))
                            thread {
                                runOnUiThread {
                                    adapter.update(item)
                                }
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            val item = ShoppingItem(dc.document.toObject(ShoppingItem::class.java))
                            thread {
                                runOnUiThread {
                                    adapter.removeItem(item)
                                }
                            }
                        }
                    }
                }
            }
    }

    override fun onItemAdded(newItem: FridgeItem) {
        NewFridgeItemDialogFragment(newItem).show(
            supportFragmentManager,
            NewFridgeItemDialogFragment.TAG)
    }
}