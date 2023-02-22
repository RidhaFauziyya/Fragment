package com.example.pelayananpengaduanonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLaporan extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    EditText edJudul, edIsi, edLokasi;
    TextView edTanggal;
    Button btn_simpan, btn_tanggal;
    String username , dateMessage;
    String channelnotif = "Pengaduan Online" ;
    String channelid = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_laporan);

        edJudul = findViewById(R.id.edJudul);
        edIsi = findViewById(R.id.edIsi);
        edTanggal = findViewById(R.id.edtanggal);
        edLokasi = findViewById(R.id.edlokasi);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_tanggal = findViewById(R.id.tanggal);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        username = firebaseUser.getDisplayName();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getJudul = edJudul.getText().toString();
                String getIsi = edIsi.getText().toString();
                String getTanggal = dateMessage;
                String getLokasi = edLokasi.getText().toString();

                if (getJudul.isEmpty()) {
                    edJudul.setError("Judul Laporan perlu diisi!");
                } else if (getIsi.isEmpty()){
                    edIsi.setError("Isi Laporan perlu diisi!");
                } else if (getTanggal.isEmpty()){
                    edTanggal.setError("Tanggal Kejadian perlu diisi!");
                }else if (getLokasi.isEmpty()){
                    edLokasi.setError("Lokasi Kejadian perlu diisi! ");
                }else{
                    database.child("Laporan").push().setValue(new ModelLaporan(getJudul, getIsi, getTanggal, getLokasi, username )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddLaporan.this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddLaporan.this, MainActivity.class));
                            finish();
                            notif();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddLaporan.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }

            private void notif() {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AddLaporan. this, channelid )
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle( "Layanan Pengaduan Online" )
                        .setContentText( "Laporan anda berhasil disimpan!" );
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
    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.date_picker)) ;

    }

    public void processDatePickerResult(int year, int month, int
            day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        dateMessage = (month_string + "/" + day_string + "/" + year_string);
    }

}