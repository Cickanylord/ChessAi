package ai_engine.board.pieces

import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName



data class Bishop(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,
    override var hasMoved: Boolean = false
) : Piece {
    override val name: PieceName = PieceName.BISHOP
    override var position: Pair<Int, Int> = Pair(i,j)

    override fun step(i: Int, j: Int){
        this.i = i
        this.j = j
        this.position = Pair(i,j)
        hasMoved = true
    }

    override fun flip() {
        i = 7 - i
        j = 7 - j
        this.position = Pair(i,j)
    }

    override fun getAllMoves(): Array<MutableList<Pair<Int, Int>>> {
        val steps = Array(4) { mutableListOf<Pair<Int, Int>>() }
            for (col in 1 until 8) {
                steps[0].add(Pair(i + col, j + col))
                steps[1].add(Pair(i + col, j - col))
                steps[2].add(Pair(i - col, j + col))
                steps[3].add(Pair(i - col, j - col))
            }
        return dropOutOfBoardSteps(steps)
    }
}