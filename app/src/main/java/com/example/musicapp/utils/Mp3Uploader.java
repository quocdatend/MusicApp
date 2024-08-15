package com.example.musicapp.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class Mp3Uploader {

    public interface UploadCallbackListener {
        void onUploadSuccess(String url);
        void onUploadError(String error);
    }

    public void uploadMp3(Context context, Uri mp3Uri, UploadCallbackListener listener) {
        if (mp3Uri != null) {
            MediaManager.get().upload(mp3Uri)
                    .option("resource_type", "auto")  // Đặt loại tài nguyên thành auto để xử lý các loại tệp khác nhau
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Toast.makeText(context, "Đang tải lên MP3...", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // Theo dõi tiến trình tải lên
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String mp3Url = resultData.get("url").toString();
                            if (!mp3Url.startsWith("https://")) {
                                mp3Url = mp3Url.replace("http://", "https://");
                            }
                            if (listener != null) {
                                listener.onUploadSuccess(mp3Url);
                            }
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            if (listener != null) {
                                listener.onUploadError(error.getDescription());
                            }
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // Xử lý trường hợp lên lịch lại
                        }
                    }).dispatch();
        }
    }
}