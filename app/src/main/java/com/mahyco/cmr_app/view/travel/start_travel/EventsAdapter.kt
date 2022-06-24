package com.mahyco.cmr_app.view.travel.start_travel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.android.roomwordssample.Word
import com.mahyco.cmr_app.R
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import java.text.SimpleDateFormat

class EventsAdapter(val context:Context,val items: List<Word> ) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.items
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
       val event = items.get(listPosition)
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.textViewDate)
        val expandedListTextViewEventDateTime = convertView!!.findViewById<TextView>(R.id.textViewEventDateTime)
        val expandedListTextViewEventDiscription = convertView!!.findViewById<TextView>(R.id.textViewDescription)
        val expandedListTextViewEventStatus = convertView!!.findViewById<TextView>(R.id.textViewStatus)
        val expandedListimageViewEvent = convertView!!.findViewById<ImageView>(R.id.imageViewEvent)
        val expandedListlayoutImage = convertView!!.findViewById<LinearLayout>(R.id.layoutImage)

        var spfdate = SimpleDateFormat("MM/dd/yyyy")
        val uDate = spfdate.parse(event.uDate)
        spfdate = SimpleDateFormat("dd/MM/yyyy")
        val u_date = spfdate.format(uDate)
        expandedListTextView.text = u_date

        if (event.uStatus == "1"){
            expandedListTextViewEventStatus.text = "Uploaded"
        }else{
            expandedListTextViewEventStatus.text = "Not-Uploaded"
        }

        var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val newDate = spf.parse(event.uEventDateTime)
        spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
        val date = spf.format(newDate);
        expandedListTextViewEventDateTime.text = date
        expandedListTextViewEventDiscription.text = event.uEventDescription

        if (event.uKmImageEvent != null && event.uKmImageEvent != ""){
            val decodedStringEnd: ByteArray = Base64.decode(event.uKmImageEvent, Base64.DEFAULT)
            val decodedByteEnd: Bitmap =
                BitmapFactory.decodeByteArray(decodedStringEnd, 0, decodedStringEnd.size)
            expandedListimageViewEvent.setImageBitmap(decodedByteEnd)
            expandedListimageViewEvent.visibility = View.VISIBLE
            expandedListlayoutImage.visibility = View.VISIBLE
        }else{
            expandedListimageViewEvent.visibility = View.GONE
            expandedListlayoutImage.visibility = View.GONE
        }



        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return 1
    }
    override fun getGroup(listPosition: Int): Any {
        return this.items[listPosition]
    }
    override fun getGroupCount(): Int {
        return this.items.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
    //    val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item_title, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.textViewTitle)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = items.get(listPosition).uEventType
        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}