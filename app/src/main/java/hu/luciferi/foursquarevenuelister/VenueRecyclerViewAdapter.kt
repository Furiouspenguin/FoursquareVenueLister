package hu.luciferi.foursquarevenuelister

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData


class VenueRecyclerViewAdapter(
    private val values: LiveData<List<SearchData>>
) : RecyclerView.Adapter<VenueRecyclerViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    private var listener : OnItemClickListener? = null


    //private var values : List<SearchData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venue, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values.value!!.get(position)//values.get(position)
        holder.idView.text = item.name
        holder.contentView.text = item.location.address
    }

    override fun getItemCount(): Int = values.value?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_name)
        val contentView: TextView = view.findViewById(R.id.item_location)
/*
        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/

        init {
            itemView.setOnClickListener {
                if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(adapterPosition)
                }
            }
        }
    }

    /*
    fun setData(data : List<SearchData>) {
        if(data.isEmpty()) {
            return
        }

        values = data
        Log.e("notify","notify")
        notifyDataSetChanged()
    }*/
}