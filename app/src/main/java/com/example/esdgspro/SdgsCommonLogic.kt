package com.example.esdgspro

import com.example.esdgspro.R.drawable.*

class SdgsCommonLogic()  {

    //var curSpn: Int = 0

    fun selectProductImage(curSpn:Int): Int {

        var spnNum:Int = 0

        when (curSpn) {
            1 -> {
                spnNum = kokurui_01
            }
            2 -> {
                spnNum = mamerui_02
            }
            3 -> {
                spnNum = yasairui_03
            }
            4 -> {
                spnNum = kajiturui_04
            }
            5 -> {
                spnNum = kinokorui_05
            }
            6 -> {
                spnNum = gyokairui_06
            }
            7 -> {
                spnNum = nikurui_07
            }
            8 -> {
                spnNum = tamagorui_08
            }
            9 -> {
                spnNum = nyurui_09
            }
            10 -> {
                spnNum = sikoinryo_10
            }
            11 -> {
                spnNum = cyomiryo_11
            }
            12 -> {
                spnNum =  sonota_12
            }
        }

        return spnNum
    }

    fun setProductClass(number:Int): String {

        var productClass:String = ""

        when (number) {
            1 -> {
                productClass = "穀類"
            }
            2 -> {
                productClass = "豆(種実)類"
            }
            3 -> {
                productClass = "野菜類"
            }
            4 -> {
                productClass = "果実類"
            }
            5 -> {
                productClass = "きのこ類"
            }
            6 -> {
                productClass = "魚介類"
            }
            7 -> {
                productClass = "肉類"
            }
            8 -> {
                productClass = "卵類"
            }
            9 -> {
                productClass = "乳類"
            }
            10 -> {
                productClass = "嗜好飲料"
            }
            11 -> {
                productClass = "調味料"
            }
            12 -> {
                productClass =  "その他"
            }
        }

        return productClass
    }

}