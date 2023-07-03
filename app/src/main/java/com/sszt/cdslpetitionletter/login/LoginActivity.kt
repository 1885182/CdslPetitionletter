package com.sszt.cdslpetitionletter.login

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.StatusBar
import com.sszt.basis.util.Utils
import com.sszt.cdslpetitionletter.BuildConfig
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLoginBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 登录
 */
@Route(path = IRoute.login_login)
class LoginActivity : BaseActivity<PublicViewModel, ActivityLoginBinding>() {


    override fun layoutId() = R.layout.activity_login

    private val request by lazy { CaseRequestViewModel() }
    private val mainRequest by lazy { MainRequestViewModel() }
    private var downTimer: CountDownTimer? = null

    override fun initView(savedInstanceState: Bundle?) {
        StatusBar.fullScreen(this)

        if (BuildConfig.FLAVOR == "chanpin"){
            bind.switchLogin.visibility = View.GONE
            bind.forgetPas.visibility = View.GONE
        }

        SPUtils.getInstance().put("token", "")
        bind.login.setOnClickListener {
            if (bind.switchLogin.textString() == "账号登录") {
                when {
                    !Utils.isPhone(bind.userName.textString()) -> toast("手机号码格式错误")
                    bind.verificationCode.isTrimEmpty() -> toast("验证码不能为空")
                    else -> {
                        tipDialog("登录中")
                        mainRequest.loginCode(
                            requestForBody(
                                "mobile" to bind.userName.textString(),
                                "msgCode" to bind.verificationCode.textString(),
                            )
                        )
                    }
                }
            } else if (bind.switchLogin.textString() == "验证码登录") {
                when {
                    bind.userName.isTrimEmpty() -> toast("账号不能为空")
                    bind.passWord.isTrimEmpty() -> toast("密码不能为空")
                    else -> {
                        tipDialog("登录中")
                        request.login(
                            requestForParameter(
                                "username" to bind.userName.textString(),
                                "password" to bind.passWord.textString(),
                            )
                        )
                    }
                }
            }


        }

        bind.switchLogin.setOnClickListener {
            if (bind.switchLogin.textString() == "验证码登录") {
                bind.passWord.visibility = View.GONE
                bind.getVerificationCode.visibility = View.VISIBLE
                bind.verificationCode.visibility = View.VISIBLE
                bind.switchLogin.text = "账号登录"
                bind.userName.hint = "手机号"
                return@setOnClickListener
            } else if (bind.switchLogin.textString() == "账号登录") {
                bind.passWord.visibility = View.VISIBLE
                bind.getVerificationCode.visibility = View.GONE
                bind.verificationCode.visibility = View.GONE
                bind.switchLogin.text = "验证码登录"
                bind.userName.hint = "账号"
                return@setOnClickListener
            }
        }

        request.loginResul.observe(this) {
            if (it.code == 2000) {
                successDialog("登录成功") {
                    router(IRoute.main_main)
                    finish()
                }
            } else {
                errorDialog(it.msg ?: "登陆失败")
            }
        }

        //忘记密码
        bind.forgetPas.setOnClickListener {
            router(IRoute.login_phone)
        }

        //获取验证码
        bind.getVerificationCode.setOnClickListener {
            if (!Utils.isPhone(bind.userName.textString())) {
                toast("手机号码格式错误")
                return@setOnClickListener
            }
            downTimer = object : CountDownTimer(60 * 1000 - 1, 1000) {
                override fun onFinish() {
                    bind.getVerificationCode.text = "重新获取"
                    bind.getVerificationCode.setTextColor(Color.parseColor("#ffffff"))
                    bind.getVerificationCode.isClickable = true
                    bind.getVerificationCode.background =
                        getDrawable(com.sszt.moduleresources.R.drawable.r8_main_shape)
                    bind.getVerificationCode.isEnabled = true
                    cancel()
                }

                override fun onTick(millisUntilFinished: Long) {
                    var s = millisUntilFinished / 1000
                    bind.getVerificationCode.isClickable = false
                    bind.getVerificationCode.isEnabled = false
                    bind.getVerificationCode.text = "${s}s"
                    bind.getVerificationCode.background =
                        getDrawable(com.sszt.moduleresources.R.drawable.r8_eee_shape)
                    bind.getVerificationCode.setTextColor(Color.parseColor("#333333"))

                }
            }.start()
            tipDialog()
            mainRequest.getVerificationCode(requestForBody("phoneNum" to bind.userName.textString()))

        }

        mainRequest.getVerificationCodeResult.observe(this) {
            if (it.code == 2000) {
                successDialog("验证码获取成功") {
                }
            } else {
                errorDialog(it.msg)
            }
        }

        mainRequest.loginCodeResult.observe(this){
            if (it.code == 2000) {
                successDialog("登录成功") {
                    router(IRoute.main_main)
                    finish()
                }
            } else {
                errorDialog(it.msg ?: "登陆失败")
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (downTimer != null) {
            downTimer?.cancel()
        }

    }
}