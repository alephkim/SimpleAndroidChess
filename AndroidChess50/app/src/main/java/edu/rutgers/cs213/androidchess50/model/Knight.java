package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The Knight subclass of Piece.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Knight extends Piece implements Serializable {
    private static final long serialVersionUID = 1L;
    /**Knight constructor.
     * @param color the color of the Knight
     */
    public Knight(Color color) {
        super(color);
        this.type = PieceType.KNIGHT;
    }

    @Override
    public List<Tile> findLegalMoves(Board board) {
        LinkedList<Tile> moves = new LinkedList<Tile>();
        int x = this.getCurrentTile().getFile();
        int y = this.getCurrentTile().getRank();

        //destX and destY are the relative positions that the knight can move to.
        int[] destX = {x+1, x+1, x+2, x+2, x-1, x-1, x-2, x-2};
        int[] destY = {y-2, y+2, y-1, y+1, y-2, y+2, y-1, y+1};

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