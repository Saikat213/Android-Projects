package com.example.fundooapp

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class Pagination(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val visibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if ((visibleItemCount + visibleItemPosition) >= totalItemCount &&
                visibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
            loadMoreItems()
        }
    }

    abstract fun loadMoreItems()
    abstract fun isLoading() : Boolean
    abstract fun isLastPage() : Boolean

    companion object {
        private const val PAGE_SIZE = 10
        const val PAGE_START = 1
    }
}