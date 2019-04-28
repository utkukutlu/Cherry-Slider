package com.utkukutlu.library.cherryslider

import com.bumptech.glide.request.RequestOptions

data class CherrySliderModel(
    var imageUrl: String? = null,
    var imagePath: Int? = null,
    var title: String? = null,
    var opts: RequestOptions? = null
)