package hu.bme.aut.android.redivel.freshfridge

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.android.redivel.freshfridge.adapter.ShoppingAdapter
import hu.bme.aut.android.redivel.freshfridge.data.ShoppingItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivityShoppingBinding
import kotlin.concurrent.thread

class ShoppingActivity : BaseActivity(),
    ShoppingAdapter.ShoppingItemClickListener
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
        adapter = ShoppingAdapter(this, baseContext)
        binding.rvShopping.layoutManager = LinearLayoutManager(applicationContext)
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
}