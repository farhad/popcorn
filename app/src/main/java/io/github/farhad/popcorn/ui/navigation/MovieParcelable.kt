package io.github.farhad.popcorn.ui.navigation

import android.os.Parcel
import android.os.Parcelable
import io.github.farhad.popcorn.domain.model.Movie

class MovieParcelable constructor(val movie: Movie) : Parcelable {

    
    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}