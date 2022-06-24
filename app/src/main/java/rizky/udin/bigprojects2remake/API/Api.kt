package rizky.udin.bigprojects2remake.API

import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("apipredict")
    fun getPosts(): Call<ArrayList<PostResponse>>
}