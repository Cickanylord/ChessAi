package ai_engine.ai

import ai_engine.ai.BoardPositionValues.aiPawnPos
import ai_engine.ai.BoardPositionValues.bishopPositionValue
import ai_engine.ai.BoardPositionValues.bishopValue
import ai_engine.ai.BoardPositionValues.kingPos
import ai_engine.ai.BoardPositionValues.knightPositionValue
import ai_engine.ai.BoardPositionValues.knightValue
import ai_engine.ai.BoardPositionValues.pawnValue
import ai_engine.ai.BoardPositionValues.queenPos
import ai_engine.ai.BoardPositionValues.queenValue
import ai_engine.ai.BoardPositionValues.rookPos
import ai_engine.ai.BoardPositionValues.rookValue
import ai_engine.board.BoardData
import ai_engine.board.BoardLogic
import ai_engine.board.pieces.enums.PieceColor
import ai_engine.board.pieces.enums.PieceName
import ai_engine.board.pieces.Piece
import kotlin.math.max
import kotlin.math.min

class ChessAI(
    private val aiColor: PieceColor,
    private val boardData: BoardData
) {
    fun getTheNextStep(): Pair<Piece?, Pair<Int, Int>> {
        minMax(boardData, originalDepth, true, Int.MIN_VALUE, Int.MAX_VALUE)

        return bestChoice
    }

    val originalDepth = 3
    var branch: Pair<Piece?, Pair<Int, Int>> = Pair(null, Pair(-1, -1))
    var bestChoice: Pair<Piece?, Pair<Int, Int>> = Pair(null, Pair(-1, -1))

    fun minMax(
        boardData: BoardData,
        depth: Int,
        maximizing: Boolean,
        alpha: Int,
        beta: Int
    ): Int {
        var alpha = alpha
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE
        val boardLogic = BoardLogic(boardData)

        val stepsForAi = boardLogic.getMovesWithPiece(aiColor)
        val stepsForOppositeColor = boardLogic.getMovesWithPiece(aiColor.oppositeColor())

        if (depth == 0) return boardEvaluator(boardData)

        // ai loses
        if (stepsForAi.isEmpty()) {
            return Int.MIN_VALUE
        }

        // ai wins
        if (stepsForOppositeColor.isEmpty()) {
            return Int.MAX_VALUE
        }

        if (maximizing) {

            for (i in stepsForAi.indices) {
                //save current branch
                if (depth == originalDepth) {
                    branch = stepsForAi[i]
                }
                //init new board
                val tmp = BoardData(boardData.toString()).boardLogic
                val tmpPiece = tmp.board.getPiece(stepsForAi[i].first.position)
                tmp.board.movePiece(tmpPiece, stepsForAi[i].second)
                //new board eval
                val value = minMax(tmp.board, depth - 1, false, alpha, beta)
                max = max(value, max)
                if (max == value && depth == originalDepth) {
                    bestChoice = branch
                }

                alpha = max(alpha, value)
                if (beta <= alpha) {
                    break
                }

            }
            return max
        } else {
            //Log.d("MIN", "\n${board.printBoard()}\n")
            val steps = stepsForOppositeColor
            for (i in steps.indices) {
                //init new board
                val tmp = BoardLogic(BoardData(boardData.toString()))
                val tmpPiece = tmp.board.getPiece(steps[i].first.position)
                tmp.board.movePiece(tmpPiece, steps[i].second)
                //Log.d("MIN", "\n${boardEvaluator(tmp)}\n${tmp.printBoard()}\n")
                //new board eval
                val value = minMax(tmp.board, depth - 1, true, alpha, beta)
                min = min(value, min)
                alpha = min(beta, value)
                if (beta <= alpha) {
                    break
                }
            }
        }
        return min
    }


    //old stuff
    private fun boardEvaluator(board: BoardData): Int {
        var value = 0
        board.getAllPieces().forEach {
            if (it.pieceColor == aiColor) {
                when (it.name) {
                    PieceName.PAWN -> {
                        value += pawnValue + aiPawnPos(it.position)
                    }

                    PieceName.KNIGHT -> {
                        value += knightValue + knightPositionValue(it.position)
                    }

                    PieceName.BISHOP -> {
                        value += bishopValue + bishopPositionValue(it.position)
                    }

                    PieceName.ROOK -> {
                        value += rookValue + rookPos(it.position)
                    }

                    PieceName.QUEEN -> {
                        value += queenValue + queenPos(it.position)
                    }

                    PieceName.KING -> {
                        value += kingPos(it.position)
                    }
                }
            } else {
                when (it.name) {
                    PieceName.PAWN -> {
                        value -= (pawnValue + aiPawnPos(Pair(7 - it.i, 7 - it.j)))
                    }

                    PieceName.KNIGHT -> {
                        value -= (knightValue + knightPositionValue(it.position))
                    }

                    PieceName.BISHOP -> {
                        value -= (bishopValue + bishopPositionValue(it.position))
                    }

                    PieceName.ROOK -> {
                        value -= (rookValue + rookPos(it.position))
                    }

                    PieceName.QUEEN -> {
                        value -= (queenValue + queenPos(it.position))
                    }

                    PieceName.KING -> {
                        value -= kingPos(Pair(7 - it.i, 7 - it.j))
                    }
                }
            }
        }
        return value
    }
}

