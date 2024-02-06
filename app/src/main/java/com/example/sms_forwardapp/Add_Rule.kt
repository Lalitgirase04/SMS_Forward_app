package com.example.sms_forwardapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_rule.*

class Add_Rule : AppCompatActivity() {

    lateinit var myAdapter : MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rule)

       nradd.setOnClickListener{


           val rname=rulename.text.toString()
           val pattern =patternname.text.toString()
           val mob = mobno.text.toString()
        //   val SwitchState = true

           var rule=ruleDetails(rname,pattern,mob)
          // ruleDetails.switchState = SwitchState

           listOfrule.add(rule)
         //  myAdapter.notifyDataSetChanged()

           //To save the ArrayList to SharedPreferences, convert it to a JSON string using the Gson library:
           val json = Gson().toJson(listOfrule)
           //Get a reference to the SharedPreferences instance:
           val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
           //Get an instance of the SharedPreferences.Editor class:
           val editor = sharedPreferences.edit()
           //Save the JSON string to the SharedPreferences instance:
           editor.putString("rule_list", json)
           editor.apply()
           Toast.makeText(this,"Stored successfully",Toast.LENGTH_SHORT).show()

           val intent = Intent(this, MainActivity::class.java)
           startActivity(intent)
       }


    }

}