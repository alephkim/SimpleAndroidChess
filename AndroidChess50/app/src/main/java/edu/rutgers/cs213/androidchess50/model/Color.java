package edu.rutgers.cs213.androidchess50.model;

import java.io.Serializable;

public enum Color implements Serializable {

    /**
     * The color White. String value is "White"
     */
    WHITE("White"),
    /**
     * The color Black. String value is "Black"
     */
    BLACK("Black");
    private static final long serialVersionUID = 1L;
    /**
     * The color given to an object that utilizes this enum.
     */
    private final String color;

    /**
     * @param color		the color of each Piece to be displayed on the board
     */
    private Color(String color) {
        this.color = color;
    }

    /**Overriding toString() method for the Color enum.
     * @return "White" if WHITE, "Black" if BLACK
     */
    @Override
    public String toString() {
        return this.color;
    }
}
