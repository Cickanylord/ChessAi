package ai_engine.ai

import ai_engine.ai.boardPositionValues.aiPawnPos
import ai_engine.ai.boardPositionValues.bishopPositionValue
import ai_engine.ai.boardPositionValues.bishopValue
import ai_engine.ai.boardPositionValues.kingPos
import ai_engine.ai.boardPositionValues.knightPositionValue
import ai_engine.ai.boardPositionValues.knightValue
import ai_engine.ai.boardPositionValues.pawnValue
import ai_engine.ai.boardPositionValues.queenPos
import ai_engine.ai.boardPositionValues.queenValue
import ai_engine.ai.boardPositionValues.rookPos
import ai_engine.ai.boardPositionValues.rookValue
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
        minMax(boardData, originalDepth, true, -6000000, 7000000)

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

        var stepsForAi = boardLogic.getMovesWithPiece(aiColor)
        var stepsForOppositeColor = boardLogic.getMovesWithPiece(aiColor.oppositeColor())

        if (depth == 0) return boardEvaluator(boardData)

        // ai loses
        if (stepsForAi.isEmpty()) {
            return -90000
        }

        // ai wins
        if (stepsForOppositeColor.isEmpty()) {
            return 50000
        }

        if (maximizing) {

            val steps = stepsForAi
            for (i in steps.indices) {
                //save current branch
                if (depth == originalDepth) {
                    branch = steps[i]
                }
                //init new board
                val tmp = BoardLogic(BoardData(boardData.toString()))
                val tmpPiece = tmp.board.getPiece(steps[i].first.position)
                tmp.board.movePiece(tmpPiece, steps[i].second)
                //Log.d("MAX", "\n${boardEvaluator(tmp)}\n${tmp.printBoard()}\n")
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

