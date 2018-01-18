package se.tuxflux.onyktert.repo

import io.reactivex.Observable
import retrofit2.http.GET


interface IFooRepo {
    @GET("537de0faa19993e2f77009271c9d2198/raw/314a64ef30f43f094bacf6b3c35ab425e17f7bf2/gistfile1.txt")
    fun getNeverHaveIEver(): Observable<List<String>>
}