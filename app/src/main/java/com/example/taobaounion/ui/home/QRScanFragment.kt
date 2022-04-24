package com.example.taobaounion.ui.home

import com.example.taobaounion.uitl.LogUtil
import com.google.zxing.Result
import com.king.zxing.CaptureFragment
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer

class QRScanFragment:CaptureFragment() {
    override fun onScanResultCallback(result: Result?): Boolean {
        LogUtil.d("RESULE",result.toString())
        return true
    }

    override fun initCameraScan() {
        super.initCameraScan()
        val decodeConfig=DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
            .setFullAreaScan(false)//设置是否全区域识别，默认false
            .setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
            .setAreaRectVerticalOffset(0)
            .areaRectHorizontalOffset = 0//设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        cameraScan
            .setVibrate(true)//设置是否震动，默认为false
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig))//设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现

    }
}