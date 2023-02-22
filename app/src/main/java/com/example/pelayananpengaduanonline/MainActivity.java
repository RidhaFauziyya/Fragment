package com.example.pelayananpengaduanonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AdapterClass adapterClass;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private Button logout;
    RecyclerView tv_tampil;
    ArrayList<ModelLaporan> listLaporan;
    String channelnotif = "Pengaduan Online" ;
    String channelid = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                notif();
            }

            private void notif() {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, channelid )
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle( "Layanan Pengaduan Online" )
                        .setContentText( "Logout Berhasil" );
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
                if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                    int importance = NotificationManager. IMPORTANCE_HIGH ;
                    NotificationChannel notificationChannel = new
                            NotificationChannel( channelnotif , "contoh channel" , importance) ;
                    notificationChannel.enableLights( true ) ;
                    notificationChannel.setLightColor(Color. RED ) ;
                    mBuilder.setChannelId( channelnotif ) ;
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(notificationChannel) ;
                }
                assert mNotificationManager != null;
                mNotificationManager.notify(( int ) System. currentTimeMillis (), mBuilder.build()) ;
            }
        });

        tv_tampil = findViewById(R.id.tv_tampil);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        tv_tampil.setLayoutManager(mLayout);
        tv_tampil.setItemAnimator(new DefaultItemAnimator());
        tampilData();
    }


    private void tampilData() {
        database.child("Laporan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLaporan =new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    ModelLaporan mhs = item.getValue(ModelLaporan.class);
                    mhs.setKey(item.getKey());
                    listLaporan.add(mhs);
                }
                adapterClass = new AdapterClass(listLaporan, MainActivity.this);
                tv_tampil.setAdapter(adapterClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {
            Intent add_lap = new Intent(this, AddLaporan.class);
            startActivity(add_lap);
        }
        return super.onOptionsItemSelected(item);
    }
}