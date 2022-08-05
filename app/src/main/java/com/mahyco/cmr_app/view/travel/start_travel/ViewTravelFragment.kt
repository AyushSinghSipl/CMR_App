package com.mahyco.cmr_app.view.travel.start_travel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.android.roomwordssample.Word
import com.example.android.roomwordssample.WordViewModel
import com.mahyco.cmr_app.core.Constant
import com.mahyco.cmr_app.core.Messageclass
import com.mahyco.isp.core.MainApplication
import com.mahyco.rcbucounterboys2020.utils.EncryptDecryptManager
import com.mahyco.rcbucounterboys2020.utils.SharedPreference
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewTravelFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: com.mahyco.cmr_app.databinding.FragmentViewTravelBinding? = null
    private val binding get() = _binding!!
    var msclass: Messageclass? = null
    private val eventList: MutableList<Word> = ArrayList()
    private var list: MutableList<Word> = ArrayList()
    private var adapter: EventsAdapter? = null
    private lateinit var wordViewModel: WordViewModel


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

        _binding = com.mahyco.cmr_app.databinding.FragmentViewTravelBinding.inflate(
            inflater,
            container,
            false
        )
        val root: View = binding.root
        msclass = Messageclass(this.activity)
        val sd = SimpleDateFormat("MM/dd/yyyy")
        val currentDate = sd.format(Date())
        wordViewModel = WordViewModel(
            (activity?.application as MainApplication).repository,
            activity?.application as MainApplication
        )


        runBlocking {
            list.clear()
            list = wordViewModel.getCurrentTravel(currentDate) as MutableList<Word>

            for (item in list) {
                if (item.uType == "end") {

                    var spfdate = SimpleDateFormat("MM/dd/yyyy")
                    val uDate = spfdate.parse(item.uDate)
                    spfdate = SimpleDateFormat("dd/MM/yyyy")
                    val u_date = spfdate.format(uDate)

                    binding.textViewDate.text = u_date
                    var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val newDate = spf.parse(item.uStartDateTime)
                    spf = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa")
                    val date = spf.format(newDate);
                    binding.textViewStartDateTime.text = date.toString()
                    var spfEnd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val endDate = spfEnd.parse(item.uEndDateTime)
                    val endDateTime = spf.format(endDate);
                    binding.textViewEndDateTime.text = endDateTime

                    if (item.uVehicleType != "OTHER") {

                        binding.textViewStartKm.text = item.uKmReadingStart
                        binding.textViewEndKm.text = item.uKmReadingEnd

                      /*  val decodedString: ByteArray =
                            Base64.decode(item.uKmImageStart, Base64.DEFAULT)*/
                        val decodedByte: Bitmap =
                            BitmapFactory.decodeByteArray(item.uKmImageStart, 0, item.uKmImageStart.size)
                        binding.imageViewStart.setImageBitmap(decodedByte)

                       /* val decodedStringEnd: ByteArray =
                            Base64.decode(item.uKmImageEnd, Base64.DEFAULT)*/
                        val decodedByteEnd: Bitmap =
                            BitmapFactory.decodeByteArray(
                                item.uKmImageEnd,
                                0,
                                item.uKmImageEnd.size
                            )
                        binding.imageViewEnd.setImageBitmap(decodedByteEnd)
                    } else {
                        binding.textViewStartKm.visibility = View.GONE
                        binding.textViewEndKm.visibility = View.GONE
                        binding.imageViewEnd.visibility = View.GONE
                        binding.imageViewStart.visibility = View.GONE
                        binding.startKmText.visibility = View.GONE
                        binding.endKmText.visibility = View.GONE
                        binding.startImageText.visibility = View.GONE
                        binding.endImageText.visibility = View.GONE
                    }

                    if (item.uStatus == "1") {
                        binding.textViewStatus.text = "Uploaded"
                    } else {
                        binding.textViewStatus.text = "Not-Uploaded"
                    }
                } else
                    if (item.uType == "add_event") {
                        eventList.add(item)
                    }
            }
        }
        adapter = EventsAdapter(requireContext(), eventList)
        binding.expendableList!!.setAdapter(adapter)

        binding.expendableList.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
            setListViewHeight(parent, groupPosition)
            false
        })

        val sharedPreference: SharedPreference = SharedPreference(requireContext())
        val encryptedUserCode = sharedPreference.getValueString(Constant.USER_NAME)
        val decryptedUserCode = "" + EncryptDecryptManager.decryptStringData(encryptedUserCode)

        binding.lblwelcome.text = decryptedUserCode

        return root
    }


    private fun setListViewHeight(
        listView: ExpandableListView,
        group: Int
    ) {
        val listAdapter: ExpandableListAdapter =
            listView.getExpandableListAdapter() as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.getWidth(),
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.getGroupCount()) {
            val groupItem: View = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem: View = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params: ViewGroup.LayoutParams = listView.getLayoutParams()
        var height: Int = (totalHeight
                + listView.getDividerHeight() * (listAdapter.getGroupCount() - 1))
        if (height < 10) height = 200
        params.height = height
        listView.setLayoutParams(params)
        listView.requestLayout()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewTravelFragment.
         */
        // TODO: Rename and change types and number of parameters
      /*  @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewTravelFragment(wordViewModel).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/
    }
}