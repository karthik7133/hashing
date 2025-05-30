package com.carcar.duplicatedocx;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Window;
import android.widget.Toast;


import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_DOCX_FILE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.neon_violate));

        FloatingActionButton fb = findViewById(R.id.fab);
        CardView cv = findViewById(R.id.files);

        fb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_DOCX_FILE);
        });

        cv.setOnClickListener(view -> {
            Intent i = new Intent(this, filesListActivity.class);
            startActivity(i);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_DOCX_FILE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                String fileName = getFileName(fileUri);
                String hash = computeTextHashFromUri(fileUri);

                if (fileName != null && hash != null) {
                    filesdatabse db = Room.databaseBuilder(
                                    getApplicationContext(),
                                    filesdatabse.class,
                                    "file_database"
                            ).allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();

                    // Check if file with same hash exists
                    filedata existingFile = db.getfileDAO().getFileByHashcode(hash);
                    if (existingFile != null) {
                        Toast.makeText(this, "Same file exists", Toast.LENGTH_SHORT).show();
                    } else {
                        db.getfileDAO().addfile(new filedata(fileName, hash));
                        Toast.makeText(this, "File added to database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to get file details", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index >= 0) {
                    result = cursor.getString(index);
                }
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private String computeTextHashFromUri(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {

            // Step 1: Read .docx file using Apache POI
            XWPFDocument document = new XWPFDocument(inputStream);

            // Step 2: Extract plain text
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String textContent = extractor.getText();

            // Step 3: Generate SHA-256 hash of the plain text
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(textContent.getBytes(StandardCharsets.UTF_8));

            // Step 4: Convert hash to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            // Cleanup
            extractor.close();
            document.close();

            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

