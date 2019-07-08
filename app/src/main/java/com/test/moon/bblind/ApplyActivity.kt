package com.test.moon.bblind

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.support.constraint.solver.widgets.Snapshot
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_apply.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account.*
import java.lang.ArithmeticException
import java.lang.Exception
import kotlin.random.Random
import kotlin.random.nextInt
import com.google.firebase.database.DataSnapshot
import java.util.*
import kotlin.collections.ArrayList
import java.util.Date;


class ApplyActivity : AppCompatActivity()
{
    var Match = false
    private var mAuth: FirebaseAuth? = null
    private var user : FirebaseUser? = null

    var datee : String?=null
    var selectyear : Int? = null
    var selectmonth : Int? = null
    var selectday : Int? = null

    @SuppressLint("ObsoleteSdkInt")

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)




        var i =0
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser


        var sex : String? = null
        var id : String? = null
        var findtemp : ArrayList<IdData> = ArrayList<IdData>()
        var find : IdData? = null
        var delme : IdData? = null
        var iddata : IdData? = null
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()



        val ref : DatabaseReference = database.reference
        val Chatref : DatabaseReference = database.getReference("Chat")
        val Applyref : DatabaseReference = database.getReference("Apply")



        val keyboard : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var personspinadapter = ArrayAdapter.createFromResource(this,R.array.person,R.layout.spinner_item)
        var agespinadapter = ArrayAdapter.createFromResource(this,R.array.age,R.layout.spinner_item)
        var timespinadapter = ArrayAdapter.createFromResource(this,R.array.time,R.layout.spinner_item)
        var drinkspinadapter = ArrayAdapter.createFromResource(this,R.array.drink,R.layout.spinner_item)

        personspinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        agespinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        timespinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
         drinkspinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        Apply_Spinner_PersonNum.adapter = personspinadapter
        Apply_Spinner_AverageAge.adapter = agespinadapter
        Apply_Spinner_After_Time.adapter = timespinadapter
        Apply_Spinner_Before_Time.adapter = timespinadapter
        Apply_Spinner_Average_Drink.adapter = drinkspinadapter


      //  Apply_Toggle_Sex.

        Apply_button_Cancel.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?) {

                finish()

            }

        })


        Apply_button_Notice.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                val today = Date()
                var strdate : String? = null

                var format1 : SimpleDateFormat? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    format1 = SimpleDateFormat("yyyy-MM-dd")

                    strdate = format1.format(today)


                }

                var selectday  : Date? =null
                var selectdatstr : String? = null
                var format2 : SimpleDateFormat? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    format2 = SimpleDateFormat("yyyy-MM-dd")
                    if(datee==null)
                    {
                        Toast.makeText(this@ApplyActivity, "날짜는 다시 입력해주세요.",Toast.LENGTH_SHORT).show()

                    }
                    else {

                        selectday = format2.parse(datee)
                        selectdatstr = format2.format(selectday)
                    }

                }

                try {



                    if(Apply_Edittext_Introduction.text.toString().length!=5)
                    {
                        throw ArithmeticException("Five Introduction")
                    }
                    else if(Apply_Spinner_Before_Time.selectedItem.toString().toInt()+1 >Apply_Spinner_After_Time.selectedItem.toString().toInt())
                    {

                            throw ArrayStoreException("Can't use Time")

                    }
                    else if(selectdatstr!!.compareTo(strdate!!)<=0||selectdatstr.length<7)
                    {
                        throw NoSuchElementException("OverDate")
                    }
                    else if(Apply_Textview_Subway.text.toString().equals(""))
                    {
                        throw CloneNotSupportedException("Please set Venue")
                    }
                    else {

                         id = user!!.uid

                        if(Apply_Toggle_Sex.isChecked)
                        {
                            sex="Woman"
                        }
                        else
                        {
                            sex="Man"
                        }



                        iddata = IdData(id!!,sex!!,
                                Apply_Spinner_Before_Time.selectedItem.toString(),
                                Apply_Spinner_After_Time.selectedItem.toString(),
                                selectdatstr,
                                Apply_Spinner_AverageAge.selectedItem.toString(),
                                Apply_Spinner_Average_Drink.selectedItem.toString(),
                                Apply_Edittext_Introduction.text.toString())
                        Log.d("chattt","온다1")







                        //*********************************************여기서 나와 같은 조건의 상대성별 확인

                        MainActivity.checkapplylist!!.checklist!!.add(Apply_Textview_Subway.text.toString()+"/"+Apply_Spinner_PersonNum.selectedItem.toString())

                        ref.child("Account").child(id!!).child("Myapply").setValue(MainActivity.checkapplylist)



                          Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                  .child(Apply_Spinner_PersonNum.selectedItem.toString())
                                  .push().setValue(iddata)




                            Applyref.child("SubwayStation/" + Apply_Textview_Subway.text.toString() + "/" + Apply_Spinner_PersonNum.selectedItem.toString()).addChildEventListener(object : ChildEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                }

                                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                                }

                                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                                }

                                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                                    //메소드가 find의 개수만큼 실행된다.
                                    //ex) 3개 있으면 메소드가 3번 실행됨

                                    var deldata: DataSnapshot? = null
                                        Log.d("aaaaz","111")
                                    if (Match == false) {

                                        Log.d("aaaaz","222")
                                        find = p0.getValue(IdData::class.java)

                                        if (selectdatstr.equals(find!!.date)) {


                                            Log.d("aaaaz","333")
                                            if (!find!!.sex.equals(sex))//성이 다르면 add
                                            {


                                                Log.d("aaaaz","444")
                                                if (find!!.from.toString() > Apply_Spinner_After_Time.selectedItem.toString()
                                                        || find!!.to.toString() < Apply_Spinner_Before_Time.selectedItem.toString()) {
                                                    //시간조건이 겹치지 않음.
                                                } else {


                                                    Log.d("aaaaz","555")
                                                    //시간 조건이 겹침
                                                    Match = true
                                                    Log.d("chat", "온다2")

                                                    if (sex.equals("Man")) {


                                                        MainActivity.ChatRoomNum = id + find!!.name

                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ManId").setValue(id)
                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("WomanId").setValue(find!!.name)

                                                    } else {

                                                        MainActivity.ChatRoomNum = find!!.name + id

                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ManId").setValue(find!!.name)
                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("WomanId").setValue(id)

                                                    }

                                                    ref.child("Account").addListenerForSingleValueEvent(object : ValueEventListener {
                                                        override fun onCancelled(p0: DatabaseError) {

                                                        }

                                                        override fun onDataChange(p0: DataSnapshot) {
                                                            if(p0.child(id!!).child("ChatNum").getValue(ChatRoomData::class.java)==null)
                                                            {
                                                                MainActivity.crd = ChatRoomData()
                                                            }
                                                            else {
                                                                MainActivity.crd = p0.child(id!!).child("ChatNum").getValue(ChatRoomData::class.java)

                                                            }
                                                            if(p0.child(find!!.name!!).child("ChatNum").getValue(ChatRoomData::class.java)==null)
                                                            {
                                                                MainActivity.crdd = ChatRoomData()
                                                            }
                                                            else {
                                                                MainActivity.crdd = p0.child(find!!.name!!).child("ChatNum").getValue(ChatRoomData::class.java)
                                                            }
                                                            MainActivity.crd!!.ChatRoom.add(MainActivity.ChatRoomNum!!)
                                                           MainActivity.crd!!.Token.add(p0.child(find!!.name!!).child("fcmToken").getValue(true).toString())
                                                            //내꺼에 상대방꺼 저장

                                                            ref.child("Account").child(id!!).child("ChatNum").setValue(MainActivity.crd)

                                                            MainActivity.crdd!!.ChatRoom.add(MainActivity.ChatRoomNum!!)
                                                            MainActivity.crdd!!.Token.add(p0.child(id!!).child("fcmToken").getValue(true).toString())

                                                            ref.child("Account").child(find!!.name!!).child("ChatNum").setValue(MainActivity.crdd)





                                                            MainActivity.checkapplylistt = p0.child(find!!.name!!).child("Myapply").getValue(CheckApplyListData::class.java)!!


                                                            MainActivity.checkapplylistt!!.checklist!!.remove(Apply_Textview_Subway.text.toString()+"/"+Apply_Spinner_PersonNum.selectedItem.toString())

                                                            ref.child("Account").child(find!!.name!!).child("Myapply").setValue(MainActivity.checkapplylistt)





                                                            MainActivity.checkapplylist = p0.child(id!!).child("Myapply").getValue(CheckApplyListData::class.java)!!

                                                            MainActivity.checkapplylist!!.checklist!!.remove(Apply_Textview_Subway.text.toString()+"/"+Apply_Spinner_PersonNum.selectedItem.toString())

                                                            ref.child("Account").child(id!!).child("Myapply").setValue(MainActivity.checkapplylist)


                                                        }
                                                    })


                                                    var cd: ChatData? = ChatData()

                                                    cd!!.message = "언행이 바른자가 미인을 얻는다."
                                                    cd!!.userName = "Notice"
                                                    cd!!.time = System.currentTimeMillis()





                                                    if (selectdatstr.length > 7) {

                                                        val cr = ChatRoomListData()

                                                        cr.PersonNum = Apply_Spinner_PersonNum.selectedItem.toString()
                                                        cr.Subway = Apply_Textview_Subway.text.toString()
                                                        cr.MeetDate = selectdatstr
                                                        cr.ChatRoomNum = MainActivity.ChatRoomNum

                                                            Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ChatRoomList").setValue(cr)
                                                    }


                                                    Chatref.child(MainActivity.ChatRoomNum.toString()).child("message").push().setValue(cd)






                                                    Log.d("finddd", "매치는 트루 츠루")

                                                    val delquery: Query = Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                                            .child(Apply_Spinner_PersonNum.selectedItem.toString()).orderByChild("name"  ).equalTo(id)

                                                    delquery.addValueEventListener(object : ValueEventListener {
                                                        override fun onCancelled(p0: DatabaseError) {
                                                        }

                                                        override fun onDataChange(p0: DataSnapshot) {

                                                            for (appleSnapshot in p0.getChildren()) {
                                                                appleSnapshot.getRef().removeValue()
                                                            }

                                                        }
                                                    })

                                                    val delquery1: Query = Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                                            .child(Apply_Spinner_PersonNum.selectedItem.toString()).orderByChild("name").equalTo(find!!.name)

                                                    delquery1.addValueEventListener(object : ValueEventListener {
                                                        override fun onCancelled(p0: DatabaseError) {
                                                        }

                                                        override fun onDataChange(p0: DataSnapshot) {

                                                            for (appleSnapshot in p0.getChildren()) {
                                                                appleSnapshot.getRef().removeValue()
                                                            }

                                                        }
                                                    })







                                                    val it: Intent = Intent(this@ApplyActivity, LobbyActivity::class.java)
                                                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    startActivity(it);


                                                }


                                            }


                                        }

                                    }

                                }

                                override fun onChildRemoved(p0: DataSnapshot) {

                                }


                            })

                        }



                    val it : Intent = Intent(this@ApplyActivity,LobbyActivity::class.java)

                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(it);


                }

                catch(e : ArithmeticException)
                {

                        Toast.makeText(this@ApplyActivity, "5글자를 맞춰주세요.",Toast.LENGTH_SHORT).show()
                        Apply_Edittext_Introduction.setText("")
                        keyboard.showSoftInput(Apply_Edittext_Introduction,0)



                }
                catch(e : ArrayStoreException)
                {
                    Toast.makeText(this@ApplyActivity, "시간을 설정해주세요.",Toast.LENGTH_SHORT).show()


                } catch(e : CloneNotSupportedException)
                {
                    Toast.makeText(this@ApplyActivity, "지하철역을 설정해주세요.",Toast.LENGTH_SHORT).show()

                }
                catch(e : NoSuchElementException)
                {
                    Toast.makeText(this@ApplyActivity, "이미 지난 날짜입니다.",Toast.LENGTH_SHORT).show()

                }
                catch(e:Exception)
                {

                    Toast.makeText(this@ApplyActivity, "정보를 다시 확인해주세요.",Toast.LENGTH_SHORT).show()

                }



            }



        })









    }

    fun NoticeDate(view: View) {


            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                datee = year.toString() +"-"+ (month + 1).toString() +"-"+ dayOfMonth.toString()
                selectyear = Integer.parseInt(year.toString())
                selectmonth = Integer.parseInt((month + 1).toString())
                selectday = Integer.parseInt(dayOfMonth.toString())

                SampleBidYearText.setText(year.toString() + "년")
                SampleBidMonthText.setText((month + 1).toString() + "월")
                SampleBidDayText.setText(dayOfMonth.toString() + "일")
            }, 2019, 0, 1)


            dialog.show()
        }




    fun  OpenSubwayPopup(view: View) {

        var temp : String?  = null

        when(view.getId())
        {

            R.id.Apply_Button_Subway1 -> temp = "1"
            R.id.Apply_Button_Subway2 -> temp = "2"
            R.id.Apply_Button_Subway3 -> temp = "3"
            R.id.Apply_Button_Subway4 -> temp = "4"
            R.id.Apply_Button_Subway5 -> temp = "5"
            R.id.Apply_Button_Subway6 -> temp = "6"
            R.id.Apply_Button_Subway7 -> temp = "7"
            R.id.Apply_Button_Subway8 -> temp = "8"
            R.id.Apply_Button_Subway9 -> temp = "9"
            R.id.Apply_Button_Subwaygyungjung -> temp = "gyungjung"
            R.id.Apply_Button_Subwayincheon1 -> temp = "incheon1"
            R.id.Apply_Button_Subwayincheon2 -> temp = "incheon2"

        }


        val intent = Intent(this@ApplyActivity, SubwayPopup::class.java)

        intent.putExtra("Subway", temp)
        startActivityForResult(intent,0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0)
        {

            if (resultCode == RESULT_OK)
            {


                Apply_Textview_Subway.setText(data?.getStringExtra("result"))


            }

        }

    }



}