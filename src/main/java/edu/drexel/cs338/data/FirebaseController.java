package edu.drexel.cs338.data;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Angel on 8/16/2016.
 */
public class FirebaseController {

    public FirebaseController() {
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setServiceAccount(new FileInputStream("conf/GroupDraw-627eb46a5c8c.json"))
                    .setDatabaseUrl("https://groupdraw-247cf.firebaseio.com/")
                    .build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FirebaseApp.initializeApp(options);
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("fun_stuff");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.print(databaseError.getMessage());
            }
        });
    }
}
