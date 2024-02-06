package com.example.sms_forwardapp

import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.SEND_SMS
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_update_rule.*
import java.lang.reflect.Type


var listOfrule: ArrayList<ruleDetails> = ArrayList()
lateinit var myAdapter : MyAdapter
val checkedPositions = HashSet<Int>()

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        add.setOnClickListener{

            val intent = Intent(this,Add_Rule::class.java)
            startActivity(intent)
        }

        recycler_view.adapter = MyAdapter(listOfrule)
        recycler_view.layoutManager = LinearLayoutManager(this)

        Loaddata()


     /*   //Creating objects of data class
        var rule1 = ruleDetails(1,"Finance",7385565681)
        var rule2 = ruleDetails(2,"Advertise",9858589878)
        var rule3 = ruleDetails(3,"Photography",7989797889)

        listOfrule.add(rule1)
        listOfrule.add(rule2)
        listOfrule.add(rule3) */

     //   val ruleName = arrayListOf<ruleDetails>()
     /*  ruleName.add(ruleDetails(1,"abc","Family",99999999))
        ruleName.add(ruleDetails(2,"bcd","Family",99999999))
        ruleName.add(ruleDetails(3,"efg","Family",99999999))
        ruleName.add(ruleDetails(4,"ghi","Family",99999999)) */

     /*   if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS),111)
        }
        else{
            receiveMsg()
        }
   */
    }

  /*  override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            receiveMsg()
        }
    }

    fun receiveMsg(){

        var br = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){

                   for(sms in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                      Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()


                    /*  val posi= intent.getIntExtra("position",-1)
                       val myObj = listOfrule[posi]
                       if(sms.messageBody.contains(myObj.pattern)){
                           Toast.makeText(this@MainActivity,"Pattern present",Toast.LENGTH_SHORT).show()
                       } */
                   }
                }
            }
        }

        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }


 */



    private val smsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (smsMessage in smsMessages) {
                    val messageBody = smsMessage.messageBody

                    for (position in checkedPositions) {
                        val ruleDetail = listOfrule[position]
                        if (checkPatternInMessageBody(messageBody, ruleDetail.pattern)) {
                            forwardSmsMessage(smsMessage, ruleDetail.Mobno)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(smsReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(smsReceiver)
    }

    private fun checkPatternInMessageBody(messageBody: String, pattern: String): Boolean {
        val regex = pattern.toRegex()
        return regex.containsMatchIn(messageBody)
    }

    private fun forwardSmsMessage(smsMessage: SmsMessage, mobileNumbers: String) {
        val mobileNumberList = mobileNumbers.split(",")
        // Forward the SMS message to the specified mobile numbers
        for (mobileNumber in mobileNumberList) {
            // Use your preferred method to send the SMS, such as using SmsManager or a third-party library
            // Example using SmsManager:
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(mobileNumber, null, smsMessage.messageBody, null, null)
        }
    }


    fun Loaddata(){

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        // creating a variable for gson.
        val gson = Gson()
        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = sharedPreferences.getString("rule_list", null)
        // below line is to get the type of our array list.
      //  val type: Type = object : TypeToken<ArrayList<ruleDetails?>?>() {}.type
        // in below line we are getting data from gson
        // and saving it to our array list
      //  listOfrule = gson.fromJson<Any>(json, type) as ArrayList<ruleDetails>
        val listOfrule: ArrayList<ruleDetails> = gson.fromJson(json, object : TypeToken<ArrayList<ruleDetails>>() {}.type) as? ArrayList<ruleDetails> ?: ArrayList()

        // checking below if the array list is empty or not
       /* if (listOfrule == null) {
            // if the array list is empty
            // creating a new array list.
            listOfrule = ArrayList()
        } */


        // on below line we are initializing our adapter
        // and setting it to recycler view.
     //   myAdapter = MyAdapter(listOfrule)
      //  recycler_view.adapter = myAdapter


        myAdapter = MyAdapter(listOfrule)
        recycler_view.adapter = myAdapter
        myAdapter.setOnItemClickListner(object :MyAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {

              // Toast.makeText(this@MainActivity,"You clicked on Item no.$position",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity,Update_Rule::class.java)
                intent.putExtra("rname", listOfrule[position].name)
                intent.putExtra("pattern", listOfrule[position].pattern)
                intent.putExtra("mobno", listOfrule[position].Mobno)
                intent.putExtra("POSITION", position)
                startActivity(intent)


            }

        })
    }


}