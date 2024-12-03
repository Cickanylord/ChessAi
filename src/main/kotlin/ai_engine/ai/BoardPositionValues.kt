package ai_engine.ai

object BoardPositionValues {
    const val pawnValue = 100
    const val bishopValue = 300
    const val knightValue = 300
    const val rookValue = 500
    const val queenValue = 900

    private val bishopPos = arrayOf(
        intArrayOf(-5, -5, -5, -5, -5, -5, -5, -5),
        intArrayOf(-5, 10, 5, 8, 8, 5, 10, -5),
        intArrayOf(-5, 5, 3, 8, 8, 3, 5, -5),
        intArrayOf(-5, 3, 10, 3, 3, 10, 3, -5),
        intArrayOf(-5, 3, 10, 3, 3, 10, 3, -5),
        intArrayOf(-5, 5, 3, 8, 8, 3, 5, -5),
        intArrayOf(-5, 10, 5, 8, 8, 5, 10, -5),
        intArrayOf(-5, -5, -5, -5, -5, -5, -5, -5)
    )

    private val knightPos = arrayOf(
        intArrayOf(-10, -5, -5, -5, -5, -5, -5, -10),
        intArrayOf(-8, 0, 0, 3, 3, 0, 0, -8),
        intArrayOf(-8, 0, 10, 8, 8, 10, 0, -8),
        intArrayOf(-8, 0, 8, 10, 10, 8, 0, -8),
        intArrayOf(-8, 0, 8, 10, 10, 8, 0, -8),
        intArrayOf(-8, 0, 10, 8, 8, 10, 0, -8),
        intArrayOf(-8, 0, 0, 3, 3, 0, 0, -8),
        intArrayOf(-10, -5, -5, -5, -5, -5, -5, -10)
    )

    private val pawnPosEnemy = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(5, 10, 15, 20, 20, 15, 10, 5),
        intArrayOf(4, 8, 12, 16, 16, 12, 8, 4),
        intArrayOf(0, 6, 9, 10, 10, 9, 6, 0),
        intArrayOf(0, 4, 6, 10, 10, 6, 4, 0),
        intArrayOf(0, 2, 3, 4, 4, 3, 2, 0),
        intArrayOf(0, 0, 0, -5, -5, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    )

    private val pawnPosAi = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, -5, -5, 0, 0, 0),
        intArrayOf(0, 2, 3, 4, 4, 3, 2, 0),
        intArrayOf(0, 4, 6, 10, 10, 6, 4, 0),
        intArrayOf(0, 6, 9, 10, 10, 9, 6, 0),
        intArrayOf(4, 8, 12, 16, 16, 12, 8, 4),
        intArrayOf(5, 10, 15, 20, 20, 15, 10, 5),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    )

    private val rookPos = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(5, 10, 10, 10, 10, 10, 10, 5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
        intArrayOf(0, 0, 0, 5, 5, 0, 0, 0)
    )

    private val queenPos = arrayOf(
        intArrayOf(-20, -10, -10, -5, -5, -10, -10, -20),
        intArrayOf(-10, 0, 0, 0, 0, 0, 0, -10),
        intArrayOf(-10, 0, 5, 5, 5, 5, 0, -10),
        intArrayOf(-5, 0, 5, 5, 5, 5, 0, -5),
        intArrayOf(0, 0, 5, 5, 5, 5, 0, -5),
        intArrayOf(-10, 5, 5, 5, 5, 5, 0, -10),
        intArrayOf(-10, 0, 5, 0, 0, 0, 0, -10),
        intArrayOf(-20, -10, -10, -5, -5, -10, -10, -20)
    )

    private val kingPos = arrayOf(
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
        intArrayOf(-20, -30, -30, -40, -40, -30, -30, -20),
        intArrayOf(-10, -20, -20, -20, -20, -20, -20, -10),
        intArrayOf(20, 20, 0, 0, 0, 0, 20, 20),
        intArrayOf(20, 30, 10, 0, 0, 10, 30, 20)
    )

    fun bishopPositionValue(pair: Pair<Int, Int>): Int {
        return bishopPos[pair.first][pair.second]
    }
    
    fun knightPositionValue(pair: Pair<Int, Int>): Int {
        return knightPos[pair.first][pair.second]
    }

    fun enemyPawnPosition(pair: Pair<Int, Int>): Int {
        return pawnPosEnemy[pair.first][pair.second]
    }

    fun aiPawnPos(pair: Pair<Int, Int>): Int {
        return pawnPosAi[pair.first][pair.second]
    }

    fun rookPos(pair: Pair<Int, Int>): Int {
        return rookPos[pair.first][pair.second]
    }

    fun queenPos(pair: Pair<Int, Int>): Int {
        return queenPos[pair.first][pair.second]
    }

    fun kingPos(pair: Pair<Int, Int>): Int {
        return kingPos[pair.first][pair.second]
    }
}