package com.papyrus.bshairwebapp.Preferences

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log

class Preferences (val context:Context){
    var myPreferences = PreferenceManager.getDefaultSharedPreferences(context)!!
    var myEditor = myPreferences.edit()
    var localMyFontName:String = ""
    var localMyFontSize:String = ""
    var localMyFontNameNumberOfArray:Int = 1
    var localMyFontSizeNumberOfArray:Int = 1

    fun setStyle(myFont:String, mySize:String){
        localMyFontName = myFont
        localMyFontSize = mySize
        myEditor.putString("userFontName", localMyFontName)
        myEditor.putString("userFontSize", localMyFontSize)
        myEditor.commit()
    }

    fun setFontNameNumberOfArrayForDialog(myFontNameNumber:Int){
        Log.i("tgggt2", myFontNameNumber.toString())
        localMyFontNameNumberOfArray = myFontNameNumber
        myEditor.putInt("userFontNameNumberForDialog", localMyFontNameNumberOfArray)
        myEditor.commit()
    }

    fun setFontSizeNumberOfArrayForDialog(myFontSizeNumber:Int){
        Log.i("tgggt3", myFontSizeNumber.toString())
        localMyFontSizeNumberOfArray = myFontSizeNumber
        myEditor.putInt("userFontSizeNumberForDialog", localMyFontSizeNumberOfArray)
        myEditor.commit()
    }

    fun getFontName():String{
//        TODO: FOR CHANGE FONT DEFAULT MUST CHANGE USERFONTNAME AND USERFONTSIZE AND THE NUMBER IN NEXT FUNCTIONS
        var newFontName = myPreferences.getString("userFontName", "droidkufi_regular.ttf")
        return newFontName
    }

    fun getFontSize():String{
        var newFontSize = myPreferences.getString("userFontSize", "100%")
        return newFontSize
    }

    fun getFontNameNumber():Int{
        var newFontNameNumber:Int = myPreferences.getInt("userFontNameNumberForDialog", 1)
        return newFontNameNumber
    }

    fun getFontSizeNumber():Int{
        var newFontSizeNumber:Int = myPreferences.getInt("userFontSizeNumberForDialog", 1)
        return newFontSizeNumber
    }
}