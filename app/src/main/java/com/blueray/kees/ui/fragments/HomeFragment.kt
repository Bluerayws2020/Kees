package com.blueray.kees.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.kees.R
import com.blueray.kees.ui.activities.HomeActivity
import com.blueray.kees.adapters.HomeCategoryAdapter
import com.blueray.kees.adapters.HomeSliderAdapter
import com.blueray.kees.databinding.FragmentHomeBinding
import com.blueray.kees.helpers.CarouselLayoutManager
import com.blueray.kees.helpers.CustomLinearSmoothScroller
import com.blueray.kees.helpers.HelperUtils
import com.blueray.kees.model.GetMainCategoriesData
import com.blueray.kees.model.NetworkResults
import com.blueray.kees.ui.AppViewModel
import com.blueray.kees.ui.activities.CartActivity
import com.blueray.kees.ui.activities.ProductsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var sliderAdapter :HomeSliderAdapter
    private lateinit var categoryAdapter :HomeCategoryAdapter
    private lateinit var binding : FragmentHomeBinding
    private val viewModel: AppViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        (requireContext() as HomeActivity).setSelectedId(R.id.homeFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.includedTap.menuButton.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }
        binding.includedTap.cartLabel.setOnClickListener {
            startActivity(Intent(requireContext(),CartActivity::class.java)).apply {
                // add extras
            }
        }
        viewModel.retrieveMainCategories()
        getData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init Adapters
        initSliderAdapter()


        // set on scroll listener for nested scroll view
        binding.nested.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY && scrollY > 0) {
                // Scrolling down and not at the top
                if((activity as HomeActivity).getIsBottomShowing()){
                    (activity as HomeActivity).animateHideBottomNav()
                }
            } else {
                // Scrolling up or at the top
                if(!(activity as HomeActivity).getIsBottomShowing()){
                    (activity as HomeActivity).animateShowBottomNav()
                }
            }
        }

        // navigate to all categories
        binding.allCategoriesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allCategoriesFragment)
        }



    }

    private fun initCategoriesItem(list: List<GetMainCategoriesData>) {
        categoryAdapter = HomeCategoryAdapter(list){
            startActivity(Intent(requireActivity(),ProductsActivity::class.java).apply {
                putExtra("cat_id",it)
            })
        }
        val lm = GridLayoutManager(requireContext(),3)
        binding.categoriesRv.layoutManager = lm
        binding.categoriesRv.adapter = categoryAdapter
    }

    private fun initSliderAdapter() {
        sliderAdapter = HomeSliderAdapter(listOf())
        val lm = CarouselLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        binding.sliderRv.layoutManager = lm
        binding.sliderRv.adapter = sliderAdapter

        // start Scroll
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            async{
                startAutoScroll()
            }
        }
    }

    // slider implementation
    private suspend fun startAutoScroll() {
        lifecycleScope.launch{

            delay(2000)
            val layoutManager = binding.sliderRv.layoutManager as CarouselLayoutManager
            val currentLocation = layoutManager.findFirstVisibleItemPosition()

            val itemCount = sliderAdapter.itemCount
            val nextPosition = (currentLocation + 1) % itemCount

            if(nextPosition == 0){
                startReverseAutoScroll()
                return@launch
            }

            val smoothScroller = CustomLinearSmoothScroller(requireContext())

            // Set the target position and start the smooth scroll
            smoothScroller.targetPosition = nextPosition
            layoutManager.startSmoothScroll(smoothScroller)
            // Call startAutoScroll recursively
            startAutoScroll()
        }
    }

    private suspend fun startReverseAutoScroll() {
        lifecycleScope.launch{
            delay(1500)
            val layoutManager = binding.sliderRv.layoutManager as CarouselLayoutManager
            val currentLocation = layoutManager.findFirstVisibleItemPosition()

            val nextPosition = currentLocation - 1

            if(currentLocation == 0){
                startAutoScroll()
                return@launch
            }

            val smoothScroller = CustomLinearSmoothScroller(requireContext())

            // Set the target position and start the smooth scroll
            smoothScroller.targetPosition = nextPosition
            layoutManager.startSmoothScroll(smoothScroller)

            // Call startAutoScroll recursively
            startReverseAutoScroll()
        }
    }

    private fun getData(){
        viewModel.getMainCategories().observe(this){
                result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        initCategoriesItem(result.data.data)
                    } else {
                        HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                    }
                }
                is NetworkResults.ErrorMessage -> {
                    HelperUtils.showMessage(requireContext(), result.data?.message ?: getString(R.string.Error))
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    HelperUtils.showMessage(requireContext(), getString(R.string.Error))
                }
            }
        }
    }
}