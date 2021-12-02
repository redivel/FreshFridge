package hu.bme.aut.android.redivel.freshfridge.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.redivel.freshfridge.R
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.data.ItemCategory
import hu.bme.aut.android.redivel.freshfridge.databinding.DialogNewFridgeItemBinding

class NewFridgeItemDialogFragment : DialogFragment(), DatePickerDialogFragment.DateListener {
    interface NewFridgeItemDialogListener {
        fun onFridgeItemCreated(newItem: FridgeItem)
    }

    private lateinit var listener: NewFridgeItemDialogListener
    private lateinit var binding: DialogNewFridgeItemBinding

    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private val uid: String?
        get() = firebaseUser?.uid

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewFridgeItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewFridgeItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewFridgeItemBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        binding.tvExpirationDate.setOnClickListener {
            val datePicker = DatePickerDialogFragment()
            datePicker.setTargetFragment(this, 0)
            fragmentManager?.let { datePicker.show(it, DatePickerDialogFragment.TAG) }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_fridge_item)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                okClick()
            }

            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun okClick() {
        if (isValid()) {
            listener.onFridgeItemCreated(getFridgeItem())
        }
    }

    private fun isValid() = binding.etName.text.isNotEmpty() && binding.tvExpirationDate.text.isNotEmpty()

    private fun getFridgeItem() = FridgeItem(
        uid = uid,
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        expirationDate = binding.tvExpirationDate.text.toString(),
        category = ItemCategory.getByOrdinal(binding.spCategory.selectedItemPosition) ?: ItemCategory.OTHER,
        isOpen = binding.cbOpened.isChecked
    )

    override fun onDateSelected(date: String) {
        binding.tvExpirationDate.setText(date)
    }

    private val userName: String?
        get() = firebaseUser?.displayName

    companion object {
        const val TAG = "NewFridgeItemDialogFragment"
    }
}