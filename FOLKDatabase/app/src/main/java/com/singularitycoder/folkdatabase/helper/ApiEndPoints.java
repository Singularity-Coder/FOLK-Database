package com.singularitycoder.folkdatabase.helper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndPoints {

    // All API data we are storing it in the Call data type
    @GET("/populateListOfZones")
    Call<String> getZones();

    @GET("/populateTLsByZone")
    Call<String> getTeamLeadsBasedOnZone(
            @Query("zone") String zone);

    @GET("/validateShortName")
    Call<String> doesShortNameExist(
            @Query("short_name") String shortName,
            @Query("sign_up") String memberType);
}