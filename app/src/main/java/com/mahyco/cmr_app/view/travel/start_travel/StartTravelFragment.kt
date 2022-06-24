package com.mahyco.cmr_app.view.travel.start_travel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.cmr_app.databinding.FragmentStartTravelBinding
import com.mahyco.cmr_app.model.getVehicleTypeResponse.GetVehicleTypeResponseItem
import com.mahyco.cmr_app.utils.GPSTracker
import com.mahyco.cmr_app.utils.searchspinner.GeneralMaster
import com.mahyco.cmr_app.viewmodel.CMRDataViewModel
import com.mahyco.isp.core.MainApplication
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.enums.EPickType
import com.vansuita.pickimage.listeners.IPickCancel
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_my_travel.*
import kotlinx.android.synthetic.main.fragment_start_travel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartTravelFragment(
) : Fragment(), CoroutineScope {

    var mCurrentPhotoPath: String = ""

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentStartTravelBinding? = null
    private val binding get() = _binding!!
    var imageselect = 0
    var userCode: String? = null
    var photoFile: File? = null
    var msclass: Messageclass? = null

    /*   private val wordViewModel: WordViewModel by viewModels {
           WordViewModelFactory((activity?.application as MainApplication).repository)
       }*/
    public fun StartTravelFragment() {}




    private val CAPTURE_IMAGE_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

    lateinit var Vehicle: ArrayList<GetVehicleTypeResponseItem>
    var location: Location? = null

    var cordinate = ""
    var address = ""
    var latitude = ""
    var imageHashCode = ""
    var imageBitmap: Bitmap? = null
    var dateTime = ""
    var longitude = ""

    //    var dbHelper: DatabaseHelperImpl? = null
    var gm: MutableList<GeneralMaster>? = null
    var travelList: List<Word>? = null
    private var isVisibleTo_User = false
    var gps_enabled = false;
    var network_enabled = false;



    private lateinit var wordViewModel: WordViewModel
    lateinit var cmrDataViewModel: CMRDataViewModel


    private var job: Job = Job()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           // wordViewModel = it.getString(ARG_PARAM1)
          //  cmrDataViewModel = it.getString(ARG_PARAM2)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        wordViewModel= WordViewModel(
            (requireActivity()?.application as MainApplication).repository,
            requireActivity()?.application as MainApplication
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartTravelBinding.inflate(inflater, container, false)
        val root: View = binding.root

        msclass = Messageclass(this.activity)

      /*  cmrDataViewModel =
            ViewModelProviders.of(this)
                .get<CMRDataViewModel>(CMRDataViewModel::class.java)*/
        registerObserver()




        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())



        if (!isLocationEnabled(requireContext())) {
            // notify user

            AlertDialog.Builder(context)
                .setMessage("PLease enable your location from setting")
                .setPositiveButton("Enable", DialogInterface.OnClickListener { dialogInterface, i ->
                    context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                })
                .setNegativeButton("Cancel", null)
                .show();
        } else {
            setListner()
        }
        val sharedPreference: SharedPreference = SharedPreference(requireContext())
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_NAME)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)

        binding.lblwelcome.text = decryptedUserCode

        return root
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

            wordViewModel.getCurrentDateTravelType(currentDate, "start")
                .observe(owner = this) { words ->
                    // Update the cached copy of the words in the adapter.
                    words.let {
                        Log.e("start", "onCreateView: " + it.size)
                    }
                    if (words.size != 0) {
                        if (isVisibleTo_User) {
                            // showDialog()
                            binding.btnstUpdate.visibility = View.GONE
                            binding.btnstUpdate.isEnabled = false
                        }
                    }

                }

            wordViewModel.getCurrentDateTravelType(currentDate, "end")
                .observe(owner = this) { words ->
                    // Update the cached copy of the words in the adapter.
                    words.let {
                        Log.e("end", "onCreateView: " + it)
                    }
                    if (words.size != 0) {
                        if (isVisibleTo_User) {
                            binding.btnstUpdate.visibility = View.GONE
                            binding.btnstUpdate.isEnabled = false
                            /* showDialogEnd(
                             "Tour already ended for today",
                             "Tour cycle is ended for today please comeback next day"
                         )*/
                            msclass?.showMessage("Tour is ended for today")
                        }
                    }

                }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isVisibleTo_User = true
            checkData()
        } else {
            isVisibleTo_User = false
        }

    }


    private fun setListner() {


        if (imageBitmap == null) {
            binding.ivImage.visibility == View.GONE
        } else {
            binding.ivImage.visibility == View.VISIBLE
            binding?.ivImage?.setImageBitmap(imageBitmap)
        }

        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())
        binding.btnstUpdate.setOnClickListener {
            llProgressBarStartTravel.visibility = View.VISIBLE
            wordViewModel.getCurrentDateTravelType(currentDate, "start")
                .observe(owner = viewLifecycleOwner) { words ->
                    // Update the cached copy of the words in the adapter.
                    words.let {
                        Log.e("start", "onCreateView: " + it.size)
                    }
                    if (words != null && words.size != 0) {
                        if (isVisibleTo_User) {
                            for (item in words) {
                                if (item.uStartDateTime == dateTime) {
                                    CoroutineScope(Main).launch {
                                        if (isNetworkConnected()) {
                                            val tourList: MutableList<Word> =
                                                wordViewModel.allWords() as MutableList<Word>
                                            if (tourList.size != 0) {
                                                cmrDataViewModel?.postTourEventDataAPI(tourList)

                                            }
                                        } else {
                                            llProgressBarStartTravel.visibility = View.GONE
                                            val alertDialog = AlertDialog.Builder(context).create()
                                            alertDialog.setTitle("Crop Monitor Report")
                                            alertDialog.setCancelable(false)
                                            alertDialog.setMessage("Tour is started for today")
                                            //alertDialog.setIcon(R.drawable.tick);
                                            alertDialog.setButton("OK") { dialog, which ->
                                                activity?.finish()
                                                startActivity(activity?.getIntent())
                                                return@setButton
                                            }


                                            alertDialog.show()
                                        }
                                    }


                                }
                            }
                        }
                    }
                }

            checkLocationPermission()
            if (validate()) {
                binding.llProgressBarStartTravel.visibility = View.VISIBLE
                lifecycleScope.launch {
                    addData()


                }

            }

        }


        _binding?.btnTakephoto?.setOnClickListener {

            PickImageDialog.build(PickSetup().setPickTypes(EPickType.CAMERA))
                .setOnPickResult(object : IPickResult {
                    override fun onPickResult(r: PickResult?) {
                        photoFile = createImageFile()

                        if (r?.bitmap != null) {
                            imageBitmap = r?.bitmap

                            try {
                                FileOutputStream(photoFile?.absolutePath).use { out ->
                                    r?.bitmap.compress(
                                        Bitmap.CompressFormat.PNG,
                                        100,
                                        out
                                    ) // bmp is your Bitmap instance
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

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

    @SuppressLint("ServiceCast")
    private fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager =
            context?.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()!!.isConnected()
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
    private fun registerObserver() {
        cmrDataViewModel =
            ViewModelProviders.of(this)
                .get<CMRDataViewModel>(CMRDataViewModel::class.java)


        //        For handling Progress bar
        cmrDataViewModel!!.loadingLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                llProgressBarStartTravel.visibility = View.VISIBLE
            } else {
                llProgressBarStartTravel.visibility = View.GONE
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

                    activity?.finish()
                    activity?.startActivity(activity?.getIntent())
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

    private fun checkBackgroundLocation() {

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

                            /*  val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
                              val addresses: List<Address> =
                                  geocoder.getFromLocation(latitude.toDouble(),longitude.toDouble(), 1)
                              val cityName: String = addresses[0].locality
                              address = cityName
                              binding.txtlocation.setText(cityName)*/
                        }
                    }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }


    private fun validate(): Boolean {
        if (binding.spvehicletype.selectedItemPosition == 0) {
            msclass?.showMessage("Please select vehicle type")
            return false
        }
        /*  if (binding.txtlocation.text.length == 0) {
              msclass?.showMessage("Please enter correct location")
              checkLocationPermission()
              return false
          }*/
        if (binding.txtkm.text.length == 0) {
            msclass?.showMessage("Please enter your current speedometer reading")
            return false
        }
        if (imageBitmap == null) {
            msclass?.showMessage("Please click image of your Speedometer")
            return false
        }

        if (!Constant.isTimeAutomatic(requireContext())) {
            msclass?.showAutomaticTimeMessage("Please update time setting to automatic")
            return false
        }
        return true

    }

    private suspend fun addData() {
        checkLocationPermission()
        llProgressBarStartTravel.visibility = View.VISIBLE
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val currentDateTime = sdf.format(Date())
        dateTime = currentDateTime


        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())

        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val vehicleId = Vehicle?.get(binding.spvehicletype.selectedItemPosition)?.trId
        val vehicleType =
            Vehicle?.get(binding.spvehicletype.selectedItemPosition)?.vehicleDescription

        val travel = Word(
            0,
            currentDateTime,
            "",
            "",
            vehicleId.toString(),
            vehicleType.toString(),
            address,
            latitude,
            longitude,
            "",
            "",
            "",
            "",
            binding.txtkm.text.toString(),
            encoded,
            "0",
            "",
            "",
            "0",
            "start",
            "",
            "0",
            "0",
            "",
            currentDate, "0", "0"

        )


        //  llProgressBarStartTravel.visibility = View.GONE

        if (travel.uStartLat.isNotEmpty() && travel.uStartLng.isNotEmpty() &&!travel.uStartLat.equals("0.0")&& !travel.uStartLng.equals("0.0"))  {
            val inserted = wordViewModel.insert(travel).isCompleted
        } else {
            msclass?.showMessage("Unable to access location")
            val gpsTracker = GPSTracker(context)
            if (gpsTracker.getIsGPSTrackingEnabled())
            {
             latitude =   gpsTracker.latitude.toString()
                longitude = gpsTracker.longitude.toString()

                llProgressBarStartTravel.visibility = View.GONE
            }
//            checkLocationPermission()
        }

        // Toast.makeText(context, inserted.toString(), Toast.LENGTH_SHORT).show()
        llProgressBarStartTravel.visibility = View.GONE

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_CAMERA_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(activity?.packageManager!!) != null) {
                        // Create the File where the photo should go
                        /*try {
                            photoFile = createImageFile()
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                val photoURI = FileProvider.getUriForFile(
                                    context!!,
                                    "com.mahyco.cmr_app.provider",
                                    photoFile!!
                                )
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                            }
                        } catch (ex: Exception) {
                            // Error occurred while creating the File
                            msclass?.showMessage(ex.message.toString())
                        }*/
                        val setup: PickSetup = PickSetup()
                            .setFlip(true)
                            .setPickTypes(EPickType.CAMERA)
                            .setIconGravity(Gravity.LEFT)
                            .setButtonOrientation(LinearLayout.VERTICAL)
                            .setSystemDialog(false)
                        PickImageDialog.build(setup)
                            .setOnPickResult(object : IPickResult {
                                override fun onPickResult(r: PickResult?) {
                                    //TODO: do what you have to...
                                }
                            })
                            .setOnPickCancel(object : IPickCancel {
                                override fun onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel
                                }
                            }).show(fragmentManager)
                    }
                } else {
                    Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
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

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    /*  if (!ActivityCompat.shouldShowRequestPermissionRationale(
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
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
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
        var sharedPreference: SharedPreference = SharedPreference(requireContext())
        //binding.spvehicletype.setAdapter(null)
        /*  gm = ArrayList<GeneralMaster>()
          gm?.add(GeneralMaster("0", "SELECT VEHICLE TYPE"))
          gm?.add(GeneralMaster("1", "COMPANY VEHICLE"))
          gm?.add(GeneralMaster("2", "PERSONAL VEHICLE(4 wheeler)"))
          gm?.add(GeneralMaster("3", "PERSONAL VEHICLE(2 wheeler)"))
          gm?.add(GeneralMaster("4", "PUBLIC VEHICLE"))
          gm?.add(GeneralMaster("5", "OTHER"))*/
        try {


            val gson = Gson()
            val json: String = sharedPreference.getValueString(Constant.VEHICLE_LIST).toString()
            val type: Type = object : TypeToken<List<GetVehicleTypeResponseItem?>?>() {}.type
            Vehicle = gson.fromJson(json, type)
            Vehicle.add(0, GetVehicleTypeResponseItem("0", "0", "Please select vehicle type", 0))

            val adapter: ArrayAdapter<GetVehicleTypeResponseItem> =
                ArrayAdapter<GetVehicleTypeResponseItem>(
                    this.requireContext(), android.R.layout.simple_spinner_dropdown_item,
                    Vehicle as ArrayList<GetVehicleTypeResponseItem>
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spvehicletype.setAdapter(adapter)
        } catch (e: Exception) {
            Log.e("error", "intialbinddata: " + e.toString())
        }
    }


    companion object {

      /*   @JvmStatic
         fun newInstance(param1: WordViewModel, param2: CMRDataViewModel?) =
             StartTravelFragment(param1,param2).apply {
                 arguments = Bundle().apply {
                     putString(ARG_PARAM1, param1)
                     putString(ARG_PARAM2, param2)
                 }
             }*/

        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val sharedPreference: SharedPreference? = context?.let { SharedPreference(it) }
        val encryptedUserCode = sharedPreference?.getValueString(Constant.USER_CODE)
        val decryptedUserCode = ""+ EncryptDecryptManager.decryptStringData(encryptedUserCode)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = decryptedUserCode + "_start_" + timeStamp + "_"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {


         //   val bitmap = myBitmap(BitmapFactory.decodeFile(photoFile!!.absolutePath))


          /*  if (bitmap != null) {
                imageBitmap = bitmap
                binding?.ivImage?.setImageBitmap(bitmap)
                _binding?.ivImage?.visibility = View.VISIBLE
            }*/
        } else {
            msclass!!.showMessage("Request cancelled or something went wrong.")
        }
    }

    private fun myBitmap(bitmap: Bitmap?): Bitmap {
        /*   val ei = ExifInterface(photoFile!!.absolutePath)
           val orientation: Int = ei.getAttributeInt(
               ExifInterface.TAG_ORIENTATION,
               ExifInterface.ORIENTATION_UNDEFINED
           )

           var rotatedBitmap: Bitmap? = null
           rotatedBitmap = when (orientation) {
               ExifInterface.ORIENTATION_ROTATE_90 -> bitmap?.let {
                   rotateImage(
                       it, 90
                   )
               }
               ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap!!, 180)
               ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap!!, 270)
               ExifInterface.ORIENTATION_NORMAL -> bitmap
               else -> bitmap
           }
   */
        val width = bitmap?.width
        val height = bitmap?.height

        val scaleWidth = width!! / 10
        val scaleHeight = height?.div(10)

        if (bitmap != null) {
            if (bitmap.byteCount <= 1000000)
                return bitmap
        }

        return Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight!!, false)

    }


    override fun onResume() {
        super.onResume()
        if (isLocationEnabled(requireContext())) {
            //setListner()
//            checkLocationPermission()

        }


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}