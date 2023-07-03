package com.sszt.cdslpetitionletter.app

import android.content.Context
import android.text.TextUtils
import android.util.Log
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.interfaces.BaseDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sszt.basis.base.BaseApp
import com.sszt.basis.map.util.BaiduMapUtil
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.activity.MainActivity
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * <b>标题：</b>  初始化必要操作 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2021/11/12 11:07<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */
class MyApplication : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        if (true) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }

        mContext = this
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        initDialog(applicationContext)


        initCrash()


        CrashReport.initCrashReport (applicationContext, "6ea3bee684", false)

        initTBS(applicationContext)

        SDKInitializer.setAgreePrivacy(this,true)
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        BaiduMapUtil.initLocationClient(this)
    }

    private fun initDialog(context: Context) {
        DialogX.init(context)
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW
        DialogX.useHaptic = false
        DialogX.autoShowInputKeyboard = false

        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.onlyOnePopTip = false
        DialogX.dialogLifeCycleListener = object : DialogLifecycleCallback<BaseDialog>() {
            override fun onShow(dialog: BaseDialog?) {
                super.onShow(dialog)
                dialog?.activity?.let {

                    //KeyboardUtils.hideSoftInput(it)

                }
            }

            override fun onDismiss(dialog: BaseDialog?) {
                super.onDismiss(dialog)
                dialog?.activity?.let {

                    //KeyboardUtils.hideSoftInput(it)

                }

            }
        }


        initRefresh()
    }



    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

    companion object {
        var mContext: Context? = null
    }

}

private fun initCrash() {
    //防止项目崩溃，崩溃后打开错误界面x
    CaocConfig.Builder.create()
        .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
        .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
        .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
        .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
        .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
        .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
        .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
        .restartActivity(MainActivity::class.java) // 重启的activity
        .errorActivity(MainActivity::class.java) //发生错误跳转的activity
        .eventListener(null) //允许你指定事件侦听器，以便在库显示错误活动 default: null
        .apply()
}


private fun initRefresh() {

    //设置全局的Header构建器
    SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
        layout.setPrimaryColorsId(
            R.color.white,
            R.color.black90
        ) //全局设置主题颜
        layout.setHeaderHeight(105f)
        layout.setHeaderMaxDragRate(1.5f)
//layout.setHeaderInsetStartPx()
//            layout.setHeaderInsetStart()
        ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
    }
    //设置全局的Footer构建器
    SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> //指定为经典Footer，默认是 BallPulseFooter
        ClassicsFooter(context).setDrawableSize(20f)
    }


}


private var isDownTbsSuccess = false //TBS X5插件是否下载成功

private var downTbsCount = 0 //尝试下载次数


/**
 * 腾讯TBS初始化流程
 */
fun initTBS(applicationContext: Context) {
    /********************************TBS服务器下发版本开始 */
    if (QbSdk.getTbsVersion(applicationContext) == 0) { //获取不到版本号，说明插件没有加载成功，重新跑流程
        //判断是否是x5内核未下载成功，存在缓存 重置化sdk，这样就清除缓存继续下载了
        QbSdk.reset(applicationContext)
        val map: HashMap<String,Any> = HashMap()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        /* 设置允许移动网络下进行内核下载。默认不下载，会导致部分一直用移动网络的用户无法使用x5内核 */
        QbSdk.setDownloadWithoutWifi(true)


        /* SDK内核初始化周期回调，包括 下载、安装、加载 */
        QbSdk.setTbsListener(object : TbsListener {
            /**
             * @param progress 110: 表示当前服务器认为该环境下不需要下载
             */
            override fun onDownloadFinish(progress: Int) {
                Log.i("TAG", "onDownloadFinished: $progress")
                //下载结束时的状态，下载成功时errorCode为100,其他均为失败，外部不需要关注具体的失败原因
                if (isDownTbsSuccess) {
                    return
                }
                if (progress < 100) {
                    return
                }
                if (progress != 100) { //回调里面还是没有成功，那就再次尝试下载
                    if (downTbsCount < 5) { //尝试下载10次，失败就开始从自己的服务器端下载
                        downTbs(applicationContext)
                    } else { //加载从自己的服务器端下载的x5内核
                        /*FileCopyAssetToSD.getInstance(getApplicationContext()).copyAssetsToSD("apk" , FileCopyAssetToSD.getDiskCacheDir(getApplicationContext()).toString()).setFileOperateCallback(new FileCopyAssetToSD.FileOperateCallback() {
                                @Override
                                public void onSuccess() {
                                    String filePath = FileCopyAssetToSD.getDiskCacheDir(getApplicationContext()).toString() + File.separator + "x5.apk";
                                    initLocalTbsCore(filePath);
                                    Toast.makeText(getApplicationContext() , "复制成功" , Toast.LENGTH_LONG).show();
                                    Log.i("TAG","复制成功:"+filePath);

                                }

                                @Override
                                public void onFailed(String error) {

                                }
                            });*/
                    }
                    downTbsCount++
                }
                if (progress == 100) {
                    isDownTbsSuccess = true
                }
            }

            /**initX5Environment
             * @param stateCode 200、232安装成功
             */
            override fun onInstallFinish(stateCode: Int) {
                Log.i("TAG", "onInstallFinished: $stateCode")
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            override fun onDownloadProgress(progress: Int) {
                Log.i("TAG", "Core Downloading: $progress")
            }
        })


        /* 此过程包括X5内核的下载、预初始化，接入方不需要接管处理x5的初始化流程，希望无感接入 */QbSdk.initX5Environment(
            applicationContext,
            object : PreInitCallback {
                override fun onCoreInitFinished() {
                    // 内核初始化完成，可能为系统内核，也可能为系统内核
                }

                /**
                 * 预初始化结束
                 * 由于X5内核体积较大，需要依赖wifi网络下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
                 * 内核下发请求发起有24小时间隔，卸载重装、调整系统时间24小时后都可重置
                 * @param isX5 是否使用X5内核
                 */
                override fun onViewInitFinished(isX5: Boolean) {
                    Log.i("TAG", "onViewInitFinished: $isX5")
                }
            })
    }
}


/**
 * 下载TBS插件 通过腾讯端下发
 */
fun downTbs(applicationContext: Context) {
    //判断是否是x5内核未下载成功，存在缓存 重置化sdk，这样就清除缓存继续下载了
    QbSdk.reset(applicationContext)
    //开始下载x5内核
    TbsDownloader.startDownload(applicationContext)
}

