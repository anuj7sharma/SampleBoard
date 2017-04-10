package com.sampleboard.utils;

/**
 * Created by Mobilyte India Pvt Ltd on 2/28/2017.
 */

public interface Constants {
    // Environment to use when creating an instance of Wallet.WalletOptions

    /*
    API BASE URL
     */
    String LIVE_APIURL ="http://starlord.hackerearth.com/";
    String DEF_PROFILE_URL = "https://organicthemes.com/demo/profile/files/2012/12/profile_img.png";

    String MUSIC_API = "edfora/cokestudio";

    //services action name
    String ACTION_PLAY = "com.action.PLAY";
    String BROADCAST_MUSIC_PLAY = "com.music.play";
    String BROADCAST_MUSIC_DOWNLOAD = "com.music.download";
    String BROADCAST_HANDLE_NEW_REQUEST = "com.music.manage.download";

    public interface MusicAction {
        String PLAY_PREVIOUS = "com.play.previous";
        String PLAY_NEXT = "com.play.next";
        String TOGGLE = "com.play.toggle";
        String PLAY_UPDATE_ACTION = "com.foregroundservice.action.play.update";   //update from ui to notification
    }

    public interface ACTION {
        public static String MAIN_ACTION = "com.foregroundservice.action.main";
        public static String INIT_ACTION = "com.foregroundservice.action.init";
        public static String PREV_ACTION = "com.foregroundservice.action.prev";
        public static String PLAY_ACTION = "com.foregroundservice.action.play";

        public static String NEXT_ACTION = "com.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "com.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.foregroundservice.action.stopforeground";
    }
    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
