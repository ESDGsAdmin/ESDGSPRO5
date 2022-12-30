package com.example.esdgspro
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import com.example.esdgspro.R.*
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime

class DisplayMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_display_message)

        // Activity開始時にIntentを取得し、文字列をセットする。
        val intent: Intent = getIntent()
        //val message: String? = intent.getStringExtra(MainActivity().extraMessage)
        //val id: Int = intent.getIntExtra("ingredientId", 0)
        val id: Int = intent.getIntExtra("ingredientId", 0)
        val barcodeId: TextView = findViewById(R.id.barcode_id)

        barcodeId.setText(id.toString())

        // 2022.12.29 h.takeda add
        val insUpdFlg = intent.getStringExtra("INS_UPD_FLG").toString()
        var idStr: String = ""
        var idAlpha: String = ""
        var idNum: Int = 0
        // h.takeda add end

        //DB接続用
        val helper = DBHelper(this@DisplayMessageActivity)
        val db = helper.writableDatabase
        //takeda add
        //食材ID取得用
        if (insUpdFlg == "INS"){
            val idResult = db.rawQuery("select count(*),max(ingredient_id) from esdgs;",null)
            if (idResult.count > 0 ){
                idResult.moveToFirst()
                var idFromTB:String = ""
                while(!idResult.isAfterLast){
                    if (idResult.getString(0) == "0"){
                        idFromTB = "0"
                    }else{
                        idFromTB = idResult.getString(1)
                    }
                    idResult.moveToNext()
                }
                if (idFromTB == "0"){
                    idStr = "A0000000000001"
                }else{
                    idAlpha = idFromTB.substring(0,1)
                    idNum = idFromTB.substring(1).toInt()+ 1
                    idStr = idAlpha + idNum.toString().padStart(13,'0')
                }
            }
        }
        //takeda add end
        //ID採番

        val itemData = db.rawQuery("select * from esdgs where ingredient_id = " + id, null)

        val image1: ImageView = findViewById(R.id.imageView)
        val productName: TextView = findViewById(R.id.product_name)
        val spinner: Spinner = findViewById(R.id.spinner)
        val purchaseDateText: TextView = findViewById(R.id.purchaseDate)
        val expiryDateText: TextView = findViewById(R.id.expiryDate)
        val quantity: TextView = findViewById(R.id.product_qty)
        val plusClick: TextView = findViewById(R.id.plus)
        val minusClick: TextView = findViewById(R.id.minus)
        val barcode_id:TextView = findViewById(R.id.barcode_id)
        barcode_id.text = idStr

        ArrayAdapter.createFromResource(
            this,
            array.product_class,
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
                        image1.setImageResource(drawable.camel)
                    }
                    productName.setText(getString(1))
                    println(array.product_class)
                    spinner.setSelection(getInt(2) - 1)
                    purchaseDateText.setText(getString(3))
                    expiryDateText.setText(getString(4))
                    quantity.setText(getInt(5).toString())
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
            quantity.setText(sum)
        }

        minusClick.setOnClickListener {
            val sum: String = quantity_mod(quantity, minusClick)
            quantity.setText(sum)
        }

        //takeda add
        findViewById<Button>(R.id.reserve_button).setOnClickListener {

            helper.writableDatabase.use { db ->
                val cv = ContentValues().apply {
                    put("ingredient_id",idStr)
                    put("ingredient_name",productName.text.toString())
                    put("product_class",spinner.selectedItem.toString())
                    put("purchase_date",purchaseDateText.text.toString())
                    put("expiry_date",expiryDateText.text.toString())
                    put("quantity",quantity.text.toString())
                    put("state",0)
                }
                db.insert("esdgs",null,cv)
            }
            Toast.makeText(
                this,"データの登録が完了しました",
                Toast.LENGTH_SHORT).show()

        }
        //takeda add end


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

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

    /* 保存ボタン押下時 */
    fun updateData(view: View) {
        val helper = DBHelper(this@DisplayMessageActivity)
        val db = helper.writableDatabase
        val sqlUpdate = "update esdgs set ingredient_name = '', product_class = 0, purchase_date = '2022-12-12', expiry_date = '2022-12-12', quantity = 1, state = 0, modified = '2022-12-12', modifier = 'shimizu'"
        val updateData = db.compileStatement(sqlUpdate)
        updateData.executeUpdateDelete()
    }



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