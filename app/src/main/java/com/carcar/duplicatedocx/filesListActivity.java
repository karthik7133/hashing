package com.carcar.duplicatedocx;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executors;

public class filesListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FilesAdapter adapter;
    filesdatabse db;
    fileDAO dao;
    List<filedata> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileslist);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Room Database
        db = Room.databaseBuilder(getApplicationContext(), filesdatabse.class, "file_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        dao = db.getfileDAO();
        Executors.newSingleThreadExecutor().execute(() -> {
            fileList = dao.getallfiles();
            new Handler(Looper.getMainLooper()).post(() -> {
                adapter = new FilesAdapter(filesListActivity.this, fileList, dao);
                recyclerView.setAdapter(adapter);
            });
        });
    }
}
