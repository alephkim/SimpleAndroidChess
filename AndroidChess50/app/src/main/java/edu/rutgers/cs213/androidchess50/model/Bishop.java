package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The Bishop subclass of Piece.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Bishop extends Piece implements Serializable {

    private static final long serialVersionUID = 1L;
    /**Bishop constructor.
     * @param color the color of the Bishop
     */
    public Bishop(Color color) {
        super(color);
        this.type = PieceType.BISHOP;
    }

    @Override
    public List<Tile> findLegalMoves(Board board) {
        LinkedList<Tile> moves = new LinkedList<Tile>();
        int x = this.getCurrentTile().getFile();
        int y = this.getCurrentTile().getRank();

        int[] incX = {1, 1, -1, -1};
        int[] incY = {1, -1, 1, -1};

        for(int i = 0; i < 4; i++) {
            int mult = 1;
            int file = x + incX[i]*mult;
            int rank = y + incY[i]*mult;
            while(file >= 0 && file < 8 && rank >= 0 && rank < 8) {
                Tile tile = board.getTile(file, rank);
                if(!tile.isOccupied()) {
                    moves.add(tile);
                } else {
                    if(this.isDifferentColor(tile.getPiece())) {
                        moves.add(tile);
                    }
                    break;
                }
                mult++;
                file = x + incX[i]*mult;
                rank = y + incY[i]*mult;
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
    };





}