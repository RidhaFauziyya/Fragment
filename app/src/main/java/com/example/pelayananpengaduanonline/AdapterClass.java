package com.example.pelayananpengaduanonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder>  {

    private Activity activity;
    private List<ModelLaporan> mList;
    String judul, isi, tanggal, lokasi, key;
    String Key_Judul = "Judul";
    String Key_Isi = "Isi";
    String Key_Tanggal = "Tanggal";
    String Key_Lokasi = "Lokasi";
    String Key_Position = "Posisi";


    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterClass(List<ModelLaporan>mList, Activity activity){
        this.mList = mList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item,parent,false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.MyViewHolder holder, int position) {
        final ModelLaporan data = mList.get(position);
        holder.tv_judul.setText("Judul : " + data.getJudul());
        holder.tv_tanggal.setText("Tanggal Kejadian : " + data.getTanggal());

        holder.card_hasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judul = data.getJudul();
                isi = data.getIsi();
                tanggal = data.getTanggal();
                lokasi = data.getLokasi();
                key = data.getKey();
                Intent intent = new Intent(activity.getApplicationContext(), DetailClass.class);
                intent.putExtra(Key_Judul, judul);
                intent.putExtra(Key_Isi, isi);
                intent.putExtra(Key_Tanggal, tanggal);
                intent.putExtra(Key_Lokasi, lokasi);
                intent.putExtra(Key_Position, key);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_judul, tv_tanggal;
        CardView card_hasil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judul = itemView.findViewById(R.id.judul_laporan);
            tv_tanggal = itemView.findViewById(R.id.tanggal);
            card_hasil = itemView.findViewById(R.id.card_hasil);
        }
    }
}
