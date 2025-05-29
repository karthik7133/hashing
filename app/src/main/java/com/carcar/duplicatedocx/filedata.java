package com.carcar.duplicatedocx;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "filedata")
public class filedata {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="fileid")
    int id;

    @ColumnInfo(name = "file_name")
    String name;

    @ColumnInfo(name = "Hashcode")
    String hashcode;

    // Constructor
    public filedata(String name, String hashcode) {
        this.name = name;
        this.hashcode = hashcode;
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getHashcode() { return hashcode; }

    public void setHashcode(String hashcode) { this.hashcode = hashcode; }
}
