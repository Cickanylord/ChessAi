package ai_engine.board

import ai_engine.ai.ChessAI
import ai_engine.board.BoardCoordinates.getCoordinate
import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName
import ai_engine.board.pieces.Piece


import kotlin.math.absoluteValue

fun main() {

    println(BoardCoordinates.toString())
    println(BoardCoordinates.getCoordinate("c6"))
    println(BoardCoordinates.getTileName(0, 3))


    //TODO
    //there is something wrong with castling. if the rook is removed by piece the castling right is not revoked
    //The castling right isn't revoked when the rook is captured
    //debug this
    //Bn2kbnr/p1p1pppp/8/3N4/2q3P1/5P2/PPPPP2P/R1BQK2R w KQkq - 0 1
    //rn2kbnr/pBp1pppp/4b3/4q3/6P1/2N5/PPPPPP1P/R1BQK2R w KQkq - 0 1
    //Bn2kbnr/p1p1pppp/8/3N4/2q3P1/5P2/PPPPP2P/R1BQK2R w KQkq - 0 1

    //new bug castling right? 
    //rn2kbnr/pBp1pppp/4b3/4q3/6P1/2N5/PPPPPP1P/R1BQK2R w KQkq - 0 1

    //EMPTY PIECE WTF?????
    //8/p4Rpr/4k3/1R1Q4/4p1p1/8/P1PPP1K1/2B5 w - - 0 1



    val boardData = BoardData("rnbqkbnr/pp1pppp1/8/1PpP4/7p/8/P1P1PPPP/RNBQKBNR w KQkq c6 0 5")
    val board = BoardLogic(boardData)
   // println(boardData.fen)
    //println(boardData.printBoard())
    cordinate()
    val king = board.board.getPiece(6,0)!!
   // println(board.getLegalMoves(king))
    println(boardData.toString())
    println( board.getLegalMoves(king))
    //board.move(king, Pair(2,2))
    println(boardData.printBoard())

    //board.move(board.board.getPiece(6,7), Pair(4,7))
    println(boardData.toString())



    var color = PieceColor.WHITE

    while (true) {
        val ai = ChessAI(color ,boardData)
        val nextStep = ai.getTheNextStep()
        println(nextStep)
        println(boardData.toString())
        board.move(nextStep.first, nextStep.second)
        println(boardData.printBoard())
        color = color.oppositeColor()
    }






   // king.getAllMoves().forEach { println(it) }


    //println(board.getLegalMoves(king))
    //println(boardData.printBoard())

    //board.board.getPiece(5,2)!!.getAllMoves().forEach { println(it) }
    /*
    println(board.getAvailableSteps(board.board.getPiece(0,4)!!))
    board.board.getPiece(0,4)!!.getAllMoves().forEach { println(it) }
    board.step(board.board.getPiece(0,4)!!, Pair(0,3))
    println(Fen(boardData).toString())
    println(boardData.printBoard())

     */
}
class BoardLogic(val board: BoardData) {
    /**
     * This function gets all the legal moves for a piece
     * @param piece the piece which moves we get
     * @return the list of legal moves
     */
    fun getLegalMoves(piece: Piece): MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()

        getPieceVision(piece, final)
        //removeMovesResultingInCheck(piece, final)

        return final
            .filter { pos ->

                //init new board to check if the king is in check.
                val tmpBoard = BoardLogic(BoardData(board.toString()))
                tmpBoard.board.movePiece(tmpBoard.board.getPiece(piece.position),pos)

                //if the king in check it can't castle.
                if (piece.name == PieceName.KING) {
                    if (scanBoardForCheck(piece.pieceColor) && (pos.second - piece.j).absoluteValue == 2) {
                        //filters out castling moves
                        return@filter false
                    }
                }
                //scans if the king is in check after movement
                !scanBoardForCheck(piece.pieceColor, tmpBoard)
        } as MutableList<Pair<Int, Int>>
    }


    /**
     * Scans the pieces vision line by line puts the truly visible fields in a list
     * @param piece the piece which moves are getting calculated
     * @param final the list of the visible fields
     */
    private fun getPieceVision(piece: Piece, final: MutableList<Pair<Int, Int>>, possibleMoves: Array<MutableList<Pair<Int, Int>>> = emptyArray()) {
        val allMoves: Array<MutableList<Pair<Int, Int>>> = if(possibleMoves.isEmpty()) piece.getAllMoves() else possibleMoves
        allMoves.forEach {
            for (i in it.indices) {
                val currentField = it[i]
                val currentPiece = board.getPiece(currentField.first, currentField.second)

                if (currentField != piece.position) {
                    //There is a piece on this field
                    if (currentPiece != null) {
                        //if the piece is from the other color it can be captured
                        if (piece.pieceColor != currentPiece.pieceColor) { final.add(currentField) }
                        //because pawns can only hit sideways -> it can't capture piece in front
                        if (currentField.second == piece.j && piece.name == PieceName.PAWN) { final.remove(currentField) }
                        //the piece can't be move past other pieces -> the lines has no more legal moves
                        break
                    }

                    //add en passant move if possible
                    if (PieceName.PAWN == piece.name && currentField == getCoordinate(board.possibleEnPassantTargets)) { final.add(currentField) }

                    if (piece.name != PieceName.PAWN || currentField.second == piece.j) { final.add(currentField) }



                    if (piece.name == PieceName.KING && (currentField.second - piece.j).absoluteValue == 2 ) {
                        //Invalid castling removing
                        //the king can't castle if it has no castling right or the king doesn't see the rook
                        if (board.colorHasCastlingRight(piece.pieceColor)) {
                            if (removeInvalidCastling(piece, currentField)) { final.remove(currentField) }
                        } else {
                            final.remove(currentField)
                        }
                    }
                }
            }
        }
    }

    /**
     * Implement piece movement logic.
     * @param move the move we want to make
     * @param piece the piece we want to move with.
     *
     * The function invokes the getLegalMoves
     * if @param move is not in the list of legal moves then nothing happens
     */

    fun move(piece: Piece?, move: Pair<Int, Int>) {
        if(piece != null) {
            val moves = getLegalMoves(piece)
            if (moves.contains(move)) {
                //TODO EN-PASSANT

                //Moving the rook when castling
                if (piece.name == PieceName.KING) {
                    val i = if (piece.pieceColor == PieceColor.WHITE) {
                        7
                    } else {
                        0
                    }

                    //Castles KingSide rook
                    if (move.second - piece.j == 2) {
                        board.movePiece(board.getPiece(i, 7), Pair(i, 5))
                    }
                    //Castles QueenSide rook
                    if (move.second - piece.j == -2) {
                        board.movePiece(board.getPiece(i, 0), Pair(i, 3))
                    }
                }

                //En Passant move
                if(piece.name == PieceName.PAWN && move == getCoordinate(board.possibleEnPassantTargets)) {
                    when(piece.pieceColor) {
                        PieceColor.WHITE -> {
                            board.movePiece(null, move.copy(first = move.first + 1))
                        }
                        PieceColor.BLACK -> {
                            board.movePiece(null, move.copy(first = move.first - 1))
                        }
                    }
                }


                //moves the piece on the board
                board.movePiece(piece, move)
                board.activeColor = board.activeColor.oppositeColor()

                //TODO Check if this is necessary
                //updating the board after the piece movement
                //setting castling rights after a movement on the board
                //The king moves
                if (piece.name == PieceName.KING) {
                    when (piece.pieceColor) {
                        PieceColor.WHITE -> {
                            board.castlingRights.kw = false
                            board.castlingRights.qw = false
                        }

                        PieceColor.BLACK -> {
                            board.castlingRights.kb = false
                            board.castlingRights.qb = false
                        }
                    }
                }
            }
        }

    }
    /**
     * This function checks if the king can legally do the castling move
     * @param piece the king which moves we check if ti can castle
     * @param fieldPos the position where we want to castle
     * @return true if the castling move is not legal and return false if the move is legal
     */

    private fun removeInvalidCastling(piece: Piece, fieldPos: Pair<Int, Int>): Boolean {
        val rights = board.castlingRights
        //QueenSide
        if (fieldPos.second == piece.j - 2) {
            //checks if there is a piece between the king and rook
            for (k in 1..3) {
                if(piece.j != 4)
                println(piece)
                //returns true if there is a piece between the king and rook
                board.getPiece(piece.i, piece.j - k)?.let {
                    return true 
                }

            }
            //checks for castling right
            return when (piece.pieceColor) {
                PieceColor.WHITE -> { rights.qw.not() }
                PieceColor.BLACK -> { rights.qb.not() }
            }
        }
        //KingSideCastling
        if (fieldPos.second == piece.j + 2) {
            //checks if there is a piece between the king and rook
            for (k in 1..2) {
                if (board.getPiece(piece.i, piece.j + k) != null) { return true }
            }
            //checks for castling rights
            return when(piece.pieceColor) {
                PieceColor.WHITE -> { rights.kw.not() }
                PieceColor.BLACK -> { rights.kb.not() }
            }
        }
        return false
    }

    /**
     * This function scans if the next move would result in check for the current players king
     * The king can't be too checked after a move because of the rules of chess
     *
     * @param piece the piece which movement could result in check for its king
     * @param pos the position where we want to move the piece
     *
     * @return if the king gets in check returns true
     */
    private fun scanBoardForCheck(color: PieceColor, boardLogic: BoardLogic = this): Boolean {
        // Get the position of the king for the current player
        val kingPosition = boardLogic.board.getKing(color).position
        // Get all opponent pieces
        val opponentPieces = boardLogic.board.getPiecesByColor(color.oppositeColor())
        // Iterate through each opponent's piece
        for (piece in opponentPieces) {
            //all possible move of opponent
            val allMoves = piece.getAllMoves()
            // Check if the piece has any moves that can target the king
            if (allMoves.flatMap { it }.contains(kingPosition)) {
                val pieceVision = mutableListOf<Pair<Int, Int>>()
                boardLogic.getPieceVision(piece, pieceVision, allMoves)

                // Check if the king's position is within the piece's vision
                if (pieceVision.contains(kingPosition)) {
                    return true // King is in check
                }
            }
        }
        // No opponent pieces can attack the king
        return false
    }

    /**
     * This function removes the moves from the legal which would result in check
     *
     * @param piece the piece which movement could result in check for its king
     * @param final the possible legal moves the piece could make
     */
    fun removeMovesResultingInCheck(piece: Piece, final: MutableList<Pair<Int, Int>>) {
        val shouldRemove: MutableList<Pair<Int, Int>> = mutableListOf()
        final.forEach { pos->
            val tmpBoard = BoardLogic(BoardData(board.toString()))
            tmpBoard.board.movePiece(tmpBoard.board.getPiece(piece.position), pos)
            if (scanBoardForCheck(piece.pieceColor, tmpBoard)) {
                shouldRemove.add(pos)
            }
        }
        final.removeAll(shouldRemove)
    }

    fun getLegalSteps(color: PieceColor):  MutableList<Pair<Int, Int>> {
        val final: MutableList<Pair<Int, Int>> = mutableListOf()
        board.getAllPieces().forEach { final.addAll(getLegalMoves(it)) }
        return final
    }

    /**
     * This function gets all the piece with its moves
     * @param color the color which move we want to get
     * @return the list of all steps with the piece we want to make the step
     */
    fun getMovesWithPiece(color: PieceColor): List<Pair<Piece, Pair<Int,Int>>> {
        val steps = mutableListOf<Pair<Piece, Pair<Int,Int>>>()
        board.getPiecesByColor(color)
            .forEach { piece->
            getLegalMoves(piece)
                .forEach { move->
                steps.add(Pair(piece,move))
            }
        }
        return steps
    }


}