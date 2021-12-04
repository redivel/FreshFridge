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
import hu.bme.aut.android.redivel.freshfridge.databinding.DialogAddToShoppingBinding

class AddToShoppingDialogFragment(private val item: FridgeItem) : DialogFragment() {
    interface AddToShoppingDialogListener {
        fun onItemAdded(newItem: ShoppingItem)
    }

    private lateinit var listener: AddToShoppingDialogListener
    private lateinit var binding: DialogAddToShoppingBinding

    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private val uid: String?
        get() = firebaseUser?.uid

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as AddToShoppingDialogListener
            ?: throw RuntimeException("Activity must implement the AddToShoppingDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddToShoppingBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireContext())
            .setTitle("Add to shoppinglist?")
            .setView(binding.root)
            .setPositiveButton("Yes") { dialogInterface, i ->
                listener.onItemAdded(ShoppingItem(item))
            }

            .setNegativeButton("No", null)
            .create()
    }

    companion object {
        const val TAG = "AddToShoppingDialogFragment"
    }
}