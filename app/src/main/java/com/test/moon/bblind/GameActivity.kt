package com.test.moon.bblind

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.test.moon.bblind.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kakao.usermgmt.LoginButton
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity()
{

    internal val tabIcons = intArrayOf(R.drawable.homed,R.drawable.chatd, R.drawable.heartd)
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)



        setupViewPager(Game_Viewpager)

       Game_tabs.setupWithViewPager(Game_Viewpager)
        setupTabIcons()


    }

    private fun setupTabIcons() {
        for(i in 0..2) {
            val view1 = layoutInflater.inflate(R.layout.customtab, null) as View
            view1.findViewById<ImageView>(R.id.icon).setBackgroundResource(tabIcons[i])
           Game_tabs!!.getTabAt(i)!!.setCustomView(view1)
        }

    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(Home(), "초반/어색")
        adapter.addFrag(ChatRoom(), "중반/무르익을때")
        adapter.addFrag(Store(), "퐈이어")
        //adapter.addFrag(heart(), "HEART")
        Game_Viewpager!!.adapter = adapter
        Game_Viewpager.offscreenPageLimit= 7         // 한번에 5개의 ViewPager를 띄우겠다 -> 성능향상
    }    //ADAPT FRAGMENT

    ///////////////////////////////////// Adapter ///////////////////////////////////////////////////////////////
    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()   //MAKE FRAGMENT LIST
        private val mFragmentTitleList = ArrayList<String>()  //MAKE FRAGMENT TITLE LIST

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]       // RETURN THE ITEM OBJECT(mFragmentList)
        }

        override fun getCount(): Int {
            return mFragmentList.size       //FRAGMENT SIZE
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }  //ADD FRAGMENT

        override fun getPageTitle(position: Int): CharSequence? {
            // return null to display only the icon
            return null
        }
    }

}

