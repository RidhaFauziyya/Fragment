package com.example.pelayananpengaduanonline;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogForm  extends DialogFragment {
    String judul, isi, tanggal, lokasi, key, pilih;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm(String judul, String isi, String tanggal, String lokasi, String key, String pilih) {
        this.judul = judul;
        this.isi = isi;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.key = key;
        this.pilih = pilih;
    }

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String username = firebaseUser.getDisplayName();
    String dateMessage;
    TextView tjudul, tisi, ttanggal, tlokasi;
    Button btn_simpan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_laporan, container, false);
        tjudul = view.findViewById(R.id.edJudul);
        tisi = view.findViewById(R.id.edIsi);
        ttanggal = view.findViewById(R.id.edtanggal);
        tlokasi = view.findViewById(R.id.edlokasi);
        btn_simpan = view.findViewById(R.id.btn_simpan);

        tjudul.setText(judul);
        tisi.setText(isi);
        ttanggal.setText(tanggal);
        tlokasi.setText(lokasi);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String judul = tjudul.getText().toString();
                String isi = tisi.getText().toString();
                String tanggal = ttanggal.getText().toString();
                String lokasi = tlokasi.getText().toString();

                if(pilih.equals("Ubah")){
                    database.child("Laporan").child(key).setValue(new ModelLaporan(judul, isi, tanggal, lokasi, username)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Berhasil diupdate!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Data gagal diupdate!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}
