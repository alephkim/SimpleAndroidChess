package edu.rutgers.cs213.androidchess50.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import edu.rutgers.cs213.androidchess50.R;
import edu.rutgers.cs213.androidchess50.model.Board;
import edu.rutgers.cs213.androidchess50.model.ImageAdapter;
import edu.rutgers.cs213.androidchess50.model.SavedGame;

public class ReplayActivity extends AppCompatActivity {

    private SavedGame replay;
    private Board current_board;
    private GridView gridview_replay;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        replay = (SavedGame) getIntent().getSerializableExtra("serialize_data");
        current_board = replay.getBoardstates().get(0);
        imageAdapter = new ImageAdapter(current_board.getBoard(),this);
        gridview_replay = (GridView)findViewById(R.id.replay_board);
        gridview_replay.setVerticalScrollBarEnabled(false);
        gridview_replay.setAdapter(imageAdapter);

        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {nextBoard();}
        });

        Button prevButton = (Button) findViewById(R.id.previous_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {prevBoard();}
        });

    }
    private void nextBoard(){
        int index = replay.getIndex(current_board);
        if(index == replay.getBoardstates().size() - 1) return;

        current_board = replay.getBoardstates().get(index + 1);
        imageAdapter = new ImageAdapter(current_board.getBoard(),this);
        gridview_replay = (GridView)findViewById(R.id.replay_board);
        gridview_replay.setVerticalScrollBarEnabled(false);
        gridview_replay.setAdapter(imageAdapter);
    }

    private void prevBoard(){
        int index = replay.getIndex(current_board);
        if(index == 0) return;

        current_board = replay.getBoardstates().get(index - 1);
        imageAdapter = new ImageAdapter(current_board.getBoard(),this);
        gridview_replay = (GridView)findViewById(R.id.replay_board);
        gridview_replay.setVerticalScrollBarEnabled(false);
        gridview_replay.setAdapter(imageAdapter);
    }
}
