package hu.bme.aut.android.redivel.freshfridge.data


data class FridgeItem(
    var id: String = "",
    var uid: String? = null,
    var name: String? = null,
    var description: String? = null,
    var category: Category = Category.OTHER,
    var expirationDate: String? = null,
    var isOpen: Boolean = false
) {

    constructor(item: FridgeItem) : this() {
        id= item.id
        uid= item.uid
        name= item.name
        description= item.description
        category= item.category
        expirationDate= item.expirationDate
        isOpen= item.isOpen
    }

    override fun equals(item: Any?): Boolean {
        item ?: return false

        if(item !is FridgeItem) return false

        return id == item.id
    }

    enum class Category {
        DAIRY, FRUITS_VEGETABLES, RAW_MEAT, PROCESSED_MEAT, BAKED, READY_MEAL, OTHER;
        companion object {
            @JvmStatic
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
            fun toInt(category: Category): Int {
                return category.ordinal
            }
        }
    }
}
