package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DayOfTheWeekAdapter
import com.blueray.kees.adapters.ShiftsAdapter
import com.blueray.kees.databinding.ActivityChooseOrderRecievalTimeBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.HelperUtils.showMessage
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.Day
import com.blueray.kees.model.Days
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.ShiftsData
import com.blueray.kees.ui.AppViewModel

class ChooseOrderReceiveTime : BaseActivity() {

    private lateinit var binding: ActivityChooseOrderRecievalTimeBinding
    private val viewModel by viewModels<AppViewModel>()
    private lateinit var shiftsAdapter: ShiftsAdapter
    private lateinit var daysAdapter: DayOfTheWeekAdapter
    private var shiftsList = listOf<ShiftsData>()
    private var activeDaysList = listOf<Days>()
    private var selectedWeeks = listOf<Int>()
    private var selectedDay = ""
    private var weeksList = listOf(
        Days(Day("week 1","1")),
        Days(Day("week 2","2")),
        Days(Day("week 3","3")),
        Days(Day("week 4","4"))
    )
    private lateinit var weekAdapter: DayOfTheWeekAdapter
    private var selectedShift :ShiftsData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseOrderRecievalTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueBtn.setOnClickListener {
            verifyData()
        }
        binding.includedTap.title.text = getString(R.string.create_accounttt)
        binding.previousBtn.setOnClickListener {
            onBackPressed()
        }

//      hide data
        binding.daysRv.hide()
        binding.chooseDay.hide()
        binding.weeksSpinner.hide()
        binding.ChooseWeek.hide()

        setUpShiftsRv()
        setUpDaysRv()
        setUpWeeksRv()

        // get Shifts Api
        viewModel.retrieveShifts()

        // observe to LiveData
        getShifts()
        getData()



    }

    private fun verifyData() {
        if(selectedShift == null){
            showMessage(this,"Please Choose shift")
            return
        }
        if(selectedDay.isNullOrEmpty()){
            showMessage(this,"Please Choose Day")
            return
        }
        if (selectedWeeks.isNullOrEmpty()){
            showMessage(this,"Please Choose Weeks")
            return
        }
        viewModel.retrieveWeeksAfterRegistration(selectedWeeks,selectedShift!!.end_time,selectedShift!!.start_time,selectedDay)
    }

    private fun setUpWeeksRv() {
        weekAdapter = DayOfTheWeekAdapter(weeksList) { _, position ->

            weeksList[position].selected = !weeksList[position].selected

            weekAdapter.list = weeksList
            weekAdapter.notifyDataSetChanged()

            // add just the selected Items to the selected list
            val newSelectedWeeksList = mutableListOf<Int>()
            weeksList.forEach {
                if (it.selected) {
                    newSelectedWeeksList.add(it.dayWeek.day_number.toInt())
                }
            }
            selectedWeeks = newSelectedWeeksList


        }
        val lm = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.weeksSpinner.adapter = weekAdapter
        binding.weeksSpinner.layoutManager =lm
    }

    private fun setUpDaysRv() {
        daysAdapter = DayOfTheWeekAdapter(listOf()) { id, position ->
            activeDaysList[position].selected = !activeDaysList[position].selected

            activeDaysList.forEach {
                if (it != activeDaysList[position]) {
                    it.selected = false
                }
            }

            daysAdapter.list = activeDaysList
            daysAdapter.notifyDataSetChanged()

            if (activeDaysList[position].selected) {
                // call next Api
                binding.weeksSpinner.show()
                binding.ChooseWeek.show()
                selectedDay = id
                weeksList.forEach {
                    it.selected = false
                }
                weekAdapter.list = weeksList
                weekAdapter.notifyDataSetChanged()
            } else {
                // hide all
                selectedDay = ""
                weeksList.forEach {
                    it.selected = false
                }
                weekAdapter.list = weeksList
                weekAdapter.notifyDataSetChanged()

                binding.weeksSpinner.hide()
                binding.ChooseWeek.hide()
            }
        }
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.daysRv.adapter = daysAdapter
        binding.daysRv.layoutManager = lm
    }

    private fun setUpShiftsRv() {
        shiftsAdapter = ShiftsAdapter(listOf()) { _, position ->
            shiftsList[position].selected = !shiftsList[position].selected
            shiftsList.forEach {
                if (it != shiftsList[position]) {
                    it.selected = false
                }
            }
            shiftsAdapter.list = shiftsList
            shiftsAdapter.notifyDataSetChanged()
            if (shiftsList[position].selected) {
                // call next Api
                binding.daysRv.show()
                binding.chooseDay.show()
                selectedShift = shiftsList[position]
                activeDaysList = selectedShift!!.active_days.map {
                    Days(
                        it,
                        false
                    )
                }
                weeksList.forEach {
                    it.selected = false
                }
                weekAdapter.list = weeksList
                weekAdapter.notifyDataSetChanged()
                daysAdapter.list = activeDaysList
                daysAdapter.notifyDataSetChanged()


            } else {
                selectedShift = null
                // hide all
                daysAdapter.list = listOf()
                daysAdapter.notifyDataSetChanged()
                binding.daysRv.hide()
                binding.chooseDay.hide()
                binding.weeksSpinner.hide()
                binding.ChooseWeek.hide()
            }
        }
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.shiftsRv.adapter = shiftsAdapter
        binding.shiftsRv.layoutManager = lm
    }

    private fun getShifts() {
        viewModel.getShifts().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        shiftsAdapter.list = result.data.data
                        shiftsList = result.data.data
                        shiftsAdapter.notifyDataSetChanged()

                    } else {
                        HelperUtils.showMessage(this, getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(this, getString(R.string.Error))
                }
            }
        }
    }

    private fun getData(){
        viewModel.getWeeksAfterRegistration().observe(this){
            result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        // nothing
                        // todo dismiss progress dialog when added
                        startActivity(Intent(this,HomeActivity::class.java))
                    } else {
                        showMessage(this, getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    showMessage(
                        this,
                        result.data?.message ?: getString(R.string.Error)
                    )
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    showMessage(this, getString(R.string.Error))
                }
            }
        }
    }
}