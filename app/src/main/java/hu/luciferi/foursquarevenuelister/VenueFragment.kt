package hu.luciferi.foursquarevenuelister

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData

/**
 * A fragment representing a list of Items.
 */
class VenueFragment : Fragment() {

    private val viewModel : VenueViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_venue_list, container, false)

        val venueAdapter = VenueRecyclerViewAdapter(viewModel.searchData)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }

                viewModel.searchData.observe(viewLifecycleOwner, Observer {
                    adapter = venueAdapter
                })
            }
        }
        venueAdapter.setOnItemClickListener(object : VenueRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "clicked on #${position}", Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }
}