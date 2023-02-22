package com.example.pelayananpengaduanonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DetailClass extends AppCompatActivity{

    private TextView judul_detail;
    private TextView isi_detail;
    private TextView tanggal_detail;
    private TextView lokasi_detail;
    String judul, isi, tanggal, lokasi, key;
    String Key_Judul = "Judul";
    String Key_Isi = "Isi";
    String Key_Tanggal = "Tanggal";
    String Key_Lokasi = "Lokasi";
    String Key_Position = "Posisi";
    Button hapus, update;
    FirebaseUser firebaseUser;
    String username;

    List<ModelLaporan> mList;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        judul_detail = findViewById(R.id.judul_detail);
        isi_detail = findViewById(R.id.isi_detail);
        tanggal_detail = findViewById(R.id.tanggal_detail);
        lokasi_detail = findViewById(R.id.lokasi_detail);
        hapus = findViewById(R.id.hapus);
        update = findViewById(R.id.update);

        Bundle extras = getIntent().getExtras();
        judul = extras.getString(Key_Judul);
        isi = extras.getString(Key_Isi);
        tanggal = extras.getString(Key_Tanggal);
        lokasi = extras.getString(Key_Lokasi);
        key = extras.getString(Key_Position);


        judul_detail.setText(judul);
        isi_detail.setText(isi);
        tanggal_detail.setText(tanggal);
        lokasi_detail.setText(lokasi);

        database.child("Laporan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(key)){
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    username = firebaseUser.getDisplayName();
                    final String getUser = snapshot.child(key).child("username").getValue(String.class);
                    if(getUser.equals(username)) {
                        hapus.setVisibility(View.VISIBLE);
                        update.setVisibility(View.VISIBLE);
                        hapus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("Usernmae", getUser);
                                Log.d("UserLogin", username);
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailClass.this);
                                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        database.child("Laporan").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(DetailClass.this, "Data berhasil dihapus!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(DetailClass.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Gagal menghapus data!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setMessage("Apakah anda ingin mengapus data ?");
                                builder.show();
                            }
                        });

                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                update.setVisibility(View.VISIBLE);
                                FragmentManager manager = ((AppCompatActivity)DetailClass.this ).getSupportFragmentManager();
                                DialogForm dialog = new DialogForm(
                                        judul,
                                        isi,
                                        tanggal,
                                        lokasi,
                                        key,
                                        "Ubah"
                                );
                                dialog.show(manager, "form");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}