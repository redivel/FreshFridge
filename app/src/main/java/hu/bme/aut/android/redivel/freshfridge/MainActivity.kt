package hu.bme.aut.android.redivel.freshfridge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.redivel.freshfridge.adapter.FridgeAdapter
import hu.bme.aut.android.redivel.freshfridge.data.FridgeDatabase
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.ActivityMainBinding
import hu.bme.aut.android.redivel.freshfridge.ui.NewFridgeItemDialogFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), FridgeAdapter.FridgeItemClickListener, NewFridgeItemDialogFragment.NewFridgeItemDialogListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var database: FridgeDatabase
    private lateinit var adapter: FridgeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        database = FridgeDatabase.getDatabase(applicationContext)

        initRecyclerView()

        binding.fab.setOnClickListener {
            NewFridgeItemDialogFragment().show(
                supportFragmentManager,
                NewFridgeItemDialogFragment.TAG)
        }
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        adapter = FridgeAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.fridgeItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: FridgeItem) {
        thread {
            database.fridgeItemDao().update(item)
            Log.d("MainActivity", "FridgeItem update was successful")
        }
    }

    override fun onItemDeleted(item: FridgeItem, pos: Int) {
        thread {
            database.fridgeItemDao().deleteItem(item)

            runOnUiThread {
                adapter.removeItem(item,pos)
            }
        }
    }

    override fun onFridgeItemCreated(newItem: FridgeItem) {
        thread {
            database.fridgeItemDao().insert(newItem)

            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }
}