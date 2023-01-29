package com.example.esdgspro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.widget.ArrayAdapter
import android.widget.ListView
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.Switch
import androidx.core.graphics.drawable.toIcon
import androidx.cursoradapter.widget.SimpleCursorAdapter
import java.io.Serializable
import kotlin.time.measureTimedValue

//import android.widget.Toast
//import com.example.esdgspro.DisplayMessageActivity

//import android.widget.EditText

class MainActivity : AppCompatActivity() {
    //val extraMessage:String = "com.example.sample.MESSAGE"
    var data: MutableList<Map<String, Serializable>> = mutableListOf()

    private var arrayListId: ArrayList<String> = arrayListOf()
    private var arrayListName: ArrayList<String> = arrayListOf()
    private var arrayListSt: ArrayList<String> = arrayListOf()
    private var arrayListQua: ArrayList<Int> = arrayListOf()
    private var arrayListImage: ArrayList<ByteArray> = arrayListOf()
    private var arrayListExpire: ArrayList<String> = arrayListOf()
    private var arrayListProClass: ArrayList<String> = arrayListOf()

    private lateinit var foodListDBAdapter: FoodListDBAdapter


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

        //リスト初期化
        arrayListId.clear()
        arrayListImage.clear()
        arrayListName.clear()
        arrayListSt.clear()
        arrayListProClass.clear()
        arrayListExpire.clear()
        arrayListQua.clear()


        var setProductClass = SdgsCommonLogic()
        val resultFood = db.rawQuery(
            "select ingredient_id as _id,ingredient_name," +
                    "product_class,purchase_date," +
                    "expiry_date,quantity,state,image" +
                    //"expiry_date,quantity,state" +
                " from food_ingredient_tb", null)
        println(resultFood.count)
        data = mutableListOf()
        //val map = mapOf<String, Int>()
        resultFood.use{
            while (it.moveToNext()){
                with(it){
                    var consumeCheck: String = "　　　"
                    if (getInt(6) == 1){
                        consumeCheck = "消費済"
                    }
                    val blob: ByteArray = getBlob(7)
                    //val bitmap:Bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)


                    arrayListId.add(getString(0))
                    arrayListImage.add(blob)
                    arrayListName.add(getString(1))
                    arrayListSt.add(consumeCheck)
                    arrayListProClass.add(setProductClass.setProductClass(getString(2).toInt()))
                    arrayListExpire.add(getString(4))
                    arrayListQua.add(getInt(5))



                    //data.add(mapOf("id" to getString(0), "image" to getBlob(7), "name" to getString(1), "consume_flag" to consumeCheck, "classification" to setProductClass.setProductClass(getString(2).toInt()), "expiry" to getString(4), "quantity" to getInt(5)))
                }
            }
        }
        val listFood = findViewById<ListView>(R.id.list)
/*
        listFood.adapter = SimpleAdapter(
            this,
            /*android.R.layout.simple_list_item_1,*/
            data,
            R.layout.list_item,
            arrayOf("id", "image", "name", "consume_flag", "classification", "expiry", "quantity"),
            intArrayOf(R.id.ingredient_id, R.id.image, R.id.name, R.id.consume_flag, R.id.classification, R.id.detail, R.id.quantity,)
        )
*/
        foodListDBAdapter = FoodListDBAdapter(this)

        listFood.adapter = foodListDBAdapter
        foodListDBAdapter.idList = arrayListId
        foodListDBAdapter.nameList = arrayListName
        foodListDBAdapter.imageList = arrayListImage
        foodListDBAdapter.statusList = arrayListSt
        foodListDBAdapter.expireList = arrayListExpire
        foodListDBAdapter.proClassList = arrayListProClass
        foodListDBAdapter.quantifyList = arrayListQua
        foodListDBAdapter.notifyDataSetChanged()



        
        /*リスト内アイテムクリック時*/
        listFood.setOnItemClickListener { adapterView, view, position, id ->
            val intent: Intent = Intent(this@MainActivity,
                EditProductActivity::class.java)
            val barcodeId = arrayListId.get(position).toString()
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

