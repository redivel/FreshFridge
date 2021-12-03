package hu.bme.aut.android.redivel.freshfridge.data

enum class ItemCategory {
    DAIRY, FRUITS_VEGETABLES, RAW_MEAT, PROCESSED_MEAT, BAKED, READY_MEAL, DRINK, OTHER;
    companion object {
        @JvmStatic
        fun getByOrdinal(ordinal: Int): ItemCategory? {
            var ret: ItemCategory? = null
            for (cat in values()) {
                if (cat.ordinal == ordinal) {
                    ret = cat
                    break
                }
            }
            return ret
        }

        @JvmStatic
        fun toInt(category: ItemCategory): Int {
            return category.ordinal
        }
    }
}