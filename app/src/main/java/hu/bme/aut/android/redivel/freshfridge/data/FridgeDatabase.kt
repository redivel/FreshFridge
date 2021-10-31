package hu.bme.aut.android.redivel.freshfridge.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FridgeItem::class], version = 1)
@TypeConverters(value = [FridgeItem.Category::class])
abstract class FridgeDatabase : RoomDatabase() {
    abstract fun fridgeItemDao(): FridgeItemDao

    companion object {
        fun getDatabase(applicationContext: Context): FridgeDatabase {
            return Room.databaseBuilder(
                applicationContext,
                FridgeDatabase::class.java,
                "fridge"
            ).build();
        }
    }
}