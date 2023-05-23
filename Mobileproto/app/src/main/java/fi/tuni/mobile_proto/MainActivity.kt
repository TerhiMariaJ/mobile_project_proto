package fi.tuni.mobile_proto


import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread



class MainActivity : AppCompatActivity() {


    private lateinit var adapter: ArrayAdapter<Person>
    private lateinit var alertDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var lv: ListView = findViewById<ListView>(R.id.listView)
        val addButton = findViewById<Button>(R.id.addButton)
        val searchField = findViewById<EditText>(R.id.searchField)
        val searchButton = findViewById<Button>(R.id.searchItemButton)

        addButton.setOnClickListener {
            addUserPopUp()
        }

        searchButton.setOnClickListener {
            val search = searchField.text.toString()
            searchUser(search)
        }

        adapter = ArrayAdapter<Person>(this, R.layout.item, R.id.myTextView, ArrayList())
        lv.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        val url: String = "https://dummyjson.com/users"

        thread {

            val json : String? = getUrl(url)
            if(json != null) {
                val mp = ObjectMapper()
                val myObject: DummyJsonObject = mp.readValue(json, DummyJsonObject::class.java)
                val persons: MutableList<Person>? = myObject.users
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

    fun getUrl(url: String): String? {
        print("getUrl method called")
        var result: String? = null
        val sb = StringBuffer()
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        val inputStream = conn.getInputStream()
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.use {
            var line: String ? = null

            do {
                line = it.readLine()
                sb.append(line)

            } while (line != null)

            result = sb.toString()

        }

        return result
    }

    fun addUserPopUp(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add User")
        val addView = LayoutInflater.from(this).inflate(R.layout.add_user, null)
        builder.setView(addView)

        val firstNameEdit = addView.findViewById<EditText>(R.id.firstNameEdit)
        val lastNameEdit = addView.findViewById<EditText>(R.id.lastNameEdit)
        val addNew = addView.findViewById<Button>(R.id.addNew)
        val cancelButton = addView.findViewById<Button>(R.id.cancelButton)


        addNew.setOnClickListener {
            val firstName = firstNameEdit.text.toString()
            val lastName = lastNameEdit.text.toString()


            if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val newUser = Person(firstName, lastName)
                addUser(newUser)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter valid names", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun addUser(user: Person) {
        val url = "https://dummyjson.com/users/add"
        thread {
            val mp = ObjectMapper()
            val userJson = mp.writeValueAsString(user)
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            val outputStream: OutputStream = conn.outputStream
            outputStream.write(userJson.toByteArray(Charsets.UTF_8))
            val response = conn.responseCode
            if (response == HttpURLConnection.HTTP_OK) {
                runOnUiThread {
                    adapter.add(user)
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Adding failed", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    fun searchUser(search: String){
        val url = "https://dummyjson.com/users/search?q=$search"
        thread {
            val json: String? = getUrl(url)
            if(json != null) {
                val mp = ObjectMapper()
                val myObject: DummyJsonObject = mp.readValue(json, DummyJsonObject::class.java)
                val persons: MutableList<Person>? = myObject.users
                if(persons != null){
                    persons?.forEach {
                        runOnUiThread {
                            adapter.clear()
                            persons.forEach {
                                adapter.add(it)
                            }
                        }

                    }
                }

            }


        }
    }



}