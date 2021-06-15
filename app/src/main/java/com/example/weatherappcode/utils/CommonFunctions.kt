package com.example.weatherappcode.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


fun getJsonFromAssets(context: Context, fileName: String):String {
    val jsonString: String
    jsonString = try {
        val `is`: InputStream = context.getAssets().open(fileName)
        val size: Int = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        val charset: Charset = Charsets.UTF_8
        String(buffer, charset)
    } catch (e: IOException) {
        e.printStackTrace()
        return "null"
    }
    return jsonString
}
