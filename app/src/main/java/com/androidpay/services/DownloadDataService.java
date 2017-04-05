package com.androidpay.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.androidpay.R;
import com.androidpay.bean.MusicBean;
import com.androidpay.utils.Constants;
import com.androidpay.utils.Utils;
import com.androidpay.view.detail.DetailActivity;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.androidpay.utils.Constants.BROADCAST_HANDLE_NEW_REQUEST;

/**
 * Created by Anuj Sharma on 3/2/2017.
 */

public class DownloadDataService extends IntentService {
    public static final String ACTION_DOWNLOAD = "com.androidpay.download";
    public static final String ACTION_DOWNLOAD_SONG = "com.androidpay.download.song"; //for downloading songs
    private NotificationManager nm;
    private NotificationCompat.Builder notificationBuilder;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadDataService() {
        super("DOWNLOAD SERVICE");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        System.out.println("---------------------DownloadDataService.onHandleIntent");
        if (intent.getAction().equals(ACTION_DOWNLOAD_SONG)) {
            downloadUsingOKHTTP(intent.getExtras().getString("url").toString(),
                    intent.getExtras().getString("file_name").toString(),
                    intent.getExtras().getString("file_ext").toString());
//            downloadUsingURLConnection(intent.getExtras().getString("url").toString());
        }
        if (intent != null && intent.getExtras().getString("url") != null) {
//

        }
        Utils.d("working", "working handle intent");
    }

    private File saveToDirectory(String name, String fileExtension) {
//
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator +
                getString(R.string.app_name));
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + "." +  fileExtension);
        if (file.isFile()) file.delete();

        if (!file.isFile()) {
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method is to download songs
     * @param fileURL
     * @param fileName
     */
    private void downloadUsingOKHTTP(String fileURL, String fileName, String fileExtension) {
        File file = saveToDirectory(fileName, fileExtension);

        if (file == null) {
            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Utils.getInstance().showToast("Error while creating file");
                }
            });
            return;
        }

        try {
            URL url = new URL(fileURL);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            InputStream in = response.body().byteStream();
            OutputStream output = new FileOutputStream(file.getAbsolutePath());
            int count = 0;
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = in.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                System.out.println("-->>> okHTTP in while--> " + total);
                output.write(data, 0, count);// writing data to file
            }
            output.flush();// flushing output
            output.close();// closing streams
            in.close();
            response.body().close();
            // Call Broadcast receiver in Detail Activity
            Intent broadcasrIntent = new Intent(Constants.BROADCAST_MUSIC_DOWNLOAD);
            broadcasrIntent.putExtra("song_url",fileURL);
            sendBroadcast(broadcasrIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (intent.getAction().equals("com.androidpay.download") && intent.getExtras() != null) {
            downloadData(intent.getExtras().getString("url").toString());
        }*/

        return super.onStartCommand(intent, flags, startId);
//        return START_FLAG_REDELIVERY;
//        return Service.START_CONTINUATION_MASK ;

    }

    @Override
    public void onCreate() {
        System.out.println("----------------DownloadDataService.onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        System.out.println("--------------------------------------DownloadDataService.onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }
}
