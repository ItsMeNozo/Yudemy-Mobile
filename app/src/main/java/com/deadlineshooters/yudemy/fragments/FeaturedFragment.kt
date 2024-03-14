package com.deadlineshooters.yudemy.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.deadlineshooters.yudemy.R
import com.deadlineshooters.yudemy.activities.MainActivity
import com.deadlineshooters.yudemy.adapters.CategoryAdapter1
import com.deadlineshooters.yudemy.adapters.CourseListAdapter
import com.deadlineshooters.yudemy.databinding.FragmentFeaturedBinding
import com.deadlineshooters.yudemy.viewmodels.CourseViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FeaturedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeaturedFragment : Fragment() {
    private lateinit var courseViewModel: CourseViewModel
    private var _binding: FragmentFeaturedBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeaturedBinding.inflate(inflater, container, false)
        // Inflate the layout containing the RecyclerView
        val view = inflater.inflate(R.layout.fragment_featured, container, false)

        // Obtain a reference to the RecyclerView
        val recyclerView = binding.categoryButtonList // Replace with your RecyclerView's ID

        // Create sample data for demonstration
        val categories = listOf(
            "Development", "Business", "Office Productivity", "Design",
            "Marketing", "Photography & Video", "Teaching & Academics",
            "Finance & Accounting", "IT & Software", "Personal Development",
            "Lifestyle", "Health & Fitness", "Music"
        )

        // Create an instance of the CategoryAdapter
        val adapter = CategoryAdapter1(categories)
        adapter.onItemClick = { category ->
            val fragment = FeaturedCategoryFragment()
            val bundle = Bundle()
            bundle.putString("category", category)
            fragment.arguments = bundle
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.frameLayout, fragment)
            fragmentTransaction?.commit()
        }

        // Set the adapter on the RecyclerView
        recyclerView.adapter = adapter

        // Set a horizontal LinearLayoutManager for horizontal scrolling
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.addItemDecoration(SpaceItemDecoration(8))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        courseViewModel.courses.observe(viewLifecycleOwner, Observer { courses ->
            val adapter = CourseListAdapter(requireContext(), R.layout.course_list_item, courses)
            binding.courseList.adapter = adapter
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space
            }
        }
    }
}