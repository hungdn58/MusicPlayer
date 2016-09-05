package com.example.hoang.mymediaplayer.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.hoang.mymediaplayer.R;

/**
 * Created by hoang on 2/23/2016.
 */
public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.example.hoang.mymediaplayer.action.main";
        public static String INIT_ACTION = "com.example.hoang.mymediaplayer.action.init";
        public static String PREV_ACTION = "com.example.hoang.mymediaplayer.action.prev";
        public static String PLAY_ACTION = "com.example.hoang.mymediaplayer.action.play";
        public static String NEXT_ACTION = "com.example.hoang.mymediaplayer.action.next";
        public static String STARTFOREGROUND_ACTION = "com.example.hoang.mymediaplayer.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.hoang.mymediaplayer.action.stopforeground";
    }


    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.san_khau2, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}
