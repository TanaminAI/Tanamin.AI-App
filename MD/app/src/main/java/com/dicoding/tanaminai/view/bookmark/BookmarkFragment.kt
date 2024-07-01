package com.dicoding.tanaminai.view.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tanaminai.R
import com.dicoding.tanaminai.data.local.entity.BookmarkEntity
import com.dicoding.tanaminai.databinding.FragmentBookmarkBinding
import com.dicoding.tanaminai.view.factory.MainViewModelFactory
import com.dicoding.tanaminai.view.prediction.ResultActivity


class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val bookmarkViewModel: BookmarkViewModel by viewModels() {
        MainViewModelFactory.getInstance(requireActivity())
    }

    private val adapter = BookmarkAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvBookmark.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvBookmark.addItemDecoration(itemDecoration)

        bookmarkViewModel.getAllBookmark()
            .observe(viewLifecycleOwner) { bookmarkList: List<BookmarkEntity> ->
                setBookmarkData(bookmarkList)
            }
    }

    private fun setBookmarkData(bookmarkList: List<BookmarkEntity>) {
        adapter.setOnItemClickCallback(object : BookmarkAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BookmarkEntity) {
                val mBundle = Bundle()
                mBundle.putString(ResultActivity.EXTRA_RESULT, data.result)
                mBundle.putString(ResultActivity.EXTRA_TIME, data.predictAt)
                mBundle.putString(ResultActivity.EXTRA_N, data.n)
                mBundle.putString(ResultActivity.EXTRA_P, data.p)
                mBundle.putString(ResultActivity.EXTRA_K, data.k)
                mBundle.putString(ResultActivity.EXTRA_HUM, data.hum)
                mBundle.putString(ResultActivity.EXTRA_PH, data.ph)
                mBundle.putString(ResultActivity.EXTRA_TEMP, data.temp)
                findNavController().navigate(R.id.act_bookmarkFrag_to_resultActivity, mBundle)
            }
        })
        adapter.submitList(bookmarkList)
        binding.rvBookmark.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}