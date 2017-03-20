package innovecs.tes.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;

import innovecs.tes.android.DataStructures.DatabaseValue;

public class ChatActivity extends AppCompatActivity {
    private static final String MESSAGES_REF = "messages";
    private static final String WORDS_REF = "words";

    private GeometricProgressView progressView;
    //list of messages
    private ListView messagesList;
    //will be shown if something goes wrong
    private TextView errorMessage;

    private DatabaseReference storage;

    private ArrayList<String> messagesToShow = new ArrayList<>();

    private SanitiseManager sanitiseManager = new SanitiseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //this button allows to refresh all data
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                refresh();
            }
        });
        storage = FirebaseDatabase.getInstance().getReference();
        setupViews();
        getReplaceWords();
    }

    private void refresh() {
        getReplaceWords();
    }

    //Use it to get all messages from server once
    private void getMessages() {
        storage.child(MESSAGES_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DatabaseValue> messages = new ArrayList<>();
                messagesToShow.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DatabaseValue message = new DatabaseValue(data.getKey(), data.getValue().toString());
                    messages.add(message);
                    Log.d("CHAT_ACTIVITY", "New message received " + message.getId() + " " + message.getMessage());
                    messagesToShow.add(message.getMessage());
                }
                messagesToShow = sanitiseManager.replaceWords(messagesToShow);
                setupListeners();
                showData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();
            }
        });
    }

    private void setupListeners() {

    }

    private void removeListeners() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        removeListeners();
    }

    //Use it to get all replacement words from server once
    private void getReplaceWords() {
        storage.child(WORDS_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DatabaseValue> wordsToFilter = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DatabaseValue word = new DatabaseValue(data.getKey(), data.getValue().toString());
                    wordsToFilter.add(word);
                    Log.d("CHAT_ACTIVITY", "New word received " + word.getMessage());
                }
                sanitiseManager.createSenitiseList(wordsToFilter);
                getMessages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();
            }
        });
    }

    private void showData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                messagesToShow.toArray(new String[messagesToShow.size()]));

        messagesList.setAdapter(adapter);
        hideProgress();
    }

    private void showErrorMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setVisibility(View.VISIBLE);
                messagesList.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
            }
        });
    }

    private void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.GONE);
                errorMessage.setVisibility(View.GONE);
                messagesList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.GONE);
                messagesList.setVisibility(View.GONE);
            }
        });
    }

    private void setupViews() {
        progressView = (GeometricProgressView) findViewById(R.id.progress_view);
        progressView.setType(GeometricProgressView.TYPE.KITE);
        progressView.setNumberOfAngles(6);
        progressView.setColor(getResources().getColor(R.color.colorPrimary));

        errorMessage = (TextView) findViewById(R.id.error_text);
        messagesList = (ListView) findViewById(R.id.messages_list);

        errorMessage.setVisibility(View.GONE);
        messagesList.setVisibility(View.GONE);
    }
}
