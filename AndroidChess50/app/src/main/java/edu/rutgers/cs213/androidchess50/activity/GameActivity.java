package edu.rutgers.cs213.androidchess50.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Random;

import edu.rutgers.cs213.androidchess50.R;
import edu.rutgers.cs213.androidchess50.model.Bishop;
import edu.rutgers.cs213.androidchess50.model.Board;
import edu.rutgers.cs213.androidchess50.model.GameList;
import edu.rutgers.cs213.androidchess50.model.ImageAdapter;
import edu.rutgers.cs213.androidchess50.model.Knight;
import edu.rutgers.cs213.androidchess50.model.Piece;
import edu.rutgers.cs213.androidchess50.model.PieceType;
import edu.rutgers.cs213.androidchess50.model.Queen;
import edu.rutgers.cs213.androidchess50.model.Rook;
import edu.rutgers.cs213.androidchess50.model.SavedGame;
import edu.rutgers.cs213.androidchess50.model.Tile;

/**
 * The activity representing a chess game, contains a gridview for the chess board.
 *
 * @author Greg Melillo
 * @author Eric Kim
 */

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // the gridlayout representing the chess board
    private GridView gridview_board;
    private Board board;
    private Board temp;
    private Tile src;
    private ImageView srcView;
    private Tile dest;
    private ImageView destView;
    private ImageAdapter imageAdapter;
    private Button undoButton;
    private Button aiButton;
    private Button drawButton;
    private Button resignButton;
    private GameList gamelist;
    private SavedGame currentgame;
    private boolean canUndo;

    private String filename = "chess.dat";
    private edu.rutgers.cs213.androidchess50.model.Color turn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        canUndo = false;
        gamelist = GameList.loadGameList(this, filename);
        if (gamelist == null) gamelist = new GameList();

        turn = edu.rutgers.cs213.androidchess50.model.Color.WHITE;
        board = new Board();
        board.initializeBoard();
        currentgame = new SavedGame();
        imageAdapter = new ImageAdapter(board.getBoard(), this);
        gridview_board = (GridView) findViewById(R.id.gridview_board);
        gridview_board.setVerticalScrollBarEnabled(false);
        gridview_board.setAdapter(imageAdapter);
        gridview_board.setOnItemClickListener(this);


        undoButton = (Button) findViewById(R.id.previous_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!canUndo) {
                    return;
                }
                board = currentgame.undoState();
                imageAdapter = new ImageAdapter(board.getBoard(), v.getContext());
                gridview_board.setAdapter(imageAdapter);
                changeTurn();
                canUndo = false;
            }
        });

        aiButton = (Button) findViewById(R.id.aiButton);
        aiButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    temp = board.deepCopy();
                } catch (Exception e) {
                }
                Tile dest = board.moveRandom(turn);
                if (dest != null) {
                    if (canPromotePawn(dest, turn)){
                        String [] choices = getResources().getStringArray(R.array.choices);
                        String s = choices[new Random().nextInt(choices.length)];

                        switch (s) {
                            case "Queen":
                                dest.putPiece(new Queen(turn));
                                imageAdapter.notifyDataSetChanged();
                                gridview_board.setAdapter(imageAdapter);
                                break;
                            case "Knight":
                                dest.putPiece(new Knight(turn));
                                imageAdapter.notifyDataSetChanged();
                                gridview_board.setAdapter(imageAdapter);
                                break;
                            case "Bishop":
                                dest.putPiece(new Bishop(turn));
                                imageAdapter.notifyDataSetChanged();
                                gridview_board.setAdapter(imageAdapter);
                                break;
                            case "Rook":
                                dest.putPiece(new Rook(turn));
                                imageAdapter.notifyDataSetChanged();
                                gridview_board.setAdapter(imageAdapter);
                                break;
                        }
                    }
                    canUndo = true;
                    checkForMate();
                    currentgame.addState(temp);
                    imageAdapter.notifyDataSetChanged();
                    gridview_board.setAdapter(imageAdapter);

                    // change turn after valid move
                    changeTurn();
                }
            }
        });

        drawButton = (Button) findViewById(R.id.drawButton);
        drawButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                draw();
            }
        });

        resignButton = (Button) findViewById(R.id.next_button);
        resignButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resign();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int x = getPosX(position);
        int y = getPosY(position);

        String s = x + ", " + y;
        Tile t = board.getTile(x, y);

        if (src == null) {
            // invalid move if clicked on empty tile or wrong color piece
            if (t.getPiece() == null) {
                return;
            }
            if (!t.getPiece().getColor().equals(turn)) {
                return;
            }
            src = t;
            srcView = (ImageView) view;
            srcView.setBackgroundColor(Color.DKGRAY);
            return;
        }


        if (dest == null) {
            dest = t;
            destView = (ImageView) view;
        }

        try {
            temp = board.deepCopy();
        } catch (Exception e) {
        }
        boolean moved = board.movePiece(src, dest);
        if (moved) {
            canUndo = true;
            currentgame.addState(temp);
            //pawn promotion check
            if (canPromotePawn(dest, turn)) {
                showPromotionDialog(turn, dest);
            }
            checkForMate();
            imageAdapter.notifyDataSetChanged();
            gridview_board.setAdapter(imageAdapter);


            // change turn after valid move
            changeTurn();
        }

        srcView.setBackgroundColor(src.getTileColor().equals(edu.rutgers.cs213.androidchess50.model.Color.WHITE) ?
                Color.parseColor("#FFCE9E") : Color.parseColor("#D18B47"));
        src = dest = null;
        srcView = destView = null;
    }


    public int getPosX(int position) {
        return position % 8;
    }

    public int getPosY(int position) {
        return Math.abs(7 - position / 8);
    }

    public void changeTurn() {
        turn = turn == edu.rutgers.cs213.androidchess50.model.Color.WHITE ?
                edu.rutgers.cs213.androidchess50.model.Color.BLACK :
                edu.rutgers.cs213.androidchess50.model.Color.WHITE;
    }

    public boolean canPromotePawn(Tile dest, edu.rutgers.cs213.androidchess50.model.Color col) {
        if ((dest.getRank() == 7 && col == edu.rutgers.cs213.androidchess50.model.Color.WHITE) ||
                (dest.getRank() == 0 && col == edu.rutgers.cs213.androidchess50.model.Color.BLACK)) {
            if (dest.getPiece().getType() == PieceType.PAWN) {
                return true;
            }
        }
        return false;
    }

    public void checkForMate() {
        //assign opponent color
        edu.rutgers.cs213.androidchess50.model.Color opponent = turn == edu.rutgers.cs213.androidchess50.model.Color.WHITE ?
                edu.rutgers.cs213.androidchess50.model.Color.BLACK :
                edu.rutgers.cs213.androidchess50.model.Color.WHITE;

        //check if the move that was made results in checkmate
        if (board.isItCheckmate(opponent)) {
            //add the new board state to currentgame's to the end of the list of board states
            currentgame.addState(board);
            //alert dialog saying who won and asking if you want to save game
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Checkmate! " + turn.toString() + " wins!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showSaveGameDialog();
                }
            });
            builder.show();
        }
    }


    public void showSaveGameDialog() {
        //alert dialog saying who won and asking if you want to save game
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a save name for your game.");

        //text input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setCancelable(false);

        //buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();

                //add the current game to the game list and serialize it
                System.out.println(gamelist.getGamelist().size());
                currentgame.saveGameName(name);
                gamelist.addGame(currentgame);
                GameList.saveGameList(GameActivity.this, gamelist, filename);
                finish();
            }
        });
        builder.setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //quit to the main activity
                finish();
            }
        });
        builder.show();
    }


    public void draw() {
        //alert dialog saying who won and asking if you want to save game
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to draw?");
        builder.setCancelable(false);

        //buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentgame.addState(board);///
                dialog.dismiss();
                final AlertDialog.Builder b = new AlertDialog.Builder(GameActivity.this);
                b.setCancelable(false);
                b.setTitle("It's a draw!");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showSaveGameDialog();
                    }
                });
                b.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void resign() {
        //alert dialog saying who won and asking if you want to save game
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to resign?");
        builder.setCancelable(false);

        //buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentgame.addState(board);///
                dialog.dismiss();
                final AlertDialog.Builder b = new AlertDialog.Builder(GameActivity.this);
                b.setCancelable(false);
                b.setTitle((turn = turn == edu.rutgers.cs213.androidchess50.model.Color.WHITE ?
                        edu.rutgers.cs213.androidchess50.model.Color.BLACK : edu.rutgers.cs213.androidchess50.model.Color.WHITE).toString() + " wins!");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentgame.addState(board);///
                        dialog.dismiss();
                        showSaveGameDialog();
                    }
                });
                b.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    public void showPromotionDialog(edu.rutgers.cs213.androidchess50.model.Color turn, Tile dest) {

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // Set the dialog title
        builder.setTitle("Select desired promotion");
        builder.setCancelable(false);

        // specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive call backs when items are selected
        // again, R.array.choices were set in the resources res/values/strings.xml
        builder.setSingleChoiceItems(R.array.choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }

        });

        // Set the action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Piece p;

                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                String s = ((AlertDialog) dialog).getListView().getItemAtPosition(selectedPosition).toString();

                switch (s) {
                    case "Queen":
                        dest.putPiece(new Queen(turn));
                        imageAdapter.notifyDataSetChanged();
                        gridview_board.setAdapter(imageAdapter);
                        break;
                    case "Knight":
                        dest.putPiece(new Knight(turn));
                        imageAdapter.notifyDataSetChanged();
                        gridview_board.setAdapter(imageAdapter);
                        break;
                    case "Bishop":
                        dest.putPiece(new Bishop(turn));
                        imageAdapter.notifyDataSetChanged();
                        gridview_board.setAdapter(imageAdapter);
                        break;
                    case "Rook":
                        dest.putPiece(new Rook(turn));
                        imageAdapter.notifyDataSetChanged();
                        gridview_board.setAdapter(imageAdapter);
                        break;
                }
            }
        });
        builder.show();
    }
}

