package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The Rook subclass of Piece.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Rook extends Piece implements Serializable {
    private static final long serialVersionUID = 1L;
    /**Rook constructor.
     * @param color the color of the Rook
     */
    public Rook(Color color) {
        super(color);
        this.type = PieceType.ROOK;
    }

    @Override
    public List<Tile> findLegalMoves(Board board) {
        LinkedList<Tile> moves = new LinkedList<Tile>();
        int x = this.getCurrentTile().getFile();
        int y = this.getCurrentTile().getRank();

        // Look UP
        if(y<7) {
            int j = y+1;
            while(j < 8) {
                Tile tile = board.getTile(x, j);
                if(!tile.isOccupied()) {
                    moves.add(tile);
                    j++;
                } else {
                    if(this.isDifferentColor(tile.getPiece())) {
                        moves.add(tile);
                    }
                    break;
                }
            }
        }

        // Look DOWN
        if(y>0) {
            int j = y-1;
            while(j >= 0) {
                Tile tile = board.getTile(x, j);
                if(!tile.isOccupied()) {
                    moves.add(tile);
                    j--;
                } else {
                    if(this.isDifferentColor(tile.getPiece())) {
                        moves.add(tile);
                    }
                    break;
                }
            }
        }

        // Look LEFT
        if(x>0) {
            int i = x-1;
            while(i >= 0) {
                Tile tile = board.getTile(i, y);
                if(!tile.isOccupied()) {
                    moves.add(tile);
                    i--;
                } else {
                    if(this.isDifferentColor(tile.getPiece())) {
                        moves.add(tile);
                    }
                    break;
                }
            }
        }

        // Look RIGHT
        if(x<7) {
            int i = x+1;
            while(i < 8) {
                Tile tile = board.getTile(i, y);
                if(!tile.isOccupied()) {
                    moves.add(tile);
                    i++;
                } else {
                    if(this.isDifferentColor(tile.getPiece())) {
                        moves.add(tile);
                    }
                    break;
                }
            }
        }

        return moves;
    };

    @Override
    public List<Tile> drawAttackPath(Board board, Tile dest) {
        List<Tile> moves = findLegalMoves(board);
        List<Tile> path = new LinkedList<Tile>();
        if(!moves.contains(dest)) return path;

        int file = this.getCurrentTile().getFile();
        int rank = this.getCurrentTile().getRank();

        int xInc = 0;
        int yInc = 0;
        if(dest.getFile() - file > 0) {
            xInc = 1;
        } else if (dest.getFile() - file < 0) {
            xInc = -1;
        } else {
            xInc = 0;
        }

        if(dest.getRank() - rank > 0) {
            yInc = 1;
        } else if (dest.getRank() - rank < 0) {
            yInc = -1;
        } else {
            yInc = 0;
        }

        int diff = Math.max(Math.abs(dest.getFile() - file), Math.abs(dest.getRank() - rank));
        for(int i = 1; i <= diff; i++) {
            Tile tile = board.getTile(file + xInc*diff, rank + yInc*diff);
            if(moves.contains(tile)) path.add(tile);
        }

        return path;
    }

}