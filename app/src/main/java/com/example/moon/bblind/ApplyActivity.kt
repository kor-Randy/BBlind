package com.example.moon.bblind

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.support.constraint.solver.widgets.Snapshot
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_apply.*
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account.*
import java.lang.ArithmeticException
import java.lang.Exception
import kotlin.random.Random
import kotlin.random.nextInt
import com.google.firebase.database.DataSnapshot





class ApplyActivity : AppCompatActivity()
{

    var Match = false

    @SuppressLint("ObsoleteSdkInt")

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)
        var i =0

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



        val ApplyInforef : DatabaseReference = database.getReference("ApplyInformation")
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

        Apply_button_Match.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?) {


            }

        })


        Apply_button_Notice.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {



                try {



                    if(Apply_Edittext_Introduction.text.toString().length!=5)
                    {
                        throw ArithmeticException("Five Introduction")
                    }
                    else if(Apply_Spinner_Before_Time.selectedItem.toString().toInt()+1 >Apply_Spinner_After_Time.selectedItem.toString().toInt())
                    {

                            throw ArrayStoreException("Can't use Time")

                    }
                    else {

                         id = Random.nextInt(0,10000000).toString()

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
                                Apply_Spinner_After_Time.selectedItem.toString())
                        Log.d("chattt","온다1")
                        Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                .child(Apply_Spinner_PersonNum.selectedItem.toString())
                                .push().setValue(iddata)




                        ApplyInforef.child(id.toString()).child("Introduction").setValue(Apply_Edittext_Introduction.text.toString())
                        ApplyInforef.child(id.toString()).child("AverageAge").setValue(Apply_Spinner_AverageAge.selectedItem.toString())
                        ApplyInforef.child(id.toString()).child("AverageDrink").setValue(Apply_Spinner_Average_Drink.selectedItem.toString())


                        //*********************************************여기서 나와 같은 조건의 상대성별 확인









                            Applyref.child("SubwayStation/"+Apply_Textview_Subway.text.toString()+"/"+Apply_Spinner_PersonNum.selectedItem.toString()).addChildEventListener(object: ChildEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                        }

                                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                        }

                                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                                            }

                                        override fun onChildAdded(p0: DataSnapshot, p1: String?)
                                        {
                                            //메소드가 find의 개수만큼 실행된다.
                                            //ex) 3개 있으면 메소드가 3번 실행됨

                                            var deldata : DataSnapshot?=null

                                           if(Match==false)
                                           {

                                              find = p0.getValue(IdData::class.java)



                                               if(find!!.name!!.toInt().toString().equals(id))
                                               {


                                               }

                                                Log.d("finddd", find!!.name!!.toInt().toString() + " " + find!!.sex + " " + find!!.from + " " + find!!.to)

                                                if (!find!!.sex.equals(sex))//성이 다르면 add
                                                {


                                                    if (find!!.from.toString() > Apply_Spinner_After_Time.selectedItem.toString()
                                                            || find!!.to.toString() < Apply_Spinner_Before_Time.selectedItem.toString()) {
                                                        //시간조건이 겹치지 않음.
                                                    } else {
                                                        //시간 조건이 겹침
                                                        Match=true
                                                        Log.d("chat","온다2")

                                                        Chatref.child(id+find!!.name).child("Info").child("Id1").setValue(id)
                                                        Chatref.child(id+find!!.name).child("Info").child("Id2").setValue(find!!.name)

                                                        Chatref.child(id+find!!.name).child("Letter").push().setValue("언행이 바른자가 미인을 얻는다.")
                                                        Log.d("finddd","매치는 트루 츠루")

                                                        val delquery : Query =  Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                                                .child(Apply_Spinner_PersonNum.selectedItem.toString()).orderByChild("name").equalTo(id)

                                                        delquery.addListenerForSingleValueEvent(object: ValueEventListener {
                                                            override fun onCancelled(p0: DatabaseError) {
                                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                                            }

                                                            override fun onDataChange(p0: DataSnapshot) {

                                                                for (appleSnapshot in p0.getChildren()) {
                                                                    appleSnapshot.getRef().removeValue()
                                                                }

                                                            }
                                                        })

                                                        val delquery1 : Query =  Applyref.child("SubwayStation").child(Apply_Textview_Subway.text.toString())
                                                                .child(Apply_Spinner_PersonNum.selectedItem.toString()).orderByChild("name").equalTo(find!!.name)

                                                        delquery1.addListenerForSingleValueEvent(object: ValueEventListener {
                                                            override fun onCancelled(p0: DatabaseError) {
                                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                                            }

                                                            override fun onDataChange(p0: DataSnapshot) {

                                                                for (appleSnapshot in p0.getChildren()) {
                                                                    appleSnapshot.getRef().removeValue()
                                                                }

                                                            }
                                                        })






                                                    }


                                                }



                                            }



                                            }

                                        override fun onChildRemoved(p0: DataSnapshot) {
                                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                        }



                                    })
                        }




                }
                catch(e : ArrayStoreException)
                {
                    Toast.makeText(this@ApplyActivity, "시간을 설정해주세요.",Toast.LENGTH_SHORT).show()


                }
                catch(e : ArithmeticException)
                {

                        Toast.makeText(this@ApplyActivity, "5글자를 맞춰주세요.",Toast.LENGTH_SHORT).show()
                        Apply_Edittext_Introduction.setText("")
                        keyboard.showSoftInput(Apply_Edittext_Introduction,0)



                }
                catch(e:Exception)
                {

                    Toast.makeText(this@ApplyActivity, e.toString()+"예외상황 발생",Toast.LENGTH_SHORT).show()

                }

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