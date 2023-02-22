package com.example.pelayananpengaduanonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    private EditText nama,  email, password, confirmpass;
    private Button registbtn;
    private TextView login_now;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    String channelnotif = "Pengaduan Online" ;
    String channelid = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpass = findViewById(R.id.passwordconfirm);
        registbtn = findViewById(R.id.registbtn);
        login_now = findViewById(R.id.login_now);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

        login_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nama.getText().length()>0 && email.getText().length()>0 && password.getText().length()>0 && confirmpass.getText().length()>0){
                    if(password.getText().toString().equals(confirmpass.getText().toString())){
                        register(nama.getText().toString(),email.getText().toString(), password.getText().toString());
                    }else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);

                        alertDialogBuilder.setMessage("Password harus sama!");
                        alertDialogBuilder
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);

                    alertDialogBuilder.setMessage("Semua data harus diisi!");
                    alertDialogBuilder
                            .setCancelable(false)
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void register(String name, String email, String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser!=null){
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                                notif();
                            }

                            private void notif() {
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Register.this, channelid )
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle( "Layanan Pengaduan Online" )
                                        .setContentText( "Regristasi Berhasil! Selamat Datang!" );
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
                    }else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);

                        alertDialogBuilder.setMessage("Register, silahkan coba lagi!");
                        alertDialogBuilder
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        Toast.makeText(getApplicationContext(), "Register Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

}