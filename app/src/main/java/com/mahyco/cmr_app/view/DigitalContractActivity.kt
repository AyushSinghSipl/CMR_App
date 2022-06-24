package com.mahyco.cmr_app.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.aadhaarlib.AadharCard
import com.mahyco.cmr_app.aadhaarlib.DataAttributes
import com.mahyco.cmr_app.aadhaarlib.QrCodeException
import com.mahyco.cmr_app.aadhaarlib.SecureQrCode
import com.mahyco.cmr_app.core.BaseActivity
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.leegality.DCOnFieldInputs
import com.mahyco.cmr_app.leegality.ILeegalityHelper
import com.mahyco.cmr_app.leegality.LeegalityTaskHelper
import com.mahyco.cmr_app.model.vendordetailsforcontract.VendorRecord
import com.mahyco.isp.core.MainApplication
import kotlinx.android.synthetic.main.activity_digital_contract.*
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class DigitalContractActivity : BaseActivity(),ILeegalityHelper {

    private lateinit var leegalityTaskHelper: LeegalityTaskHelper
    private lateinit var iLeegalityHelper: ILeegalityHelper
    private val SCAN_CAMERA_REQUEST_CODE = 100
    private val witnessOne = 1
    private val witnessTwo = 2
    private var callFor = 2
    var aadharData: AadharCard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_contract)
        supportActionBar?.title = getString(R.string.app_name_long)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        iLeegalityHelper = this
        leegalityTaskHelper = LeegalityTaskHelper(this, iLeegalityHelper)

        setUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUI(){
        val vendorRecord:VendorRecord
        vendorRecord = MainApplication.instance?.getVDRecords()!!
        if(vendorRecord.growerName!=null)
            edt_grower_name.setText(vendorRecord.growerName)
        /*else
            edt_grower_name.setText("Vaibhav Chaware")
        edt_grower_pno.setText("9970595950")
        edt_first_party_name.setText("Mahendra Mahajan")
        edt_first_party_pno.setText("9970595950")
        edt_witness_one_name.setText("Rajshri Shukla")
        edt_witness_one_add.setText("Bavadhan")
        edt_witness_one_village.setText("Bhukum")
        edt_witness_one_district.setText("Pune")
        edt_witness_one_pno.setText("9970595950")
        edt_witness_two_name.setText("Akshay Nikam")
        edt_witness_two_add.setText("Aurangabad")
        edt_witness_two_village.setText("Dhawalvadi")
        edt_witness_two_district.setText("Aurangabad")
        edt_witness_two_pno.setText("9970595950")*/

        btn_start_agreement.setOnClickListener(View.OnClickListener {
            val inputs = DCOnFieldInputs()
            inputs.growerFullName = edt_grower_name.text.toString()
            inputs.growerPhoneNo = edt_grower_pno.text.toString()
            inputs.firstPartyFullName = edt_first_party_name.text.toString()
            inputs.firstPartyPhoneNo = edt_first_party_pno.text.toString()
            inputs.witnessOneName = edt_witness_one_name.text.toString()
            inputs.witnessOneAddress = edt_witness_one_add.text.toString()
            inputs.witnessOneVillage = edt_witness_one_village.text.toString()
            inputs.witnessOneDistrict = edt_witness_one_district.text.toString()
            inputs.witnessOnePhoneNo = edt_witness_one_pno.text.toString()
            inputs.witnessTwoName = edt_witness_two_name.text.toString()
            inputs.witnessTwoAddress = edt_witness_two_add.text.toString()
            inputs.witnessTwoVillage = edt_witness_two_village.text.toString()
            inputs.witnessTwoDistrict = edt_witness_two_district.text.toString()
            inputs.witnessTwoPhoneNo = edt_witness_two_pno.text.toString()
            leegalityTaskHelper.doWorkContract(inputs)

        })

        edt_witness_one_add.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }

        edt_witness_one_add.setOnTouchListener(OnTouchListener { v, event ->
            if (edt_witness_one_add.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        edt_witness_two_add.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }

        edt_witness_two_add.setOnTouchListener(OnTouchListener { v, event ->
            if (edt_witness_two_add.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        btn_scann_w1.setOnClickListener(View.OnClickListener {
            callFor = witnessOne
            scanNow()
        })

        btn_scann_w2.setOnClickListener(View.OnClickListener {
            callFor = witnessTwo
            scanNow()
        })
    }

    override fun getLeegalityResult(result: String) {
        DLog.d("Leegality RESULT RECEIVED ::$result");

    }

    fun scanNow() {
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (!checkCameraPermission()) {
            return
        }
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a Aadharcard QR Code")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }


    fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                SCAN_CAMERA_REQUEST_CODE
            )
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanningResult != null) {
            //we have a result
            val scanContent = scanningResult.contents
            val scanFormat = scanningResult.formatName

            // process received data
            if (scanContent != null && !scanContent.isEmpty()) {

//                if (newData) {
//                    processScannedData(scanContent);
//                } else {
//                    parseXml(scanContent);
//                }
                try {
                    processScannedData(scanContent)
                    Log.d("AdhaarCard", "New Card")
                    Toast.makeText(this, "New Card", Toast.LENGTH_SHORT).show()
                } catch (e1: NumberFormatException) {
                    parseXml(scanContent)
                    Log.d("AdhaarCard", "Old Card")
                    Toast.makeText(this, "Old Card", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No scan data received!", Toast.LENGTH_SHORT).show()
        }
    }

    protected fun processScannedData(scanData: String?) {
        // check if the scanned string is XML
        // This is to support old QR codes
        if (isXml(scanData)) {
            val pullParserFactory: XmlPullParserFactory
            try {
                // init the parserfactory
                pullParserFactory = XmlPullParserFactory.newInstance()
                // get the parser
                val parser = pullParserFactory.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(StringReader(scanData))
                aadharData = AadharCard()

                // parse the XML
                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(
                            parser.name
                        )
                    ) {
                        // extract data from tag
                        //uid
                        aadharData!!.setUuid(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_UID_ATTR
                            )
                        )
                        //name
                        aadharData!!.setName(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_NAME_ATTR
                            )
                        )
                        //gender
                        aadharData!!.setGender(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_GENDER_ATTR
                            )
                        )
                        // year of birth
                        aadharData!!.setDateOfBirth(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_DOB_ATTR
                            )
                        )
                        // care of
                        aadharData!!.setCareOf(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_CO_ATTR
                            )
                        )
                        // village Tehsil
                        aadharData!!.setVtc(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_VTC_ATTR
                            )
                        )
                        // Post Office
                        aadharData!!.setPostOffice(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_PO_ATTR
                            )
                        )
                        // district
                        aadharData!!.setDistrict(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_DIST_ATTR
                            )
                        )
                        // state
                        aadharData!!.setState(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_STATE_ATTR
                            )
                        )
                        // Post Code
                        aadharData!!.setPinCode(
                            parser.getAttributeValue(
                                null,
                                DataAttributes.AADHAR_PC_ATTR
                            )
                        )
                    } else if (eventType == XmlPullParser.END_TAG) {
                    } else if (eventType == XmlPullParser.TEXT) {
                    }
                    // update eventType
                    eventType = parser.next()
                }

                // display the data on screen
                displayScannedData()
                return
            } catch (e: XmlPullParserException) {
                Toast.makeText(this, "Error in processing QRcode XML", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                return
            } catch (e: IOException) {
                Toast.makeText(this, e.message + "", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                return
            }
        }

        // process secure QR code
        processEncodedScannedData(scanData)
    }


    protected fun processEncodedScannedData(scanData: String?) {
        try {
            val decodedData = SecureQrCode(this, scanData)
            aadharData = decodedData.getScannedAadharCard()
            // display the Aadhar Data
            // Toast.makeText(this, "Scanned Aadhar Card Successfully", Toast.LENGTH_SHORT).show();
            displayScannedData()
        } catch (e: QrCodeException) {
            // Toast.makeText(this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }
    }


    fun displayScannedData() {
        if(callFor == witnessOne) {
            // clear old values if any
            edt_witness_one_name.text?.clear()
            edt_witness_one_add.text?.clear()
            edt_witness_one_village.text?.clear()
            edt_witness_one_district.text?.clear()
            edt_witness_one_pno.text?.clear()
            tv_uidw1.text = ""

            tv_uidw1.text = ""+ aadharData!!.uuid
            val nameData = aadharData!!.name
            edt_witness_one_name.setText(nameData)
            val addressData = " " +
                aadharData!!.location + " "
                aadharData!!.postOffice + " "
                aadharData!!.district + " "
                aadharData!!.state + " "
                aadharData!!.pinCode
            edt_witness_one_add.setText(addressData)
            edt_witness_one_village.setText(aadharData!!.location)
            edt_witness_one_district.setText(aadharData!!.district)
            edt_witness_two_pno.setText(aadharData!!.mobile)

        }

        if(callFor == witnessTwo) {
            // clear old values if any
            // clear old values if any
            edt_witness_two_name.text?.clear()
            edt_witness_two_add.text?.clear()
            edt_witness_two_village.text?.clear()
            edt_witness_two_district.text?.clear()
            edt_witness_two_pno.text?.clear()

            tv_uidw2.text = ""+ aadharData!!.uuid
            val nameData = aadharData!!.name
            edt_witness_two_name.setText(nameData)
            val addressData =
                aadharData!!.location + " "
                        aadharData!!.postOffice + " "
                        aadharData!!.district + " "
                        aadharData!!.state + " "
                        aadharData!!.pinCode
            edt_witness_two_add.setText(addressData)
            edt_witness_two_village.setText(aadharData!!.location)
            edt_witness_two_district.setText(aadharData!!.district)
            edt_witness_two_pno.setText(aadharData!!.mobile)
        }
    }


    protected fun isXml(testString: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        var retBool = false

        // REGULAR EXPRESSION TO SEE IF IT AT LEAST STARTS AND ENDS
        // WITH THE SAME ELEMENT
        val XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>"

        // IF WE HAVE A STRING
        if (testString != null && testString.trim { it <= ' ' }.length > 0) {

            // IF WE EVEN RESEMBLE XML
            if (testString.trim { it <= ' ' }.startsWith("<")) {
                pattern = Pattern.compile(
                    XML_PATTERN_STR,
                    Pattern.CASE_INSENSITIVE or Pattern.DOTALL or Pattern.MULTILINE
                )

                // RETURN TRUE IF IT HAS PASSED BOTH TESTS
                matcher = pattern.matcher(testString)
                retBool = matcher.matches()
            }
            // ELSE WE ARE FALSE
        }
        return retBool
    }


    fun parseXml(scanData: String?) {
        val dbf = DocumentBuilderFactory.newInstance()
        var db: DocumentBuilder? = null
        try {
            db = dbf.newDocumentBuilder()
            val `is` = InputSource()
            `is`.characterStream = StringReader(scanData)
            try {
                val doc = db.parse(`is`)

                edt_witness_one_name.setText("")
                edt_witness_one_add.setText("")
                edt_witness_one_village.setText("")
                edt_witness_one_village.setText("")
                edt_witness_one_district.setText("")

                edt_witness_two_name.setText("")
                edt_witness_two_add.setText("")
                edt_witness_two_village.setText("")
                edt_witness_two_village.setText("")
                edt_witness_two_district.setText("")

                val uid = doc.documentElement.getAttribute(DataAttributes.AADHAR_UID_ATTR)
                val name = doc.documentElement.getAttribute(DataAttributes.AADHAR_NAME_ATTR)
                val gender = doc.documentElement.getAttribute(DataAttributes.AADHAR_GENDER_ATTR)
                val dob = doc.documentElement.getAttribute(DataAttributes.AADHAR_DOB_ATTR)
                val yob = doc.documentElement.getAttribute(DataAttributes.AADHAR_YOB_ATTR)
                val careof = doc.documentElement.getAttribute(DataAttributes.AADHAR_CO_ATTR)
                val vtc = doc.documentElement.getAttribute(DataAttributes.AADHAR_VTC_ATTR)
                val postoffice = doc.documentElement.getAttribute(DataAttributes.AADHAR_PO_ATTR)
                val district = doc.documentElement.getAttribute(DataAttributes.AADHAR_DIST_ATTR)
                val landmark = doc.documentElement.getAttribute(DataAttributes.AADHAR_LAND_ATTR)
                val house = doc.documentElement.getAttribute(DataAttributes.AADHAR_HOUSE_ATTR)
                val location = doc.documentElement.getAttribute(DataAttributes.AADHAR_LOCATION_ATTR)
                val state = doc.documentElement.getAttribute(DataAttributes.AADHAR_STATE_ATTR)
                val pincode = doc.documentElement.getAttribute(DataAttributes.AADHAR_PC_ATTR)
                val street = doc.documentElement.getAttribute(DataAttributes.AADHAR_STREET_ATTR)
                val subdistrict =
                    doc.documentElement.getAttribute(DataAttributes.AADHAR_SUBDIST_ATTR)

                if(callFor == witnessOne) {
                    // clear old values if any
                    edt_witness_one_name.text?.clear()
                    edt_witness_one_add.text?.clear()
                    edt_witness_one_village.text?.clear()
                    edt_witness_one_district.text?.clear()
                    edt_witness_one_pno.text?.clear()
                    tv_uidw1.text = ""

                    tv_uidw1.text = uid
                    val nameData = name
                    edt_witness_one_name.setText(nameData)
                    val addressData =
                        location + " "+postoffice + " "+
                                district +" "+
                                state + " "+
                                pincode
                    edt_witness_one_add.setText(addressData)
                    edt_witness_one_village.setText(location)
                    edt_witness_one_district.setText(district)
                    edt_witness_two_pno.setText("")

                }

                if(callFor == witnessTwo) {
                    // clear old values if any
                    edt_witness_two_name.text?.clear()
                    edt_witness_two_add.text?.clear()
                    edt_witness_two_village.text?.clear()
                    edt_witness_two_district.text?.clear()
                    edt_witness_two_pno.text?.clear()

                    tv_uidw2.text = ""+ uid
                    val nameData = name
                    edt_witness_two_name.setText(nameData)
                    val addressData =
                        location + " "+postoffice + " "+
                                district +" "+
                                state + " "+
                                pincode
                    edt_witness_two_add.setText(addressData)
                    edt_witness_two_village.setText(location)
                    edt_witness_two_district.setText(district)
                    edt_witness_two_pno.setText("")
                }

            } catch (e: SAXException) {
                // handle SAXException
            } catch (e: IOException) {
                // handle IOException
            }
        } catch (e1: ParserConfigurationException) {
            // handle ParserConfigurationException
        }
    }

}