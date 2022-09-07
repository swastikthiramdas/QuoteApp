package com.swastik.shayariapp.Adapter

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.swastik.shayariapp.AllQuoteActivity
import com.swastik.shayariapp.BuildConfig
import com.swastik.shayariapp.Model.QuoteModel
import com.swastik.shayariapp.R


class AllQuoteAdapter(val context: Context, val list: ArrayList<QuoteModel>) :
    RecyclerView.Adapter<AllQuoteAdapter.viewholder>() {
    class viewholder(view: View) : RecyclerView.ViewHolder(view) {

        val textview = view.findViewById<TextView>(R.id.quote_text)
        val layout = view.findViewById<LinearLayout>(R.id.layout_quote)
        val share = view.findViewById<FrameLayout>(R.id.ic_share_btn)
        val whatsapp = view.findViewById<FrameLayout>(R.id.ic_whatsapp_btn)
        val copy = view.findViewById<FrameLayout>(R.id.ic_copy_btn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val itemview =
            LayoutInflater.from(parent.context).inflate(R.layout.quote_view, parent, false)

        return viewholder(itemview)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val pos = list[position]

        if (position % 5 == 0) {
            holder.layout.setBackgroundResource(R.drawable.gradieant_1)
        } else if (position % 4 == 0) {
            holder.layout.setBackgroundResource(R.drawable.gradieant_2)
        } else if (position % 3 == 0) {
            holder.layout.setBackgroundResource(R.drawable.gradieant_3)
        } else if (position % 2 == 0) {
            holder.layout.setBackgroundResource(R.drawable.gradieant_4)
        } else if (position % 1 == 0) {
            holder.layout.setBackgroundResource(R.drawable.gradieant_5)
        }

        holder.share.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quotes")
                var shareMessage = "\n ${pos.data} \n\n"
                shareMessage =
                    """
                           ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}       
                           """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                context.startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }

        }


        holder.copy.setOnClickListener {

            val clipboard: ClipboardManager? =
                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", "${pos.data}")
            clipboard!!.setPrimaryClip(clip)

            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
        }

        holder.whatsapp.setOnClickListener {


            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, pos.data.toString())
            try {
                context.startActivity(whatsappIntent)
            } catch (ex: ActivityNotFoundException) {

            }

        }

        holder.textview.text = pos.data.toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }
}