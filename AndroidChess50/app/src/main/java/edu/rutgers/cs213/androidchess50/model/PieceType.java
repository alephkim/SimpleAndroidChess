package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;

/**
 * The PieceType enum type to help distinguish between each chess Piece.
 *
 * @author	Eric S Kim
 * @author	Greg Melillo
 */
public enum PieceType implements Serializable {
    /**
     * The King type. String value is "K"
     */
    KING("K"),
    /**
     * The Queen type. String value is "Q"
     */
    QUEEN("Q"),
    /**
     * The Pawn type. String value is "p"
     */
    PAWN("p"),
    /**
     * The Rook type. String value is "R"
     */
    ROOK("R"),
    /**
     * The Knight type. String value is "N"
     */
    KNIGHT("N"),
    /**
     * The Bishop type. String value is "B"
     */
    BISHOP("B");

    private static final long serialVersionUID = 1L;
    /**
     * The string variable given to each PieceType.
     */
    private final String name;

    /**Enumerator for each PieceType will be given the appropriate displayed Piece name
     * @param name		the name of each Piece according to its PieceType
     */
    private PieceType(String name) {
        this.name = name;
    }

    /**Overriding toString method for the PieceType enum
     * @return the name of the Piece by its first capital letter. (i.e., "K" for King). "N" stands for Knight
     */
    @Override
    public String toString() {
        return this.name;
    }
}