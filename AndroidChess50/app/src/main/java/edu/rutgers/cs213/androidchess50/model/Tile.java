package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;

/**The Tile class which the Board class is composed of. Each Tile represents a tile on a chessboard and holds a Piece.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 */
public class Tile implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * The piece occupying the chosen tile.
     */
    private Piece currentPiece;
    /**
     * The color of the chosen tile.
     */
    Color tile_color;
    /**
     * The rank and file positions of the Tile on the Board. Rank and file are both counted from 0 upward.
     */
    private int file, rank;



    /**Tile constructor. Keeps index location within the Board array.
     * @param file		the column file of the tile (a to h represented as 0 to 7 in code)
     * @param rank		the row rank of the tile (1 to 8 represented as 0 to 7 in code)
     */
    public Tile(int file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    /**Gets the Color ENUM value of a given Tile.
     * @return		the Color of the Tile
     */
    public Color getTileColor() {
        return this.tile_color;
    }

    /**Gets the current Piece occupying the Tile.
     * @return		the Piece on the Tile
     */
    public Piece getPiece() {
        return this.currentPiece;
    }

    /**Determines whether the Tile is occupied by a Piece.
     * @return		true if the Tile is occupied, false otherwise
     */
    public boolean isOccupied() {
        return this.currentPiece != null;
    }

    /**Gets the rank of the Tile. int 0 corresponds to rank 1, and int 7 corresponds to rank 8.
     * @return		the rank of the Tile
     */
    public int getRank() {
        return this.rank;
    }

    /**Gets the file of the Tile. int 0 corresponds to file a, and int 7 corresponds to file h.
     * @return		the file of the Tile
     */
    public int getFile() {
        return this.file;
    }

    /**Places the given Piece on the Tile using the Piece.setCurrentTile() method.
     * @param p		the Piece to be placed on the current Tile
     */
    public void putPiece(Piece p) {
        if(p == null) return;
        currentPiece = p;
        p.setCurrentTile(this);
    }

    /**Removes the Piece on the Tile. Provides the removed Piece as a return value.
     * @return		the removed Piece
     */
    public Piece removePiece() {
        Piece p = this.currentPiece;
        this.currentPiece = null;
        return p;
    }


    /**Moves a Piece to the destination Tile if it is possible. Returns a boolean value.
     * @param dest		the destination Tile
     * @return true if the Piece was moved, false otherwise
     */
    public boolean movePiece(Tile dest) {
        System.out.println(this.getPiece());
        System.out.println(dest.getPiece() + "\n");

        if(dest.isOccupied() && (dest.getPiece().getColor() == this.getPiece().getColor())) {
            return false;
        }

        dest.putPiece(this.removePiece());
        return true;
    }


    /**
     * Overrides the toString() method.
     *
     * @return the String of the piece or color of the tile as it will appear on the terminal board
     *
     */
    @Override
    public String toString() {
        if(this.isOccupied()) {
            return currentPiece.toString();
        } else {
            return this.tile_color == Color.WHITE ? "  " : "##";
        }
    }
}