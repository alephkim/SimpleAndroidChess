package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The King subclass of Piece.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class King extends Piece implements Serializable {
    private static final long serialVersionUID = 1L;
    /**King constructor.
     * @param color the color of the King
     */
    public King(Color color) {
        super(color);
        this.type = PieceType.KING;
    }

    @Override
    public List<Tile> findLegalMoves(Board board) {
        Tile[][] b = board.getBoard();
        LinkedList<Tile> moves = new LinkedList<Tile>();
        int x = this.getCurrentTile().getFile();
        int y = this.getCurrentTile().getRank();

        //destX and destY are the relative positions that the king can move to.
        int[] destX = {  x,  x, x-1, x-1, x-1, x+1, x+1, x+1};
        int[] destY = {y+1,y-1,   y, y+1, y-1,   y, y+1, y-1};

        for(int i = 0; i < 8; i++) {
            try {
                Tile tile = board.getTile(destX[i], destY[i]);
                if(tile.isOccupied()) {
                    if(this.isDifferentColor(tile.getPiece())) {
                        moves.add(tile);
                    }
                } else {
                    moves.add(tile);
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }

        //NAIVE CASTLING, does not check to see if empty Tiles are under attack
        if(!this.hasMoved) {
            if(b[7][y].isOccupied() && !b[7][y].getPiece().hasMoved && !this.isDifferentColor(b[7][y].getPiece())) {
                if(!b[6][y].isOccupied() && !b[5][y].isOccupied()) {
                    moves.add(b[x+2][y]);
                }
            }
            if(b[0][y].isOccupied() && !b[0][y].getPiece().hasMoved && !this.isDifferentColor(b[0][y].getPiece())) {
                if(!b[1][y].isOccupied() && !b[2][y].isOccupied() && !b[3][y].isOccupied()) {
                    moves.add(b[x-2][y]);
                }
            }
        }

        return moves;
    }

    @Override
    public List<Tile> drawAttackPath(Board board, Tile dest) {
        List<Tile> moves = findLegalMoves(board);
        List<Tile> path = new LinkedList<Tile>();
        if(moves.contains(dest)) path.add(dest);
        return path;
    }



}