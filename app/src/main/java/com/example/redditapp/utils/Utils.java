package com.example.redditapp.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

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
