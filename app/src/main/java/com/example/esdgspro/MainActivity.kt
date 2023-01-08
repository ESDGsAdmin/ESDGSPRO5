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
    var data: MutableList<Map<String, Serializable>> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*//DB接続用
        val helper = DBHelper(this@MainActivity)
        val db = helper.writableDatabase
        println(db)

        val test = db.rawQuery("select * from esdgs", null)
        println(test.count)

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
        }*/

    }

    override fun onResume(){
        super.onResume()

        //DB接続用
        val helper = DBHelper(this@MainActivity)
        val db = helper.writableDatabase
        println(db)

        val test = db.rawQuery("select * from food_ingredient_tb", null)
        println(test.count)
        data = mutableListOf()
        //val map = mapOf<String, Int>()
        test.use{
            while (it.moveToNext()){
                with(it){
                    var consumeCheck: String = ""
                    if (getInt(6) == 1){
                        consumeCheck = "消費済"
                    }
                    data.add(mapOf("id" to getString(0), "image" to getBlob(7), "name" to getString(1), "consume_flag" to consumeCheck, "classification" to getString(2), "expiry" to getString(4), "quantity" to getInt(5)))
                }
            }
        }

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
                EditProductActivity::class.java)
            //val editText: EditText = findViewById(R.id.editText) as EditText
            val barcodeId = data[position]["id"]
            //val message: String = data[position]["name"] as String
            //intent.putExtra(extraMessage, message)
            intent.putExtra("ingredientId", barcodeId)
            startActivity(intent)
        }
    }

    /* Sendボタン押下時 */
    fun createProduct(view: View) {
        val intent: Intent = Intent(this@MainActivity,
            CreateProductActivity::class.java)
        /*val editText: EditText = findViewById(R.id.editText) as EditText
        val message: String = editText.text.toString()
        intent.putExtra(EXTRA_MESSAGE, message)*/
        startActivity(intent)
    }
}