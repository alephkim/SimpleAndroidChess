package edu.rutgers.cs213.androidchess50.model;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * Created by Greg Melillo
 * Dark Brown Tile Hex : #D18B47
 *  Light Brown Title Hex: #FFCE9E
 */

public class ImageAdapter extends BaseAdapter {
    private Tile[][] board;
    private Context mContext;
    private int posX;
    private int posY;

    public ImageAdapter(Tile[][] board, Context context){
        this.board = board;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Piece piece;

        if(convertView == null){
            boolean evenCol = (position/8)%2 == 0;
            boolean evenRow = position%2 == 0;
            imageView = new ImageView(mContext);
            if(position == 0){
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
                // first tile is all wonky without these specified
                imageView.getLayoutParams().height = 100;
                imageView.getLayoutParams().width = 100;

            }else {
                imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / 8, parent.getWidth() / 8));
            }

            /*
               Translates the gridView coordinates to the inverse coordinate to account for the reverse indexing of the gridView matrix
             */
            int posX = getPosX(position);
            int posY = getPosY(position);

            if(board[posX][posY].isOccupied()) {
                piece = board[posX][posY].getPiece();

                if (piece != null) {
                    imageView.setImageResource(mContext.getResources().getIdentifier(
                            piece.getFileName().toLowerCase(), "drawable", mContext.getPackageName()));
                }
            }


            if(board[posX][posY].getTileColor() == edu.rutgers.cs213.androidchess50.model.Color.WHITE){
                imageView.setBackgroundColor(Color.parseColor("#FFCE9E"));
            }else{
                imageView.setBackgroundColor(Color.parseColor("#D18B47"));
            }
        } else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return board.length*board.length;
    }


    public int getPosX(int position){
        return position % 8;
    }

    public int getPosY(int position){
        return Math.abs(7 - position/8);
    }

}
