package hu.luciferi.foursquarevenuelister.retrofit.model

data class FSPhoto(var id : String, var createdAt : Long, var source : FSPhotoSource, var prefix : String, var suffix : String,
                   var width : Int, var height : Int, var user : FSUser, var checkin : FSPhotoCheckin, var visibility : String)