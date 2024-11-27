package ai_engine.board

import ai_engine.board.pieces.PieceFactory
import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName

object BoardValidator {
    private val promotionOptions = listOf(PieceName.QUEEN, PieceName.ROOK, PieceName.BISHOP, PieceName.KNIGHT)

    fun  isMoveValid(board: BoardData, fen: String): Boolean {
        val possibleFens = getPossibleFENs(board)
        return possibleFens.contains(fen)
    }

    fun getPossibleFENs(board: BoardData): List<String> {
        val fenList = mutableListOf<String>()
        val pieces = board.getPiecesByColor(board.activeColor)

        for (piece in pieces) {
            val legalMoves = board.boardLogic.getLegalMoves(piece)

            for (move in legalMoves) {
                val tmpBoard = BoardLogic(BoardData(board.toString()))

                tmpBoard.move(tmpBoard.board.getPiece(piece.position), move)

                val row = if(tmpBoard.board.activeColor == PieceColor.WHITE) 7 else 0

                if ( tmpBoard.board.board[row].any{ it.piece?.name == PieceName.PAWN }) {
                    val pawn = tmpBoard.board.board[row].find { it.piece?.name == PieceName.PAWN }?.piece!!

                    for (option in promotionOptions) {
                        tmpBoard.board.addPiece(
                            PieceFactory.makePiece(option, pawn.pieceColor, pawn.position)
                        )
                        fenList.add(tmpBoard.board.toString())
                    }
                }

                fenList.add(tmpBoard.board.toString())
            }
        }
        return fenList
    }
}