package hu.bme.aut.android.redivel.freshfridge.adapter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import hu.bme.aut.android.redivel.freshfridge.R
import hu.bme.aut.android.redivel.freshfridge.data.ItemCategory

interface BaseAdapter {
    fun getCategory(category: ItemCategory): Int{
        return when (category){
            ItemCategory.DAIRY -> R.string.DAIRY
            ItemCategory.FRUITS_VEGETABLES -> R.string.FRUITS_VEGETABLES
            ItemCategory.RAW_MEAT -> R.string.RAW_MEAT
            ItemCategory.PROCESSED_MEAT -> R.string.PROCESSED_MEAT
            ItemCategory.BAKED -> R.string.BAKED
            ItemCategory.READY_MEAL -> R.string.READY_MEAL
            ItemCategory.OTHER -> R.string.OTHER
        }
    }

    @DrawableRes()
    fun getImageResource(category: ItemCategory): Int {
        return when (category) {
            ItemCategory.DAIRY -> R.drawable.groceries
            ItemCategory.FRUITS_VEGETABLES -> R.drawable.groceries
            ItemCategory.RAW_MEAT -> R.drawable.groceries
            ItemCategory.PROCESSED_MEAT -> R.drawable.groceries
            ItemCategory.BAKED -> R.drawable.groceries
            ItemCategory.READY_MEAL -> R.drawable.groceries
            ItemCategory.OTHER -> R.drawable.ic_pencil_grey600_48dp
        }
    }

    fun getBackgroundColor(category: ItemCategory, context: Context): Int{
        return when (category){
            ItemCategory.DAIRY -> ContextCompat.getColor(context, R.color.DAIRY)
            ItemCategory.FRUITS_VEGETABLES -> ContextCompat.getColor(context, R.color.FRUITS_VEGETABLES)
            ItemCategory.RAW_MEAT -> ContextCompat.getColor(context, R.color.RAW_MEAT)
            ItemCategory.PROCESSED_MEAT -> ContextCompat.getColor(context, R.color.PROCESSED_MEAT)
            ItemCategory.BAKED -> ContextCompat.getColor(context, R.color.BAKED)
            ItemCategory.READY_MEAL -> ContextCompat.getColor(context, R.color.READY_MEAL)
            ItemCategory.OTHER -> ContextCompat.getColor(context, R.color.OTHER)
        }
    }
}