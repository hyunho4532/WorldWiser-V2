import com.hyun.worldwiser.model.tourSpots.Tour
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TourLocationApiService {
    @GET("/B551011/KorService1/locationBasedList1")
    fun getLocationBasedList(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String,
        @Query("arrange") arrange: String,
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
        @Query("contentTypeId") contentTypeId: Int
    ): Call<Tour>
}