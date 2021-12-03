package hu.bme.aut.android.redivel.freshfridge.data

data class ShoppingItem(
    var id: String = "",
    var uid: String? = null,
    var name: String? = null,
    var category: ItemCategory = ItemCategory.OTHER,
    var bought: Boolean = false
){
    constructor(item: ShoppingItem) : this() {
        id= item.id
        uid= item.uid
        name= item.name
        category= item.category
        bought= item.bought
    }

    constructor(item: FridgeItem) : this() {
        uid= item.uid
        name= item.name
        category= item.category
    }

    override fun equals(item: Any?): Boolean {
        item ?: return false

        if(item !is ShoppingItem) return false

        return id == item.id
    }
}