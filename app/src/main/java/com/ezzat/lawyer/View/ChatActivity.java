package com.ezzat.lawyer.View;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.lawyer.Controller.ChatHolder;
import com.ezzat.lawyer.Model.ChatMessage;
import com.ezzat.lawyer.Model.Client;
import com.ezzat.lawyer.Model.User;
import com.ezzat.lawyer.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;

public class ChatActivity extends AppCompatActivity {

    LinearLayout activity_main;
    ImageView submitButton, fileBtn;
    EditText editText;
    User admin;
    Client client;
    String username;
    Toolbar toolbar;
    private StorageReference mStorageRef;
    RecyclerView listOfMessage;
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity_main = findViewById(R.id.activity_main);
        submitButton = findViewById(R.id.btnSend);
        fileBtn = findViewById(R.id.btnSendFile);
        listOfMessage = findViewById(R.id.list);
        editText = findViewById(R.id.editWriteMessage);
        admin = (User) getIntent().getSerializableExtra("admin");
        client = (Client) getIntent().getSerializableExtra("client");
        username = getIntent().getStringExtra("username");
        mStorageRef = FirebaseStorage.getInstance().getReference().child((admin.user)? client.getUsername() : username);
        setup_toolbar();
        displayChatMessage();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!admin.user) {
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(username).push().setValue(new ChatMessage(editText.getText().toString(), admin.username));
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(client.getUsername()).push().setValue(new ChatMessage(editText.getText().toString(), client.getUsername()));
                }
                editText.setText("");
                listOfMessage.scrollToPosition(adapter.getItemCount() - 1);
            }
        });

        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getContentIntent = FileUtils.createGetContentIntent();
                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                startActivityForResult(intent, 12);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 12:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    final String path = FileUtils.getPath(this, uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    /*if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);
                    }*/

                    Uri file = Uri.fromFile(new File(path));

                    mStorageRef.child(path.replaceAll("/", "-")).putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    //Uri downloadUrl = taskSnapshot.getDownload;
                                    mStorageRef.child(path.replaceAll("/", "-")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uril = uri.toString();
                                            if (!admin.user) {
                                                FirebaseDatabase.getInstance().getReference().child("Chat").child(username).push().setValue(new ChatMessage(uril, admin.username));
                                            } else {
                                                FirebaseDatabase.getInstance().getReference().child("Chat").child(client.getUsername()).push().setValue(new ChatMessage(uril, client.getUsername()));
                                            }
                                            listOfMessage.scrollToPosition(adapter.getItemCount() - 1);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });
                }
                break;
        }
    }

    private void displayChatMessage() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Chat")
                .child((!admin.user)? username : client.getUsername())
                .limitToLast(50);
        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(query, ChatMessage.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatHolder>(options) {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new ChatHolder(view);
            }

            @Override
            protected void onBindViewHolder(ChatHolder holder, int position, ChatMessage model) {
                holder.user.setText(model.getMessageUser());
                holder.time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                holder.messageText_me.setVisibility(View.VISIBLE);
                holder.messageText_rep.setVisibility(View.VISIBLE);
                if (admin.user) {
                    if (model.getMessageUser().equals(client.getUsername())) {
                        holder.messageText_me.setText(model.getMessageText());
                        holder.messageText_rep.setVisibility(View.GONE);
                    } else {
                        holder.messageText_rep.setText(model.getMessageText());
                        holder.messageText_me.setVisibility(View.GONE);
                    }
                } else {
                    if (model.getMessageUser().equals(admin.username)) {
                        holder.messageText_me.setText(model.getMessageText());
                        holder.messageText_rep.setVisibility(View.GONE);
                    } else {
                        holder.messageText_rep.setText(model.getMessageText());
                        holder.messageText_me.setVisibility(View.GONE);
                    }
                }
                if (model.getMessageText().startsWith("http")) {
                    Linkify.addLinks(holder.messageText_me, Linkify.ALL);
                    Linkify.addLinks(holder.messageText_rep, Linkify.ALL);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
            }
        };
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listOfMessage.setLayoutManager(mLayoutManager);
        listOfMessage.setItemAnimator(new DefaultItemAnimator());
        listOfMessage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listOfMessage.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /*private void displayChatMessage() {
        listOfMessage.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Query query = FirebaseDatabase.getInstance().getReference().child("Chat").child((!admin.user)? username : client.getUsername()).limitToLast(50);
        FirebaseListOptions<ChatMessage> options =
                new FirebaseListOptions.Builder<ChatMessage>()
                        .setQuery(query, ChatMessage.class)
                        .setLayout(R.layout.list_item)
                        .build();
            adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child("Chat").child((!admin.user)? username
                            : client.getUsername()))
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText_me, messageText_rep, messageUser, messageTime;
                messageText_me = (BubbleTextView) v.findViewById(R.id.message_text_me);
                messageText_rep = (BubbleTextView) v.findViewById(R.id.message_text_rep);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                if (admin.user) {
                    if (model.getMessageUser().equals(client.getUsername())) {
                        messageText_me.setText(model.getMessageText());
                        messageText_rep.setVisibility(View.GONE);
                    } else {
                        messageText_rep.setText(model.getMessageText());
                        messageText_me.setVisibility(View.GONE);
                    }
                } else {
                    if (model.getMessageUser().equals(admin.username)) {
                        messageText_me.setText(model.getMessageText());
                        messageText_rep.setVisibility(View.GONE);
                    } else {
                        messageText_rep.setText(model.getMessageText());
                        messageText_me.setVisibility(View.GONE);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
        listOfMessage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }*/

    /*private void displayChatMessage() {
        listOfMessage.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Query query = FirebaseDatabase.getInstance().getReference().child("Chat").child((!admin.user)? username : client.getUsername()).limitToLast(50);
        FirebaseListOptions<ChatMessage> options =
                new FirebaseListOptions.Builder<ChatMessage>()
                        .setQuery(query, ChatMessage.class)
                        .setLayout(R.layout.list_item)
                        .build();
        Log.i("dodoE", "Before adabter");
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText_me, messageText_rep, messageUser, messageTime;
                messageText_me = (BubbleTextView) v.findViewById(R.id.message_text_me);
                Log.i("dodoE", model.getMessageText());
                messageText_rep = (BubbleTextView) v.findViewById(R.id.message_text_rep);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                if (admin.user) {
                    if (model.getMessageUser().equals(client.getUsername())) {
                        messageText_me.setText(model.getMessageText());
                        messageText_rep.setVisibility(View.GONE);
                    } else {
                        messageText_rep.setText(model.getMessageText());
                        messageText_me.setVisibility(View.GONE);
                    }
                } else {
                    if (model.getMessageUser().equals(admin.username)) {
                        messageText_me.setText(model.getMessageText());
                        messageText_rep.setVisibility(View.GONE);
                    } else {
                        messageText_rep.setText(model.getMessageText());
                        messageText_me.setVisibility(View.GONE);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child("Chat").child((!admin.user)? username
                            : client.getUsername()))
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText_me, messageText_rep, messageUser, messageTime;
                messageText_me = (BubbleTextView) v.findViewById(R.id.message_text_me);
                messageText_rep = (BubbleTextView) v.findViewById(R.id.message_text_rep);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                if (admin.user) {
                    if (model.getMessageUser().equals(client.getUsername())) {
                        messageText_me.setText(model.getMessageText());
                        messageText_rep.setVisibility(View.GONE);
                    } else {
                        messageText_rep.setText(model.getMessageText());
                        messageText_me.setVisibility(View.GONE);
                    }
                } else {
                    if (model.getMessageUser().equals(admin.username)) {
                        messageText_me.setText(model.getMessageText());
                        messageText_rep.setVisibility(View.GONE);
                    } else {
                        messageText_rep.setText(model.getMessageText());
                        messageText_me.setVisibility(View.GONE);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };

        };
        listOfMessage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }*/

    public void setup_toolbar() {
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        ImageView back = toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, Home.class);
                intent.putExtra("user", admin);
                intent.putExtra("client", client);
                startActivity(intent);
            }
        });
    }
}
