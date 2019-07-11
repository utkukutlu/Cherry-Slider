package com.utkukutlu.library.cherryslider

data class CherrySliderModel(
    var imageUrl: String? = null,
    var imagePath: Int? = null,
    var title: String? = null,
    var scale: CherrySlider.Scale? = null
)