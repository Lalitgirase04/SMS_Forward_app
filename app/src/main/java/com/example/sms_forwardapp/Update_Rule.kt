package com.example.sms_forwardapp

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_rule.*
import kotlinx.android.synthetic.main.activity_update_rule.*

class Update_Rule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_rule)

        val bundle : Bundle?= intent.extras
        val rname = intent.getStringExtra("rname")
        val pattern = intent.getStringExtra("pattern")
        val mob = intent.getStringExtra("mobno")

      /*  val rname = bundle!!.getString("rname")
        val pattern = bundle.getString("pattern")
        val mob = bundle.getString("mobno") */
        val position = intent.getIntExtra("POSITION", -1)

    /*    val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("position",position)
        startActivity(intent) */


        runame.setText(rname)
        pattn.setText(pattern)
        mobi.setText(mob)

        update.setOnClickListener{

            val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("rule_list", "")
            val listType = object : TypeToken<ArrayList<ruleDetails>>() {}.type
            listOfrule = gson.fromJson<ArrayList<ruleDetails>>(json, listType)

            val myObject = listOfrule[position]
            myObject.name=runame.text.toString()
            myObject.pattern=pattn.text.toString()
            myObject.Mobno=mobi.text.toString()

            val updatedJson = gson.toJson(listOfrule)
            val editor = sharedPreferences.edit()
            editor.putString("rule_list", updatedJson)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        delete.setOnClickListener{

            //Get a reference to the SharedPreferences instance:
            val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val itemListJson = sharedPreferences.getString("rule_list", null)
            listOfrule = gson.fromJson(itemListJson, object : TypeToken<ArrayList<ruleDetails>>() {}.type) ?: ArrayList()
            listOfrule.removeAt(position)
            val updatedItemListJson = gson.toJson(listOfrule)
            sharedPreferences.edit().putString("rule_list", updatedItemListJson).apply()


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}