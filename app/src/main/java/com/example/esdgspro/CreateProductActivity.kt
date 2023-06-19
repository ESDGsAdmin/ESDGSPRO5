package com.example.esdgspro

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

class CreateProductActivity : AppCompatActivity() {

    private lateinit var image1: ImageView
    private lateinit var textView: TextView
    private lateinit var spinner: Spinner
    private lateinit var purchaseDateText: TextView
    private lateinit var expiryDateText: TextView
    private lateinit var quantity: TextView
    private lateinit var plusClick: TextView
    private lateinit var minusClick: TextView
    private lateinit var barcodeId: TextView

    var selectProImg = SdgsCommonLogic()
    var curSpnVal = 0
    val CAMERA_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        // Activity開始時にIntentを取得し、文字列をセットする。
        //val intent: Intent = getIntent()
        //val message: String? = intent.getStringExtra(MainActivity().extraMessage)
        //val id: Int = intent.getIntExtra("ingredientId", 0)

        //var selectProImg = SdgsCommonLogic()

        //DB接続用
        val helper = DBHelper(this@CreateProductActivity)
        val db = helper.writableDatabase
        val selectId = db.rawQuery("select * from food_ingredient_tb", null)

        image1 = findViewById(R.id.imageView)
        textView = findViewById(R.id.productName)
        spinner = findViewById(R.id.spinner)
        purchaseDateText = findViewById(R.id.purchaseDate)
        expiryDateText = findViewById(R.id.expiryDate)
        quantity = findViewById(R.id.product_qty)
        plusClick = findViewById(R.id.plus)
        minusClick = findViewById(R.id.minus)
        barcodeId = findViewById(R.id.barcode_id)
        barcodeId.setText("A"+(selectId.count + 1).toString().padStart(13,'0'))

        ArrayAdapter.createFromResource(
            this,
            R.array.product_class,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        /*itemData.use{
            while (it.moveToNext()){
                with(it){
                    if (getBlob(7) != null) {
                        image1.setImageResource(getInt(7))
                    }
                    else{
                        //image1.setImageResource(R.drawable.camel)
                    }
                    textView.setText(getString(1))
                    println(R.array.product_class)
                    spinner.setSelection(getInt(2) - 1)
                    purchaseDateText.setText(getString(3))
                    expiryDateText.setText(getString(4))
                    quantity.setText(getInt(5).toString())
                }
            }
        }*/
        /*「商品分類」選択時*/
        spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ){
                    curSpnVal = (parent as Spinner).selectedItemPosition + 1

                    //selectProImg.curSpn = curSpnVal
                    //image1.setImageResource(selectProImg.selectProductImage(curSpnVal))
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                        //
                }
        }


        purchaseDateText.setOnClickListener {
            showDatePicker(purchaseDateText.text.toString(), purchaseDateText)
        }

        expiryDateText.setOnClickListener {
            showDatePicker(expiryDateText.text.toString(), expiryDateText)
        }

        plusClick.setOnClickListener {
            val sum: String = quantity_mod(quantity, plusClick)
            quantity.setText(sum)
        }

        minusClick.setOnClickListener {
            val sum: String = quantity_mod(quantity, minusClick)
            quantity.setText(sum)
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }*/
    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 2
    }


    override fun onResume(){
        super.onResume()
        image1 = findViewById(R.id.imageView)
        image1.setOnClickListener{
            // カメラ機能を実装したアプリが存在するかチェック
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager)?.let {
                if (checkCameraPermission()) {
                    takePicture()
                } else {
                    grantCameraPermission()
                }
            } ?: Toast.makeText(this, "カメラを扱うアプリがありません", Toast.LENGTH_LONG).show()
        }
    }

    private fun grantCameraPermission() =
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE)

    private fun checkCameraPermission() = PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)


    private fun showDatePicker(getText: String, getTextValue: TextView) {
        val setYear: Int
        val setMonth: Int
        val setDay: Int
        if (getText == ""){
            val dateNow = LocalDateTime.now()
            setYear = dateNow.year
            setMonth = dateNow.monthValue
            setDay = dateNow.dayOfMonth
        }
        else{
            setYear = getText.substring(0,4).toInt()
            setMonth = getText.substring(5,7).toInt()
            setDay = getText.substring(8,10).toInt()
        }
        println(setYear)
        println(setMonth)
        println(setDay)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() {view, year, month, dayOfMonth->
                //val text: TextView = findViewById(R.id.purchaseDate)
                getTextValue.setText("${year}/" + (month + 1).toString().padStart(2, '0') + "/" + dayOfMonth.toString().padStart(2, '0'))
            },
            setYear,
            setMonth - 1,
            setDay)
        datePickerDialog.show()
    }

    private fun quantity_mod(quantity: TextView, mode: TextView): String {
        val qty: String = quantity.getText().toString()
        var sum: Int = qty.toInt()

        if (mode.getText().toString() == "+"){
            sum = qty.toInt() + 1
        }
        else if (mode.getText().toString() == "−"){
            if (sum > 0){
                sum = qty.toInt() - 1
            }
        }

        return sum.toString()
    }

    private fun takePicture(){
        println("カメラ起動します")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }

        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("★写真撮られた！")

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            image1.setImageBitmap(imageBitmap)
        }
    }

    /* 保存ボタン押下（データ登録）時 */
    fun createData(view: View) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_ins_message)
            .setPositiveButton(R.string.dialog_ok) { dialog, which ->

                val helper = DBHelper(this@CreateProductActivity)
                val db = helper.writableDatabase
                //println(spinner.selectedItem)
                //println(spinner.selectedItemPosition + 1)
                println(barcodeId.getText())
                val status: Int
                val user: String = "Insert"
                if (quantity.getText().toString().toInt() > 0){
                    status = 0
                    println("未消費")
                    println(status)
                }
                else{
                    status = 1
                    println("消費済")
                    println(status)
                }

                /*画像ファイルをBlobに変換*/
                if (image1.drawable == null){
                    image1.setImageResource(selectProImg.selectProductImage(curSpnVal))
                }
                val bitmapDrawable = image1.drawable as BitmapDrawable
                val bitmapDrawablex = bitmapDrawable.bitmap
                val byteArrayOutputStream = ByteArrayOutputStream();

                bitmapDrawablex.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val bytes = byteArrayOutputStream.toByteArray()

                try {

                    val values = ContentValues()
                    values.put("ingredient_id", barcodeId.getText().toString())
                    values.put("ingredient_name", textView.getText().toString())
                    values.put("product_class", spinner.selectedItemPosition + 1)
                    values.put("purchase_date", purchaseDateText.getText().toString())
                    values.put("expiry_date", expiryDateText.getText().toString())
                    values.put("quantity", quantity.getText().toString().toInt())
                    values.put("state", status)
                    values.put("image", bytes)
                    values.put("reg_user", user)
                    values.put("reg_date",LocalDateTime.now().toString())
                    values.put("upd_user", user)
                    values.put("upd_date",LocalDateTime.now().toString())

                    db.insertOrThrow("food_ingredient_tb", null, values)
                }catch(exception: Exception) {
                    Log.e("insertData", exception.toString())
                }




                finish()

                //登録完了メッセージ表示
                val toast = Toast.makeText(this@CreateProductActivity,R.string.cmp_ins_message,Toast.LENGTH_LONG)
                toast.show()

                //メニューに戻る
                val intent: Intent = Intent(this@CreateProductActivity,
                    MainActivity::class.java)
                startActivity(intent)

            }
            // Cancelの時は何もしない
            .setNegativeButton(R.string.dialog_cancel) { dialog, which ->

            }
            .show()

    }
}

/*class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}*/