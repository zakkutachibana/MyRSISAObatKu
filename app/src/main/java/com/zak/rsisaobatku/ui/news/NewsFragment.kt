package com.zak.rsisaobatku.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zak.rsisaobatku.MainActivity
import com.zak.rsisaobatku.data.remote.response.ArticlesItem
import com.zak.rsisaobatku.databinding.FragmentNewsBinding
import com.zak.rsisaobatku.util.NewsAdapter

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvNews.layoutManager = layoutManager

        setViewModel()
        return binding.root
    }

    private fun setViewModel() {
        newsViewModel.getNews()
        newsViewModel.newsItems.observe(viewLifecycleOwner) { newsItems ->
            setNewsList(newsItems)
        }
        newsViewModel.newsStatus.observe(viewLifecycleOwner) {
//            Toast.makeText(activity, it.status )
        }

    }

    private fun setNewsList(newsItems: List<ArticlesItem>) {
        val listNews = ArrayList<ArticlesItem>()
        for (newsItem in newsItems) {
            listNews.add(newsItem)
        }
        val adapter = NewsAdapter(listNews)
        binding.rvNews.adapter = adapter

        adapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticlesItem) {
                AlertDialog.Builder(activity as MainActivity).apply {
                    setMessage("Anda akan dialihkan ke artikel berita. Lanjutkan?")
                    setPositiveButton("Ya") { _, _ ->
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                        startActivity(browserIntent)
                    }
                    setNegativeButton("Tidak") { _, _ ->
                    }
                    create()
                    show()
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}