package com.mahyco.cmr_app.view

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.leegality.DisplayFields
import com.mahyco.cmr_app.model.getProductListResponse.ProductRecord
import com.mahyco.cmr_app.model.getcenterlistresponse.RecordCenter
import com.mahyco.cmr_app.utils.searchspinner.GeneralMaster
import com.mahyco.cmr_app.viewmodel.DigitalContractVM
import com.mahyco.isp.core.MainApplication
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import kotlinx.android.synthetic.main.activity_d_c_vendor_form.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class DCVendorFormActivity : BaseActivity(), BaseActivity.AlertCallback {

    var digitalContractVM: DigitalContractVM? = null
    lateinit var context: Context
    lateinit var season: String
    lateinit var center: String
    lateinit var vendorDt: String
    lateinit var product: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d_c_vendor_form)
        supportActionBar?.title = getString(R.string.app_name_long)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        registerObserver()
        context = this
        setUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUI() {

//        GET SEASON LIST CALLED
        if (isNetworkAvailable(this@DCVendorFormActivity)) {
            digitalContractVM!!.getSeasonListVM()
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }

//        GET CENTER LIST CALLED
        if (isNetworkAvailable(this@DCVendorFormActivity)) {
            digitalContractVM!!.getCenterList("97180401")
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }


        sp_season.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {

                if (position != 0) {
                    val gm = parent.selectedItem as GeneralMaster
                    try {
                        val seasonCode = URLEncoder.encode(gm.Code().trim(), "UTF-8")
                        season = gm.Desc()
                        //showShortMessage("Selected season : $seasonCode"+" code:"+gm.Code()+" Desc:"+gm.Desc())
                    } catch (e: UnsupportedEncodingException) {
                        Log.d("Msg", e.message!!)
                    }
                }
                //BindDist(state);
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        sp_center.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {

                if (position != 0) {
                    val gm = parent.selectedItem as GeneralMaster
                    try {
                        val centerCode = URLEncoder.encode(gm.Code().trim(), "UTF-8")
                        //showShortMessage("Selected center : $centerCode"+" code:"+gm.Code()+" Desc:"+gm.Desc())
                        center = gm.Code()
                        if (sp_season.selectedItemPosition != 0 && sp_center.selectedItemPosition != 0) {
                            getVendorList(gm.Code())
                        } else {
                            showShortMessage("Please select season and center")
                        }
                    } catch (e: UnsupportedEncodingException) {
                        Log.d("Msg", e.message!!)
                    }
                }
                //BindDist(state);
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        sp_vendor_name.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {

                if (position != 0) {
                    val gm = parent.selectedItem as GeneralMaster
                    try {
                        val vendorCode = URLEncoder.encode(gm.Code().trim(), "UTF-8")
                        //showShortMessage("Selected vendor : $vendorCode"+" code:"+gm.Code()+" Desc:"+gm.Desc())
                        vendorDt = gm.Code();
                        if (sp_season.selectedItemPosition != 0 && sp_center.selectedItemPosition != 0 && sp_vendor_name.selectedItemPosition != 0) {
                            getProductList()
                        } else {
                            showShortMessage("Please select all fields")
                        }

                    } catch (e: UnsupportedEncodingException) {
                        Log.d("Msg", e.message!!)
                    }
                }
                //BindDist(state);
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        sp_product_name.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {

                if (position != 0) {
                    val gm = parent.selectedItem as GeneralMaster
                    try {
                        val productCode = URLEncoder.encode(gm.Code().trim(), "UTF-8")
                        product = gm.Code()
                        //showShortMessage("Selected productCode : $productCode"+" code:"+gm.Code()+" Desc:"+gm.Desc())

                    } catch (e: UnsupportedEncodingException) {
                        Log.d("Msg", e.message!!)
                    }
                }
                //BindDist(state);
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        btn_get_details.setOnClickListener(View.OnClickListener {
            if (sp_season.selectedItemPosition == 0) {
                showShortMessage(resources.getString(R.string.select_season))
                return@OnClickListener
            }

            if (sp_center.selectedItemPosition == 0) {
                showShortMessage(resources.getString(R.string.select_center))
                return@OnClickListener
            }

            if (sp_vendor_name.selectedItemPosition == 0) {
                showShortMessage(resources.getString(R.string.select_vender))
                return@OnClickListener
            }

            if (sp_product_name.selectedItemPosition == 0) {
                showShortMessage(resources.getString(R.string.select_product))
                return@OnClickListener
            }


            //Call post for VENDOR Details For Contract
            if (isNetworkAvailable(this@DCVendorFormActivity)) {
                //digitalContractVM!!.getVendorDetailsForContract("",centerCode,vendorCode,year,season,productCode)
                //digitalContractVM!!.getVendorDetailsForContract("97180401","1008","40011564","2016","K","201000002A")
                val userCode = EncryptDecryptManager.getDecryptedUserCode(this@DCVendorFormActivity)
                val string: String = season
                val yourArray: List<String> = string.split("-")
                DLog.d("GET VENDOR DETAILS FOR CONTRACT product:" + product + " vendor=" + vendorDt + " centerCode=" + center + " season=" + yourArray[1] + " year=" + yourArray[0] + " userCode=" + userCode)
                digitalContractVM!!.getVendorDetailsForContract(
                        userCode,
                        center,
                        vendorDt,
                        yourArray[0],
                        yourArray[1],
                        product
                )
            } else {
                showLongMessage(resources.getString(R.string.no_internet))
            }

        })

        btn_proceed.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DigitalContractActivity::class.java)
            startActivityThis(intent)
        })
    }

    private fun registerObserver() {
        digitalContractVM =
                ViewModelProviders.of(this)
                        .get<DigitalContractVM>(DigitalContractVM::class.java)

//        For handling Progress bar
        digitalContractVM!!.loadingLiveData.observe(this, Observer {
            if (it) {
                llProgressBar.visibility = View.VISIBLE
            } else {
                llProgressBar.visibility = View.GONE
            }
        })

//        In Case of error will show error in  toast message
        digitalContractVM!!.errorLiveData.observe(this, Observer {
            if (it != null) {
                //showShortMessage(it)
                //startScanning()
                DLog.d("CMR errorLiveData :" + it)
                showAlert(
                        it,
                        this@DCVendorFormActivity,
                        Constant.CMR_ALTER,
                        this@DCVendorFormActivity
                )
            }
        })


        digitalContractVM!!.seasonListDataMLData.observe(this, Observer {
            //showShortMessage(it.message)
            showAlert(
                    it.msg,
                    this@DCVendorFormActivity,
                    Constant.CMR_ALTER,
                    this@DCVendorFormActivity
            )

            try {
                val seasonList: ArrayList<GeneralMaster> = ArrayList()
                seasonList.add(
                        GeneralMaster(
                                "SELECT SEASON",
                                "SELECT SEASON"
                        )
                )

                val len = (it.records.size - 1)
                if (it.records != null) {
                    for (i in 0..len) {
                        seasonList.add(
                                GeneralMaster(
                                        ("" + i),
                                        it.records.get(i)
                                )
                        )
                    }
                }
                val adapter: ArrayAdapter<GeneralMaster> = ArrayAdapter<GeneralMaster>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        seasonList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_season.adapter = adapter
            } catch (e: Exception) {
                DLog.d("MSG : " + e.message)
            }
        })

        digitalContractVM!!.centerListMLData.observe(this, Observer {
            //showShortMessage(it.message)
            showAlert(
                    it.msg,
                    this@DCVendorFormActivity,
                    Constant.CMR_ALTER,
                    this@DCVendorFormActivity
            )

            try {
                val centerList: ArrayList<GeneralMaster> = ArrayList()
                centerList.add(
                        GeneralMaster(
                                "SELECT CENTER",
                                "SELECT CENTER"
                        )
                )

                val len = (it.records.size - 1)
                if (it.records != null) {
                    for (i in 0..len) {
                        val recordCenter: RecordCenter = it.records[i]
                        centerList.add(
                                GeneralMaster(
                                        recordCenter.cntrCode,
                                        recordCenter.cntrDesc
                                )
                        )
                    }
                }
                val adapter: ArrayAdapter<GeneralMaster> = ArrayAdapter<GeneralMaster>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        centerList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_center.adapter = adapter
            } catch (e: Exception) {
                DLog.d("MSG : " + e.message)
            }

        })

        digitalContractVM!!.vendorListDataMLData.observe(this, Observer {
            //showShortMessage(it.message)
            showAlert(
                    it.msg,
                    this@DCVendorFormActivity,
                    Constant.CMR_ALTER,
                    this@DCVendorFormActivity
            )

            try {
                val vendorList: ArrayList<GeneralMaster> = ArrayList()
                vendorList.add(
                        GeneralMaster(
                                "SELECT VENDOR NAME",
                                "SELECT VENDOR NAME"
                        )
                )

                val len = (it.records.table1.size - 1)
                if (it.records != null) {
                    for (i in 0..len) {
                        val recordVendor = it.records.table1.get(i)
                        vendorList.add(
                                GeneralMaster(
                                        recordVendor.vendorCode,
                                        recordVendor.growerName
                                )
                        )
                    }
                }
                val adapter: ArrayAdapter<GeneralMaster> = ArrayAdapter<GeneralMaster>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        vendorList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_vendor_name.adapter = adapter
            } catch (e: Exception) {
                DLog.d("MSG : " + e.message)
            }
        })

        digitalContractVM!!.productListDataMLData.observe(this, Observer {
            //showShortMessage(it.message)
            showAlert(
                    it.msg,
                    this@DCVendorFormActivity,
                    Constant.CMR_ALTER,
                    this@DCVendorFormActivity
            )

            try {
                val vendorList: ArrayList<GeneralMaster> = ArrayList()
                vendorList.add(
                        GeneralMaster(
                                "SELECT PRODUCT NAME",
                                "SELECT PRODUCT NAME"
                        )
                )

                val len = (it.records.size - 1)
                if (it.records != null) {
                    for (i in 0..len) {
                        val productRecord: ProductRecord = it.records[i]
                        vendorList.add(
                                GeneralMaster(
                                        productRecord.prdCode,
                                        productRecord.prdDesc1
                                )
                        )
                    }
                }
                val adapter: ArrayAdapter<GeneralMaster> = ArrayAdapter<GeneralMaster>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        vendorList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_product_name.adapter = adapter
            } catch (e: Exception) {
                DLog.d("MSG : " + e.message)
            }

        })


        digitalContractVM!!.vendorDetailsMLData.observe(this, Observer {
            //showShortMessage(it.message)
            showAlert(
                    it.msg,
                    this@DCVendorFormActivity,
                    Constant.CMR_ALTER,
                    this@DCVendorFormActivity
            )

            MainApplication.instance?.setVDResponse(it.records)

            layout_vendor_main.visibility = View.VISIBLE

            val arrayList: ArrayList<DisplayFields> = ArrayList<DisplayFields>()

            val dateOfContract = DisplayFields()
            dateOfContract.key = "Date of Contract : "
            dateOfContract.value = Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
            arrayList.add(dateOfContract)

            val monthOfContract = DisplayFields()
            monthOfContract.key = "Month of Contract : "
            monthOfContract.value = Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
            arrayList.add(monthOfContract)

            val yearOfContract = DisplayFields()
            yearOfContract.key = "Year of Contract : "
            yearOfContract.value = Calendar.getInstance().get(Calendar.YEAR).toString()
            arrayList.add(yearOfContract)

            var centerName = DisplayFields()
            centerName.key = "Production Center : "
            centerName.value = it.records.centerName
            arrayList.add(centerName)

            var geneticPurityStd = DisplayFields()
            geneticPurityStd.key = "Genetic Purity(%)  : "
            geneticPurityStd.value = it.records.geneticPurityStd
            arrayList.add(geneticPurityStd)

            var isolationDistanceM = DisplayFields()
            isolationDistanceM.key = "Isolation(Meter) : "
            isolationDistanceM.value = it.records.isolationDistanceM
            arrayList.add(isolationDistanceM)

            var registrationFees = DisplayFields()
            registrationFees.key = "Registration Fees : "
            registrationFees.value = it.records.registrationFees
            arrayList.add(registrationFees)

            var rsRate = DisplayFields()
            rsRate.key = "Rs. Rate : "
            rsRate.value = it.records.rsRate
            arrayList.add(rsRate)

            var moisture = DisplayFields()
            moisture.key = "Moisture(%) : "
            moisture.value = it.records.moisture
            arrayList.add(moisture)

            val bprdacre = DisplayFields()
            bprdacre.key = "Issued acres : "
            bprdacre.value = it.records.bprdAcre
            arrayList.add(bprdacre)

            var center = DisplayFields()
            center.key = "Center : "
            center.value = it.records.center
            arrayList.add(center)

            var cropDesc = DisplayFields()
            cropDesc.key = "Crop Name : "
            cropDesc.value = it.records.cropDesc
            arrayList.add(cropDesc)

            var orderno = DisplayFields()
            orderno.key = "Order No. : "
            orderno.value = it.records.orderno
            arrayList.add(orderno)

            var physicalPurityStd = DisplayFields()
            physicalPurityStd.key = "Physical Purity Std. : "
            physicalPurityStd.value = it.records.physicalPurityStd
            arrayList.add(physicalPurityStd)

            var prdCode = DisplayFields()
            prdCode.key = "Production Code : "
            prdCode.value = it.records.prdCode
            arrayList.add(prdCode)

            var prdCntrCode = DisplayFields()
            prdCntrCode.key = "Production Center Code : "
            prdCntrCode.value = it.records.prdCntrCode
            arrayList.add(prdCntrCode)

            var growerName = DisplayFields()
            growerName.key = "Grower Name : "
            growerName.value = it.records.growerName
            arrayList.add(growerName)

            val fatherName = DisplayFields()
            fatherName.key = "Father Name : "
            fatherName.value = it.records.fatherName
            arrayList.add(fatherName)

            var villageName = DisplayFields()
            villageName.key = "Village Name : "
            villageName.value = it.records.villageName
            arrayList.add(villageName)

            var talukaName = DisplayFields()
            talukaName.key = "Taluka Name : "
            talukaName.value = it.records.talukaName
            arrayList.add(talukaName)

            var districtName = DisplayFields()
            districtName.key = "District Name : "
            districtName.value = it.records.districtName
            arrayList.add(districtName)

            var stateName = DisplayFields()
            stateName.key = "State Name : "
            stateName.value = it.records.stateName
            arrayList.add(stateName)

            var pinCode = DisplayFields()
            pinCode.key = "Pin Code : "
            pinCode.value = it.records.pinCode
            arrayList.add(pinCode)

            var season = DisplayFields()
            season.key = "Season : "
            season.value = it.records.season
            arrayList.add(season)

            var prdYear = DisplayFields()
            prdYear.key = "Production Year : "
            prdYear.value = it.records.prdYear
            arrayList.add(prdYear)

            var regFees = DisplayFields()
            regFees.key = "Reg. Fees : "
            regFees.value = it.records.regFees
            arrayList.add(regFees)

            var regFeeWord = DisplayFields()
            regFeeWord.key = "Reg. Fees Word : "
            regFeeWord.value = it.records.regFeeWord
            arrayList.add(regFeeWord)

            /*var ifscCode = DisplayFields()
          ifscCode.key = "IFSCCode : "
          ifscCode.value = it.records.ifscCode
          arrayList.add(ifscCode)*/ /*Remark Commented on 27 Jan 2021*/

            /*var aadharNo = DisplayFields()
           aadharNo.key = "Aadhaar No. : "
           aadharNo.value = it.records.aadharNo
           arrayList.add(aadharNo)*/ /*Remark Commented on 28 Jan 2021*/

            /*var bankAccNo = DisplayFields()
            bankAccNo.key = "Bank Acc. No. : "
            bankAccNo.value = it.records.bankAccNo
            arrayList.add(bankAccNo)*/ /*Remark Commented on 28 Jan 2021*/

            /*var bankName = DisplayFields()
            bankName.key = "Bank Name : "
            bankName.value = it.records.bankName
            arrayList.add(bankName)*/ /*Remark Commented on 28 Jan 2021*/

            /* var district = DisplayFields()
            district.key = "District : "
            district.value = it.records.district
            arrayList.add(district)*/ /*Remark Commented on 27 Jan 2021*/

            /*var state = DisplayFields()
           state.key = "State : "
           state.value = it.records.state
           arrayList.add(state)*/ /*Remark Commented on 27 Jan 2021*/

            /* var taluka = DisplayFields()
            taluka.key = "Taluka : "
            taluka.value = it.records.taluka
            arrayList.add(taluka)*/ /*Remark Commented on 27 Jan 2021*/

            /*var village = DisplayFields()
           village.key = "Village : "
           village.value = it.records.village
           arrayList.add(village)*/ /*Remark Commented on 27 Jan 2021*/

            drawTable(arrayList)

        })
    }

    override fun getAlertCallback(fromWhere: String) {
        //showShortMessage(fromWhere)
    }

    fun getVendorList(centerCode: String) {
        /*GET VENDOR LIST CALLED*/
        if (isNetworkAvailable(this@DCVendorFormActivity)) {
            //digitalContractVM!!.getVendorList("1001","K","2020","97180401")
            val userCode = EncryptDecryptManager.getDecryptedUserCode(this@DCVendorFormActivity)
            val string: String = season
            val yourArray: List<String> = string.split("-")
            DLog.d("GET VENDOR LIST : centerCode=" + centerCode + " season=" + yourArray[1] + " year=" + yourArray[0] + " userCode=" + userCode)
            digitalContractVM!!.getVendorList(centerCode, yourArray[1], yourArray[0], userCode)
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }
    }

    fun getProductList() {
        /*GET Product LIST CALLED*/
        if (isNetworkAvailable(this@DCVendorFormActivity)) {
            //digitalContractVM!!.getProductList("1001","K","2018","97180401","40038804")

            val userCode = EncryptDecryptManager.getDecryptedUserCode(this@DCVendorFormActivity)
            val string: String = season
            val yourArray: List<String> = string.split("-")
            DLog.d("GET PRODUCT LIST : vendor=" + vendorDt + " centerCode=" + center + " season=" + yourArray[1] + " year=" + yourArray[0] + " userCode=" + userCode)
            digitalContractVM!!.getProductList(
                    center,
                    yourArray[1],
                    yourArray[0],
                    userCode,
                    vendorDt
            )
        } else {
            showLongMessage(resources.getString(R.string.no_internet))
        }
    }


    private fun drawTable(dataList: List<DisplayFields>) {
        val c = Calendar.getInstance().time
        // System.out.println("Current time => " + c);
        val df = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = df.format(c)

        if ((layout_vendor_table as LinearLayout).childCount > 0) (layout_vendor_table as LinearLayout).removeAllViews()

        var rowNumber = dataList.size
        var columnNumber = 1
        for (i in 0 until rowNumber) {
            val row = TableRow(context)
            val llayoutMain = LinearLayout(context)
            val llayout = LinearLayout(context)
            llayoutMain.orientation = LinearLayout.VERTICAL;
            llayout.orientation = LinearLayout.HORIZONTAL;

            val tvKey = TextView(context)
            tvKey.text = dataList[i].key
            tvKey.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.ts_16))
            tvKey.setTextColor(resources.getColor(R.color.black))
            tvKey.setTypeface(tvKey.getTypeface(), Typeface.BOLD)

            val tvVal = TextView(context)
            tvVal.text = dataList[i].value
            tvVal.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.ts_16))
            tvVal.setTextColor(resources.getColor(R.color.blue))
            tvVal.setTypeface(tvVal.getTypeface(), Typeface.BOLD_ITALIC)

            //tvVal.setBackgroundDrawable(resources.getDrawable(R.drawable.border_btn))
            tvVal.setPadding(5, 10, 5, 10);
            tvKey.setPadding(5, 10, 5, 10);
            val v = View(this)
            v.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    2
            )
            v.setBackgroundColor(resources.getColor(R.color.black))
            llayout.addView(tvKey)
            llayout.addView(tvVal)
            llayoutMain.addView(llayout)
            llayoutMain.addView(v)
            row.addView(llayoutMain)
            layout_vendor_table.addView(row)
        }
    }
}