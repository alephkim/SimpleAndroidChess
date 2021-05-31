package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A serializable, ordered list of boards that represent all the moves made ine one game
 * of chess. Each board state is the result of one successful move.
 * */
public class SavedGame implements Serializable {

    private static final long serialVersionUID = 1L;
    private String gamename;
    private Calendar date;

    private List<Board> boardstates;

    public SavedGame() {
        this.boardstates = new ArrayList<Board>();
        this.gamename = "";
        date = Calendar.getInstance();
        date.set(Calendar.MILLISECOND,0);
    }

    public List<Board> getBoardstates(){
        return boardstates;
    }

    public int getIndex(Board board){
        try {
            for(int i = 0; i < boardstates.size(); i++){
                if(boardstates.get(i).equals(board)) return i;
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    //adds a new state to the list. to be called after each successful move.
    public void addState(Board board) {
        if (board == null) return;
        Board temp = new Board();
        temp.board = board.getBoard();
        boardstates.add(temp);
    }

    //removes the last board state from the list and returns the new last state.
    public Board undoState() {
        if (boardstates == null || boardstates.isEmpty()) return null;
        if (boardstates.size() == 1) return boardstates.get(0);
        //this.boardstates.remove(boardstates.size() - 1);
        return this.boardstates.get(boardstates.size() - 1);
    }

    //provides the list of board states with a name. to be called when the user saves a game
    public void saveGameName(String name) {
        this.gamename = name;
    }

    //gets the saved game's calendar
    public Calendar getCalender() {
        return this.date;
    }

    //gets the saved game's creation date as a String
    public String getDate() {
        return this.date.getTime().toString();
    }

    public String getGameName(){
        return this.gamename;
    }
}
