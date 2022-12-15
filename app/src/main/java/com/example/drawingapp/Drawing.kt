package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.nio.file.Path
import java.nio.file.Paths

class Drawing (context:Context,attrs:AttributeSet):View(context,attrs){
    private  var mDrawPath:CustomPath?=null
    private  var mCanvasBitmap:Bitmap?=null
    private var mDrawPaint:Paint?=null
    private  var mCanvasPaint:Paint?=null
    private  var brushsize:Float=0.toFloat()
    private  var color=Color.BLACK
    private var canvas:Canvas?=null
    private  val mpaths=ArrayList<CustomPath>()//stores the recent line path we draw
    private  val mundopath=ArrayList<CustomPath>()

    init {
        setupDrawing()
    }
    //undo
    public  fun onclickundo(){
        if(mpaths.size>0){
           mundopath.add(mpaths.removeAt(mpaths.size-1))
            invalidate()


        }
    }
    //redo
    public  fun onclickredo(){
        if(mundopath.size>0){
            mpaths.add(mundopath.removeAt(mundopath.size-1))
            invalidate()


        }
    }


    private fun setupDrawing() {
        mDrawPaint=Paint()
        mDrawPath=CustomPath(color,brushsize)
        mDrawPaint!!.color=color
        mDrawPaint!!.style=Paint.Style.STROKE
        mDrawPaint!!.strokeJoin=Paint.Join.ROUND
        mDrawPaint!!.strokeCap=Paint.Cap.ROUND
        mCanvasPaint=Paint(Paint.DITHER_FLAG)
       // brushsize=20.toFloat()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas=Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas ) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)


       for(path in mpaths){
            mDrawPaint!!.strokeWidth=path.brushthinkness
            mDrawPaint!!.color=path.color
            canvas.drawPath(path,mDrawPaint!!)

        }

        if(!mDrawPath!!.isEmpty){
            mDrawPaint!!.strokeWidth=mDrawPath!!.brushthinkness
            mDrawPaint!!.color=mDrawPaint!!.color
            canvas.drawPath(mDrawPath!!,mDrawPaint!!)

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchx=event?.x
        val touchy=event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                mDrawPath!!.color=color
               mDrawPath!!.brushthinkness=brushsize
                mDrawPath!!.reset()
                if(touchx!=null){
                    if(touchy!=null){
                        mDrawPath!!.moveTo(touchx,touchy)
                    }
                }
            }
            MotionEvent.ACTION_MOVE->{
                if(touchx!=null){
                    if(touchy!=null){
                        mDrawPath!!.lineTo(touchx,touchy)


                    }
                }
            }
            MotionEvent.ACTION_UP->{
                mpaths.add(mDrawPath!!)

                        mDrawPath=CustomPath(color,brushsize)


            }
            else->return  false


        }
        invalidate()
        return true
    }
    public fun setsizeOfBrush(newsize:Float){
        brushsize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newsize,resources.displayMetrics)
         mDrawPaint!!.strokeWidth=brushsize
    }
    fun setcolor(newColor: String){
        color=Color.parseColor(newColor)
        mDrawPaint!!.color=color
    }



    internal inner  class CustomPath (var color:Int,var brushthinkness:Float):android.graphics.Path(){


    }

}