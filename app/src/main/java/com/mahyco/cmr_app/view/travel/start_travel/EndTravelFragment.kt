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
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mahyco.cmr_app.R
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.DLog
import com.mahyco.cmr_app.core.Messageclass
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
import kotlinx.android.synthetic.main.fragment_start_travel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EndTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EndTravelFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: com.mahyco.cmr_app.databinding.FragmentEndTravelBinding? = null
    private val binding get() = _binding!!
    var msclass: Messageclass? = null
    private val CAPTURE_IMAGE_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100
    var photoFile: File? = null
    var mCurrentPhotoPath: String = ""
   /* private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((activity?.application as MainApplication).repository)
    }*/
    var travelList: Word? = null
    var tourEnded = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var location: Location? = null

    var cordinate = ""
    var address = ""
    var latitude = ""
    var imageHashCode = ""
    var imageBitmap: Bitmap? = null
    var longitude = ""
    private var is_visible= false

    open fun EndTravelFragment(){}

    private lateinit var wordViewModel: WordViewModel
    lateinit var cmrDataViewModel: CMRDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = com.mahyco.cmr_app.databinding.FragmentEndTravelBinding.inflate(
            inflater,
            container,
            false
        )
        val root: View = binding.root
        msclass = Messageclass(this.activity)

      checkData()
        registerObserver()



        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        if(!isLocationEnabled(requireContext())) {
            // notify user

            AlertDialog.Builder(context)
                .setMessage("PLease enable your location from setting")
                .setPositiveButton("Enable",  DialogInterface.OnClickListener { dialogInterface, i ->
                    context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                })
                .setNegativeButton("Cancel",null)
                .show();
        }else{
            setListner()

        }

        val sharedPreference: SharedPreference = SharedPreference(requireContext())
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_NAME)
        val decryptedUserCode = ""+ EncryptDecryptManager.decryptStringData(encryptedUserCode)

        binding.lblwelcome.text = decryptedUserCode

        return root
    }


    private fun registerObserver() {
        cmrDataViewModel =
            ViewModelProviders.of(this)
                .get<CMRDataViewModel>(CMRDataViewModel::class.java)


        //        For handling Progress bar
        cmrDataViewModel!!.loadingLiveData.observe(this, androidx.lifecycle.Observer {
           /* if (it) {tjhtrjh
                llProgressBarStartTravel.visibility = View.VISIBLE
            } else {
                llProgressBarStartTravel.visibility = View.GONE
            }*/
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
                wordViewModel.getCurrentDateTravelType(currentDate, "start")
                    .observe(owner = this) { words ->
                        // Update the cached copy of the words in the adapter.
                        words.let {
                            Log.e("start", "onCreateView: " + it.size)
                        }
                        if (words.size == 0) {
                            if (is_visible) {
                                if (!tourEnded) {
                                    /*  showDialog(
                                    "Tour is not started for today",
                                    "Please start tour for today , then only you can end tour for today"
                                )*/
                                }
                            }
                            binding.btnstUpdate.isEnabled = false
                           // binding.btnstUpdate.visibility = View.GONE
                        } else {
                            binding.btnstUpdate.isEnabled = true
                            binding.btnstUpdate.visibility = View.VISIBLE
                        }
                        for (item in words) {
                            if (item.uType == "start") {
                                travelList = item
                                binding.spvehicletype.setText(item.uVehicleType)
                            }
                        }
                    }
                wordViewModel.getCurrentDateTravelType(currentDate, "end")
                    .observe(owner = this) { words ->
                        // Update the cached copy of the words in the adapter.
                        words.let {
                            Log.e("end", "onCreateView: " + it)
                        }
                        if (words != null && words.size != 0) {
                            if (is_visible) {
                                tourEnded = true
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (isNetworkConnected()) {
                                        try {
                                            val tourList: MutableList<Word> =
                                                wordViewModel.allWords() as MutableList<Word>
                                            if (tourList.size != 0) {
                                                cmrDataViewModel?.postTourEventDataAPI(tourList)

                                            }
                                        } catch (e: Exception) {
                                            Log.e("Internet error", "checkData: ",)
                                        }

                                    } else {
                                        val alertDialogEnd = AlertDialog.Builder(context).create()
                                        alertDialogEnd.setTitle("Crop Monitor Report")
                                        alertDialogEnd.setCancelable(false)
                                        alertDialogEnd.setMessage("Tour is ended for today")
                                        alertDialogEnd.setButton("OK") { dialog, which ->
                                            /* CoroutineScope(Dispatchers.Main).launch {
                                            *//* if (isNetworkConnected()) {
                                        val tourList: MutableList<Word> =
                                            wordViewModel.allWords() as MutableList<Word>
                                        if (tourList.size != 0) {
                                            cmrDataViewModel?.postTourEventDataAPI(tourList)

                                        }
                                    }*//*
                                    activity?.finish()
                                    startActivity(activity?.getIntent())
                                }*/
                                            activity?.finish()
                                            startActivity(activity?.getIntent())

                                        }
                                        if (!alertDialogEnd.isShowing) {
                                            alertDialogEnd.show()
                                        }
                                    }
                                }


                                /* showDialog(
                                "Tour already ended for today",
                                "Tour cycle is ended for today please comeback next day"
                            )*/
                                binding.btnstUpdate.isEnabled = false
                               // binding.btnstUpdate.visibility = View.GONE
                                return@observe
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
        }else{
            is_visible = false
        }

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
                                binding.txtlocation.setText(cityName)*/
                            }
                        }
                }
            }
        }catch (e:Exception){
            Log.e("error", "checkBackgroundLocation: "+e.message )
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

    private fun validate(): Boolean {

        /*if (binding.txtlocation.text.length == 0) {
            msclass?.showMessage("Please enter correct location")
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

        if (!Constant.isTimeAutomatic(requireContext())){
            msclass?.showAutomaticTimeMessage("Please update time setting to automatic")
            return false
        }

        return true

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
                } else {

                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("You need to allow Camera permission to perform further operations")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Allow"
                        ) { dialog, id ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri =
                                Uri.fromParts("package", requireContext().getPackageName(), null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .setNegativeButton(
                            "Deny"
                        ) { dialog, id ->  }
                    val alert = builder.create()
                    alert.show()
//                    Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
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
                            checkLocationPermission()
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
        }

    }

    private fun setListner() {

        if (imageBitmap == null){
            binding.ivImage.visibility == View.GONE
        }else{
            binding.ivImage.visibility == View.VISIBLE
            binding?.ivImage?.setImageBitmap(imageBitmap)
        }

        binding.btnstUpdate.setOnClickListener {
            val sd = SimpleDateFormat("MM/dd/yyyy")
            val currentDate = sd.format(Date())

            CoroutineScope(Dispatchers.Main).launch {
                val list = wordViewModel.getCurrentDateTypeTravel(currentDate, "end")
                if (list != null && list.size != 0) {
                    tourEnded = true
                    val alertDialogEnd = AlertDialog.Builder(context).create()
                    alertDialogEnd.setTitle("Crop Monitor Report")
                    alertDialogEnd.setMessage("Tour is ended for today")
                    alertDialogEnd.setButton("OK") { dialog, which ->
                     /*   activity?.finish()
                        startActivity(activity?.getIntent())*/
                    }
                    if (!alertDialogEnd.isShowing) {
                        alertDialogEnd.show()
                    }


                } else {
                    if (is_visible) {
                        if (validate()) {

                            lifecycleScope.launch {
                                addData()
                            }

                        }
                    }
                }
            }


          /*  wordViewModel.getCurrentDateTravelType(currentDate, "end")
                .observe(owner = this) { words ->
                    // Update the cached copy of the words in the adapter.



                }*/

        }


        _binding?.btnTakephoto?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_DENIED) {

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
            }else{
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                    ),
                    MY_CAMERA_PERMISSION_CODE
                )

            }

        }
        intialbinddata()

    }

    private suspend fun addData() {
        checkLocationPermission()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val currentDateTime = sdf.format(Date())

        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())

        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val vehicleId = travelList?.uVehicleId
        val vehicleType = travelList?.uVehicleType
        val travel = Word(
            0,
            travelList?.uStartDateTime.toString(),
            currentDateTime,
            "",
            vehicleId.toString(),
            vehicleType.toString(),
            address,
            travelList?.uStartLat.toString(),
            travelList?.uStartLng.toString(),
            latitude,
            longitude,
            "",
            "",
          travelList?.uKmReadingStart.toString(),
            travelList?.uKmImageStart.toString(),
            binding.txtkm.text.toString(),
            encoded,
            "",
            "0",
            "end",
            "",
            "0",
            "0",
            "",
            currentDate,"0","0"

        )

        if (travel.uEndLat.isNotEmpty()&&travel.uEndLng.isNotEmpty() && !travel.uEndLat.equals("0.0")&& !travel.uEndLng.equals("0.0")) {
            wordViewModel.insert(travel)
        }else{
            val gpsTracker = GPSTracker(context)
            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                latitude =   gpsTracker.latitude.toString()
                longitude = gpsTracker.longitude.toString()

                if (latitude.equals("")||longitude.equals("")){
                    msclass?.showMessage("Unable to access location")
                }else{
                    addData()
                }

                llProgressBarStartTravel.visibility = View.GONE
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
                binding?.ivImage?.setImageBitmap(bitmap )
                _binding?.ivImage?.visibility = View.VISIBLE
            }
        } else {
            msclass!!.showMessage("Request cancelled or something went wrong.")
        }
    }
    private fun myBitmap(bitmap: Bitmap?): Bitmap {


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

    fun resizeImage(image: Bitmap): Bitmap {

        val width = image.width
        val height = image.height

        val scaleWidth = width / 10
        val scaleHeight = height / 10

        if (image.byteCount <= 1000000)
            return image

        return Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, false)
    }

    override fun onResume() {
        super.onResume()
        if (isLocationEnabled(context!!)){
            setListner()
            checkLocationPermission()
        }
    }

    fun intialbinddata() {
        //binding.spvehicletype.setAdapter(null)
        val gm: MutableList<GeneralMaster> = ArrayList<GeneralMaster>()
        gm.add(GeneralMaster("0", "SELECT VEHICLE TYPE"))
        gm.add(GeneralMaster("1", "COMPANY VEHICLE"))
        gm.add(GeneralMaster("2", "PERSONAL VEHICLE(4 wheeler)"))
        gm.add(GeneralMaster("3", "PERSONAL VEHICLE(2 wheeler)"))
        gm.add(GeneralMaster("4", "PUBLIC VEHICLE"))
        gm.add(GeneralMaster("5", "OTHER"))
        val adapter: ArrayAdapter<GeneralMaster> =
            ArrayAdapter<GeneralMaster>(
                this.context!!,
                android.R.layout.simple_spinner_dropdown_item,
                gm
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // binding.spvehicletype.setAdapter(adapter)

    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
/*
        @JvmStatic
        fun newInstance(param1: String, param2: String): EndTravelFragment {

            return EndTravelFragment(wordViewModel).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }*/
    }
}