package hu.bme.aut.android.redivel.freshfridge.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.data.ShoppingItem
import hu.bme.aut.android.redivel.freshfridge.databinding.DialogAddToFridgeBinding

class AddToFridgeDialogFragment(private val item: ShoppingItem) : DialogFragment() {
    interface AddToFridgeDialogListener {
        fun onItemAdded(newItem: FridgeItem)
    }

    private lateinit var listener: AddToFridgeDialogListener
    private lateinit var binding: DialogAddToFridgeBinding

    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private val uid: String?
        get() = firebaseUser?.uid

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as AddToFridgeDialogListener
            ?: throw RuntimeException("Activity must implement the AddToFridgeDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddToFridgeBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireContext())
            .setTitle("Add to fridge?")
            .setView(binding.root)
            .setPositiveButton("Yes") { dialogInterface, i ->
                listener.onItemAdded(FridgeItem(item))
            }

            .setNegativeButton("No") { dialogInterface, i ->
                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
            }
            .create()
    }

    companion object {
        const val TAG = "AddToFridgeDialogFragment"
    }
}