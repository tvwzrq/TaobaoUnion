package com.example.taobaounion.ui.ticket

import android.content.ClipData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.taobaounion.databinding.FragmentTicketBinding
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import com.example.taobaounion.uitl.API_RESPONSE_SUCCESS
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.uitl.TicketUtil
import com.example.taobaounion.viewmodel.ticket.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.content.ClipboardManager as ClipboardManager1

@AndroidEntryPoint
class TicketFragment:Fragment() {
    private lateinit var binding:FragmentTicketBinding
    private val viewModel by viewModels<TicketViewModel>()
    private var isInstallTaoBao=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= FragmentTicketBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        arguments?.let {
            binding.apply {
                loadingView.visibility=View.VISIBLE
                errorMsg.visibility=View.GONE
            }
            //获取参数
            initUi(it.getString("url"),it.getString("title"),it.getString("cover"))
        }

        //检查是否安装淘宝
        // act=android.intent.action.MAIN
        // cat=[android.intent.category.LAUNCHER
        //  flg=0x10200000 hwFlg=0
        //cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
        //包名com.taobao.taobao
        val manager=context?.packageManager
        isInstallTaoBao = try {
            val packageInfo=manager?.getPackageInfo("com.taobao.taobao",PackageManager.MATCH_UNINSTALLED_PACKAGES)
            packageInfo!=null
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
        LogUtil.d("PACAKAGE",isInstallTaoBao.toString())
        binding.lingJuan.text=if (isInstallTaoBao)"去淘宝领卷" else "复制口令"
        return binding.root
    }

    private fun initUi(url:String?,title:String?,imageUrl:String?){
        LogUtil.d("ARGUMENT","url->$url")
        url?.let {
            viewModel.getTicket(url, title).observe(viewLifecycleOwner){response->
                when(response.code){
                    API_RESPONSE_SUCCESS->{
                        LogUtil.d("RESPONSE",response.toString())
                        val model=response.data?.tbk_tpwd_create_response?.data?.model
                        model?.let {
                            val text=TicketUtil.getTicket(model)
                            binding.ticket.setText(text)
                            LogUtil.d("TICKET",text)
                        }
                        binding.apply {
                            loadingView.visibility=View.GONE
                            errorMsg.visibility=View.GONE
                        }

                    }
                    API_RESPONSE_NO_NET->{
                        binding.apply {
                            loadingView.visibility=View.GONE
                            errorMsg.visibility=View.VISIBLE
                        }
                    }
                }
            }
        }
        Glide.with(this).load(imageUrl).into(binding.ticketImage)
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.lingJuan.setOnClickListener {
            //复制到剪贴板
            val ticketCode=binding.ticket.text.trim()
            val clipboardManager= context?.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clipData=ClipData.newPlainText("tao_bao_ticket_code",ticketCode)
            clipboardManager.setPrimaryClip(clipData)
            if (isInstallTaoBao&&ticketCode.isNotEmpty()){
                //安装淘宝则跳转
                val intent=Intent()
               // intent.action="com.taobao.taobao"
               // intent.addCategory("android.intent.category.LAUNCHER")
                val component=ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity")
                intent.component = component
                // 跳转后要等一会儿,因该是淘宝的原因
                startActivity(intent)
            }else{
                Toast.makeText(this.context, "复制成功", Toast.LENGTH_SHORT).show()
            }



        }
    }

}