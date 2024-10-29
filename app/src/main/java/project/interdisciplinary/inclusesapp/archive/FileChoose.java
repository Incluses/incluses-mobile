package project.interdisciplinary.inclusesapp.archive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FileChoose {
    private static final int PICK_FILE_REQUEST = 1;

    public void openFileChooser(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, PICK_FILE_REQUEST);
    }
    @SuppressLint("Range")
    public List<String> getFileDetails(Uri uri, Activity activity) {
        String fileName = null, fileType = null, fileSize = null;
        List<String> returnList = new ArrayList<>();
        if (uri.getScheme().equals("content")) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    fileSize = String.valueOf(cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE)));
                    returnList.add(fileSize);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getPath();
            int cut = fileName.lastIndexOf('/');
            if (cut != -1) {
                fileName = fileName.substring(cut + 1);
            }
        }
        if (fileName != null && fileName.contains(".")) {
            int lastIndex = fileName.lastIndexOf('.');
            fileType = fileName.substring(lastIndex + 1).toLowerCase();
            returnList.add(fileName);
            returnList.add(fileType);
        }
        return returnList;
    }

}
