package hu.luciferi.foursquarevenuelister.ui.main.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hu.luciferi.foursquarevenuelister.R
import kotlinx.android.synthetic.main.activity_venue_details.*

class VenueDetailsActivity : AppCompatActivity() {

    private val viewModel : VenueDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)

        title = viewModel.name
        details_name.text = viewModel.name
        details_address.text = viewModel.address
        details_distance.text = viewModel.distance.toString()
        if (viewModel.imageSrc != null) {
            details_photo_image.setImageDrawable(viewModel.imageSrc)
            if (viewModel.isBtnPressed) {
                details_show_photos_btn.visibility = View.GONE
                details_photo_image.visibility = View.VISIBLE
            }
        }


        details_show_photos_btn.setOnClickListener {
            viewModel.isBtnPressed = true
            it.visibility = View.GONE
            details_photo_image.visibility = View.VISIBLE
            viewModel.downloadPhotos()
            viewModel.photos.observe(this, Observer {
                Toast.makeText(this, "${viewModel.photos.value?.size ?: "empty"}", Toast.LENGTH_SHORT).show()

                if (!viewModel.photos.value.isNullOrEmpty()) {
                    viewModel.imageIndex = 0
                    loadImage(viewModel.imageIndex)
                }
            })
        }

        details_photo_image.setOnClickListener {
            if (!viewModel.photos.value.isNullOrEmpty()) {
                val currentIndex = viewModel.imageIndex
                if ((currentIndex + 1) < viewModel.photos.value!!.size) {
                    viewModel.imageIndex++
                }
                else {
                    viewModel.imageIndex = 0
                }
                if (currentIndex != viewModel.imageIndex) {

                    loadImage(viewModel.imageIndex)

                    Toast.makeText(this, "${viewModel.imageIndex}", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "no more images", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun loadImage(index : Int){
        val requestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher_round)
        //requestOptions.placeholder(R.mipmap.ic_launcher)
        //requestOptions.error(R.mipmap.ic_launcher_round)
        val url = "${viewModel.photos.value!![index].prefix}${viewModel.photos.value!![index].width}x${viewModel.photos.value!![index].height}${viewModel.photos.value!![index].suffix}"
        Glide.with(this)
            //.load("https://fastly.4sqi.net/img/general/540x720/YlJg-6QVaHF1nGqNCDnjPtADHNfRa8IAv4yvKBUQVuU.jpg")
            .load(url)
            .apply(requestOptions)
            .into(details_photo_image)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.imageSrc = details_photo_image.drawable
    }
}