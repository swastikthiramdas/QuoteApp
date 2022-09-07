package com.swastik.shayariapp.Adapter


import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.swastik.shayariapp.AllQuoteActivity
import com.swastik.shayariapp.MainActivity
import com.swastik.shayariapp.Model.CatModel
import com.swastik.shayariapp.R

class CatQuotesAdapter(val requaredcontext: MainActivity, val list: ArrayList<CatModel>) : RecyclerView.Adapter<CatQuotesAdapter.viewholder>() {

    private var colorlist = arrayListOf<String>("#7FBCD2","#94B49F","#F4E06D","#00FFAB","#AD8B73")

    class viewholder(view: View) : RecyclerView.ViewHolder(view) {


        val textview = view.findViewById<TextView>(R.id.card_textview)
        val card = view.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val itemview = LayoutInflater.from(requaredcontext).inflate(R.layout.item_view,parent,false)

        return viewholder(itemview)
    }


    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val pos = list[position]

        if (position % 5 ==0)
        holder.textview.setBackgroundColor(Color.parseColor(colorlist[0]))

        else if (position % 5 ==1)
            holder.textview.setBackgroundColor(Color.parseColor(colorlist[1]))

        else if (position % 5 ==2)
            holder.textview.setBackgroundColor(Color.parseColor(colorlist[2]))

        else if (position % 5 ==3)
            holder.textview.setBackgroundColor(Color.parseColor(colorlist[3]))

       else if (position % 5 ==4)
            holder.textview.setBackgroundColor(Color.parseColor(colorlist[4]))


        holder.textview.text = pos.name.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(requaredcontext,AllQuoteActivity::class.java)
            intent.putExtra("id",pos.id)
            intent.putExtra("name",pos.name)
            requaredcontext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}