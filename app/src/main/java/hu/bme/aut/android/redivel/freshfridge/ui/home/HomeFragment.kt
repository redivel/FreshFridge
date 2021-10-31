package hu.bme.aut.android.redivel.freshfridge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.redivel.freshfridge.adapter.FridgeAdapter
import hu.bme.aut.android.redivel.freshfridge.data.FridgeDatabase
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.FragmentHomeBinding
import hu.bme.aut.android.redivel.freshfridge.ui.NewFridgeItemDialogFragment
import kotlin.concurrent.thread
import androidx.fragment.app.FragmentActivity
import android.app.Activity
import androidx.fragment.app.DialogFragment


class HomeFragment : Fragment(), FridgeAdapter.FridgeItemClickListener, NewFridgeItemDialogFragment.NewFridgeItemDialogListener {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var database: FridgeDatabase
    private lateinit var adapter: FridgeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = context?.let { FridgeDatabase.getDatabase(it) }!!

        initRecyclerView()

        _binding!!.fab.setOnClickListener {
            fragmentManager?.let { it1 -> NewFridgeItemDialogFragment().show(it1,NewFridgeItemDialogFragment.TAG) }
        }

        return root
    }

    private fun initRecyclerView() {
        adapter = FridgeAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this.context)
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
            Log.d("MainActivity", "ShoppingItem update was successful")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
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