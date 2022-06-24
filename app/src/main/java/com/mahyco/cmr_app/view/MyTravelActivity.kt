package com.mahyco.cmr_app.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.example.android.roomwordssample.WordViewModelFactory
import com.google.gson.Gson
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.cmr_app.view.travel.start_travel.AddEvenFragment
import com.mahyco.cmr_app.view.travel.start_travel.EndTravelFragment
import com.mahyco.cmr_app.view.travel.start_travel.StartTravelFragment
import com.mahyco.cmr_app.view.travel.start_travel.ViewTravelFragment
import com.mahyco.cmr_app.viewmodel.CMRDataViewModel
import com.mahyco.isp.core.MainApplication
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_travel.*
import kotlinx.android.synthetic.main.activity_my_travel.llProgressBar
import kotlinx.android.synthetic.main.activity_my_travel.textViewVersionName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyTravelActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    lateinit var context: Context
    lateinit var Imagepath1: String
    private val tabIcons = intArrayOf(
        R.drawable.start,
        R.drawable.addtravel,
        R.drawable.end
    )

    var cmrDataViewModel: CMRDataViewModel? = null
    var update = true
    private val fragment_List: MutableList<Fragment> = ArrayList()
    private val fragmentTitle: MutableList<String> = ArrayList()
    private var eventList: MutableList<Word> = ArrayList()
    private val tourList: MutableList<Word> = ArrayList()
    var msclass: Messageclass? = null

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory(
            (application as MainApplication).repository,
            application as MainApplication
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_travel)
        supportActionBar?.title = getString(R.string.my_travel)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context = this
        //  setUI()
        CoroutineScope(Main).launch  {
            llProgressBar.visibility = View.VISIBLE
            checkData()
        }

        tabs.setupWithViewPager(viewcontainer)
       // setupTabIcons()

        msclass = Messageclass(this)

        buttonUploadData.setOnClickListener {
            if (isNetworkConnected()) {
                syncData()
            } else {
                msclass?.showMessage("Internet not connected")
            }
        }
        registerObserver()

        try {
            val pInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            val version: String = pInfo.versionName
            textViewVersionName.text = "version : "+version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        if (!Constant.isTimeAutomatic(this)){
            msclass?.showAutomaticTimeMessage("Please update time setting to automatic")
        }
    }
    @SuppressLint("ServiceCast")
    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
    }

    private suspend fun checkData() {
        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())
     val list = wordViewModel.getCurrentTravel(currentDate) as MutableList<Word>
        for (item in list) {
            if (item.uType == "end") {
                msclass?.showMessage("Tour is completed for today")

                fragment_List.clear()
                fragmentTitle.clear()
                fragmentTitle.add("View Travel")
                fragment_List.add(ViewTravelFragment(wordViewModel))
                setupViewPager(viewcontainer)

                llProgressBar.visibility = View.GONE
                return
            }
        }

       /* wordViewModel.getCurrentDateTravelType(currentDate,"end")
            .observe(owner = this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let {
                Log.e("end", "onCreateView: " + it)
            }
                for (item in eventList) {
                    if (item.uType == "end") {
                        msclass?.showMessage("Tour is completed for today")

                        fragment_List.clear()
                        fragmentTitle.clear()
                        fragmentTitle.add("View Travel")
                        fragment_List.add(ViewTravelFragment(wordViewModel))
                        setupViewPager(viewcontainer)

                        llProgressBar.visibility = View.GONE
                        return@observe
                    }
                }

        }
*/
        eventList.clear()
       eventList = wordViewModel.getCurrentTravel(currentDate) as MutableList<Word>
        if (eventList != null && eventList.size != 0){

            for (item in eventList){
                if (item.uType == "end"){
//                    textTravelComplete.visibility = View.VISIBLE
                    msclass?.showMessage("Tour is completed for today")
                            fragment_List.clear()
                            fragmentTitle.clear()
                            fragmentTitle.add("View Travel")
                            fragment_List.add(ViewTravelFragment(wordViewModel))
                    setupViewPager(viewcontainer)

                    llProgressBar.visibility = View.GONE
                    return
                }else{
                    fragment_List.clear()
                    fragmentTitle.clear()
                    fragmentTitle.add("Add Event")
                    fragmentTitle.add("End Travel")

                    fragment_List.add(AddEvenFragment())
                    fragment_List.add(EndTravelFragment())
                    setupViewPager(viewcontainer)
                    llProgressBar.visibility = View.GONE
                 //   return
                }
            }

        }
        else{
            fragment_List.clear()
            fragmentTitle.clear()
            fragmentTitle.add("Start travel")


            fragment_List.add(StartTravelFragment())
            setupViewPager(viewcontainer)
            llProgressBar.visibility = View.GONE
        }


    }



    private fun syncData() {
        update = true
        tourList.clear()


      /*  wordViewModel.getTravelType("start")
            .observe(owner = this) { words ->
                // Update the cached copy of the words in the adapter.
                words.let {
                    Log.e("end", "onCreateView: " + it.size)
                }
                tourList.addAll(words)
                if (tourList.size != 0) {
                    if (update) {
                        cmrDataViewModel?.postTourEventDataAPI(tourList)
                    }
                    val sd = SimpleDateFormat("MM/dd/yyyy")
                    val currentDate = sd.format(Date())

                }
            }
*/
        GlobalScope.launch {
            val listTourEvent = wordViewModel.allWords()
            val gson = Gson()
            val json = gson.toJson(listTourEvent)
            Log.d("allData", "syncData: "+json)
        }
          wordViewModel.getNotTravelType("start")
            .observe(owner = this) { words ->
                // Update the cached copy of the words in the adapter.
                words.let {
                    Log.e("end", "onCreateView: " + it.size)
                }
                tourList.addAll(words)
                if (tourList.size != 0) {
                    if (update) {
                        cmrDataViewModel?.postTourEventDataAPI(tourList)
                    }
                    val sd = SimpleDateFormat("MM/dd/yyyy")
                    val currentDate = sd.format(Date())

                }
            }

        Log.d("MyTravels", "syncData() called tourists " + tourList.size)
        Log.d("MyTravels", "syncData() called events " + eventList.size)


    }

    private fun registerObserver() {
        cmrDataViewModel =
            ViewModelProviders.of(this)
                .get<CMRDataViewModel>(CMRDataViewModel::class.java)

        //        For handling Progress bar
        cmrDataViewModel!!.loadingLiveData.observe(this, Observer {
            if (it) {
                llProgressBar.visibility = View.VISIBLE
            } else {
                llProgressBar.visibility = View.GONE
            }
        })

//        In Case of error will show error in  toast message
        cmrDataViewModel!!.errorLiveData.observe(this, Observer {
            if (it != null) {
                showShortMessage(it)
                DLog.d("CMR errorLiveData :" + it)
            }
        })

        cmrDataViewModel!!.postTourEventsdata.observe(this, Observer {
            var result = it.toString()
            DLog.d("CMR DATA Activity : " + result)
            if (it.status == "Success") {
                update = false
                val events: MutableList<Word> = ArrayList()
                val tours: MutableList<Word> = ArrayList()

                val sd = SimpleDateFormat("MM/dd/yyyy")
                val currentDate = sd.format(Date())

/*
                for (item in tourList) {
                    if (item.uType == "add_event") {
                        events.add(item)
                    } else {
                        tours.add(item)
                    }
                }
*/
                lifecycleScope.launch {
                    val list = wordViewModel.allWords()

                    for (item in list) {
                        if (item.uDate != currentDate) {
                            wordViewModel.deleteWord(item)
                        } else {
                            item.uStatus = "1"
                            wordViewModel.update(item)
                            /*  if (events.size!= 0) {
                            for (event in events) {

                                for (tour in tours) {
                                    if (event.uDate == tour.uDate)
                                        item.uStatus = "1"
                                    wordViewModel.update(item)
                                }
                            }
                        }else{
                                item.uStatus = "1"
                            wordViewModel.update(item)
                        }*/
                        }
                    }
                }
                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setTitle("Crop Monitor Report")
                alertDialog.setMessage("Data uploaded successfully")
                alertDialog.setCancelable(false)
                alertDialog.setButton("OK") { dialog, which ->
                    // Write your code here to execute after dialog closed
                    //        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

                    finish()
                    startActivity(this?.getIntent())
                }


                // Showing Alert Message
                if (alertDialog.isShowing){
                    alertDialog.dismiss()
                }
                alertDialog.show()
            } else {
                if (!it.errorMessage.toString().equals("null")) {
                    msclass?.showMessage(it.errorMessage.toString())
                }else{
                    msclass?.showMessage("Something went wrong")
                }
            }

        })


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (llProgressBar.visibility == View.VISIBLE){
            return false
        }else{
            return super.onTouchEvent(event)
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupTabIcons() {
        try {
            tabs.getTabAt(0)?.setIcon(tabIcons[0])
            tabs.getTabAt(1)?.setIcon(tabIcons[1])
            tabs.getTabAt(2)?.setIcon(tabIcons[2])
        } catch (e: Exception) {
            Log.d("Msg", e.message!!)
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager, fragment_List, fragmentTitle)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
        adapter.notifyDataSetChanged()
    }

    internal class ViewPagerAdapter(
        manager: FragmentManager?,
        var fragment_List: MutableList<Fragment>,
        var fragmentTitle: MutableList<String>
    ) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return fragment_List[position]
        }

        override fun getCount(): Int {
            return fragment_List.size
        }


        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitle[position]
        }
    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        fragment_List.get(position).userVisibleHint = true
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

}