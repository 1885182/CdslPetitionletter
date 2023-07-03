package com.sszt.cdslpetitionletter.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.resources.IRoute
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.permissionx.guolindev.PermissionX
import com.sszt.basis.ext.loadListData
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.setColor
import com.sszt.basis.ext.toast
import com.sszt.basis.util.StatusBar
import com.sszt.basis.util.StatusBarUtil
import com.sszt.basis.util.chat.FileUtils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityMainBinding
import com.sszt.cdslpetitionletter.fragment.MessageFragment
import com.sszt.cdslpetitionletter.fragment.MineFragment
import com.sszt.cdslpetitionletter.fragment.WorkbenchFragment
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.Base64.getDecoder


@Route(path = IRoute.main_main)
class MainActivity : BaseActivity<PublicViewModel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main


    private var index = 0

    private val mFragments = ArrayList<Fragment>()

    private var mCurrentFragment: Fragment? = null

    private var mFragmentManager: FragmentManager? = null
    private val request by lazy { MainRequestViewModel() }

    private val workbenchFragment by lazy {
        ARouter.getInstance().build(IRoute.workbench_fragment).navigation() as WorkbenchFragment
    }

    private val msgFragment by lazy {
        ARouter.getInstance().build(IRoute.message_fragment).navigation() as MessageFragment
    }

    private val mineFragment by lazy {
        ARouter.getInstance().build(IRoute.mine_fragment).navigation() as MineFragment
    }

    override fun initView(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        StatusBar.fullScreen(this)

        SPUtils.getInstance().put("ceshiyemian", false)


        mFragmentManager = supportFragmentManager

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)

        //初始化页面
        mFragments.add(workbenchFragment)
        mFragments.add(msgFragment)
        mFragments.add(mineFragment)

        switchFragment(savedInstanceState?.getInt("index") ?: 0)


        //页面切换
        initClick()


        //权限申请
        requestPermission()

        request.getMsgList(
            requestForBody(
                "pageNum" to 1,
                "pageSize" to 10,
            ), false,
            1
        )

        request.getMsgListResult.observe(this) {
            var num = 0
            for (i in 0 until it.listData.size) {
                if (it.listData[i].messageRead == "0") {
                    num += 1
                }
            }
            bind.isReadNum.text = num.toString()
            if (num == 0) {
                bind.isReadNum.visibility = View.GONE
            } else {
                bind.isReadNum.visibility = View.VISIBLE
            }
        }

    }

    @Subscribe
    fun updateEvent(str: String) {
        if (str == "未读消息变更") {

            request.getMsgList(
                requestForBody(
                    "pageNum" to 1,
                    "pageSize" to 10,
                ), false,
                1
            )

        }
    }

    private fun initClick() {
        bind.mainHomeLL.setOnClickListener {
            if (index == 0) return@setOnClickListener
            index = 0
            switchFragment(index)

        }
        bind.mainMsgLL.setOnClickListener {
            if (index == 1) return@setOnClickListener
            index = 1
            switchFragment(index)
        }

        bind.mainMineLL.setOnClickListener {
            if (index == 2) return@setOnClickListener
            index = 2
            switchFragment(index)
        }
    }


    /**
     * 请求权限
     */
    private fun requestPermission() {
        val permission10 = arrayListOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )


        PermissionX.init(this)
            .permissions(permission10)
            .request { _, _, _ ->

            }

    }


    private fun switchFragment(index: Int) {

        initBottomTab()
        var to = mFragmentManager?.findFragmentByTag(index.toString() + "")
        if (to == null) {
            to = when (index) {
                0 -> workbenchFragment
                1 -> msgFragment
                2 -> mineFragment
                else -> workbenchFragment
            }
        }

        if (to === mCurrentFragment && mCurrentFragment is Fragment) {
            (mCurrentFragment as Fragment)
        } else if (to.isAdded) {
            mCurrentFragment?.let {
                mFragmentManager?.beginTransaction()?.hide(it)?.show(to)?.commit()
            }
        } else {
            to.userVisibleHint = true
            if (mCurrentFragment != null) {
                mFragmentManager?.beginTransaction()?.hide(mCurrentFragment!!)
                    ?.add(R.id.mainFl, to, index.toString() + "")?.commit()
            } else {
                mFragmentManager?.beginTransaction()
                    ?.add(R.id.mainFl, to, index.toString() + "")?.commit()
            }
        }
        mCurrentFragment = to
        when (index) {
            0 -> {
                bind.mainHomeTV.setColor(com.sszt.moduleresources.R.color.color_main)
                bind.mainHomeIV.setImageResource(com.sszt.moduleresources.R.mipmap.main_home_select)
            }
            1 -> {
                bind.mainMsgTV.setColor(com.sszt.moduleresources.R.color.color_main)
                bind.mainMsgIV.setImageResource(com.sszt.moduleresources.R.mipmap.main_sms_select)
            }
            2 -> {
                bind.mainMineTV.setColor(com.sszt.moduleresources.R.color.color_main)
                bind.mainMineIV.setImageResource(com.sszt.moduleresources.R.mipmap.main_mine_select)
            }
        }

    }

    private fun initBottomTab() {
        bind.mainHomeTV.setColor(com.sszt.moduleresources.R.color.color_666666)
        bind.mainMsgTV.setColor(com.sszt.moduleresources.R.color.color_666666)
        bind.mainMineTV.setColor(com.sszt.moduleresources.R.color.color_666666)

        bind.mainHomeIV.setImageResource(com.sszt.moduleresources.R.mipmap.main_home_unselect)
        bind.mainMsgIV.setImageResource(com.sszt.moduleresources.R.mipmap.main_sms_unselect)
        bind.mainMineIV.setImageResource(com.sszt.moduleresources.R.mipmap.main_mine_unselect)

    }

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if (System.currentTimeMillis() - mExitTime > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                toast("再按一次退出应用")
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis()
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                moveTaskToBack(true)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}