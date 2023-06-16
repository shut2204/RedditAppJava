package com.example.redditapp.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Утилитарный класс с различными утилитами.
 */
public class Utils {

    /**
     * Проверяет, является ли заданная строка допустимым URL-адресом.
     *
     * @param url Строка, предполагаемая URL-адрес.
     * @return true, если строка является допустимым URL-адресом, false в противном случае.
     */
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Возвращает строку, представляющую временной промежуток между заданным временем и текущим временем.
     * Преобразует разницу в человекочитаемый формат времени, такой как "только что", "N минут назад", "N часов назад", "N дней назад".
     *
     * @param time Время, для которого требуется вычислить разницу с текущим временем.
     * @return Строка, представляющая временной промежуток между заданным временем и текущим временем.
     */
    public static String getTimeAgo(long time) {
        long diff = new Date().getTime() - time;

        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return "just now";
        } else if (diff < 60L * TimeUnit.MINUTES.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes ago";
        } else if (diff < 24L * TimeUnit.HOURS.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toHours(diff) + " hours ago";
        } else {
            return TimeUnit.MILLISECONDS.toDays(diff) + " days ago";
        }
    }
}
