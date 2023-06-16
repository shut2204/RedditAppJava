package com.example.redditapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Утилитарный класс для работы с изображениями.
 * Предоставляет методы для сохранения изображений в галерею и запроса разрешений на запись во внешнее хранилище.
 */
public class ImageUtils {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private final Activity activity;

    /**
     * Конструктор класса ImageUtils.
     *
     * @param activity Активность, в которой используется утилита.
     */
    public ImageUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * Запрашивает разрешение на запись в хранилище.
     *
     * @param imageUrl URL-адрес изображения.
     */
    public void requestPermission(String imageUrl) {
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        } else {
            saveImageToGallery(imageUrl);
        }
    }

    /**
     * Сохраняет изображение в галерею.
     *
     * @param imageUrl URL-адрес изображения.
     */
    public void saveImageToGallery(String imageUrl) {
        // Используем библиотеку Glide для загрузки изображения по URL-адресу
        Glide.with(activity)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveBitmapToGallery(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Метод вызывается, когда изображение очищено из памяти
                    }
                });
    }

    /**
     * Сохраняет битмап в галерею.
     *
     * @param bitmap Битмап для сохранения.
     */
    private void saveBitmapToGallery(Bitmap bitmap) {
        String savedImagePath;

        // Генерируем имя файла с помощью текущей даты и времени
        String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg";

        // Определяем путь к папке Pictures/RedditApp на внешнем хранилище
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/RedditApp");

        boolean success = true;
        if (!storageDir.exists()) {
            // Если папка не существует, создаем ее
            success = storageDir.mkdirs();
        }

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                // Создаем поток вывода для записи битмапа в файл
                OutputStream fOut = Files.newOutputStream(imageFile.toPath());
                // Сжимаем битмап в формате JPEG с качеством 100 и записываем в поток вывода
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Добавление изображения в галерею системы
            galleryAddPic(savedImagePath);
            Toast.makeText(activity, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Добавляет изображение в системную галерею.
     *
     * @param imagePath Путь к изображению.
     */
    private void galleryAddPic(String imagePath) {
        // Создаем интент для сканирования файла и добавления его в галерею
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        // Отправляем широковещательное сообщение с интентом
        activity.sendBroadcast(mediaScanIntent);
    }

}
