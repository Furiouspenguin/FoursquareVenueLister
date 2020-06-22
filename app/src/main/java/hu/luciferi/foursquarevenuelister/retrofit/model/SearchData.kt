package hu.luciferi.foursquarevenuelister.retrofit.model

data class SearchData(var id : String, var name : String, var location : FSLocation, var categories : List<FSCategory>,
                      var venuePage : FSVenuePage?, var referralId : String?, var hasPerk : Boolean?, var delivery : FSDelivery?)