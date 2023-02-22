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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText username_login, pass_login;
    private Button btnLogin;
    private TextView registNow;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    String channelnotif = "Pengaduan Online" ;
    String channelid = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_login = findViewById(R.id.userlogin);
        pass_login = findViewById(R.id.passwordlogin);
        btnLogin = findViewById(R.id.loginbtn);
        registNow = findViewById(R.id.regist_now);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

        registNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username_login.getText().length()>0 && pass_login.getText().length()>0){
                    login(username_login.getText().toString(), pass_login.getText().toString());
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);

                    alertDialogBuilder.setMessage("Username dan Password harus diisi!");
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

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!= null){
                    if(task.getResult().getUser()!=null){
                        reload();
                        notif();
                    }else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);

                        alertDialogBuilder.setMessage("Username atau password salah, silahkan coba lagi!");
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);

                    alertDialogBuilder.setMessage("Username atau password salah, silahkan coba lagi!");
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

            private void notif() {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Login.this, channelid )
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle( "Layanan Pengaduan Online" )
                        .setContentText( "Login berhasil! Selamat Datang!" );
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