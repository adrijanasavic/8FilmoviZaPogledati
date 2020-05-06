package com.example.a8filmovizapogledati.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a8filmovizapogledati.R;
import com.example.a8filmovizapogledati.adapters.SearchAdapter;
import com.example.a8filmovizapogledati.net.MyService;
import com.example.a8filmovizapogledati.net.model1.Search;
import com.example.a8filmovizapogledati.net.model1.SearchRezult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchAdapter.OnItemClickListener {

    private Toolbar toolbar;

    private ImageButton btnSearch;
    private EditText movieName;
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter adapter;
    public static String KEY = "KEY";


    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setupToolbar();
        fillData();
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitle( "Lista filmova" );
        toolbar.setTitleTextColor( Color.WHITE );

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.heart );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.omiljeni_lista:
                break;

            case R.id.settings:
                break;
        }

        return super.onOptionsItemSelected( item );
    }

    public void fillData() {
        btnSearch = findViewById( R.id.btn_search );
        movieName = findViewById( R.id.ime_filma );
        recyclerView = findViewById( R.id.rvLista );

        btnSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieByName( movieName.getText().toString() );
            }
        } );
    }

    private void getMovieByName(String name) {
        Map<String, String> query = new HashMap<>();
        //TODO unesite api key
        query.put( "apikey", "API KEY" );
        query.put( "s", name.trim() );

        Call<SearchRezult> call = MyService.apiInterface().getMovieByName( query );
        call.enqueue( new Callback<SearchRezult>() {
            @Override
            public void onResponse(Call<SearchRezult> call, Response<SearchRezult> response) {

                if (response.code() == 200) {
                    try {
                        SearchRezult searches = response.body();

                        ArrayList<Search> search = new ArrayList<>();

                        for (Search e : searches.getSearch()) {

                            if (e.getType().equals( "movie" )) {
                                search.add( e );
                            }
                        }

                        layoutManager = new LinearLayoutManager( MainActivity.this );
                        recyclerView.setLayoutManager( layoutManager );

                        adapter = new SearchAdapter( MainActivity.this, search, MainActivity.this );
                        recyclerView.setAdapter( adapter );

                        Toast.makeText( MainActivity.this, "Prikaz filmova.", Toast.LENGTH_SHORT ).show();

                    } catch (NullPointerException e) {
                        Toast.makeText( MainActivity.this, "Ne postoji film sa tim nazivom", Toast.LENGTH_SHORT ).show();
                    }

                } else {

                    Toast.makeText( MainActivity.this, "Greska sa serverom", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<SearchRezult> call, Throwable t) {
                Toast.makeText( MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @Override
    public void onItemClick(int position) {
        Search movie = adapter.get( position );

        Intent i = new Intent( this, DetaljiActivity.class );
        i.putExtra( KEY, movie.getImdbID() );
        startActivity( i );
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState( savedInstanceState );
        savedInstanceState.putInt( "position", position );
    }
}

