package fi.tuni.mobile_proto

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {


    private lateinit var adapter: ArrayAdapter<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var lv: ListView = findViewById<ListView>(R.id.listView)

        adapter = ArrayAdapter<Users>(this, R.layout.item, R.id.myTextView, ArrayList())
        lv.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        val url : String = "https://dummyjson.com/users"

        thread {

            val json : String? = getUrl(url)
            if(json != null) {
                val mp = ObjectMapper()
                val myObject: DummyJsonObject = mp.readValue(json, DummyJsonObject::class.java)
                val persons: MutableList<Users>? = myObject.results
                if(persons != null){
                    persons?.forEach {
                        println(it)
                        runOnUiThread{
                            adapter.add(it)
                        }

                    }
                }

            }


        }
    }

    fun getUrl(url: String) : String? {
        var result : String? = null
        val sb = StringBuffer()
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        val inputStream = conn.getInputStream()
        val reader = BufferedReader(InputStreamReader(inputStream));

        reader.use {
            var line : String ? = null

            do {
                line = it.readLine()
                sb.append(line)

            } while (line != null)

            result = sb.toString()

        }

        return result
    }
}