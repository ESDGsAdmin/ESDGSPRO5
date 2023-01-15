package com.example.esdgspro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.widget.ArrayAdapter
import android.widget.ListView
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.SimpleAdapter
import androidx.core.graphics.drawable.toIcon
import androidx.cursoradapter.widget.SimpleCursorAdapter
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

    }

    override fun onResume(){
        super.onResume()

        //DB接続用
        val helper = DBHelper(this@MainActivity)
        val db = helper.writableDatabase
        println(db)

        val resultFood = db.rawQuery(
            "select ingredient_id,ingredient_name," +
                    "product_class,purchase_date," +
                    "expiry_date,quantity,state,image" +
                " from food_ingredient_tb", null)
        println(resultFood.count)
        data = mutableListOf()
        //val map = mapOf<String, Int>()
        resultFood.use{
            while (it.moveToNext()){
                with(it){
                    var consumeCheck: String = ""
                    if (getInt(6) == 1){
                        consumeCheck = "消費済"
                    }
/*
                    val bitmap:Bitmap
                    if (getBlob(7) != null){
                        val blob: ByteArray = getBlob(7)
                        bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
                    }
*/

                    data.add(mapOf("id" to getString(0), "image" to "test", "name" to getString(1), "consume_flag" to consumeCheck, "classification" to getString(2), "expiry" to getString(4), "quantity" to getInt(5)))
                }
            }
        }

        val listFood = findViewById<ListView>(R.id.list)

        listFood.adapter = SimpleAdapter(
            this,
            /*android.R.layout.simple_list_item_1,*/
            data,
            R.layout.list_item,
            arrayOf("id", "image", "name", "consume_flag", "classification", "expiry", "quantity"),
            intArrayOf(R.id.ingredient_id, R.id.image, R.id.name, R.id.consume_flag, R.id.classification, R.id.detail, R.id.quantity,)
        )


/*
        val adapter:SimpleCursorAdapter
        //listFood.adapter = SimpleCursorAdapter(
        adapter = SimpleCursorAdapter(
            getApplicationContext(),
            R.layout.list_item,
            resultFood,
            arrayOf("id", "image", "name", "consume_flag", "classification", "expiry", "quantity"),
            intArrayOf(R.id.ingredient_id, R.id.image, R.id.name, R.id.consume_flag, R.id.classification, R.id.detail, R.id.quantity,),
        0
        )
        adapter.setViewBinder(this)*/
        /*リスト内アイテムクリック時*/
        listFood.setOnItemClickListener { adapterView, view, position, id ->
            val intent: Intent = Intent(this@MainActivity,
                EditProductActivity::class.java)
            val barcodeId = data[position]["id"]
            intent.putExtra("ingredientId", barcodeId)
            //編集画面へ遷移
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

    fun action_delete(view: View){

    }
}