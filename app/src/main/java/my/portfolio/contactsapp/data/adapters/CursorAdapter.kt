package my.portfolio.contactsapp.data.adapters

import android.database.Cursor
import android.database.DataSetObserver
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class CursorAdapter<VH : RecyclerView.ViewHolder?>(private var cursor: Cursor) :
    RecyclerView.Adapter<VH>() {
    private var mDataValid = false
    private var mRowIDColumn = 0
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    protected abstract fun onBindViewHolder(holder: VH, cursor: Cursor?)
    override fun onBindViewHolder(holder: VH, position: Int) {
        check(mDataValid) { "this should only be called when the cursor is valid" }
        check(cursor.moveToPosition(position)) { "couldn't move cursor to position $position" }
        onBindViewHolder(holder, cursor)
    }

    override fun getItemId(position: Int): Long {
        return if (mDataValid && cursor.moveToPosition(position)) {
            cursor.getLong(mRowIDColumn)
        } else RecyclerView.NO_ID
    }

    override fun getItemCount(): Int {
        return if (mDataValid) {
            cursor.count
        } else {
            0
        }
    }

//    fun changeCursor(cursor: Cursor?) {
//        val old: Cursor? = swapCursor(cursor)
//        if (old != null) {
//            old.close()
//        }
//    }

    private fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val oldCursor: Cursor? = cursor
        if (oldCursor != null) {
            if (mDataSetObserver != null) {
                oldCursor.unregisterDataSetObserver(mDataSetObserver)
            }
        }
        if (newCursor != null) {
            cursor = newCursor
        }
        if (newCursor != null) {
            if (mDataSetObserver != null) {
                newCursor.registerDataSetObserver(mDataSetObserver)
            }
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id")
            mDataValid = true
            notifyDataSetChanged()
        } else {
            mRowIDColumn = -1
            mDataValid = false
            notifyDataSetChanged()
        }
        return oldCursor
    }

    private val mDataSetObserver: DataSetObserver? = object : DataSetObserver() {
        override fun onChanged() {
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            mDataValid = false
            notifyDataSetChanged()
        }
    }

    init {
//        setHasStableIds(true)
        swapCursor(cursor)
    }
}