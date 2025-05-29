package com.carcar.duplicatedocx;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface fileDAO {

    @Insert
    void addfile(filedata file);

    @Delete
    void deletefile(filedata file);

    @Query("SELECT * FROM filedata")
    List<filedata> getallfiles();

    @Query("SELECT * FROM filedata WHERE fileid = :fileid")
    filedata getfile(int fileid); // FIXED: Return the file, not void
    @Query("SELECT * FROM filedata WHERE Hashcode = :hashcode LIMIT 1")
    filedata getFileByHashcode(String hashcode);

}
