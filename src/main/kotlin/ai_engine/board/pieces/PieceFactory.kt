package ai_engine.board.pieces

import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName
import javax.swing.text.Position

object PieceFactory {
    fun makePiece(pieceName: PieceName,color: PieceColor, position: Pair<Int, Int> ): Piece {
        validatePosition(position)
        return when(pieceName) {
           PieceName.PAWN -> Pawn(color, position.first, position.second)
           PieceName.KNIGHT -> Knight(color, position.first, position.second)
           PieceName.BISHOP -> Bishop(color, position.first, position.second)
           PieceName.ROOK -> Rook(color, position.first, position.second)
           PieceName.QUEEN -> Queen(color, position.first, position.second)
           PieceName.KING -> King(color, position.first, position.second)
        }
    }
    private fun validatePosition(position: Pair<Int, Int>) {
        val (row, col) = position
        if (row !in 0..7 || col !in 0..7) {
            throw IllegalArgumentException("Invalid position: ($row, $col). Position must be within 0..7 for both row and column.")
        }
    }
}