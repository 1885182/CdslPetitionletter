package com.sszt.cdslpetitionletter.cases

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.download.downloadDialogEx
import com.sszt.basis.ext.util.loge
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.network.UploadFileRequest
import com.sszt.basis.util.GetFilePathFromUri
import com.sszt.basis.util.PickerUtils
import com.sszt.basis.util.PublicValueModel
import com.sszt.basis.util.Utils
import com.sszt.basis.util.chat.FileUtils
import com.sszt.basis.weight.DataDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.CaseAgentAdapter
import com.sszt.cdslpetitionletter.adapter.CaseInsertFileAdapter
import com.sszt.cdslpetitionletter.bean.ByProposerBean
import com.sszt.cdslpetitionletter.bean.CaseAgentBean
import com.sszt.cdslpetitionletter.bean.ProposerBean
import com.sszt.cdslpetitionletter.databinding.ActivityCaseInsertProposerBinding
import com.sszt.resources.IRoute
import java.io.File
import java.io.Serializable

/**
 * 添加申请人/被申请人
 */

@Route(path = IRoute.case_insert_proposer)
class CaseInsertProposerActivity :
    BaseActivity<PublicViewModel, ActivityCaseInsertProposerBinding>() {

    private var type: Int = 0
    private var index: Int = -1

    private val listProposerType = ArrayList<String>()
    private val listProposerCall = ArrayList<String>()
    private val listProposerAgentType = ArrayList<String>()
    private val listProposerEntrustType = ArrayList<String>()
    private val moreLinkageBean = ArrayList<PickerUtils.MoreLinkageBean>()


    private val fileRequest by lazy { UploadFileRequest() }

    private val mAdp by lazy { CaseAgentAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.activity_case_insert_proposer

    fun updatas(position: Int, type: Int, value: String) {
        when (type) {
            1 -> mAdp.data[position].agentName = value
            2 -> mAdp.data[position].agentPhone = value
            3 -> mAdp.data[position].agentIdentity = value
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {

        bind.addProposerTl.setOnBackClick { finish() }
        type = intent.getIntExtra("type", 0)

        if (type == 0) {
            bind.addProposerTl.titleTitle.text = "添加申请人"
            bind.addProposerTV.text = "申请方信息"
            bind.addProposerIcon.text = "申"
            bind.addProposerIcon.background =
                getDrawable(com.sszt.moduleresources.R.drawable.r15_main_shape)
            bind.addProposerNameTip.text = "申请人"
        } else if (type == 1) {
            bind.addProposerTV.text = "被申请方信息"
            bind.addProposerTl.titleTitle.text = "添加被申请人"
            bind.addProposerIcon.text = "被"
            bind.addProposerIcon.background =
                getDrawable(com.sszt.moduleresources.R.drawable.r15_e9b04f_shape)
            bind.addProposerNameTip.text = "被申请人"
        }

        bind.recyclerAgent.init(LinearLayoutManager(this), mAdp)

        initData()

        //添加代理人
        bind.addProposerAgent.setOnClickListener {
            mAdp.data.add(CaseAgentBean(type + 1, "", "", "", "", "", "", "", ""))
            mAdp.notifyDataSetChanged()
        }

        mAdp.addChildClickViewIds(
            R.id.addProposerAgentDel,
            R.id.addProposerAgentRelation,
            R.id.addProposerAgentType,
            R.id.addProposerEntrustType,
            R.id.addProposerAgentEntrust,
            R.id.addProposerAgentEntrustUp
        )
        mAdp.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.addProposerAgentDel -> {//删除代理人
                    mAdp.data.removeAt(position)
                    mAdp.notifyDataSetChanged()
                }
                R.id.addProposerAgentRelation -> {//与当事人关系
                    PickerUtils.showMoreLinkage(
                        this,
                        moreLinkageBean
                    ) {
                        mAdp.data[position].agentRelation = "[\""+it[0] + "\",\"" + it[1]+"\"]"
                        mAdp.notifyItemChanged(position)
                    }
                }
                R.id.addProposerAgentType -> {//选择代理人类型
                    DataDialog(this, listProposerAgentType) {
                        if (mAdp.data[position].agentType != listProposerAgentType[it]) {
                            mAdp.data[position].agentEntrust = ""
                        }
                        mAdp.data[position].agentType = listProposerAgentType[it]
                        mAdp.notifyItemChanged(position)
                    }.show(supportFragmentManager, "dialogData")
                }
                R.id.addProposerEntrustType -> {//选择委托类型
                    if (mAdp.data[position].agentType == "") {
                        toast("请先选择代理人类型")
                        return@setOnItemChildClickListener
                    }
                    listProposerEntrustType.clear()
                    if (mAdp.data[position].agentType == "委托代理人") {
                        listProposerEntrustType.add("一般代理人")
                        listProposerEntrustType.add("特被授权代理人")
                    } else if (mAdp.data[position].agentType == "法定/指定代理人") {
                        listProposerEntrustType.add("未成年人")
                        listProposerEntrustType.add("无民事行为能力")
                    }
                    DataDialog(this, listProposerEntrustType) {
                        mAdp.data[position].agentEntrust = listProposerEntrustType[it]
                        mAdp.notifyItemChanged(position)
                    }.show(supportFragmentManager, "dialogData")
                }
                R.id.addProposerAgentEntrust -> {//下载委托书模板
                    downloadDialogEx(
                        url = "http://192.168.100.244:9000/cdslqxf/4172E0C7C70941768B36086990A0B3B1.docx" ?: "",
                        saveName = "社会治理公众号上线配置.docx",
                        isOpen = false
                    ) { s, p ->
                        "$s >>进度>> $p".loge()
                    }
                }
                R.id.addProposerAgentEntrustUp -> {//上传委托书
                    index = position
                    FileUtils.checkFile(this)
                }
            }
        }

        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功") {
                    mAdp.data[index].agentAuthorization = it.uuid
                    mAdp.data[index].sss = it.title
                    mAdp.notifyItemChanged(index)
                }
            }, {
                toast(it.errorMsg)
            })
        }

        //选择类型
        bind.addProposerType.setOnClickListener {
            DataDialog(this, listProposerType) {
                bind.addProposerType.text = listProposerType[it]
                if (listProposerType[it] == "自然人") {
                    bind.addProposerCL.visibility = View.GONE
                } else {
                    bind.addProposerCL.visibility = View.VISIBLE
                }
            }.show(supportFragmentManager, "dialogData")
        }

        //选择其他联系方式
        bind.addProposerCall.setOnClickListener {
            DataDialog(this, listProposerCall) {
                bind.addProposerCall.text = listProposerCall[it]

                bind.addProposerCallValueTip.visibility = View.VISIBLE
                bind.addProposerCallValue.visibility = View.VISIBLE
                bind.addProposerCallValueLine.visibility = View.VISIBLE

                bind.addProposerCallValueTip.text = listProposerCall[it] + ":"
                bind.addProposerCallValue.hint = "请输入" + listProposerCall[it] + "号:"
            }.show(supportFragmentManager, "dialogData")
        }


        //保存
        bind.saveProposer.setOnClickListener {
            when {
                bind.addProposerType.isTrimEmpty() -> toast(bind.addProposerType.getHintStr())
                bind.addProposerName.isTrimEmpty() -> toast(bind.addProposerName.getHintStr())
                !Utils.isTelOrPhone(bind.addProposerPhone.textString()) -> toast("联系电话格式错误")
                !Utils.isIDCard(bind.addProposerIDCard.textString()) -> toast("身份证号格式错误")
                bind.addProposerAddress.isTrimEmpty() -> toast(bind.addProposerAddress.getHintStr())

                mAdp.data.size > 0 -> {
                    for (i in 0 until mAdp.data.size) {
                        if (mAdp.data[i].agentType.isNullOrEmpty()) {
                            toast("代理人类型不能为空")
                            return@setOnClickListener
                        }
                        if (mAdp.data[i].agentEntrust.isNullOrEmpty()) {
                            toast("委托类型不能为空")
                            return@setOnClickListener
                        }
                        if (mAdp.data[i].agentName.isNullOrEmpty()) {
                            toast("代理人不能为空")
                            return@setOnClickListener
                        }
                        if (!Utils.isTelOrPhone(mAdp.data[i].agentPhone)) {
                            toast("联系电话格式错误")
                            return@setOnClickListener
                        }
                        if (mAdp.data[i].agentRelation.isNullOrEmpty()) {
                            toast("与当事人关系不能为空")
                            return@setOnClickListener
                        }
                    }
                    save()
                }
                else -> save()
            }
        }

    }

    private fun save() {
        var ban = Bundle()
        ban.putSerializable("list", mAdp.data as Serializable)
        if (type == 0) {
            setResult(
                2000, Intent().putExtra(
                    "bean", ProposerBean(
                        bind.addProposerType.text.toString(),
                        bind.addProposerName.text.toString(),
                        bind.addProposerSocial.text.toString(),
                        bind.addProposerLegalPerson.text.toString(),
                        bind.addProposerPhone.text.toString(),
                        bind.addProposerCall.text.toString(),
                        bind.addProposerCallValue.text.toString(),
                        bind.addProposerIDCard.text.toString(),
                        bind.addProposerAddress.text.toString(),
                    )
                ).putExtras(ban)
            )
        } else if (type == 1) {
            setResult(
                2000, Intent().putExtra(
                    "bean", ByProposerBean(
                        bind.addProposerType.text.toString(),
                        bind.addProposerName.text.toString(),
                        bind.addProposerSocial.text.toString(),
                        bind.addProposerLegalPerson.text.toString(),
                        bind.addProposerPhone.text.toString(),
                        bind.addProposerCall.text.toString(),
                        bind.addProposerCallValue.text.toString(),
                        bind.addProposerIDCard.text.toString(),
                        bind.addProposerAddress.text.toString(),
                    )
                ).putExtras(ban)
            )
        }

        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FileUtils.CHECK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            val path = GetFilePathFromUri.getFileAbsolutePath(this, uri)

            val file = File(path)

            if (file.length() > 1024 * 1024 * 40) {
                toast("文件过大,暂不支持")
                return
            }
            tipDialog("上传中")
            fileRequest.uploadFile(requestFileAll(file))
        }
    }

    private fun initData() {
        listProposerType.add("自然人")
        listProposerType.add("法人")
        listProposerType.add("非法人组织")

        listProposerAgentType.add("委托代理人")
        listProposerAgentType.add("法定/指定代理人")

        listProposerCall.add("邮箱")
        listProposerCall.add("QQ")
        listProposerCall.add("微信")

        moreLinkageBean.add(
            PickerUtils.MoreLinkageBean(
                "近亲属", arrayListOf(
                    PickerUtils.MoreLinkageBean("配偶", null),
                    PickerUtils.MoreLinkageBean("父亲", null),
                    PickerUtils.MoreLinkageBean("母亲", null),
                    PickerUtils.MoreLinkageBean("儿子", null),
                    PickerUtils.MoreLinkageBean("女儿", null),
                    PickerUtils.MoreLinkageBean("弟弟", null),
                    PickerUtils.MoreLinkageBean("姐姐", null),
                    PickerUtils.MoreLinkageBean("祖父", null),
                    PickerUtils.MoreLinkageBean("祖母", null),
                    PickerUtils.MoreLinkageBean("外祖父", null),
                    PickerUtils.MoreLinkageBean("外祖母", null),
                )
            )
        )

        moreLinkageBean.add(
            PickerUtils.MoreLinkageBean(
                "律师", arrayListOf(
                    PickerUtils.MoreLinkageBean("律师", null)
                )
            )
        )
        moreLinkageBean.add(
            PickerUtils.MoreLinkageBean(
                "推荐公民", arrayListOf(
                    PickerUtils.MoreLinkageBean("团体推荐", null),
                    PickerUtils.MoreLinkageBean("单位推荐", null),
                    PickerUtils.MoreLinkageBean("社区推荐", null)
                )
            )
        )
        moreLinkageBean.add(
            PickerUtils.MoreLinkageBean(
                "其他", arrayListOf(
                    PickerUtils.MoreLinkageBean("基层法律服务工作者", null),
                    PickerUtils.MoreLinkageBean("公司员工", null)
                )
            )
        )
    }
}