package com.test.moon.bblind

import android.app.ProgressDialog
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
import kotlin.math.log


class Store : Fragment(),BillingProcessor.IBillingHandler{


    lateinit var bp : BillingProcessor
    private lateinit var list: ListView
    private lateinit var btn : Button
    private val storedata = ArrayList<StoreListData>()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val myRef : DatabaseReference = database.getReference("Account/"+currentUser!!.uid+"/heart")
    lateinit var pdid : String
    lateinit var  asyncDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asyncDialog = ProgressDialog(
                activity)
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)



        var fragmentManager = fragmentManager!!.fragments
        bp = BillingProcessor(activity, resources.getString(R.string.license), this)
        bp.initialize()
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var value : String = p0.value.toString()
                heart = Integer.parseInt(value)
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

                0 ->{
                    pdid = "heart50"
                    bp.purchase(activity,"heart50")
                    asyncDialog.show()

                }
                1 ->{
                    pdid = "heart100"
                    bp.purchase(activity,"heart100")
                    asyncDialog.show()

                }
            }

        }
        return view
    }

    override fun onPurchaseHistoryRestored() {
        Toast.makeText(context,"Restored",Toast.LENGTH_LONG).show()
    }
    override fun onBillingInitialized() {


    }
    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        when(productId){
            "heart50" -> heart += 50
            "heart100" -> heart += 100
        }
        myRef.setValue(heart)
        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show()

        bp.consumePurchase(productId)
        asyncDialog.dismiss()
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
        asyncDialog.dismiss()
        Log.d("Bill","Bill  "+pdid)
        if(errorCode == 7)//이미 소유해서 구매불가
        {

            Log.d("Bill","Bill  "+pdid)
            Toast.makeText(context,pdid,Toast.LENGTH_SHORT).show()
            bp.consumePurchase(pdid)
        }
        else if(errorCode == 8)
        {
            Log.d("Bill","Bill  "+errorCode+pdid)
            Toast.makeText(context,errorCode.toString()+pdid,Toast.LENGTH_SHORT).show()
        }
        //Toast.makeText(context,errorCode.toString() +"   "+error.toString(),Toast.LENGTH_LONG).show()

    }


    override fun onDestroy() {
        if(bp != null)
        {
            bp.release()
        }
        super.onDestroy()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(!bp.handleActivityResult(requestCode,resultCode,data))
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    companion object {
        var heart : Int = 0
    }
}