package com.test.moon.bblind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StoreAdapter(private var data: ArrayList<StoreListData>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val context = parent!!.context
        if(convertView == null)
        {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.storelist, parent, false)
        }
        var storedata = data.get(position)
        val name = convertView!!.findViewById<TextView>(R.id.name)
        val price = convertView!!.findViewById<TextView>(R.id.price)
        name.append(storedata.product_id)
        price.append(storedata.product_price)
        return convertView
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
    fun addItem( name: String, price: String) {
        val item = StoreListData()


        item.product_id = name
        item.product_price = price

        data.add(item)
    }
}