package com.example.esdgspro

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.esdgspro.R.drawable.*

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class FoodListDBAdapter(val context: Activity): BaseAdapter() {
    val inflater: LayoutInflater
        get() = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var idList: ArrayList<String> = arrayListOf()
    var nameList: ArrayList<String> = arrayListOf()
    var imageList: ArrayList<ByteArray> = arrayListOf()
    var statusList: ArrayList<String> = arrayListOf()
    var quantifyList: ArrayList<Int> = arrayListOf()
    var expireList: ArrayList<String> = arrayListOf()
    var proClassList: ArrayList<String> = arrayListOf()

    override fun getCount(): Int {
        return idList.count()
    }

    override fun getItem(index: Int): Any {
        return idList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
/*
        val listLayout = inflater.inflate(R.layout.list_item,null)
        var textId = listLayout.findViewById<TextView>(R.id.ingredient_id)
        textId.text = idList.get(position)
        var textName = listLayout.findViewById<TextView>(R.id.name)
        textName.text = nameList.get(position)
*/
        val listLayout = inflater.inflate(R.layout.list_item,null)

        var textId = listLayout.findViewById<TextView>(R.id.ingredient_id)
        textId.text =idList.get(position)

        var textName = listLayout.findViewById<TextView>(R.id.name)
        textName.text = nameList.get(position)

        var bitmapImage = listLayout.findViewById<ImageView>(R.id.image)

        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = false
        var    bitmap = BitmapFactory.decodeByteArray(imageList.get(position), 0, imageList.get(position).size,opts)

            bitmapImage.setImageBitmap(bitmap)

        var textStatus =  listLayout.findViewById<TextView>(R.id.consume_flag)
        textStatus.text = statusList.get(position)

        var textQuantify = listLayout.findViewById<TextView>(R.id.quantity)
        textQuantify.text = quantifyList.get(position).toString()

        var textExpire = listLayout.findViewById<TextView>(R.id.detail)
        textExpire.text = expireList.get(position).toString()

        var textProclass = listLayout.findViewById<TextView>(R.id.classification)
        textProclass.text = proClassList.get(position).toString()

        return listLayout
    }
}


/*
class FoodListDBAdapter(val context: Activity): BaseAdapter() {
    val inflater: LayoutInflater
    get() = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var idList: ArrayList<String> = arrayListOf()
    var nameList: ArrayList<String> = arrayListOf()
    var imageList: ArrayList<Bitmap> = arrayListOf()
    var statusList: ArrayList<String> = arrayListOf()
    var quantifyList: ArrayList<Int> = arrayListOf()
    var expireList: ArrayList<String> = arrayListOf()
    var proClassList: ArrayList<String> = arrayListOf()


    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val listLayout = inflater.inflate(R.layout.list_item,null)

        var textId = listLayout.findViewById<TextView>(R.id.ingredient_id)
        textId.text =idList.get(position)

        var textName = listLayout.findViewById<TextView>(R.id.name)
        textName.text = nameList.get(position)

        var bitmapImage = listLayout.findViewById<ImageView>(R.id.image)
        bitmapImage.setImageBitmap(imageList.get(position))

        var textStatus =  listLayout.findViewById<TextView>(R.id.consume_flag)
        textStatus.text = statusList.get(position)

        var textQuantify = listLayout.findViewById<TextView>(R.id.quantity)
        textQuantify.text = quantifyList.get(position).toString()

        var textExpire = listLayout.findViewById<TextView>(R.id.expiryDate)
        textExpire.text = expireList.get(position)

        var textProclass = listLayout.findViewById<TextView>(R.id.classification)
        textProclass.text = proClassList.get(position)

        return listLayout
    }
}
*/