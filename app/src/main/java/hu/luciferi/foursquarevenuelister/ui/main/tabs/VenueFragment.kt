package hu.luciferi.foursquarevenuelister.ui.main.tabs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import hu.luciferi.foursquarevenuelister.R
import hu.luciferi.foursquarevenuelister.VenueRecyclerViewAdapter
import hu.luciferi.foursquarevenuelister.VenueViewModel
import hu.luciferi.foursquarevenuelister.ui.main.details.VenueDetailsActivity
import kotlinx.android.synthetic.main.fragment_venue_list.*

/**
 * A fragment representing a list of Items.
 */
class VenueFragment : Fragment() {

    private val viewModel : VenueViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_venue_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener {

            viewModel.refreshSearchData()
            Toast.makeText(context, "Refreshing data...", Toast.LENGTH_SHORT).show()
            swipe_refresh.isRefreshing = false
        }

        setupRecycler()

        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            //(adapter as VenueRecyclerViewAdapter).notifyDataSetChanged()
            //(list.adapter as VenueRecyclerViewAdapter).setData(it)
            setupRecycler()
        })
    }

    private fun setupRecycler(){
        // Set the adapter

        list.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        val venueAdapter =
            VenueRecyclerViewAdapter(viewModel.searchData)

        list.adapter = venueAdapter

        venueAdapter.setOnItemClickListener(object :
            VenueRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, VenueDetailsActivity::class.java).apply {
                    val venue = viewModel.searchData.value!![position]
                    putExtra("name", venue.name)
                    putExtra("address", venue.location.address)
                    putExtra("distance", venue.location.distance)
                    putExtra("id", venue.id)
                }
                startActivity(intent)
            }
        })
    }
}