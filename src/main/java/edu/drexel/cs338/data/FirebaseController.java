package edu.drexel.cs338.data;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.internal.*;
import com.google.firebase.internal.Base64;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.Task;
import edu.drexel.cs338.interfaces.DrawHandler;
import edu.drexel.cs338.interfaces.PassFailHandler;
import edu.drexel.cs338.interfaces.WhiteboardDeleteListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Angel on 8/16/2016.
 */
public class FirebaseController {

    private static FirebaseController controller;

    FirebaseDatabase database;

    Set<WhiteboardDeleteListener> deleteListeners = new HashSet<>();

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
        DatabaseReference ref = database.getReference();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Whiteboard whiteboard = dataSnapshot.getValue(Whiteboard.class);
                for (WhiteboardDeleteListener deleteListener : deleteListeners) {
                    deleteListener.onDelete(whiteboard);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static FirebaseController get() {
        if (controller == null) {
            controller = new FirebaseController();
        }
        return controller;
    }

    /**
     * Creates a whiteboard in the database.
     *
     * @param whiteboard         the data to be added
     * @param valueExistsHandler {@link PassFailHandler#pass()} will be called if the connection succeeds {@link PassFailHandler#fail()}
     *                           will be called if the entry already exists or the connection fails
     */
    public void createWhiteboard(Whiteboard whiteboard, PassFailHandler valueExistsHandler) {
        DatabaseReference ref = database.getReference(whiteboard.getName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    ref.setValue(whiteboard).addOnCompleteListener(task -> valueExistsHandler.pass());
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

    public void addDeleteListener(WhiteboardDeleteListener whiteboardDeleteListener) {
        deleteListeners.add(whiteboardDeleteListener);
    }

    public void removeDeleteListener(WhiteboardDeleteListener whiteboardDeleteListener) {
        deleteListeners.remove(whiteboardDeleteListener);
    }

    public void setupWhiteboardTableModel(WhiteboardTableModel model) {
        DatabaseReference ref = database.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model.setData(parseWhiteboardData(dataSnapshot.getChildren()));
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

    public void getUserList(String whiteboard, UserTableModel model) {
        DatabaseReference ref = database.getReference(whiteboard + "/users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                model.setData(parseUserData(dataSnapshot.getChildren()));
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        model.addUser(dataSnapshot.getKey(), dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        model.removeUser(dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private Map<String, String> parseUserData(Iterable<DataSnapshot> children) {
        Map<String, String> users = new HashMap<>();
        for (DataSnapshot child : children) {
            users.put(child.getKey(), child.getValue(String.class));
        }
        return users;
    }

    private List<Whiteboard> parseWhiteboardData(Iterable<DataSnapshot> children) {
        List<Whiteboard> list = new ArrayList<>();
        for (DataSnapshot child : children) {
            list.add(child.getValue(Whiteboard.class));
        }
        return list;
    }

    public void joinWhiteboard(String user, Whiteboard whiteboard, DrawHandler drawHandler) {
        DatabaseReference ref = database.getReference(whiteboard.getName());
        DatabaseReference userRef = ref.child("users/" + user);
        DatabaseReference imageRef = ref.child("image");
        userRef.setValue(user);
        userRef.onDisconnect().removeValue();
        drawHandler.addOnLineDrawnListner(image -> {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                imageRef.setValue(Base64Utils.encode(imageInByte));
                Base64Utils.decode("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    String bytes = dataSnapshot.getValue(String.class);
                    InputStream inputStream = new ByteArrayInputStream(Base64Utils.decode(bytes));
                    try {
                        BufferedImage image = ImageIO.read(inputStream);
                        drawHandler.setImage(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void exitWhiteboard(String user, String whiteboardName) {
        database.getReference(whiteboardName + "/users/" + user).removeValue().addOnCompleteListener(task -> checkAndRemoveWhiteboard(whiteboardName));
    }

    private void checkAndRemoveWhiteboard(String name) {
        DatabaseReference ref = database.getReference(name + "/users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    database.getReference(name).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
