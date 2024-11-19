package ai_engine.board.pieces

import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName


data class Pawn(
    override var pieceColor: PieceColor,
    override var i: Int,
    override var j: Int,

    override var hasMoved: Boolean = false
) : Piece {

    override var position: Pair<Int, Int> = Pair(i,j)
    override val name: PieceName = PieceName.PAWN



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

    override fun getAllMoves(): Array<MutableList<Pair<Int, Int>>>{
        val steps = Array(3) { mutableListOf<Pair<Int, Int>>() }

        if (pieceColor == PieceColor.BLACK) {

            steps[0].add(Pair(i + 1, j))
            if (i == 1) {
                steps[0].add(Pair(i + 2, j))
            }


            //side step
            steps[1].add(Pair(i + 1, j - 1))
            steps[2].add(Pair(i + 1, j + 1))
        }

        if (pieceColor == PieceColor.WHITE) {
            steps[0].add(Pair(i - 1, j))

            if (i == 6) {
                steps[0].add(Pair(i - 2, j))
            }


            steps[1].add(Pair(i - 1, j - 1))
            steps[2].add(Pair(i - 1, j + 1))
        }
        return dropOutOfBoardSteps(steps)
    }
}