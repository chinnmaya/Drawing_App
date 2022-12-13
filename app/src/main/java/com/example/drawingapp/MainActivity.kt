package com.example.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var mcurrentImageBtn:ImageButton?=null
    private var drawingview:Drawing?=null
    private var brushsizebtn:ImageButton?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        brushsizebtn=findViewById(R.id.brushsize)
        drawingview=findViewById(R.id.drawing_viewsc)
        drawingview?.setsizeOfBrush(10.toFloat())
        val linearlayoutpaintbar=findViewById<LinearLayout>(R.id.ll_paintBar)
        mcurrentImageBtn=linearlayoutpaintbar[4] as ImageButton
        mcurrentImageBtn!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
        )
        brushsizebtn!!.setOnClickListener{
            showbrushsize()
        }



    }
    private fun showbrushsize(){
        var brushsizedialog=Dialog(this)
        brushsizedialog.setContentView(R.layout.dialog_brushsize)
        brushsizedialog.setTitle("Select brush size")
        val smallbtn:ImageButton=brushsizedialog.findViewById(R.id.ib_small_brush)
        val mediumbtn:ImageButton=brushsizedialog.findViewById(R.id.ib_medium_brush)
        val largebtn:ImageButton=brushsizedialog.findViewById(R.id.ib_large_brush)
        smallbtn.setOnClickListener{
            drawingview!!.setsizeOfBrush(10.toFloat())
            brushsizedialog.dismiss()
        }
        mediumbtn.setOnClickListener{
            drawingview!!.setsizeOfBrush(20.toFloat())
            brushsizedialog.dismiss()
        }
        largebtn.setOnClickListener{
            drawingview!!.setsizeOfBrush(30.toFloat())
            brushsizedialog.dismiss()
        }
        brushsizedialog.show()}
    //change the paint click
    fun paintclick(view: View){
        if(view!==mcurrentImageBtn){
            val ImageBtn=view as ImageButton
            val colortag=ImageBtn.tag.toString()
            drawingview!!.setcolor(colortag)
            ImageBtn.setImageDrawable (ContextCompat.getDrawable(this,R.drawable.pallet_pressed))
            mcurrentImageBtn!!.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)

            )
            mcurrentImageBtn=view


        }


    }



}