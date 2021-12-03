package hu.bme.aut.android.redivel.freshfridge.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "DatePickerDialog"
    }

    private val calSelectedDate = Calendar.getInstance()

    private lateinit var listener: DateListener

    private val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            datePicker: DatePicker?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            // Setting the new date
            calSelectedDate.set(Calendar.YEAR, year)
            calSelectedDate.set(Calendar.MONTH, monthOfYear)
            calSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            listener.onDateSelected(buildDateText())

            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calSelectedDate.time = Date(System.currentTimeMillis())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(
            requireContext(),
            dateSetListener,
            calSelectedDate.get(Calendar.YEAR),
            calSelectedDate.get(Calendar.MONTH),
            calSelectedDate.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as DateListener
            } else {
                activity as DateListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    private fun buildDateText(): String {
        val dateString = StringBuilder()

        dateString.append(calSelectedDate.get(Calendar.YEAR))
        dateString.append(".")

        var month: String = (calSelectedDate.get(Calendar.MONTH)+1).toString()
        if (month.length == 1) month = "0$month"
        dateString.append(month)
        dateString.append(".")

        var day: String = calSelectedDate.get(Calendar.DAY_OF_MONTH).toString()
        if (day.length == 1) day = "0$day"
        dateString.append(day)
        dateString.append(".")

        return dateString.toString()
    }

    interface DateListener {
        fun onDateSelected(date: String)
    }

}