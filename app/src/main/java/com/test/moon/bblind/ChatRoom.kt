package com.test.moon.bblind

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*

class ChatRoom : Fragment() {
    private lateinit var list: ListView
    var cd : ChatRoomListData? = null
    private var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref : DatabaseReference = mFirebaseDatabase.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.chat, container, false) as View
        var al = ArrayList<ChatRoomListData>()
        list = view.findViewById(R.id.list)


        ref.child("Chat").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (i in 0..MainActivity.crd!!.ChatRoom.size - 1) {

                    cd = p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").getValue(ChatRoomListData::class.java) as ChatRoomListData
                    Log.d("aaaaz", p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").getValue(ChatRoomListData::class.java).toString())

                    Log.d("aaaaz", cd!!.MeetDate + cd!!.PersonNum + cd!!.Subway + cd!!.ChatRoomNum)

                    al.add(cd!!)



                var adap = ChatRoomAdapter(al)

                adap.notifyDataSetChanged()

                list.adapter = adap
                setListViewHeightBasedOnChildren(list)


                }

            }
        })


        for (i in 0..MainActivity.crd!!.ChatRoom.size - 1) {

            Log.d("aaaaz", MainActivity.crd!!.ChatRoom.size.toString())
       ref.child("Chat").child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?)
            {


                for(a : DataSnapshot in p0.children) {
                    cd = a.getValue(ChatRoomListData::class.java) as ChatRoomListData
                    Log.d("aaaaz", a.getValue(ChatRoomListData::class.java).toString())

                    Log.d("aaaaz", cd!!.MeetDate + cd!!.PersonNum + cd!!.Subway + cd!!.ChatRoomNum)

                    al.add(cd!!)

                }

                var adap = ChatRoomAdapter(al)

                adap.notifyDataSetChanged()

                list.adapter = adap
                setListViewHeightBasedOnChildren(list)

                }

            override fun onChildRemoved(p0: DataSnapshot) {
                for(a : DataSnapshot in p0.children) {
                    var i=0
                    cd = a.getValue(ChatRoomListData::class.java) as ChatRoomListData


                    al.removeAt(i)
                    Log.d("rrrrm",cd.toString())
                    Log.d("rrrrm",al.size.toString())
                    i++

                }

                var adap = ChatRoomAdapter(al)

                adap.notifyDataSetChanged()

                list.adapter = adap
                setListViewHeightBasedOnChildren(list)


            }
            })


        }





        list.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                ref.child("Chat").addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot)
                    {

                        if(p0.child(al[position].ChatRoomNum!!).exists()) {


                            MainActivity.nowChatRoomNum = al[position].ChatRoomNum
                            val num = MainActivity.crd!!.ChatRoom.indexOf(MainActivity.nowChatRoomNum)
                            MainActivity.nowToken = MainActivity.crd!!.Token[num]
                            Log.d("dedede","position = "+position)
                            Log.d("dedede","ChatRoomNum = "+MainActivity.nowChatRoomNum)
                            Log.d("dedede","Token = "+MainActivity.nowToken)

                            val i = Intent(activity, Chat::class.java)
                            //i.putExtra("token",)
                            startActivity(i)
                        }
                        else
                        {
                            Toast.makeText(context, "이미 삭제된 채팅방입니다.",Toast.LENGTH_LONG).show()
                            al.removeAt(position)
                            var adap = ChatRoomAdapter(al)

                            adap.notifyDataSetChanged()

                            list.adapter = adap
                            setListViewHeightBasedOnChildren(list)
                        }

                    }
                })



            }
        })

        return view
    }

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
                ?: // pre-condition
                return

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

}