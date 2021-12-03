package hu.bme.aut.android.redivel.freshfridge.data


data class FridgeItem(
    var id: String = "",
    var uid: String? = null,
    var name: String? = null,
    var description: String? = null,
    var category: ItemCategory = ItemCategory.OTHER,
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

    constructor(item: ShoppingItem) : this() {
        uid= item.uid
        name= item.name
        category= item.category
    }

    override fun equals(item: Any?): Boolean {
        item ?: return false

        if(item !is FridgeItem) return false

        return id == item.id
    }

}
