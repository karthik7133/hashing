package com.carcar.duplicatedocx;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
@Database(entities = {filedata.class},version = 2)

public abstract  class filesdatabse extends RoomDatabase {
    public abstract fileDAO getfileDAO();

}
