package com.carcar.duplicatedocx;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileViewHolder> {

    Context context;
    List<filedata> fileList;
    fileDAO fileDao;

    public FilesAdapter(Context context, List<filedata> fileList, fileDAO fileDao) {
        this.context = context;
        this.fileList = fileList;
        this.fileDao = fileDao;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false); // replace with your actual XML name
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        filedata file = fileList.get(position);

        holder.filename.setText(file.getName());
        holder.hashcode.setText(file.getHashcode());

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this file?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        fileDao.deletefile(file); // delete from DB
                        fileList.remove(position); // update list
                        notifyItemRemoved(position);
                        Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView filename, hashcode;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            filename = itemView.findViewById(R.id.dbuname);
            hashcode = itemView.findViewById(R.id.dbupassword);
        }
    }
}
