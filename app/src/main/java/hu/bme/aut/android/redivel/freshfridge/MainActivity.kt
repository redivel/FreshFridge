package hu.bme.aut.android.redivel.freshfridge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import hu.bme.aut.android.redivel.freshfridge.adapter.FridgeAdapter
//import hu.bme.aut.android.redivel.freshfridge.data.FridgeDatabase
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivityMainBinding
import hu.bme.aut.android.redivel.freshfridge.ui.NewFridgeItemDialogFragment
import kotlin.concurrent.thread
import com.google.firebase.firestore.DocumentReference




class MainActivity : BaseActivity()
    , FridgeAdapter.FridgeItemClickListener
    , NewFridgeItemDialogFragment.NewFridgeItemDialogListener
{

    private lateinit var binding: ActivityMainBinding

//    private lateinit var database: FridgeDatabase
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
        adapter = FridgeAdapter(this, applicationContext)
        binding.rvMain.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvMain.adapter = adapter
        binding.rvMain.itemAnimator = null;
    }

    override fun onItemChanged(item: FridgeItem, idx: Int) {
        thread {
            runOnUiThread {
                adapter.update(item, idx)
            }
            Log.d("MainActivity", "FridgeItem update was successful")
        }
//        initFridgeItemsListener()
    }

    override fun onItemDeleted(item: FridgeItem, idx: Int) {
        thread {
            runOnUiThread {
                adapter.removeItem(item, idx)
            }
        }
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems")
            .document(item.id).delete()
            .addOnSuccessListener {
                toast("Item removed") }
            .addOnFailureListener { e -> toast(e.toString()) }
//        initFridgeItemsListener()
    }

    override fun onFridgeItemCreated(newItem: FridgeItem) {
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems")
            .add(newItem)
            .addOnSuccessListener {
                toast("Item created") }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    private fun initFridgeItemsListener() {
        val db = FirebaseFirestore.getInstance()
        db.collection("fridgeitems")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    toast(e.toString())
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val item = FridgeItem(dc.document.toObject(FridgeItem::class.java))
                            item.id = dc.document.id
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
                                    adapter.update(item, 0)
                                }
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            val item = FridgeItem(dc.document.toObject(FridgeItem::class.java))
                            thread {
                                runOnUiThread {
                                    adapter.removeItem(item, 0)
                                }
                            }
                        }
                    }
                }
            }
    }

}


