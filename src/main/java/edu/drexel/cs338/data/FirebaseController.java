package edu.drexel.cs338.data;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import edu.drexel.cs338.interfaces.DrawHandler;
import edu.drexel.cs338.interfaces.PassFailHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Angel on 8/16/2016.
 */
public class FirebaseController {

    private static FirebaseController controller;

    FirebaseDatabase database;

    private FirebaseController() {
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
        database = FirebaseDatabase.getInstance();
    }

    public static FirebaseController get() {
        if (controller == null) {
            controller = new FirebaseController();
        }
        return controller;
    }

    /**
     * Creates a whiteboard in the database and the synchronizes with the database actions with the draw handler.
     *
     * @param whiteboard         the data to be added
     * @param drawHandler        will be synchronized with the data
     * @param valueExistsHandler {@link PassFailHandler#pass()} will be called if the connection succeeds {@link PassFailHandler#fail()}
     *                           will be called if the entry already exists or the connection fails
     */
    public void createWhiteboard(Whiteboard whiteboard, DrawHandler drawHandler, PassFailHandler valueExistsHandler) {
        DatabaseReference ref = database.getReference(whiteboard.getName());
        whiteboard.addUser(whiteboard.getCreator());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    valueExistsHandler.pass();
                    ref.setValue(whiteboard);
                    joinWhiteboard(whiteboard.getCreator(), whiteboard, drawHandler, ref);
                } else {
                    valueExistsHandler.fail();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                valueExistsHandler.fail();
            }
        });
    }

    public void getWhiteboardList(WhiteboardTableModel model) {
        DatabaseReference ref = database.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model.setData(parseData(dataSnapshot.getChildren()));
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        model.addRow(dataSnapshot.getValue(Whiteboard.class));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        model.updateRow(dataSnapshot.getValue(Whiteboard.class));
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        model.removeRow(dataSnapshot.getValue(Whiteboard.class));
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                model.refreshData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private List<Whiteboard> parseData(Iterable<DataSnapshot> children) {
        List<Whiteboard> list = new ArrayList<>();
        for(DataSnapshot child : children) {
            list.add(child.getValue(Whiteboard.class));
        }
        return list;
    }

    private void joinWhiteboard(String user, Whiteboard whiteboard, DrawHandler drawHandler, DatabaseReference ref) {


//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Object document = dataSnapshot.getValue();
//                System.out.println(document);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.err.print(databaseError.getMessage());
//            }
//        });
    }
}
