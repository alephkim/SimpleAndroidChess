package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;
import java.util.List;

/**
 * The abstract Piece class to be extended by all the individual piece classes.
 *
 * @author Eric S Kim
 * @author Greg Melillo
 */
public abstract class Piece implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Boolean determining whether the Piece can be captured via en passant.
     */
    public boolean passantable;
    /**
     * The Piece's color enum (i.e., WHITE, BLACK)
     */
    private Color color;
    /**
     * The TIle which the Piece occupies
     */
    private Tile currentTile;
    /**
     * The type of the piece (e.g., KING, QUEEN, PAWN)
     */
    protected PieceType type;
    /**
     * A boolean determining whether a piece has been moved during a game.
     */
    public boolean hasMoved;

    /**
     * Empty Piece Constructor. Initial hasMoved value set to false upon instantiation.
     *
     * @param color the Color of the piece (white = 0, black = 1)
     */
    public Piece(Color color) {
        this.color = color;
        this.hasMoved = false;
        this.passantable = false;
    }

    /**
     * Sets the Piece's current Tile to that of the given parameter.
     *
     * @param tile the Tile on which the Piece will be placed
     */
    public void setCurrentTile(Tile tile) {
        currentTile = tile;
    }

    /**
     * Gets the Piece's current Tile.
     *
     * @return the piece's current occupying Tile
     */
    public Tile getCurrentTile() {
        return this.currentTile;
    }

    /**
     * Method that compares the colors of a Piece and a parameter piece and returns a boolean value
     *
     * @param piece the piece being compared
     * @return true if the colors are different, false otherwise
     */
    public boolean isDifferentColor(Piece piece) {
        return this.getColor() == piece.getColor() ? false : true;
    }

    /**
     * Abstract method to be implemented by subclasses. Naively finds all available destination Tiles that the
     * Piece can move to, including the opponent King's Tile and special rules (en passant, castling), so long as there is a clear path.
     *
     * @param board the chessboard on which the Piece is placed
     * @return a list of all possible destinations Tile for a Piece
     */
    public abstract List<Tile> findLegalMoves(Board board);

    /**
     * Abstract method that returns a path from the Piece's Tile to the destination Tile as a List.
     *
     * @param board the chessboard on which the PIece is placed
     * @param dest  the desired destination Tile
     * @return a list of Tiles tracing the Piece's path on the Board to a particular destination if one exists
     */
    public abstract List<Tile> drawAttackPath(Board board, Tile dest);

    /**
     * Method to determine whether a Piece can be captured via en passant. All Pieces are set to false by default.
     *
     * @return true if the Piece can be captured by en passant, false otherwise
     */
    public boolean isPassantable() {
        return passantable;
    }

    /**
     * Returns the color of the piece as an enum.
     *
     * @return the piece's color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Overriding toString method for Piece.
     *
     * @return "w" if the Piece's Color is white, "b" if it is black
     */
    @Override
    public String toString() {
        String color = this.color == Color.WHITE ? "w" : "b";
        return color + this.type.toString();
    }


    /**
     * Method to get a Piece's PieceType.
     *
     * @return the Piece's PieceType (KING, QUEEN, PAWN, etc.)
     */
    public PieceType getType() {
        return this.type;
    }

    /**
     *
     * @return the file name for this corresponding piece (type_color);
     */
    public String getFileName() {
        return type.name() + "_" + color.toString();
    }
}