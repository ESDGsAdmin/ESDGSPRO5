package com.example.esdgspro

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.time.LocalDateTime

class EditProductActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_edit_product)

        // Activity開始時にIntentを取得し、文字列をセットする。
        val intent: Intent = intent
        //val message: String? = intent.getStringExtra(MainActivity().extraMessage)
        //val id = intent.getIntExtra("ingredientId", 0)
        val id = intent.getStringExtra("ingredientId")
        barcodeId = findViewById(R.id.barcode_id)

        barcodeId.text = id.toString()

        //DB接続用
        val helper = DBHelper(this@EditProductActivity)
        val db = helper.writableDatabase
        val itemData = db.rawQuery("select * from food_ingredient_tb where ingredient_id = '" + id + "';", null)

        image1 = findViewById(R.id.imageView)
        textView = findViewById(R.id.productName)
        spinner = findViewById(R.id.spinner)
        purchaseDateText = findViewById(R.id.purchaseDate)
        expiryDateText = findViewById(R.id.expiryDate)
        quantity = findViewById(R.id.product_qty)
        plusClick = findViewById(R.id.plus)
        minusClick = findViewById(R.id.minus)

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

        itemData.use{
            while (it.moveToNext()){
                with(it){
                    if (getBlob(7) != null) {
                        image1.setImageResource(getInt(7))
                    }
                    else{
                        image1.setImageResource(R.drawable.camel)
                    }
                    textView.text = getString(1)
                    println(R.array.product_class)
                    spinner.setSelection(getInt(2) - 1)
                    purchaseDateText.text = getString(3)
                    expiryDateText.text = getString(4)
                    quantity.text = getInt(5).toString()
                }
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
            quantity.text = sum
        }

        minusClick.setOnClickListener {
            val sum: String = quantity_mod(quantity, minusClick)
            quantity.text = sum
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //オプションメニュー：削除ボタン　クリック時
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_del_message)
            .setPositiveButton(R.string.dialog_ok) { dialog, which ->
                val helper = DBHelper(this@EditProductActivity)
                val db = helper.writableDatabase
                val id = barcodeId.text
                val sqlDelete = "delete from food_ingredient_tb  where ingredient_id = '${id}'"
                val deleteData = db.compileStatement(sqlDelete)
                when(item.itemId){
                    R.id.action_delete ->
                    deleteData.executeUpdateDelete()
                }
                finish()

                //メニューに戻る
                val intent: Intent = Intent(this@EditProductActivity,
                    MainActivity::class.java)
                startActivity(intent)

            }

            // Cancelの時は何もしない
            .setNegativeButton(R.string.dialog_cancel) { dialog, which ->

            }
            .show()
        return true
    }
        /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
            println("DELETE")
            val dialog = SampleDialogFragment()
            println(dialog)
            dialog.show(supportFragmentManager, "simple")
            return super.onOptionsItemSelected(item)
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
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth->
                //val text: TextView = findViewById(R.id.purchaseDate)
                getTextValue.text = "${year}-" + (month + 1).toString().padStart(2, '0') + "-" + dayOfMonth.toString().padStart(2, '0')
            },
            setYear,
            setMonth - 1,
            setDay)
        datePickerDialog.show()
    }

    private fun quantity_mod(quantity: TextView, mode: TextView): String {
        val qty: String = quantity.text.toString()
        var sum: Int = qty.toInt()

        if (mode.text.toString() == "+"){
            sum = qty.toInt() + 1
        }
        else if (mode.text.toString() == "−"){
            if (sum > 0){
                sum = qty.toInt() - 1
            }
        }

        return sum.toString()
    }

    /* 保存ボタン押下（データ更新）時 */
    fun updateData(view: View) {

        val status: Int
        val user: String = "shimizu"

        if (quantity.text.toString().toInt() > 0){
            status = 0
        }
        else{
            status = 1
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_upd_message)
            .setPositiveButton(R.string.dialog_ok) { dialog, which ->
                //OKの場合、更新を行う。
                val helper = DBHelper(this@EditProductActivity)
                val db = helper.writableDatabase


                val dataArray = arrayOf(textView.text, spinner.selectedItemPosition + 1, purchaseDateText.text, expiryDateText.text, quantity.text.toString().toInt(), status, user, barcodeId.text)
                val sqlUpdate = "update food_ingredient_tb set ingredient_name = '${dataArray[0]}', product_class = ${dataArray[1]}, purchase_date = '${dataArray[2]}', expiry_date = '${dataArray[3]}', quantity = ${dataArray[4]}, state = ${dataArray[5]}, upd_date = CURRENT_DATE, upd_user = '${dataArray[6]}' where ingredient_id = '${dataArray[7]}'"
                val updateData = db.compileStatement(sqlUpdate)
                updateData.executeUpdateDelete()
                finish()

                //メニューに戻る
                val intent: Intent = Intent(this@EditProductActivity,
                    MainActivity::class.java)
                startActivity(intent)

            }

            // Cancelの時は何もしない
            .setNegativeButton(R.string.dialog_cancel) { dialog, which ->

            }
            .show()

    }





    class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }
}