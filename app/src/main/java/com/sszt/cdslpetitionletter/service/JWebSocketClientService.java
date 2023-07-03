package com.sszt.cdslpetitionletter.service;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sszt.basis.network.Url;
import com.sszt.cdslpetitionletter.R;
import com.sszt.cdslpetitionletter.activity.MainActivity;
import com.sszt.cdslpetitionletter.app.MyApplication;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class JWebSocketClientService extends Service {
    public JWebSocketClient client;
    private final JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1002;
    private Ringtone mRingtone;


    //灰色保活
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    PowerManager.WakeLock wakeLock;//锁屏唤醒

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    //用于Activity和service通讯
    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("JWebSocketClientService", "onTaskRemoved");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("JWebSocketClientService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //初始化websocket
        initSocketClient();
        Log.e("JWebSocketClientService", "onStartCommand" + client);
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测

        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT > 18 && Build.VERSION.SDK_INT < 25) {
            //Android4.3 - Android7.0，隐藏Notification上的图标
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("serviceshiyu",
                    "一键指挥通知", NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 正式创建
            service.createNotificationChannel(channel);

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent intent1 = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "serviceshiyu");
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(getString(R.string.app_name))
                    .setPriority(999)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    //.setContentIntent(intent1)
                    .build();

            // 开启前台进程 , API 26 以上无法关闭通知栏
            startForeground(GRAY_SERVICE_ID, notification);

        }

        acquireWakeLock();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.e("JWebSocketClientService", "onDestroy");
        closeConnect();
        super.onDestroy();
    }

    public JWebSocketClientService() {
    }


    public String main(String str) {
        //默认环境，已是UTF-8编码
         try {
             return URLEncoder.encode(str, "utf-8");

         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
             return "";
         }
    }


    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {





        //Log.e("JWebSocketClientService", "开启链接---"+str1);
        URI uri = URI.create(Url.ws);
        if (client == null) {

            client = new JWebSocketClient(uri) {
                @Override
                public void onMessage(String message) {
                    Log.e("JWebSocketClientService", "收到的消息：" + message + "=======");
                    checkLockAndShowNotification(message);
                }

                @Override
                public void onOpen(ServerHandshake data) {
                    super.onOpen(data);
                    Log.e("JWebSocketClientService", "websocket连接成功"+data.getHttpStatusMessage());

                }
            };
        }
        if (!client.isOpen()) {
            try {
                initSSL();
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    ///wss添加证书
    private void initSSL() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory factory = sslContext.getSocketFactory();
        try {
            client.setSocket(factory.createSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 连接webSocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送消息
     *
     * @param msg 消息
     */
    public void sendMsg(String msg) {
        if (null != client) {
            client.send(msg);
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }


//    -----------------------------------消息通知--------------------------------------------------------

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param content 内容
     */
    private void checkLockAndShowNotification(String content) {
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//锁屏
            //获取电源管理器对象
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isInteractive()) {//isScreenOn()已过时
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , "bright");
                wl.acquire(10*60*1000L /*10 minutes*/);  //点亮屏幕
                wl.release();  //任务结束后释放
            }
            sendNotification(content);
        } else {
            sendNotification(content);
        }
    }

    public void stopRingtone() {
        try {
            if (mRingtone != null){
                mRingtone.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String content) {
        Gson gson = new Gson();
        /*try {
            WebSocketBean bean = gson.fromJson(content, WebSocketBean.class);
            if (bean != null){
                String channel = createNotificationChannel();
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent = new Intent(this, OneKeyCommandActivity.class);



                notificationIntent.putExtra("isNotify", true);
                notificationIntent.putExtra("roomId", bean.getMessage().getRoomId());
                PendingIntent intent = PendingIntent.getActivity(this, 0,
                        notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
                vibrator.vibrate(3000);
                //Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.bell);
                mRingtone = RingtoneManager.getRingtone(MyApplication.Companion.getMContext(), uri);

                mRingtone.play();



                mBuilder.setContentText("会议号:"+bean.getMessage().getRoomId())
                        .setContentTitle(bean.getMessage().getUserName()+"发起的一键指挥")
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(intent);//点击事件
                Notification notification = mBuilder.build();
                notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.bell);
                notificationManager.notify(0, notification);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }*/


    }


    private String createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("sszt_shiyu_app", "一键指挥", NotificationManager.IMPORTANCE_HIGH);
            //channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bell), Notification.AUDIO_ATTRIBUTES_DEFAULT);
            manager.createNotificationChannel(channel);
            return "sszt_shiyu_app";
        } else {
            return null;
        }
    }


    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (SPUtils.getInstance().getString("token").isEmpty()){
                if (client != null) {
                    if (!client.isClosed()) {
                        client.close();
                    }
                }
            }
            //client.send("");
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                initSocketClient();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
