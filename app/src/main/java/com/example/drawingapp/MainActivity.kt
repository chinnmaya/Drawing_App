package com.example.drawingapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private var mcurrentImageBtn:ImageButton?=null
    private var drawingview:Drawing?=null
    private var brushsizebtn:ImageButton?=null
    val opengallery:ActivityResultLauncher<Intent> =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode=== RESULT_OK && result.data!=null){
            val Imagebackground:ImageView=findViewById(R.id.iv_background)
            Imagebackground.setImageURI(result.data?.data)
        }

    }

    //requesting permissopm
    val requestpermissionn:ActivityResultLauncher<Array<String>> =registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        permissions->permissions.entries.forEach() {
        val permissionName = it.key
        val isgranted=it.value
        if(isgranted){
            Toast.makeText(this,"Permission is granted for accessing gallery",Toast.LENGTH_LONG).show()
            var intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)//gives the image path
            opengallery.launch(intent)
        }else{
            if(permissionName==android.Manifest.permission.READ_EXTERNAL_STORAGE){
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_LONG).show()
            }
        }

    }
    }

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
        val btn_gallery:ImageButton=findViewById(R.id.btn_gallery)
        btn_gallery.setOnClickListener {
            requestpermission();
        }
        val btn_undo:ImageButton=findViewById(R.id.ic_ondo)
        btn_undo.setOnClickListener {
            drawingview?.onclickundo()

        }
        val btn_redo:ImageButton=findViewById(R.id.redo)
        btn_redo.setOnClickListener {
            drawingview?.onclickredo()

        }



    }
    //create bitmap for saving the image
    private  fun getBitmapFromView(view: View):Bitmap{
        val returnedBitmap=Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(returnedBitmap)
        val background=view.background
        if(background!=null){
            background.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
    //requesting permission
    private fun requestpermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            showRationaleDialog("Drawing App","Drawing app needs to access your external storage")
        }else{
            requestpermissionn.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
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
    //request dialough
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }



}