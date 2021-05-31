package edu.rutgers.cs213.androidchess50.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rutgers.cs213.androidchess50.R;
import edu.rutgers.cs213.androidchess50.model.GameList;
import edu.rutgers.cs213.androidchess50.model.SavedGame;

public class GameListActivity extends AppCompatActivity {

    private GameList gamelist;
    private List<SavedGame> savedGames;
    private String filename = "chess.dat";
    private ArrayList<Map<String,Object>> gameInfoList;
    private ListView listView;
    private SimpleAdapter adapter;
    private int listViewPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);


        gamelist = GameList.loadGameList(this,filename);
        if (gamelist == null) gamelist = new GameList();
        gamelist.sortByName();

        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewPos = position;
                System.out.println("index is: " +listViewPos);//testing
            }
        });

        Button loadButton = (Button) findViewById(R.id.load_game_button);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gamelist.getGamelist().size() > 0) {
                    SavedGame game = gamelist.getGamelist().get(listViewPos);
                    if (game!= null) {
                        //load selected game into the replay activity
                        Intent intent = new Intent(GameListActivity.this, ReplayActivity.class);
                        intent.putExtra("serialize_data",game);
                        startActivity(intent);
                    }
                }
            }
        });

        Button delButton = (Button) findViewById(R.id.delete_game_button);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gamelist.getGamelist().size() > 0) {
                    gamelist.removeGame(listViewPos);
                    GameList.saveGameList(GameListActivity.this, gamelist, filename);
                    populateListView();
                }
            }
        });

        ToggleButton sortToggle = (ToggleButton) findViewById(R.id.toggleSort);
        sortToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    gamelist.sortByDate();
                    populateListView();
                } else {
                    gamelist.sortByName();
                    populateListView();
                }
            }
        });

    }

    private void populateListView(){
        savedGames = gamelist.getGamelist();
        gameInfoList = new ArrayList<Map<String,Object>>();
        for(int i = 0; i < savedGames.size(); i++) {
            Map<String,Object> gameInfoMap = new HashMap<String,Object>();
            gameInfoMap.put("title", savedGames.get(i).getGameName());
            gameInfoMap.put("date", savedGames.get(i).getDate());
            gameInfoList.add(gameInfoMap);
        }
        adapter = new SimpleAdapter(this, gameInfoList, android.R.layout.simple_list_item_2,
                new String[]{"title", "date"}, new int[]{android.R.id.text1,android.R.id.text2});

        listView = (ListView) findViewById(R.id.saved_games_list);
        listView.setAdapter(adapter);


    }

}
