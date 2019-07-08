package com.test.moon.bblind
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.message.template.FeedTemplate.newBuilder
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.util.helper.log.Logger

class LobbyActivity: AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private val backpress : BackPress = BackPress(this)
    private lateinit var adapter : ViewPagerAdapter
    internal val tabIcons = intArrayOf(R.drawable.homed,R.drawable.chatd, R.drawable.heartd)
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)


        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)
        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)
        setupTabIcons()
        var mBtnSignOut  : Button = findViewById(R.id.btn_google_signout)
        Toast.makeText(this,"Hi",Toast.LENGTH_LONG).show()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
            Toast.makeText(this,currentUser.uid,Toast.LENGTH_LONG).show()
            //var intent = Intent(this, Loading::class.java)
            //startActivity(intent)

        }else{Toast.makeText(this,"null",Toast.LENGTH_LONG).show()}

        mBtnSignOut.setOnClickListener {

            var params = TextTemplate.newBuilder("안뇽",
            LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()).setButtonTitle("수상한 친구 만들러가기").build()

           var serverCallbackArgs  = HashMap<String, String>();
           var aa : Map<Any,Any> = HashMap<Any,Any>()

            serverCallbackArgs.put("user_id", MainActivity.Myuid!!);

            var aaa  = object : ResponseCallback<KakaoLinkResponse>(){
                override fun onSuccess(result: KakaoLinkResponse?) {

                    Log.d("ststst","성공")

                }

                override fun onFailure(errorResult: ErrorResult?) {

                }

            }

            KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs,aaa)




            /* UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                 override fun onCompleteLogout() {
                     FirebaseAuth.getInstance().signOut()

                     //val handler = Handler(Looper.getMainLooper())
                     //handler.post { updateUI() }
                 }
             })*/
        }

    }

    override fun onBackPressed() {
        val activity = MainActivity.activity as MainActivity
        activity.finish()


        backpress.onBackPressed()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        val eid = p0.itemId


        if (eid == R.id.nav_AppInformation) {
            Toast.makeText(this,"앱정보",Toast.LENGTH_LONG).show()
            // Handle the camera action
        } else if (eid == R.id.nav_Chat) {
            Toast.makeText(this,"채팅문의",Toast.LENGTH_LONG).show()

        } else if (eid == R.id.nav_Information) {
            Toast.makeText(this,"이용안내",Toast.LENGTH_LONG).show()

        } else if (eid == R.id.Nav_My_Matching) {
            Toast.makeText(this,"내 매칭정보",Toast.LENGTH_LONG).show()

        } else if (eid == R.id.nav_QnA) {
            Toast.makeText(this,"자주묻는질문",Toast.LENGTH_LONG).show()

        } else if (eid == R.id.Nav_Recommend) {
            Toast.makeText(this,"추천인코드적기",Toast.LENGTH_LONG).show()
        }


        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupTabIcons() {
        for(i in 0..2) {
            val view1 = layoutInflater.inflate(R.layout.customtab, null) as View
            view1.findViewById<ImageView>(R.id.icon).setBackgroundResource(tabIcons[i])
            tabLayout!!.getTabAt(i)!!.setCustomView(view1)
        }

    }

    private fun setupViewPager(viewPager: ViewPager?) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(Home(), "HOME")
        adapter.addFrag(ChatRoom(), "CHAT")
        adapter.addFrag(Store(), "STORE")
        //adapter.addFrag(heart(), "HEART")
        viewPager!!.adapter = adapter
        viewPager.offscreenPageLimit= 7         // 한번에 5개의 ViewPager를 띄우겠다 -> 성능향상
    }    //ADAPT FRAGMENT

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Activity","InActivity")
        super.onActivityResult(requestCode, resultCode, data)
        val fragmentManager = supportFragmentManager
        val fragment = adapter.getItem(2)
        if (fragment != null) {
            Log.d("Activity","toFragment")
            (fragment as Store).onActivityResult(requestCode, resultCode, data)
        }else{Log.d("Activity","NULL")}

    }

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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
}