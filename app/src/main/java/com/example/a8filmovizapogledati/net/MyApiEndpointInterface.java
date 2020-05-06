package com.example.a8filmovizapogledati.net;




import com.example.a8filmovizapogledati.net.model1.SearchRezult;
import com.example.a8filmovizapogledati.net.model2.Detail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MyApiEndpointInterface {

    //http://www.omdbapi.com/?apikey=[yourkey]&s=Batman
    @GET("/")
    Call<SearchRezult> getMovieByName(@QueryMap Map<String, String> options);

    //http://www.omdbapi.com/?apikey=[yourkey]&i=imdbid
    @GET("/")
    Call<Detail> getMovieData(@QueryMap Map<String, String> options);

}
