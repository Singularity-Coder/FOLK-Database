package com.singularitycoder.folkdatabase.helper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.singularitycoder.folkdatabase.auth.model.AuthUserItem;
import com.singularitycoder.folkdatabase.database.model.AllUsersItem;
import com.singularitycoder.folkdatabase.database.model.ContactItem;
import com.singularitycoder.folkdatabase.database.model.FolkGuideItem;
import com.singularitycoder.folkdatabase.database.model.TeamLeadItem;
import com.singularitycoder.folkdatabase.database.model.ZonalHeadItem;
import com.singularitycoder.folkdatabase.home.model.HomeItem;
import com.singularitycoder.folkdatabase.profile.model.ProfileContactItem;

//@Database(entities = {
//        AllUsersItem.class,
//        AuthUserItem.class
//}, version = 1, exportSchema = false)
public abstract class RoomDatabaseCache extends RoomDatabase {

    private static RoomDatabaseCache _instance;

    // Use Dao

    static synchronized RoomDatabaseCache getInstance(Context context) {
        if (_instance == null) {
            _instance = Room
                    .databaseBuilder(context.getApplicationContext(), RoomDatabaseCache.class, "the_folk_database_cache")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return _instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
//            new PopulateRoom(_instance).execute();
        }
    };
}
