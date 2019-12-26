package com.singularitycoder.folkdatabase.helper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiEndPoints {

    // All API data we are storing it in the Call data type
    @GET("/populateListOfZones")
    Call<String> getZones();

//    @GET("/populateTLsByZone")
    @GET
    Call<String> getTeamLeadsBasedOnZone(
            @Url String url,
            @Query("zone") String zone);

    @GET("/validateShortName")
    Call<String> doesShortNameExist(
            @Query("short_name") String shortName,
            @Query("member_type") String memberType);
}