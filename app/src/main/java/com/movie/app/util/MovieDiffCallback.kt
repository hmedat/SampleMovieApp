package com.movie.app.util

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.movie.app.modules.Movie

class MovieDiffCallback(
    private val mOldList: List<Movie>,
    private val mNewList: List<Movie>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].id == mNewList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition] == mNewList[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
