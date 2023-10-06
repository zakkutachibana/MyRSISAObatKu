package com.zak.rsisaobatku.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zak.rsisaobatku.R
import com.zak.rsisaobatku.data.remote.response.ArticlesItem
import com.zak.rsisaobatku.util.DateHelper.withDateFormat

class NewsAdapter(private val listNews: ArrayList<ArticlesItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_news_title)
        val tvAuthor: TextView = itemView.findViewById(R.id.tv_news_author)
        val tvDate: TextView = itemView.findViewById(R.id.tv_news_date)
        val ivNews : ImageView = itemView.findViewById(R.id.iv_news)
        val ivBrowse : ImageView = itemView.findViewById(R.id.iv_to_browser)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.tvTitle.text = listNews[position].title
        holder.tvAuthor.text = listNews[position].author.toString()
        holder.tvDate.text = listNews[position].publishedAt.withDateFormat()
        holder.ivNews.load(listNews[position].urlToImage) {
            placeholder(R.drawable.draaaven)
            crossfade(true)
        }

        holder.ivBrowse.setOnClickListener {
            onItemClickCallback.onItemClicked(listNews[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listNews.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ArticlesItem)
    }
}