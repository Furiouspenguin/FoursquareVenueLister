package hu.luciferi.foursquarevenuelister.retrofit.model

data class FSLocation(var address : String?, var crossStreet : String?, var lat : Float?, var lng : Float?, var labeledLatLngs : List<LabeledLatLng>?,
                      var distance : Int?, var postalCode : String?, var cc : String?, var city : String?, var state : String?, var country : String?,
                      var formattedAddress : List<String>?
)