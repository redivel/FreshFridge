package hu.bme.aut.android.redivel.freshfridge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "fridgeitem")
data class FridgeItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "expiration date") var expirationDate: String,
    @ColumnInfo(name = "is_open") var isOpen: Boolean
) {
    enum class Category {
        DAIRY, FRUITS_VEGETABLES, RAW_MEAT, PROCESSED_MEAT, BAKED, READY_MEAL, OTHER;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Category? {
                var ret: Category? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: Category): Int {
                return category.ordinal
            }
        }
    }
}
