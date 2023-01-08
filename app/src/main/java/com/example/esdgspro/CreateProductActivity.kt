package com.example.esdgspro

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        // Activity開始時にIntentを取得し、文字列をセットする。
        //val intent: Intent = getIntent()
        //val message: String? = intent.getStringExtra(MainActivity().extraMessage)
        //val id: Int = intent.getIntExtra("ingredientId", 0)

        //DB接続用
        val helper = DBHelper(this@CreateProductActivity)
        val db = helper.writableDatabase
        val selectId = db.rawQuery("select * from food_ingredient_tb", null)

        image1 = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
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
                getTextValue.setText("${year}-" + (month + 1).toString().padStart(2, '0') + "-" + dayOfMonth.toString().padStart(2, '0'))
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

    /* 保存ボタン押下時（データ登録） */
    fun createData(view: View) {
        val helper = DBHelper(this@CreateProductActivity)
        val db = helper.writableDatabase
        //println(spinner.selectedItem)
        //println(spinner.selectedItemPosition + 1)
        println(barcodeId.getText())
        val status: Int
        val user: String = "shimizu"
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
        val dataArray = arrayOf(barcodeId.getText(), textView.getText(), spinner.selectedItemPosition + 1, purchaseDateText.getText(), expiryDateText.getText(), quantity.getText().toString().toInt(), status, user)
        val sqlInsert = "INSERT INTO food_ingredient_tb (ingredient_id, ingredient_name, product_class, purchase_date, expiry_date, quantity, state, image, reg_date, reg_user, upd_date, upd_user) VALUES ('${dataArray[0]}', '${dataArray[1]}', ${dataArray[2]}, '${dataArray[3]}', '${dataArray[4]}', ${dataArray[5]}, ${dataArray[6]}, NULL, CURRENT_DATE, '${dataArray[7]}', CURRENT_DATE, '${dataArray[7]}');"
        println(sqlInsert)
        val insertData = db.compileStatement(sqlInsert)
        insertData.executeInsert()
        finish()
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