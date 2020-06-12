package com.rgs.friendlychat.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.rgs.friendlychat.R;
import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SCUtils {
  //Developed by https://github.com/AndreiD
  //returns a unique id for each device
  public static String getUniqueID(Context ctx) {
    return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  //use either formatted_date or time_ago (below)
  public static String formatted_date(long timestamp) {
    Calendar cal = Calendar.getInstance();
    TimeZone tz = cal.getTimeZone();//get your local time zone.
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mma"); //dd MMM yyyy KK:mma
    sdf.setTimeZone(tz);//set time zone.
    String localTime = sdf.format(new Date(timestamp * 1000));
    return localTime.toLowerCase();
  }

  public static String time_ago(long message_timestamp) {

    CharSequence xx = DateUtils.getRelativeTimeSpanString(message_timestamp * 1000L, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    return xx.toString();
  }

  //------- in case you want facebook login, simple way to show SHA key
  public static void show_facebook_keyhash(Context ctx) {
    try {
      PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
      for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.d("FB KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
    } catch (PackageManager.NameNotFoundException e) {
    } catch (NoSuchAlgorithmException e) {
    }
  }

  public static Snackbar showErrorSnackBar(Activity mContext, View rootView, String message) {
    if (message.equals("") || message == null) {
      message = "there was an error.";
    }
    Snackbar snack_error = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
    View view = snack_error.getView();
   // TextView tv = (TextView) view.findViewById(android.support.v4.R.);
    view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_200));
   // tv.setTextColor(Color.parseColor("#FFFFFF"));
    return snack_error;
  }
}
