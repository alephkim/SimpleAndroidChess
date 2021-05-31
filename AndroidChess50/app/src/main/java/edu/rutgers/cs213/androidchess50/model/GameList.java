package edu.rutgers.cs213.androidchess50.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A serializable list of games, each saved as an ordered list of board states.
 * */
@TargetApi(28)
public class GameList implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SavedGame> games;

    public GameList(){
        this.games = new ArrayList<SavedGame>();
    }

    //getter for the private gamelist field
    public List<SavedGame> getGamelist(){
        return this.games;
    }

    //adds the parameter saved game to the list
    public boolean addGame(SavedGame game){
        try {
            for(SavedGame savedgames : this.games) {
                if(savedgames.toString().equalsIgnoreCase(game.toString())) {
                    return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
        this.games.add(game);
        return true;
    }

    //removes the selected game from the list
    public boolean removeGame(SavedGame game) {
        try {
            if(this.games.contains(game)) {
                this.games.remove(game);
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //removes the selected game at the index from the list
    public boolean removeGame(int index) {
        try {
            this.games.remove(index);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //sorts by game name, then by date
    public void sortByName(){
        Comparator<SavedGame> comp = Comparator.comparing(SavedGame::getGameName, String.CASE_INSENSITIVE_ORDER).thenComparing(SavedGame::getDate, String.CASE_INSENSITIVE_ORDER);
        this.games.sort(comp);
    }

    public void sortByDate(){
        Comparator<SavedGame> comp = Comparator.comparing(SavedGame::getDate, String.CASE_INSENSITIVE_ORDER);
        this.games.sort(comp);
    }

    public boolean contains(String name) {
        if(this.games.isEmpty()) return false;
        for(SavedGame game : this.games) {
            if(game.toString().equalsIgnoreCase(name)) return false;
        }
        return true;
    }

    //deserializes the gamelist
    public static GameList loadGameList(Context context, String filename){
        GameList templist = null;
        try {
            FileInputStream fileIn = context.openFileInput(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            templist = (GameList) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
        return templist;
    }

    //serializes the gamelist
    public static void saveGameList(Context context, GameList gamelist, String filename){
        try {
            FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gamelist);
            out.close();
            fileOut.close();
        } catch (IOException e){e.printStackTrace();}
    }

}