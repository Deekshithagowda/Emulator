package com.sconti.studentcontinent.utils.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    @SuppressWarnings("all")
    public static String covertStringTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            TimeZone zone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(zone);

            Date pasTime = dateFormat.parse(dataDate);
            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " sec " + suffix;
            } else if (minute < 60) {
                convTime = minute + " min "+suffix;
            } else if (hour < 24) {
                convTime = hour + " hour "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " year " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " month " + suffix;
                } else {
                    convTime = (day / 7) + " week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            //Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }
    @SuppressWarnings("all")
    public static String timeAgo(Date currentDate, long pastDate) {
        long milliSecPerMinute = 60 * 1000; //Milliseconds Per Minute
        long milliSecPerHour = milliSecPerMinute * 60; //Milliseconds Per Hour
        long milliSecPerDay = milliSecPerHour * 24; //Milliseconds Per Day
        long milliSecPerMonth = milliSecPerDay * 30; //Milliseconds Per Month
        long milliSecPerYear = milliSecPerDay * 365; //Milliseconds Per Year

        long msExpired = currentDate.getTime() - pastDate;
        if (msExpired < milliSecPerMinute) {
            if (Math.round(msExpired / 10000) == 1) {
                return String.valueOf(Math.round(msExpired / 10000)) + " sec ago";
            } else {
                return String.valueOf(Math.round(msExpired / 10000) + " sec ago");
            }
        }

        else if (msExpired < milliSecPerHour) {
            if (Math.round(msExpired / milliSecPerMinute) == 1) {
                return String.valueOf(Math.round(msExpired / milliSecPerMinute)) + " min ago";
            } else {
                return String.valueOf(Math.round(msExpired / milliSecPerMinute)) + " min ago";
            }
        }
        else if (msExpired < milliSecPerDay) {
            if (Math.round(msExpired / milliSecPerHour) == 1) {
                return String.valueOf(Math.round(msExpired / milliSecPerHour)) + " hour ago";
            } else {
                return String.valueOf(Math.round(msExpired / milliSecPerHour)) + " hour ago";
            }
        }

        else if (msExpired < milliSecPerMonth) {
            if (Math.round(msExpired / milliSecPerDay) == 1) {
                return String.valueOf(Math.round(msExpired / milliSecPerDay)) + " day ago";
            } else {
                return String.valueOf(Math.round(msExpired / milliSecPerDay)) + " day ago";
            }
        }

        else if (msExpired < milliSecPerYear) {
            if (Math.round(msExpired / milliSecPerMonth) == 1) {
                return String.valueOf(Math.round(msExpired / milliSecPerMonth)) + "  month ago";
            } else {
                return String.valueOf(Math.round(msExpired / milliSecPerMonth)) + "  months ago";
            }
        }

        else {
            if (Math.round(msExpired / milliSecPerYear) == 1) {
                return String.valueOf(Math.round(msExpired / milliSecPerYear)) + " year ago";
            } else {
                return String.valueOf(Math.round(msExpired / milliSecPerYear)) + " years ago";
            }
        }
    }
}
