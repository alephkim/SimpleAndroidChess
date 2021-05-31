package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The Pawn subclass of Piece.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 *
 */
public class Pawn extends Piece implements Serializable {

    private static final long serialVersionUID = 1L;
    /**Pawn constructor.
     * @param color the color of the Pawn
     */
    public Pawn(Color color) {
        super(color);
        this.type = PieceType.PAWN;
    }

    @Override
    public List<Tile> findLegalMoves(Board board) {	//enpassant not implemented
        Tile[][] b = board.getBoard();
        List<Tile> moves = new LinkedList<Tile>();


        int file = this.getCurrentTile().getFile();
        int rank = this.getCurrentTile().getRank();

        //PAWN behaves differently according to its color
        if(this.getColor() == Color.WHITE) {
            if(rank < 7) {
                //PAWN MOVEMENT
                if(!b[file][rank+1].isOccupied()) {
                    moves.add(b[file][rank+1]);
                    if(!this.hasMoved && !b[file][rank+2].isOccupied()) {
                        moves.add(b[file][rank+2]);
                    }
                }
                //PAWN CAPTURES
                if(file > 0 && b[file-1][rank+1].isOccupied() && this.isDifferentColor(b[file-1][rank+1].getPiece())) {
                    moves.add(b[file-1][rank+1]);
                }
                if(file < 7 && b[file+1][rank+1].isOccupied() && this.isDifferentColor(b[file+1][rank+1].getPiece())) {
                    moves.add(b[file+1][rank+1]);
                }
            }
            //EN PASSANT
            if(rank == 4) {
                if(file > 0 && !b[file-1][rank+1].isOccupied() && b[file-1][rank].isOccupied()
                        && this.isDifferentColor(b[file-1][rank].getPiece()) && b[file-1][rank].getPiece().isPassantable()) {
                    moves.add(b[file-1][rank+1]);
                }
                if(file < 7 && !b[file+1][rank+1].isOccupied() && b[file+1][rank].isOccupied()
                        && this.isDifferentColor(b[file+1][rank].getPiece()) && b[file+1][rank].getPiece().isPassantable()) {
                    moves.add(b[file+1][rank+1]);
                }
            }
        } else {	//black PAWN
            if(rank > 0) {
                //PAWN MOVEMENT
                if(!b[file][rank-1].isOccupied()) {
                    moves.add(b[file][rank-1]);
                    if(!this.hasMoved && !b[file][rank-2].isOccupied()) {
                        moves.add(b[file][rank-2]);
                    }
                }
                //PAWN CAPTURES
                if(file > 0 && b[file-1][rank-1].isOccupied() && this.isDifferentColor(b[file-1][rank-1].getPiece())) {
                    moves.add(b[file-1][rank-1]);
                }
                if(file < 7 && b[file+1][rank-1].isOccupied() && this.isDifferentColor(b[file+1][rank-1].getPiece())) {
                    moves.add(b[file+1][rank-1]);
                }
            }
            //EN PASSANT
            if(rank == 3) {
                if(file > 0 && !b[file-1][rank-1].isOccupied() && b[file-1][rank].isOccupied()
                        && this.isDifferentColor(b[file-1][rank].getPiece()) && b[file-1][rank].getPiece().isPassantable()) {
                    moves.add(b[file-1][rank-1]);
                }
                if(file < 7 && !b[file+1][rank-1].isOccupied() && b[file+1][rank].isOccupied()
                        && this.isDifferentColor(b[file+1][rank].getPiece()) && b[file+1][rank].getPiece().isPassantable()) {
                    moves.add(b[file+1][rank-1]);
                }
            }
        }
        return moves;
    };

    @Override
    public List<Tile> drawAttackPath(Board board, Tile dest) {
        List<Tile> moves = findLegalMoves(board);
        List<Tile> path = new LinkedList<Tile>();
        if(moves.contains(dest)) path.add(dest);
        return path;
    };


}
