package com.example.mapsapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
 FirebaseAuth firebaseAuth;
 DatabaseReference firebaseDatabase;
//    C:\Program Files\Java\jdk1.7.0_71\bin>
//    keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth.getCurrentUser().reload();

        CreateNotification();
        if(firebaseAuth.getCurrentUser()!=null){
            UserProfileChangeRequest.Builder changeRequest = new UserProfileChangeRequest.Builder();
            changeRequest.setDisplayName("Saad");

            firebaseAuth.getCurrentUser().updateProfile(changeRequest.build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.e("print","suuccesssss ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("print","mesg "+e.getMessage());
                }
            });


            HashMap jsonObject = new HashMap();
            try {
                jsonObject.put("name",firebaseAuth.getCurrentUser().getDisplayName());
                jsonObject.put("Email",firebaseAuth.getCurrentUser().getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            firebaseDatabase.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(jsonObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.e("print","Success ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("print","Firebasemesg "+e.getMessage());
                }
            });

        }else{
            firebaseAuth.createUserWithEmailAndPassword("saad2@gmail.com","23434223")
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            UserProfileChangeRequest.Builder changeRequest = new UserProfileChangeRequest.Builder();
                            changeRequest.setDisplayName("Saad");

                            authResult.getUser().updateProfile(changeRequest.build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                firebaseDatabase.child(firebaseAuth.getCurrentUser().getUid()).setValue(firebaseAuth.getCurrentUser());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("print","mesg "+e.getMessage());
                                }
                            });
//                            firebaseAuth.getCurrentUser().updatePhoneNumber(PhoneAuthCredential.B)

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("print","mesg "+e.getMessage());
                }
            });
        }


        firebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("data","data added "+snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot sp:snapshot.getChildren()) {

                }
                Log.e("data","data "+snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void CreateNotification(){
        NotificationCompat.Builder  builder=null;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            builder= new NotificationCompat.Builder(this);
        }else {
            CreateNotificationChannel();
            builder= new NotificationCompat.Builder(this,getPackageName());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText(getResources().getString(R.string.app_name));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(120,builder.build());

    }
    public  void CreateNotificationChannel(){
        NotificationChannel notificationChannel = new NotificationChannel(getPackageName(),"My Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(R.color.black);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(notificationChannel);
    }
    private void resetEmail(){
        FirebaseAuth.getInstance().sendPasswordResetEmail("user@example.com")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                        }
                    }
                });
    }
}