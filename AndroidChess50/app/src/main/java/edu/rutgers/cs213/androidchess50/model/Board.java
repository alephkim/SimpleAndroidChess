package edu.rutgers.cs213.androidchess50.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Board class on which the game is played.
 *
 * @author	Eric S Kim
 * @author	Greg Melillo
 */
public class Board implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * The 2D Tile array to be used in the Board() constructor.
     */
    public Tile[][] board;

    /**
     * Board constructor. Default Pieces occupying Tiles should be empty (null).
     * Color is initialized via loop. board[0][0] corresponds to the tile a1.
     * board[x] corresponds to the lettered files of the chessboard.
     * board[x][y] corresponds to the numbered ranks of the chessboard.
     */
    public Board() {
        this.board = new Tile[8][8];
        for(int file = 0; file < 8; file++)  {
            for(int rank = 0; rank < 8; rank++) {
                this.board[file][rank] = new Tile(file,rank);
                if(file % 2 == 0) {
                    this.board[file][rank].tile_color = rank % 2 == 0 ? Color.BLACK : Color.WHITE;
                } else {
                    this.board[file][rank].tile_color = rank % 2 == 0 ? Color.WHITE : Color.BLACK;
                }
            }
        }
    }

    /**Returns the current board. For use by Piece subclasses.
     * @return the current board
     */
    public Tile[][] getBoard() {
        return this.board;
    }


    /**Gets the Tile located at the given file and rank.
     * @param file		the file of the Tile
     * @param rank		the rank of the Tile
     * @return the Tile at the given file and rank
     */
    public Tile getTile(int file, int rank) {
        return this.board[file][rank];
    }

    /**The method which will move Pieces on the Board.
     * @param start		the starting Tile of the Piece to be moved
     * @param end		the ending TIle of the Piece once it has moved
     * @return			a boolean value noting whether the Piece has been moved
     */
    public boolean movePiece(Tile start, Tile end) {
        if(!start.isOccupied()) {
            return false;
        }

        Piece mover = start.getPiece();
        if(mover.findLegalMoves(this).contains(end)) {
            if(mover.getType() == PieceType.KING) {
                return moveKing(start, end);
            }

            if(mover.getType() == PieceType.PAWN) {
                return movePawn(start,end);
            }

            return capture(start, end);
        }
        return false;
    }

    //randomly moves a piece of the given color
    public Tile moveRandom(Color color) {
        List<Tile> sourceTiles = new ArrayList<Tile>();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Tile t = this.board[file][rank];
                if (t.isOccupied() && t.getPiece().getColor().equals(color) && t.getPiece().findLegalMoves(this).size() > 0)
                    sourceTiles.add(t);
            }
        }

        Collections.shuffle(sourceTiles);
        for (int i = 0; i < sourceTiles.size(); i++) {
            List<Tile> destTiles = sourceTiles.get(i).getPiece().findLegalMoves(this);
            Collections.shuffle(destTiles);
            for (int j = 0; j < destTiles.size(); j++) {
                if (movePiece(sourceTiles.get(i), destTiles.get(j))) return destTiles.get(j);
            }
        }
        return null;
    }

    /**Helper method for specifically moving the King. Covers castling logic.
     * Assumes that the end Tile is a valid destination.
     * @param start		the starting Tile of the King
     * @param end		the destination of the King
     * @return a boolean value noting whether a move has been made
     */
    public boolean moveKing(Tile start, Tile end) {
        Piece king = start.getPiece();
        Color opp_color = king.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
        if(!king.hasMoved && Math.abs(start.getFile() - end.getFile()) == 2 && start.getRank() == end.getRank()) {//castling attempt
            if(isItCheck(king.getColor())) return false;	//king is in check, return false;
            if(end.getFile() - start.getFile() > 0) {	//castling to the right
                Piece rook = this.board[7][start.getRank()].getPiece();
                if(rook.hasMoved) return false;		//rook has moved or is under attack
                for(int file = 5; file < 7; file++) {
                    if(this.board[file][start.getRank()].isOccupied()) return false;	//piece blocks castle path, return false;
                    if(isUnderAttack(this.board[file][start.getRank()], opp_color)) return false;	//path under attack, return false
                }
                //passed all tests, castle and return true;
                king.hasMoved = true;
                rook.hasMoved = true;
                end.putPiece(start.removePiece());
                this.board[5][start.getRank()].putPiece(rook.getCurrentTile().removePiece());
                setAllPassantFalse();
                return true;
            } else {		//castling to the left
                Piece rook = this.board[0][start.getRank()].getPiece();
                if(rook.hasMoved || isUnderAttack(rook.getCurrentTile(), opp_color)) return false;
                for(int file = 1; file < 4; file++) {
                    if(this.board[file][start.getRank()].isOccupied()) return false;
                    if(isUnderAttack(this.board[file][start.getRank()], opp_color)) return false;
                }
                //passed all tests, castle and return true;
                king.hasMoved = true;
                rook.hasMoved = true;
                end.putPiece(start.removePiece());
                this.board[3][start.getRank()].putPiece(rook.getCurrentTile().removePiece());
                setAllPassantFalse();
                return true;
            }
        }
        //not castling, move normally
        return capture(start, end);
    }

    /**Helper method which attempts to move a Pawn and accounts for en passant and pawn promotion.
     * Assumes that Tile start contains a Pawn and that it has a path to its destination.
     * Also assumes that en passant viability has already been determined.
     * @param start		the starting Tile of the Pawn to be moved
     * @param end		the ending TIle of the Pawn once it has moved
     * @return			a boolean value noting whether the Pawn has moved
     */
    public boolean movePawn(Tile start, Tile end) {
        //double advancement
        if(Math.abs(end.getRank() - start.getRank()) == 2) {	//move up two tiles, set to eligible for en passant
            end.putPiece(start.removePiece());
            if(isItCheck(end.getPiece().getColor())) {	//check to see if the move results in a self check
                start.putPiece(end.removePiece());
                return false;
            }
            end.getPiece().hasMoved = true;
            setAllPassantFalse();
            end.getPiece().passantable = true;
            return true;
        }
        //en passant
        if(Math.abs(end.getFile() - start.getFile()) == 1) {
            if(end.getFile() - start.getFile() > 0) {
                //passant to the right
                Tile passant = this.getTile(start.getFile() + 1, start.getRank());
                if(passant.isOccupied() && passant.getPiece().isPassantable()) {
                    Piece hold = passant.removePiece();
                    end.putPiece(start.removePiece());
                    if(isItCheck(end.getPiece().getColor())) {	//check to see if the move results in a self check
                        start.putPiece(end.removePiece());
                        passant.putPiece(hold);
                        return false;
                    }
                    end.getPiece().hasMoved = true;
                    setAllPassantFalse();
                    return true;
                }
            }
            if(end.getFile() - start.getFile() < 0) {
                //passant to the left
                Tile passant = this.getTile(start.getFile() - 1, start.getRank());
                if(passant.isOccupied() && passant.getPiece().isPassantable()) {
                    Piece hold = passant.removePiece();
                    end.putPiece(start.removePiece());
                    if(isItCheck(end.getPiece().getColor())) {	//check to see if the move results in a self check
                        start.putPiece(end.removePiece());
                        passant.putPiece(hold);
                        return false;
                    }
                    end.getPiece().hasMoved = true;
                    setAllPassantFalse();
                    return true;
                }
            }
        }
        //regular capture or advancement
        if(capture(start, end)) {

            return true;
        }
        return false;
    }

    /**Standard capture method that attempts to capture a piece given a start Tile containing the capturing piece
     * and an end Tile. Assumes that both the start Tile is occupied and that its piece can move to the end Tile.
     * The starting Piece simply moves to the end Tile if it is unoccupied.
     * @param start		the Tile containing the capturing piece
     * @param end		the destination Tile that may or may not be occupied
     * @return a boolean confirming whether or not a capture was successfully made
     */
    public boolean capture(Tile start, Tile end) {
        Piece hold = end.removePiece();		//holding capturable piece until Check status is examined
        end.putPiece(start.removePiece());
        if(isItCheck(end.getPiece().getColor())) {	//check to see if the move results in a self check
            start.putPiece(end.removePiece());
            end.putPiece(hold);
            return false;
        }
        end.getPiece().hasMoved = true;
        setAllPassantFalse();
        return true;
    }

    /**Helper method which sets every piece's passantable value to false
     *
     */
    public void setAllPassantFalse() {
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                Tile tile = this.getTile(file, rank);
                if(tile.isOccupied()) tile.getPiece().passantable = false;
            }
        }
    }

    /**Helper method that checks whether a particular Tile is under attack by pieces of the chosen Color
     * @param tile		the Tile in question
     * @param opp_color		the color of the Pieces attacking the Tile
     * @return true if the Tile is under attack, false otherwise
     */
    public boolean isUnderAttack(Tile tile, Color opp_color) {
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                Tile attacker = this.getTile(file, rank);
                if(attacker.isOccupied() && attacker.getPiece().getColor() == opp_color) {
                    List<Tile> attack_path = attacker.getPiece().drawAttackPath(this, tile);
                    if(attack_path.isEmpty()) {
                        continue;
                    } else {
                        if(attack_path.contains(tile)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**Find the Tile occupied by the King matching the given Color
     * @param color		the color of the desired King
     * @return the Tile occupied by the King of the appropriate Color
     */
    public Tile getKing(Color color) {
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                Tile king_tile = this.board[file][rank];
                if(king_tile.isOccupied()) {
                    if(king_tile.getPiece().getType() == PieceType.KING && king_tile.getPiece().getColor() == color) return king_tile;
                }
            }
        }
        return null; //shouldn't happen, two kings will always exist on the board
    }

    /**Examines all pieces with the opposite color as the given parameter color to see if the current board state places that color in check.
     * @param color		the color of the Player whose check status is being determined
     * @return true if the player is in check, false otherwise
     */
    public boolean isItCheck(Color color) {
        Tile king = getKing(color);
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                Tile attacker = this.getTile(file, rank);
                if(attacker.isOccupied() && attacker.getPiece().isDifferentColor(king.getPiece())) {
                    if(attacker.getPiece().drawAttackPath(this, king).isEmpty()) {
                        continue;
                    } else {
                        if(attacker.getPiece().drawAttackPath(this, king).contains(king)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**Method that checks if the player of the given Color is checkmated.
     * @param opp_color		the color of the potentially checkmated player.
     * @return true if checkmate, false otherwise
     */
    public boolean isItCheckmate(Color opp_color) {
        for(int file = 0; file < 8; file++) {
            for(int rank = 0; rank < 8; rank++) {
                Tile start = getTile(file, rank);
                if(start.isOccupied() && start.getPiece().getColor() == opp_color) {
                    List<Tile> moves = start.getPiece().findLegalMoves(this);
                    for(Tile end : moves) {
                        Piece hold = end.removePiece();		//holding capturable piece until Check status is examined
                        end.putPiece(start.removePiece());
                        if(!isItCheck(opp_color)) {	//check to see if the move results in a self check
                            start.putPiece(end.removePiece());
                            end.putPiece(hold);
                            return false;
                        }
                        start.putPiece(end.removePiece());
                        end.putPiece(hold);
                    }
                }
            }
        }
        return true;
    }


    /**
     * Initializes the board by placing the appropriate chess pieces on their appropriate starting tiles.
     */
    public void initializeBoard() {
        for(int i = 0; i < 8; i++) {
            this.board[i][1].putPiece(new Pawn(Color.WHITE));
            this.board[i][6].putPiece(new Pawn(Color.BLACK));
        }
        this.board[0][0].putPiece(new Rook(Color.WHITE));
        this.board[1][0].putPiece(new Knight(Color.WHITE));
        this.board[2][0].putPiece(new Bishop(Color.WHITE));
        this.board[3][0].putPiece(new Queen(Color.WHITE));
        this.board[4][0].putPiece(new King(Color.WHITE));
        this.board[5][0].putPiece(new Bishop(Color.WHITE));
        this.board[6][0].putPiece(new Knight(Color.WHITE));
        this.board[7][0].putPiece(new Rook(Color.WHITE));

        this.board[0][7].putPiece(new Rook(Color.BLACK));
        this.board[1][7].putPiece(new Knight(Color.BLACK));
        this.board[2][7].putPiece(new Bishop(Color.BLACK));
        this.board[3][7].putPiece(new Queen(Color.BLACK));
        this.board[4][7].putPiece(new King(Color.BLACK));
        this.board[5][7].putPiece(new Bishop(Color.BLACK));
        this.board[6][7].putPiece(new Knight(Color.BLACK));
        this.board[7][7].putPiece(new Rook(Color.BLACK));
    }


    /**
     * serializes the game board state and reads it back to copy it
     * @return the copied board instance
     */
    /*
    public Board deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Board) ois.readObject();
        } catch (
                IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    */

    public Board deepCopy() throws Exception
    {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try
        {
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream(); // A
            oos = new ObjectOutputStream(bos); // B
            // serialize and pass the object
            oos.writeObject(this);   // C
            oos.flush();               // D
            ByteArrayInputStream bin =
                    new ByteArrayInputStream(bos.toByteArray()); // E
            ois = new ObjectInputStream(bin);                  // F
            // return the new object
            return (Board)ois.readObject(); // G
        }
        catch(Exception e)
        {
            System.out.println("Exception in ObjectCloner = " + e);
            throw(e);
        }
        finally
        {
            oos.close();
            ois.close();
        }
    }

    /**
     * Prints the current board state in the terminal, including pieces and appropriate rank and file labels.
     *
     */
    public void printBoard() {
        for(int rank=7; rank >= 0; rank--) {
            for(int file=0; file < 8; file++) {
                System.out.print(board[file][rank] + " ");
            }
            System.out.println(rank+1);
        }
        System.out.println(" a  b  c  d  e  f  g  h\n");
    }
}
