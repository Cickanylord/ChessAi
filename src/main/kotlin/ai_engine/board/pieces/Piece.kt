package ai_engine.board.pieces

import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName


interface  Piece  {
    fun step(i: Int, j: Int)
    fun getAllMoves(): Array<MutableList<Pair<Int, Int>>>
    fun flip()

    var pieceColor: PieceColor
    val name: PieceName
    var position: Pair<Int, Int>
    var hasMoved: Boolean

    var i: Int
    var j: Int

    val letter: Char
        get() = name.toString()[0].let {
            var pieceChar = it
            if(name == PieceName.KNIGHT){
                pieceChar = 'N'
            }

            if (pieceColor == PieceColor.BLACK) {
                pieceChar.lowercaseChar()
            } else {
                pieceChar
            }
        }
    fun dropOutOfBoardSteps(steps :Array<MutableList<Pair<Int, Int>>>): Array<MutableList<Pair<Int, Int>>> {
        return steps.map {
            it.filter {
                (it.first in 0..7 && it.second in 0..7 )
            }.toMutableList()
        }.toTypedArray()
    }
}