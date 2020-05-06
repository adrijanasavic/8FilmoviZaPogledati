package com.example.a8filmovizapogledati.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a8filmovizapogledati.R;
import com.example.a8filmovizapogledati.adapters.OmiljeniAdapter;
import com.example.a8filmovizapogledati.db.DatabaseHelper;
import com.example.a8filmovizapogledati.db.model.Filmovi;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OmiljeniActivity extends AppCompatActivity implements OmiljeniAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private OmiljeniAdapter adapterLista;
    private List<Filmovi> filmovi;
    private SharedPreferences prefs;

    private Toolbar toolbar;

    public static String KEY = "KEY";
    public static final String NOTIF_CHANNEL_ID = "notif_channel_007";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.omiljeni_activity );

        prefs = PreferenceManager.getDefaultSharedPreferences( this );


        recyclerView = findViewById( R.id.rvRepertoarLista );
        setupToolbar();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        try {

            filmovi = getDataBaseHelper().getFilmoviDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapterLista = new OmiljeniAdapter( this, filmovi, this );
        recyclerView.setAdapter( adapterLista );
    }

    public void setupToolbar() {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.setSubtitle( "OmiljeniAdapter filmova" );

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setHomeAsUpIndicator( R.drawable.back );
            actionBar.setHomeButtonEnabled( true );
            actionBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_omiljeni, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity( new Intent( this, MainActivity.class ) );
                break;

            case R.id.delete_all:
                deleteFilmove();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    public void deleteFilmove() {

        try {
            ArrayList<Filmovi> filmoviZaBrisanje = (ArrayList<Filmovi>) getDataBaseHelper().getFilmoviDao().queryForAll();
            getDataBaseHelper().getFilmoviDao().delete( filmoviZaBrisanje );

            adapterLista.removeAll();
            adapterLista.notifyDataSetChanged();

            String tekstNotifikacije = "OmiljeniAdapter filmovi obrisani";
            boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
            boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

            if (toast) {
                Toast.makeText( OmiljeniActivity.this, tekstNotifikacije, Toast.LENGTH_LONG ).show();
            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
                NotificationCompat.Builder builder = new NotificationCompat.Builder( OmiljeniActivity.this, NOTIF_CHANNEL_ID );
                builder.setSmallIcon( android.R.drawable.ic_menu_delete );
                builder.setContentTitle( "Notifikacija" );
                builder.setContentText( tekstNotifikacije );

                Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.ic_launcher );

                builder.setLargeIcon( bitmap );
                notificationManager.notify( 1, builder.build() );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent( this, MainActivity.class );
        startActivity( intent );

    }

    @Override
    public void onItemClick(int position) {
        Filmovi film = adapterLista.get( position );

        Intent i = new Intent( this, DetaljiOmiljeni.class );
        i.putExtra( KEY, film.getmImdbId() );
        i.putExtra( "id", film.getmId() );
        startActivity( i );
    }

    private void refresh() {

        RecyclerView recyclerView = findViewById( R.id.rvRepertoarLista );
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
            recyclerView.setLayoutManager( layoutManager );
            List<Filmovi> film = null;
            try {

                film = getDataBaseHelper().getFilmoviDao().queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            OmiljeniAdapter adapter = new OmiljeniAdapter( this, film, this );
            recyclerView.setAdapter( adapter );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}

