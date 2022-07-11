package com.mahyco.cmr_app.view.travel.start_travel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.cmr_app.model.getActivityType.GetActivityTypeResponseItem
import com.mahyco.cmr_app.model.getVehicleTypeResponse.GetVehicleTypeResponseItem
import com.mahyco.cmr_app.utils.GPSTracker
import com.mahyco.cmr_app.utils.searchspinner.GeneralMaster
import com.mahyco.cmr_app.viewmodel.CMRDataViewModel
import com.mahyco.isp.core.MainApplication
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import com.vansuita.pickimage.listeners.IPickCancel
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.fragment_start_travel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [AddEvenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEvenFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: com.mahyco.cmr_app.databinding.FragmentAddEvenBinding? = null
    private val binding get() = _binding!!
    var msclass: Messageclass? = null
    var is_visible = false
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val CAPTURE_IMAGE_REQUEST = 1888
    lateinit var activityList: ArrayList<GetActivityTypeResponseItem>

    /*  private val wordViewModel: WordViewModel by viewModels {
          WordViewModelFactory((activity?.application as MainApplication).repository)
      }*/
    var travelList: Word? = null
    var gm: MutableList<GeneralMaster>? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var location: Location? = null
    var photoFile: File? = null
    var mCurrentPhotoPath: String = ""
    var cordinate = ""
    var address = ""
    var latitude = ""
    var imageHashCode = ""
    var longitude = ""
    var timeStamp = ""
    var tourId = ""
    var insert = false
    var tourEnded = false
    var imageBitmap: Bitmap? = null
    var apiCall = false

    private lateinit var wordViewModel: WordViewModel
    lateinit var cmrDataViewModel: CMRDataViewModel

    open fun AddEvenFragment() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wordViewModel = WordViewModel(
            (requireActivity()?.application as MainApplication).repository,
            requireActivity()?.application as MainApplication
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = com.mahyco.cmr_app.databinding.FragmentAddEvenBinding.inflate(
            inflater,
            container,
            false
        )
        val root: View = binding.root
        msclass = Messageclass(this.activity)

        registerObserver()
        checkData()


        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        if (!isLocationEnabled(context!!)) {
            // notify user

            AlertDialog.Builder(context)
                .setMessage("PLease enable your location from settings")
                .setPositiveButton(
                    " Enable ",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    })
                .setNegativeButton("Cancel", null)
                .show();
        } else {
            setListner()
            //
        }
        val sharedPreference: SharedPreference = SharedPreference(requireContext())
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_NAME)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)

        binding.lblwelcome.text = decryptedUserCode

        return root
    }


    private fun registerObserver() {
        cmrDataViewModel =
            ViewModelProviders.of(this)
                .get<CMRDataViewModel>(CMRDataViewModel::class.java)


        //        For handling Progress bar
        cmrDataViewModel!!.loadingLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                binding.loader.visibility = View.VISIBLE
            } else {
                binding.loader.visibility = View.GONE
            }
        })

//        In Case of error will show error in  toast message
        cmrDataViewModel!!.errorLiveData.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                DLog.d("CMR errorLiveData :" + it)
            }
        })

        cmrDataViewModel!!.postTourEventsdata.observe(this, androidx.lifecycle.Observer {
            var result = it.toString()
            DLog.d("CMR DATA Activity : " + result)
            if (it.status == "Success") {
                val events: MutableList<Word> = ArrayList()
                val tours: MutableList<Word> = ArrayList()

                val sd = SimpleDateFormat("MM/dd/yyyy")
                val currentDate = sd.format(Date())
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
                    activity?.finish()
                    activity?.startActivity(activity?.getIntent())
                }
                if (alertDialog.isShowing) {
                    alertDialog.dismiss()
                }
                alertDialog.show()
            } else {
                if (!it.errorMessage.toString().equals("null")) {
                    msclass?.showMessage(it.errorMessage.toString())
                } else {
                    msclass?.showMessage("Something went wrong")
                }
            }

        })


    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF

        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    private fun checkData() {
        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())


        val activity: Activity? = getActivity()
        if (isAdded() && activity != null) {
            wordViewModel = WordViewModel(
                (activity?.application as MainApplication).repository,
                activity?.application as MainApplication
            )
            if (is_visible) {
                val sd = SimpleDateFormat("MM/dd/yyyy")
                val currentDate = sd.format(Date())
                wordViewModel.getCurrentDateTravelType(currentDate, "start")
                    .observe(owner = this) { words ->
                        // Update the cached copy of the words in the adapter.
                        words.let {
                            Log.e("start", "onCreateView: " + it.size)
                        }
                        for (item in words) {
                            if (item.uType == "start") {
                                travelList = item
                                tourId = item.uId.toString()
                            }
                        }
                        if (words.size == 0) {
                            if (is_visible) {
                                if (!tourEnded) {
                                    /* showDialog(
                                    "Tour is not started for today",
                                    "Please start tour for today , then only you can add events"
                                )*/
                                }
                            }
                            binding.btnstUpdate.isEnabled = false
                            binding.btnstUpdate.visibility = View.GONE
                        } else {
                            binding.btnstUpdate.isEnabled = true
                            binding.btnstUpdate.visibility = View.VISIBLE
                        }

                    }

                /* wordViewModel.getCurrentDateTravelType(currentDate, "end")
                .observe(owner = this) { words ->
                    // Update the cached copy of the words in the adapter.
                    words.let {
                        Log.e("end", "onCreateView: " + it.size)
                    }
                    if (words.size != 0) {
                        if (is_visible) {
                            tourEnded = true
                            binding.btnstUpdate.isEnabled = false
                            binding.btnstUpdate.visibility = View.GONE
                          *//*  showDialog(
                                "Tour already ended for today",
                                "Tour cycle is ended for today please comeback next day"
                            )*//*
                        }
                    }

                }*/

                wordViewModel.getCurrentDateTravelType(currentDate, "add_event")
                    .observe(owner = this) { words ->
                        // Update the cached copy of the words in the adapter.
                        words.let {
                            Log.e("add_event", "onCreateView: " + it.size)
                        }

                        if (words.size != 0) {
                            if (is_visible) {
                                for (item in words) {
                                    if (timeStamp == item.uEventDateTime) {
                                        /* Toast.makeText(
                                        context,
                                        "Event Added successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()*/

                                        CoroutineScope(Main).launch {

                                            if (isNetworkConnected()) {
                                                val tourList: MutableList<Word> =
                                                    wordViewModel.allWords() as MutableList<Word>
                                                if (tourList.size != 0) {
                                                    if (insert) {
                                                        cmrDataViewModel?.postTourEventDataAPI(
                                                            tourList
                                                        )
                                                        insert = false
                                                    }
                                                }
                                            } else {
                                                insert = false
                                            }

                                        }
                                        binding.loader.visibility = View.GONE
                                        binding.btnstUpdate.isEnabled = true

                                        imageBitmap = null
                                        binding.ivImage.visibility = View.GONE
                                        binding.txtDescription.text.clear()
                                        binding.spEventtype.setSelection(0)
                                    }

                                }
                            }
                        }

                    }

            }
        }
    }


    @SuppressLint("ServiceCast")
    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager =
            context?.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            is_visible = true
            checkData()
        } else {
            is_visible = false
            timeStamp = ""
        }

    }

    private fun validate(): Boolean {

        if (binding.spEventtype.selectedItemPosition == 0) {
            msclass?.showMessage("Please select event type")
            return false
        }
        if (!Constant.isTimeAutomatic(requireContext())) {
            msclass?.showAutomaticTimeMessage("Please update time setting to automatic")
            return false
        }
        /*  if (imageBitmap == null) {
              msclass?.showMessage("Please click image of your Speedometer")
              return false
          }*/
        /*   if (!Constant.isTimeAutomatic(requireContext())){
               msclass?.showAutomaticTimeMessage("Please update time setting to automatic")
               return false
           }*/

        return true

    }

    private fun setListner() {

        if (imageBitmap == null) {
            binding.ivImage.visibility == View.GONE
        } else {
            binding.ivImage.visibility == View.VISIBLE
            binding?.ivImage?.setImageBitmap(imageBitmap)
        }
        binding.btnstUpdate.setOnClickListener {
            insert = true
            if (validate()) {
                val sd = SimpleDateFormat("MM/dd/yyyy")
                val currentDate = sd.format(Date())
                CoroutineScope(Dispatchers.Main).launch {
                    val list = wordViewModel.getCurrentDateTypeTravel(currentDate, "end")

                    if (list != null && list.size != 0) {
                        tourEnded = true
                        /*showDialog(
                        "Tour already ended for today",
                        "Tour cycle is ended for today please comeback next day"
                    )*/
                        msclass?.showMessage("Tour is ended for today")
                    } else {

                        lifecycleScope.launch {
                            if (insert == true) {
                                addData()
                            }
                        }
                    }
                }
            }
        }

        _binding?.btnTakephoto?.setOnClickListener {
            PickImageDialog.build(PickSetup().setPickTypes(EPickType.CAMERA))
                .setOnPickResult(object : IPickResult {
                    override fun onPickResult(r: PickResult?) {
                        if (r?.bitmap != null) {
                            imageBitmap = r?.bitmap
                            binding?.ivImage?.setImageBitmap(r?.bitmap)
                            _binding?.ivImage?.visibility = View.VISIBLE
                        }
                    }
                })
                .setOnPickCancel(object : IPickCancel {
                    override fun onCancelClick() {
                        //TODO: do what you have to if user clicked cancel
                    }
                }).show(this.childFragmentManager)
        }
        intialbinddata()
    }

    private fun addData() {
        binding.loader.visibility = View.VISIBLE
        binding.btnstUpdate.isEnabled = false
        checkLocationPermission()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val currentDateTime = sdf.format(Date())
        timeStamp = currentDateTime

        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())

        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        val eventId = activityList?.get(binding.spEventtype.selectedItemPosition)?.trId
        val eventType =
            activityList?.get(binding.spEventtype.selectedItemPosition)?.activityDescription


        val travel = Word(
            0,
            "",
            "",
            currentDateTime,
            "0",
            "",
            address,
            "",
            "",
            "",
            "",
            latitude,
            longitude,
            "0",
            "",
            "0",
            "",
            encoded,
            "0",
            "add_event",
            eventType.toString(),
            eventId.toString(),
            tourId.toString(),
            binding.txtDescription.text.toString(),

            currentDate, "0", "0"


        )


        //  llProgressBarStartTravel.visibility = View.GONE

        if (travel.uEventLat.isNotEmpty() && travel.uEventLng.isNotEmpty()&& !travel.uEventLng.equals("0.0")&& !travel.uEventLat.equals("0.0")) {
            val inserted = wordViewModel.insert(travel).isCompleted
        } else {
            msclass?.showMessage("Unable to access location")
            val gpsTracker = GPSTracker(context)
            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                latitude =   gpsTracker.latitude.toString()
                longitude = gpsTracker.longitude.toString()

            }
        }


    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val bitmap = myBitmap(BitmapFactory.decodeFile(photoFile!!.absolutePath))

            if (bitmap != null) {
                imageBitmap = bitmap
                binding?.ivImage?.setImageBitmap(bitmap)
//                Glide.with(context!!).load(bitmap).into(binding.ivImage)

                _binding?.ivImage?.visibility = View.VISIBLE
            }
        } else {
            msclass!!.showMessage("Request cancelled or something went wrong.")
        }
    }

    private fun myBitmap(bitmap: Bitmap?): Bitmap {
        /*  val ei = ExifInterface(photoFile!!.absolutePath)
          val orientation: Int = ei.getAttributeInt(
              ExifInterface.TAG_ORIENTATION,
              ExifInterface.ORIENTATION_UNDEFINED
          )

          var rotatedBitmap: Bitmap? = null
          rotatedBitmap = when (orientation) {
              ExifInterface.ORIENTATION_ROTATE_90 -> bitmap?.let {
                  TransformationUtils.rotateImage(
                      it, 90
                  )
              }
              ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap!!, 180)
              ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap!!, 270)
              ExifInterface.ORIENTATION_NORMAL -> bitmap
              else -> bitmap
          }*/

        val width = bitmap?.width
        val height = bitmap?.height

        val scaleWidth = width!! / 10
        val scaleHeight = height?.div(10)

        if (bitmap != null) {
            if (bitmap.byteCount <= 1000000)
                return bitmap
        }

        return Bitmap.createScaledBitmap(bitmap!!, scaleWidth, scaleHeight!!, false)

    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                val builder = AlertDialog.Builder(context)
                builder.setMessage("You need to allow location permission to perform further operations")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Allow"
                    ) { dialog, id ->
                        requestLocationPermission()
                    }
                    .setNegativeButton(
                        "Deny"
                    ) { dialog, id -> activity?.finish() }
                val alert = builder.create()
                alert.show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    private fun checkBackgroundLocation() {
        try {
            activity?.runOnUiThread {
                if (ActivityCompat.checkSelfPermission(
                        this.requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //  requestBackgroundLocationPermission()
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                val df = DecimalFormat()
                                df.setMaximumFractionDigits(3)

                                latitude = df.format(location.latitude).toDouble().toString()
                                longitude = df.format(location.longitude).toDouble().toString()

                                /*   val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
                                   val addresses: List<Address> =
                                       geocoder.getFromLocation(
                                           latitude.toDouble(),
                                           longitude.toDouble(),
                                           1
                                       )
                                   val cityName: String = addresses[0].locality
                                   address = cityName
                                   binding.txtpalce.setText(cityName)*/
                            }
                        }
                }
            }
        } catch (e: Exception) {
            Log.e("error", "checkBackgroundLocation: " + e.message)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onResume() {
        super.onResume()
        if (isLocationEnabled(context!!)) {
            checkLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        checkBackgroundLocation()
                    }

                } else {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("You need to allow location permission to perform further operations")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Allow"
                        ) { dialog, id ->
                            requestLocationPermission()
                        }
                        .setNegativeButton(
                            "Deny"
                        ) { dialog, id -> activity?.finish() }
                    val alert = builder.create()
                    alert.show()

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    /* Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this.requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.activity?.packageName, null),
                            ),
                        )

                }*/
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this.requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        checkBackgroundLocation()
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this.requireContext(), "permission denied", Toast.LENGTH_LONG)
                        .show()
                }
                return

            }
        }

    }

    fun intialbinddata() {
        var sharedPreference: SharedPreference = SharedPreference(context!!)
        val gson = Gson()
        val json: String = sharedPreference.getValueString(Constant.ACTIVITY_LIST).toString()
        val type: Type = object : TypeToken<List<GetActivityTypeResponseItem?>?>() {}.type
        activityList = gson.fromJson(json, type)
        activityList.add(0, GetActivityTypeResponseItem("Please select event type", "0", "", 0))
        val adapter: ArrayAdapter<GetActivityTypeResponseItem> =
            ArrayAdapter<GetActivityTypeResponseItem>(
                this.context!!, android.R.layout.simple_spinner_dropdown_item,
                activityList as ArrayList<GetActivityTypeResponseItem>
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEventtype.setAdapter(adapter)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEvenFragment.
         */
        // TODO: Rename and change types and number of parameters

        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
        /*  @JvmStatic
          fun newInstance(param1: String, param2: String) =
              AddEvenFragment(wordViewModel).apply {
                  arguments = Bundle().apply {
                      putString(ARG_PARAM1, param1)
                      putString(ARG_PARAM2, param2)
                  }
              }*/
    }
}