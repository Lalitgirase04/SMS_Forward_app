package com.example.sms_forwardapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val rules: ArrayList<ruleDetails>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private lateinit var mListner: onItemClickListner

    interface onItemClickListner{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listner: onItemClickListner){

        mListner=listner
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //Created object of LayoutInflator
        var inflater = LayoutInflater.from(parent.context)
        //Created view
        var view = inflater.inflate(R.layout.view,parent,false)
        //Pass view to viewholder
        return MyViewHolder(view, mListner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //For databinding in view , we created refernce of id in MyViewHolder class so we can direct access it

        holder.ruleTitle.text = rules[position].name
    //    holder.switchButton.isChecked = rules[position].switchbtn


        //   holder.switchButton.isChecked = rules[position].switchBtn


        // Update the switch button state
    //    holder.switchButton.isChecked = rule.switchBtn

        // Set the switch state
        holder.switchButton.isChecked = checkedPositions.contains(position)


        // Set the switch button listener
        holder.switchButton.setOnCheckedChangeListener { _, isChecked ->

          //  rules[position].switchbtn = isChecked

            if (isChecked) {
                checkedPositions.add(position)
            } else {
                checkedPositions.remove(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return rules.size
    }

        class MyViewHolder(itemView: View,listner: onItemClickListner): RecyclerView.ViewHolder(itemView){
        var ruleTitle = itemView.findViewById<TextView>(R.id.rulename)
        val switchButton: Switch = itemView.findViewById(R.id.switchbtn)


        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

}