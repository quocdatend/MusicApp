package com.example.musicapp.utils;

import android.content.Context;
import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class ImageUploader {
    public interface UploadCallbackListener {
        void onUploadSuccess(String url);
        void onUploadError(String error);
    }
    public void uploadImage(Context context, Uri imageUri, final UploadCallbackListener listener) {
        String requestId = MediaManager.get().upload(imageUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // Called when the upload request is started
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Called to provide progress information
                        double progress = (double) bytes / totalBytes;
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Called when the upload completes successfully
                        String publicId = (String) resultData.get("public_id");
                        String url = (String) resultData.get("url");
                        if (listener != null) {
                            listener.onUploadSuccess(url);
                        }
                    }


                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        // Called when an error occurs
                        if (listener != null) {
                            listener.onUploadError(error.getDescription());
                        }
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Called when the upload request is rescheduled
                    }
                })
                .dispatch();
    }

}
