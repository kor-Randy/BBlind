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
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import java.util.Date;


class ApplyActivity : AppCompatActivity()
{
    private val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    private val SERVER_KEY = "AAAAghhryJU:APA91bE5FeyIHILMSGcWRgWY4hp43aQv9a5haGPMw2A5ZM0G6-102amS9gh-6YKLRRs4qAAKBE-dCBE7A1fnUjoEi3A6mZgrjVIGz-Y34x_yuOYk4fHSM_wT969p36N5oYgYobr3tyCq"

    var Match = false
    private var mAuth: FirebaseAuth? = null
    private var user : FirebaseUser? = null

    var datee : String?=null
    var selectyear : Int? = null
    var selectmonth : Int? = null
    var selectday : Int? = null
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    var OppositeToken : String? = null


    val ref : DatabaseReference = database.reference
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)


        MainActivity.applyactivity = this


        var i =0
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser


        var sex : String? = null
        var id : String? = null
        var findtemp : ArrayList<IdData> = ArrayList<IdData>()
        var find : IdData? = null
        var delme : IdData? = null
        var iddata : IdData? = null

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

                Log.d("aaaaz","Click Notice")


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
                        Toast.makeText(this@ApplyActivity, "날짜를 다시 입력해주세요.",Toast.LENGTH_SHORT).show()

                    }
                    else {

                        selectday = format2.parse(datee)
                        selectdatstr = format2.format(selectday)
                    }

                }

                Log.d("aaaaz","Try 전")
                try {

                    var s4 : Boolean? = null

                    if(MainActivity.checkapplylist!!.checklist!!.size>1)
                    {

                        Log.d("aaaaz","1111")
                        for ( i in 1..MainActivity.checkapplylist!!.checklist!!.size-1)
                        //1부터인 이유 = 0번째는 "초기화"
                        {
                            //이미지난 매칭정보가 존재할 경우


                            Log.d("aaaaz","2222")
                            var s1 = MainActivity.checkapplylist!!.checklist!![i].split("/")[0]
                            var s2 = MainActivity.checkapplylist!!.checklist!![i].split("/")[1]
                            var s3 = MainActivity.checkapplylist!!.checklist!![i].split("/")[2]

                            if(s3.equals(selectdatstr))
                            {

                                Log.d("aaaaz","3333")
                                s4 = true
                            }

                            Log.d("aaaaz","4444")

                        }
                    }


                    if(s4 == true)
                    {
                        throw NoSuchMethodException("Already Exist")
                    }
                    else if(Store.heart<10)
                    {
                        throw ReflectiveOperationException("Not Enough Heart")
                    }
                    else if(Apply_Edittext_Introduction.text.toString().length!=5)
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

                        Log.d("aaaaz","5555")

                        id = user!!.uid

                    sex = MainActivity.Mysex



                        iddata = IdData(id!!, sex!!,
                                Apply_Spinner_Before_Time.selectedItem.toString(),
                                Apply_Spinner_After_Time.selectedItem.toString(),
                                selectdatstr,
                                Apply_Spinner_AverageAge.selectedItem.toString(),
                                Apply_Spinner_Average_Drink.selectedItem.toString(),
                                Apply_Edittext_Introduction.text.toString())
                        Log.d("chattt", "온다1")






                            //*********************************************여기서 나와 같은 조건의 상대성별 확인
                            Log.d("aaaaz","one")
                            MainActivity.checkapplylist!!.checklist!!.add(Apply_Textview_Subway.text.toString() + "/" + Apply_Spinner_PersonNum.selectedItem.toString()+"/" + selectdatstr)

                            Log.d("aaaaz","two")
                            ref.child("Account").child(id!!).child("Myapply").setValue(MainActivity.checkapplylist)


                            Log.d("aaaaz","three")

                            Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                    .child(Apply_Spinner_PersonNum.selectedItem.toString())
                                    .push().setValue(iddata)


                            Log.d("aaaaz","four")


                            Applyref.child("SubwayStation/" + Apply_Textview_Subway.text.toString() + "/" + Apply_Spinner_PersonNum.selectedItem.toString()).limitToFirst(1).addChildEventListener(object : ChildEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                }

                                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                                }

                                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                                }

                                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                                    //메소드가 find의 개수만큼 실행된다.
                                    //ex) 3개 있으면 메소드가 3번 실행됨


                                    Log.d("aaaaz11", p0.getValue(IdData::class.java)!!.name.toString())

                                    var deldata: DataSnapshot? = null
                                    Log.d("aaaaz", "111")
                                    if (Match == false) {

                                        Log.d("aaaaz", "222")

                                        find = p0.getValue(IdData::class.java)

                                        Log.d("aaaaz", find!!.name!!.toString())
                                        if (iddata!!.date.equals(find!!.date)) {


                                            Log.d("aaaaz", "333")
                                            if (!find!!.sex.equals(sex))//성이 다르면 add
                                            {


                                                Log.d("aaaaz", "444")
                                                if ((find!!.from!!.toString() <= iddata!!.from!! && find!!.to!!.toString() >= iddata!!.from!!)
                                                        || (find!!.from!!.toString() <= iddata!!.to!! && find!!.to!!.toString() >= iddata!!.to!!)) {


                                                    //시간조건이 겹치지 않음.


                                                    Log.d("aaaaz", "555")
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
                                                            if (p0.child(id!!).child("ChatNum").getValue(ChatRoomData::class.java) == null) {
                                                                MainActivity.crd = ChatRoomData()
                                                            } else {
                                                                MainActivity.crd = p0.child(id!!).child("ChatNum").getValue(ChatRoomData::class.java)

                                                            }
                                                            if (p0.child(find!!.name!!).child("ChatNum").getValue(ChatRoomData::class.java) == null) {
                                                                MainActivity.crdd = ChatRoomData()
                                                            } else {
                                                                MainActivity.crdd = p0.child(find!!.name!!).child("ChatNum").getValue(ChatRoomData::class.java)
                                                            }

                                                            if(!MainActivity.crd!!.ChatRoom.contains(MainActivity.ChatRoomNum!!)) {

                                                                MainActivity.crd!!.ChatRoom.add(MainActivity.ChatRoomNum!!)
                                                                MainActivity.crd!!.Token.add(p0.child(find!!.name!!).child("fcmToken").getValue(true).toString())
                                                                OppositeToken=p0.child(find!!.name!!).child("fcmToken").getValue(true).toString();
                                                                //내꺼에 상대방꺼 저장
                                                                ref.child("Account").child(id!!).child("ChatNum").setValue(MainActivity.crd)

                                                                if(!MainActivity.crdd!!.ChatRoom.contains(MainActivity.ChatRoomNum!!)) {

                                                                    MainActivity.crdd!!.ChatRoom.add(MainActivity.ChatRoomNum!!)
                                                                    MainActivity.crdd!!.Token.add(p0.child(id!!).child("fcmToken").getValue(true).toString())
                                                                }
                                                                ref.child("Account").child(find!!.name!!).child("ChatNum").setValue(MainActivity.crdd)





                                                                MainActivity.checkapplylistt = p0.child(find!!.name!!).child("Myapply").getValue(CheckApplyListData::class.java)!!


                                                                MainActivity.checkapplylistt!!.checklist!!.remove(Apply_Textview_Subway.text.toString() + "/" + Apply_Spinner_PersonNum.selectedItem.toString()+"/" + selectdatstr)

                                                                ref.child("Account").child(find!!.name!!).child("Myapply").setValue(MainActivity.checkapplylistt)





                                                                MainActivity.checkapplylist = p0.child(iddata!!.name!!).child("Myapply").getValue(CheckApplyListData::class.java)!!

                                                                MainActivity.checkapplylist!!.checklist!!.remove(Apply_Textview_Subway.text.toString() + "/" + Apply_Spinner_PersonNum.selectedItem.toString()+"/" + selectdatstr)

                                                                ref.child("Account").child(iddata!!.name!!).child("Myapply").setValue(MainActivity.checkapplylist)

                                                                var cd: ChatData? = ChatData()

                                                                cd!!.message = "언행이 바른자가 미인을 얻는다."
                                                                cd!!.userName = "Notice"
                                                                cd!!.time = System.currentTimeMillis()




                                                                if (selectdatstr.length > 7) {

                                                                    val cr = ChatRoomListData(Apply_Textview_Subway.text.toString(),Apply_Spinner_PersonNum.selectedItem.toString(),selectdatstr,MainActivity.ChatRoomNum.toString(),"언행이 바른자가 미인을 얻는다.")



                                                                    Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ChatRoomList").setValue(cr)
                                                                }


                                                                Chatref.child(MainActivity.ChatRoomNum.toString()).child("message").push().setValue(cd)




                                                                sendPostToFCM()

                                                                val delquery: Query = Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                                                        .child(Apply_Spinner_PersonNum.selectedItem.toString()).orderByChild("name").equalTo(id)

                                                                delquery.addListenerForSingleValueEvent(object : ValueEventListener {
                                                                    override fun onCancelled(p0: DatabaseError) {
                                                                    }

                                                                    override fun onDataChange(p0: DataSnapshot) {

                                                                        for (appleSnapshot in p0.getChildren()) {

                                                                            if(appleSnapshot.child("date").getValue(true).toString().equals(selectdatstr!!)) {

                                                                                appleSnapshot.getRef().removeValue()
                                                                                Log.d("aaaaz", "삭제")


                                                                            }
                                                                        }

                                                                    }
                                                                })

                                                                val delquery1: Query = Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                                                        .child(Apply_Spinner_PersonNum.selectedItem.toString()).orderByChild("name").equalTo(find!!.name)

                                                                delquery1.addListenerForSingleValueEvent(object : ValueEventListener {
                                                                    override fun onCancelled(p0: DatabaseError) {
                                                                    }

                                                                    override fun onDataChange(p0: DataSnapshot) {


                                                                        for (appleSnapshot in p0.getChildren()) {

                                                                            if(appleSnapshot.child("date").getValue(true).toString().equals(selectdatstr!!)) {

                                                                                appleSnapshot.getRef().removeValue()
                                                                                Log.d("aaaaz", "삭제")

                                                                            }
                                                                        }

                                                                    }
                                                                })



                                                                var findheart : Int = Integer.parseInt(p0.child(find!!.name!!).child("heart").getValue(true).toString())
                                                                var myheart : Int = Integer.parseInt(p0.child(iddata!!.name!!).child("heart").getValue(true).toString())

                                                                findheart -= 10
                                                                myheart -= 10
                                                                Log.d("hearttt","내꺼 : "+myheart.toString())
                                                                Log.d("hearttt","상대꺼 : "+findheart.toString())

                                                                database.getReference("Account").child(find!!.name!!).child("heart").setValue(findheart)

                                                                database.getReference("Account").child(iddata!!.name!!).child("heart").setValue(myheart)



                                                                Log.d("finddd", "매치는 트루 츠루")






                                                            }
                                                            else
                                                            {
                                                                Log.d("hearttt","이미 같은사람과 매칭이 존재합니다.")
                                                            }
                                                            iddata = null
                                                            find=null

                                                        }
                                                    })





                                                    Log.d("aaaaz", "인텐트로비")
                                                    val it: Intent = Intent(this@ApplyActivity, LobbyActivity::class.java)
                                                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                                    startActivity(it);


                                                } else {

                                                }


                                            }


                                        }

                                    }


                                }

                                override fun onChildRemoved(p0: DataSnapshot) {

                                }


                            })




                        val it: Intent = Intent(this@ApplyActivity, LobbyActivity::class.java)

                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                       startActivity(it);


                    }

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
                catch(e : NoSuchMethodException)
                {
                    Toast.makeText(this@ApplyActivity,"이미 선택된 날짜로 매칭정보가 이미 존재합니다.",Toast.LENGTH_LONG).show()
                }
                catch (e:ReflectiveOperationException)
                {
                    Toast.makeText(this@ApplyActivity, "하트 개수가 부족합니다.",Toast.LENGTH_SHORT).show()

                }
                catch(e:Exception)
                {
                    Log.d("aaaaz",e.toString())
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

            SampleBidYearText.setText(year.toString() + "."+(month + 1).toString() + "."+dayOfMonth.toString())
        }, 2019, 0, 1)


        dialog.show()
    }


    private fun sendPostToFCM() {
        ref.child("Account")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        Log.d("checkkk", MainActivity.crd!!.Token.toString())

                        Thread(object : Runnable {

                            override fun run() {
                                try {

                                    // FMC 메시지 생성 start
                                    val root = JSONObject()
                                    val notification = JSONObject()
                                    notification.put("body", "등록한 매칭이 성사되었습니다.")
                                    notification.put("title", getString(R.string.app_name))
                                    root.put("notification", notification)
                                    root.put("to", OppositeToken)   // FMC 메시지 생성 end

                                    val Url = URL(FCM_MESSAGE_URL)
                                    val conn = Url.openConnection() as HttpURLConnection
                                    conn.requestMethod = "POST"
                                    conn.doOutput = true
                                    conn.doInput = true
                                    conn.addRequestProperty("Authorization", "key=$SERVER_KEY")
                                    conn.setRequestProperty("Accept", "application/json")
                                    conn.setRequestProperty("Content-type", "application/json")
                                    val os = conn.outputStream
                                    os.write(root.toString().toByteArray(charset("utf-8")))
                                    os.flush()
                                    conn.responseCode


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        }).start()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
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