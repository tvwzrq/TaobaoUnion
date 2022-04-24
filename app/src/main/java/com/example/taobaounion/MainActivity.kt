package com.example.taobaounion

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.taobaounion.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        //设置饱和度(灰色主题)
       /* val container=window.decorView
        val colorMatrix=ColorMatrix()
        colorMatrix.setSaturation(0f)
        val paint=Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        container.setLayerType(View.LAYER_TYPE_SOFTWARE,paint)*/

        /*
        直接findnavcontrollar需要在onstart之后
         */
       /* val fragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val controller=fragment.navController*/
        val controller=Navigation.findNavController(this,R.id.fragmentContainerView)
        controller.addOnDestinationChangedListener({ controller, destination, arguments ->
            if(destination.id==R.id.fragment_selected
                ||destination.id==R.id.fragment_on_sell
                ||destination.id==R.id.fragment_search
                ||destination.id==R.id.fragment_home){
                binding.bottomNavigationView.visibility=View.VISIBLE
            }else{
                binding.bottomNavigationView.visibility=View.GONE
            }
        })
        /*
        关联底部导航栏
         */
        binding.bottomNavigationView.setupWithNavController(controller)
        }


}