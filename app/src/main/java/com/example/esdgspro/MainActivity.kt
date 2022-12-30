package com.example.esdgspro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.widget.ArrayAdapter
import android.widget.ListView
import android.content.Intent
import android.view.View
import android.widget.SimpleAdapter
import java.io.Serializable

//import android.widget.Toast
//import com.example.esdgspro.DisplayMessageActivity

//import android.widget.EditText

class MainActivity : AppCompatActivity() {
    //val extraMessage:String = "com.example.sample.MESSAGE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DB接続用
        val helper = DBHelper(this@MainActivity)
        val db = helper.writableDatabase
        println(db)
        //val sqlInsert = "INSERT INTO esdgs (ingredient_id, ingredient_name, product_class, purchase_date, expiry_date, quantity, state, image, registered, register, modified, modifier) VALUES (1, '肉', 1, '2022-12-21', '2022-12-24', 1, 0, NULL, '2022-12-21', 'shimizu', '2022-12-21', 'shimizu');"
        //val stmt = db.compileStatement(sqlInsert)
        //stmt.executeInsert()
        val test = db.rawQuery("select * from esdgs", null)
        println(test.count)
        val data: MutableList<Map<String, Serializable>> = mutableListOf()
        //val map = mapOf<String, Int>()
        test.use{
            while (it.moveToNext()){
                with(it){
                    var consumeCheck: String = ""
                    if (getInt(6) == 1){
                        consumeCheck = "消費済"
                    }
                    data.add(mapOf("id" to getInt(0), "image" to getBlob(7), "name" to getString(1), "consume_flag" to consumeCheck, "classification" to getString(2), "expiry" to getString(4), "quantity" to getInt(5)))
                }
            }
        }

        /*val data = listOf(
            mapOf ("image" to R.drawable.camel, "name" to "北海道", "consume_flag" to "", "classification" to "都道府県", "detail" to "2022/09/05", "quantity" to 1),
            mapOf ("image" to R.drawable.goat, "name" to "宮城", "consume_flag" to "", "classification" to "都道府県", "detail" to "2022/10/12", "quantity" to 2),
            mapOf ("image" to R.drawable.gorilla, "name" to "東京", "consume_flag" to "", "classification" to "都道府県", "detail" to "2022/09/23", "quantity" to 1),
            mapOf ("image" to R.drawable.panda, "name" to "愛知", "consume_flag" to "消費済", "classification" to "都道府県", "detail" to "2022/10/29", "quantity" to 0),
            mapOf ("image" to R.drawable.camel, "name" to "大阪", "consume_flag" to "", "classification" to "都道府県", "detail" to "2022/11/01", "quantity" to 1),
            mapOf ("image" to R.drawable.goat, "name" to "広島", "consume_flag" to "消費済", "classification" to "都道府県", "detail" to "2022/10/17", "quantity" to 0),
            mapOf ("image" to R.drawable.gorilla, "name" to "福岡", "consume_flag" to "", "classification" to "都道府県", "detail" to "2022/11/02", "quantity" to 2),
            mapOf ("image" to R.drawable.panda, "name" to "沖縄", "consume_flag" to "", "classification" to "都道府県", "detail" to "2022/10/09", "quantity" to 1),
        )*/

        val list = findViewById<ListView>(R.id.list)
        list.adapter = SimpleAdapter(
            this,
            /*android.R.layout.simple_list_item_1,*/
            data,
            R.layout.list_item,
            arrayOf("id", "image", "name", "consume_flag", "classification", "expiry", "quantity"),
            intArrayOf(R.id.ingredient_id, R.id.image, R.id.name, R.id.consume_flag, R.id.classification, R.id.detail, R.id.quantity,)
        )

        list.setOnItemClickListener { adapterView, view, position, id ->
            /*val message = "「${(data[position])}」をクリックしました。"
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()*/
            val intent: Intent = Intent(this@MainActivity,
                DisplayMessageActivity::class.java)
            //val editText: EditText = findViewById(R.id.editText) as EditText
            val barcodeId: Int = data[position]["id"] as Int
            //val message: String = data[position]["name"] as String
            //intent.putExtra(extraMessage, message)
            intent.putExtra("ingredientId", barcodeId)
            startActivity(intent)
        }
    }

    /* Sendボタン押下時 */
    fun sendMessage(view: View) {
        val intent: Intent = Intent(this@MainActivity,
            DisplayMessageActivity::class.java)
        /*val editText: EditText = findViewById(R.id.editText) as EditText
        val message: String = editText.text.toString()
        intent.putExtra(EXTRA_MESSAGE, message)*/
        //2022.12.29 h.takeda add
        intent.putExtra("INS_UPD_FLG","INS")
        //takeda add end
        startActivity(intent)
    }
}