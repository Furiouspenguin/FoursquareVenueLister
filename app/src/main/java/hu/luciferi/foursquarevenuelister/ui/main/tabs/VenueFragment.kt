package hu.luciferi.foursquarevenuelister.ui.main.tabs

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import hu.luciferi.foursquarevenuelister.MainActivity
import hu.luciferi.foursquarevenuelister.R
import hu.luciferi.foursquarevenuelister.VenueRecyclerViewAdapter
import hu.luciferi.foursquarevenuelister.VenueViewModel
import hu.luciferi.foursquarevenuelister.ui.main.details.VenueDetailsActivity
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_venue_list.*

/**
 * A fragment representing a list of Items.
 */
class VenueFragment : Fragment() {

    private val viewModel : VenueViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_venue_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshSearchData()

        swipe_refresh.setOnRefreshListener {

            //Toast.makeText(context, viewModel.searchData.hasActiveObservers().toString(), Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Refreshing data... Currently ${viewModel.searchData.value?.size ?: "null"}", Toast.LENGTH_SHORT).show()


            viewModel.refreshSearchData()
            setupRecycler()


            list.adapter!!.notifyDataSetChanged()
            if (!viewModel.searchData.hasActiveObservers()){
                viewModel.searchData.observe(viewLifecycleOwner, Observer {
                    list.adapter!!.notifyDataSetChanged()
                })
            }
            swipe_refresh.isRefreshing = false
        }

        setupRecycler()
        list.adapter!!.notifyDataSetChanged()
        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            list.adapter!!.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_refresh -> {
                Toast.makeText(context, "Refreshing data... Last datasize: ${viewModel.searchData.value?.size ?: "null"}", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).calculateLocation(true)
                setupRecycler()
                list.adapter!!.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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