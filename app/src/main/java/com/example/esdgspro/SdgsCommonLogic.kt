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

}