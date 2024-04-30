package com.example.qr_code_generator

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.example.qr_code_generator.databinding.ActivityMainBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
binding.apply {
    btnGenerate.setOnClickListener{
        val text=etText.text.toString().trim()
        if(text.isNotEmpty()){
            val bitmap=generateQrCode(text)
            ivQrCode.setImageBitmap(bitmap)
            btnShareQrCode.isEnabled=true

        }
    }

    btnShareQrCode.setOnClickListener {
        shareQrcode()
    }
}

    }

    private fun generateQrCode(text:String):Bitmap{
        val writer=QRCodeWriter()
        val bitMatrix=writer.encode(text,BarcodeFormat.QR_CODE,512,512)
        val width=bitMatrix.width
        val height=bitMatrix.height
        val bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
        for(x in 0 until width){
            for(y in 0 until height){
                bitmap.setPixel(x,y,if (bitMatrix[x,y])Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    private fun shareQrcode(){

        val bitmap=(binding.ivQrCode.drawable).toBitmap()
        val uri=getImageUri(bitmap)
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_STREAM,uri)
        startActivity(Intent.createChooser(intent,"Share QR"))
    }

    private fun getImageUri(bitmap:Bitmap):Uri{

        val path=MediaStore.Images.Media.insertImage(contentResolver,bitmap,"QR Code",null)
        return Uri.parse(path)


    }

}