package hu.bme.aut.android.redivel.freshfridge

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.android.redivel.freshfridge.adapter.FridgeAdapter
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.data.ShoppingItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivityMainBinding
import hu.bme.aut.android.redivel.freshfridge.ui.AddToShoppingDialogFragment
import hu.bme.aut.android.redivel.freshfridge.ui.NewFridgeItemDialogFragment
import kotlin.concurrent.thread

class MainActivity : BaseActivity(),
    FridgeAdapter.FridgeItemClickListener,
    NewFridgeItemDialogFragment.NewFridgeItemDialogListener,
    AddToShoppingDialogFragment.AddToShoppingDialogListener
{

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FridgeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

//        database = FridgeDatabase.getDatabase(applicationContext)

        initRecyclerView()

        binding.fab.setOnClickListener {
            NewFridgeItemDialogFragment().show(
                supportFragmentManager,
                NewFridgeItemDialogFragment.TAG)
        }

        initFridgeItemsListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.icShopping -> {
                startActivity(Intent(this@MainActivity, ShoppingActivity::class.java))
                true
            }
            R.id.icSettings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                true
            }
            R.id.icLogout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        adapter = FridgeAdapter(this, applicationContext, supportFragmentManager)
        binding.rvMain.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvMain.adapter = adapter
        binding.rvMain.itemAnimator = null;
    }

    override fun onItemChanged(item: FridgeItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems-$uid")
            .document(item.id).update("open",item.isOpen)
            .addOnSuccessListener {
                toast("Item updated")
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    override fun onItemDeleted(item: FridgeItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems-$uid")
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

    override fun onItemAdded(newItem: ShoppingItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("shoppinglist-$uid")
            .add(newItem)
            .addOnSuccessListener {
                toast("Item created")
                it.update("id",it.id)
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    private fun initFridgeItemsListener() {
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems-$uid")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    toast(e.toString())
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val item = FridgeItem(dc.document.toObject(FridgeItem::class.java))
                            if (item.id != "")
                                thread{
                                    runOnUiThread{
                                        adapter.addItem(item)
                                    }
                                }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val item = FridgeItem(dc.document.toObject(FridgeItem::class.java))
                            thread {
                                runOnUiThread {
                                    adapter.update(item)
                                }
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            val item = FridgeItem(dc.document.toObject(FridgeItem::class.java))
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


