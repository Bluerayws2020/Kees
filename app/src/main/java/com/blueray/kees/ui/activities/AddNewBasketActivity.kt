package com.blueray.kees.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.adapters.DayOfTheWeekAdapter
import com.blueray.kees.adapters.ShiftsAdapter
import com.blueray.kees.databinding.ActivityAddNewBasketBinding
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.helpers.ViewUtils.hide
import com.blueray.kees.helpers.ViewUtils.show
import com.blueray.kees.model.Day
import com.blueray.kees.model.Days
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.model.ShiftsData
import com.blueray.kees.ui.AppViewModel

class AddNewBasketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewBasketBinding
    private val viewModel by viewModels<AppViewModel>()
    private lateinit var shiftsAdapter: ShiftsAdapter
    private lateinit var daysAdapter: DayOfTheWeekAdapter
    private var shiftsList = listOf<ShiftsData>()
    private var activeDaysList = listOf<Days>()
    private var selectedWeeks = listOf<Int>()
    private var selectedDay = ""
    private var weekNum = 0
    private var weeksList = listOf(
        Days(Day("week 1", "1")),
        Days(Day("week 2", "2")),
        Days(Day("week 3", "3")),
        Days(Day("week 4", "4"))
    )
    private var missingWeeksList = mutableListOf<Int>()
    private lateinit var weekAdapter: DayOfTheWeekAdapter
    private var selectedShift: ShiftsData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareAppBar("Add New Basket")
        //api calls
        viewModel.retrieveShifts()
        viewModel.retrieveWeeklyCart()

        //observe live data
        getShifts()
        getBasketsData()
        getAddedData()

        //hide for some cool looking screen
        binding.daysRv.hide()
        binding.chooseDay.hide()
        binding.weeksSpinner.hide()
        binding.ChooseWeek.hide()

        //set up the recycler views
        setUpShiftsRv()
        setUpDaysRv()
        setUpWeeksRv()

        binding.continueBtn.setOnClickListener {
            Log.d("TESTERME", selectedShift?.start_time.toString())
            Log.d("TESTERME", selectedShift?.end_time.toString())
            Log.d("TESTERME", selectedDay.toString())
            Log.d("TESTERME", weekNum.toString())
            viewModel.retrieveAddWeeklyBasket(
                weekNum.toString(),
                selectedShift?.start_time.toString(),
                selectedShift?.end_time.toString(),
                selectedDay
            )
        }
    }
    private fun prepareAppBar(title: String) {
        binding.includedTap.menu.setImageResource(R.drawable.back_button)
        binding.includedTap.menu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.includedTap.back.hide()
        binding.includedTap.title.text = title
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
                    weekNum = it.dayWeek.day_number.toInt()
                }
                if (it != weeksList[position]) {
                    it.selected = false
                }
            }
            selectedWeeks = newSelectedWeeksList


        }
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.weeksSpinner.adapter = weekAdapter
        binding.weeksSpinner.layoutManager = lm
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

    private fun getBasketsData() {
        viewModel.getWeeklyCart().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        val apiWeeksList = mutableListOf<Int>()
                        result.data.data.forEach { weeklyCart ->
                            when (weeklyCart.week_number) {
                                "First" -> apiWeeksList.add(1)
                                "Second" -> apiWeeksList.add(2)
                                "Third" -> apiWeeksList.add(3)
                                "Fourth" -> apiWeeksList.add(4)
                            }
                        }

                        missingWeeksList = filterMissingWeeks(apiWeeksList)
                        Log.d("misasas", missingWeeksList.toString())

                        weeksList = missingWeeksList.map { weekNumber ->
                            Days(Day("week $weekNumber", weekNumber.toString()))
                        }

                        // Notify the adapter about the dataset change
                        weekAdapter.list = weeksList
                        weekAdapter.notifyDataSetChanged()
                        Log.d("MESSI", weeksList.toString())
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

    private fun getAddedData() {
        viewModel.getAddWeeklyBasket().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {

                        HelperUtils.showMessage(
                            this,
                            result.data.message ?: result.data.data.toString()
                        )
                        startActivity(Intent(this, CartActivity::class.java))
                    } else {
                        HelperUtils.showMessage(
                            this,
                            result.data.message ?: result.data.data.toString()
                        )
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

    fun filterMissingWeeks(selectedWeeks: MutableList<Int>, maxWeeks: Int = 4): MutableList<Int> {
        val allWeeks = (1..maxWeeks).toMutableList()
        val missingWeeks = allWeeks.filter { !selectedWeeks.contains(it) }
        return missingWeeks.toMutableList()
    }

}