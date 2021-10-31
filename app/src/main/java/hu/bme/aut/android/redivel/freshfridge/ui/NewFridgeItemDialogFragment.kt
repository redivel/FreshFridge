package hu.bme.aut.android.redivel.freshfridge.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.redivel.freshfridge.R
import hu.bme.aut.android.redivel.freshfridge.data.FridgeItem
import hu.bme.aut.android.redivel.freshfridge.databinding.DialogNewFridgeItemBinding

class NewFridgeItemDialogFragment : DialogFragment(), DatePickerDialogFragment.DateListener {
    interface NewFridgeItemDialogListener {
        fun onFridgeItemCreated(newItem: FridgeItem)
    }

    private lateinit var listener: NewFridgeItemDialogListener

    private lateinit var binding: DialogNewFridgeItemBinding

    var picker: DatePickerDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewFridgeItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
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
                if (isValid()) {
                    listener.onFridgeItemCreated(getFridgeItem())
                }
            }

            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getFridgeItem() = FridgeItem(
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        expirationDate = binding.tvExpirationDate.text.toString(),
        category = FridgeItem.Category.getByOrdinal(binding.spCategory.selectedItemPosition)
            ?: FridgeItem.Category.OTHER,
        isOpen = binding.cbOpened.isChecked
    )

    override fun onDateSelected(date: String) {
        binding.tvExpirationDate.text = date
    }

    companion object {
        const val TAG = "NewShoppingItemDialogFragment"
    }
}