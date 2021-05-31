package edu.rutgers.cs213.androidchess50.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.rutgers.cs213.androidchess50.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play_button = (Button)findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_game();
            }
        });

        Button load_button = (Button)findViewById(R.id.load_button);
        load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {load_game();}
        });
    }

    /**
     *
     */
    public void start_game(){
        Intent myIntent = new Intent(this, GameActivity.class);
        startActivity(myIntent);
    }

    public void load_game(){
        Intent myIntent = new Intent(this, GameListActivity.class);
        startActivity(myIntent);
    }
}
