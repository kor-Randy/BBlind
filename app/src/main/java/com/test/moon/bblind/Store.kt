package com.test.moon.bblind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import android.content.DialogInterface
import android.os.Debug
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Store : Fragment(),BillingProcessor.IBillingHandler{


    lateinit var bp : BillingProcessor
    private lateinit var list: ListView
    private lateinit var btn : Button
    private val storedata = ArrayList<StoreListData>()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val myRef : DatabaseReference = database.getReference("Account/"+currentUser!!.uid+"/heart")
    var heart : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var fragmentManager = fragmentManager!!.fragments
        bp = BillingProcessor(activity, resources.getString(R.string.license), this)
        bp.initialize()
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.e("value","error : " + p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var value : String = p0.value.toString()
                heart = Integer.parseInt(value)
                Log.d("value","heart : "+heart)
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.store, container, false) as View
        list = view.findViewById(R.id.list)

        val adapter : StoreAdapter = StoreAdapter(storedata)
        list.adapter = adapter
        adapter.addItem("하트50","5000원")
        adapter.addItem("하트100","8000원")

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when(position){
                0 ->bp.purchase(activity,"heart50")
                1 ->bp.purchase(activity,"heart100")
            }

        }
        return view
    }

    override fun onPurchaseHistoryRestored() {
        Toast.makeText(context,"Restored",Toast.LENGTH_LONG).show()
    }
    override fun onBillingInitialized() {
        Toast.makeText(context,"Initialized",Toast.LENGTH_LONG).show()

    }
    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        when(productId){
            "heart50" -> heart += 50
            "heart100" -> heart += 100
        }
        myRef.setValue(heart)
        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show()
        bp.consumePurchase(productId)
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("구매 성공 하였습니다.")
                .setCancelable(false)
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, id ->
                    // to do action
                })
        val alert = builder.create()
        alert.show()
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(context,errorCode.toString() +"   "+error.toString(),Toast.LENGTH_LONG).show()

    }


    override fun onDestroy() {
        if(bp != null)
        {
            bp.release()
        }
        super.onDestroy()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Activity","InFragment")
        if(!bp.handleActivityResult(requestCode,resultCode,data))
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}