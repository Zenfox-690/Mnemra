package com.example.mnemra.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mnemra.data.dao.*
import com.example.mnemra.data.entity.*

@Database(
        entities =
                [
                        Memory::class,
                        Source::class,
                        Tag::class,
                        MemoryTag::class,
                        CollectionEntity::class,
                        MemoryCollection::class,
                        Flashcard::class,
                        Review::class],
        version = 2,
        exportSchema = true
)
abstract class MnemraDatabase : RoomDatabase() {

    abstract fun memoryDao(): MemoryDao

    abstract fun sourceDao(): SourceDao

    abstract fun tagDao(): TagDao

    abstract fun collectionDao(): CollectionDao

    abstract fun flashcardDao(): FlashcardDao

    abstract fun reviewDao(): ReviewDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create new tags table structure
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `tags_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `color` INTEGER, 
                        `createdAt` INTEGER NOT NULL, 
                        `updatedAt` INTEGER NOT NULL
                    )
                """.trimIndent())
                
                // Transfer tags data
                db.execSQL("""
                    INSERT INTO `tags_new` (`id`, `name`, `color`, `createdAt`, `updatedAt`)
                    SELECT `id`, `name`, NULL, 0, 0 FROM `tags`
                """.trimIndent())
                
                // Drop old table and rename new table
                db.execSQL("DROP TABLE `tags`")
                db.execSQL("ALTER TABLE `tags_new` RENAME TO `tags`")
                
                // Create unique index on tags.name
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tags_name` ON `tags` (`name`)")

                // Add createdAt to memory_tags
                db.execSQL("ALTER TABLE `memory_tags` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")

                // Create indices on memoryId and tagId
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_tags_memoryId` ON `memory_tags` (`memoryId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_tags_tagId` ON `memory_tags` (`tagId`)")
            }
        }
    }
}
