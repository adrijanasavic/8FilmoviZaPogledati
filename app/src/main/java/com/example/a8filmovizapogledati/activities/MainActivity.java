package com.example.a8filmovizapogledati.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;

import com.example.a8filmovizapogledati.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageButton btnSearch;
    private EditText movieName;
    private RecyclerView recyclerView;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setupToolbar();
        inicijalizacija();
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

    public void inicijalizacija() {
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
    }
}
