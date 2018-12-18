package com.bornfight.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by tomislav on 31/01/2017.
 */

object GsonUtil {

    val defaultGson: Gson by lazy { GsonBuilder().create() }

}
