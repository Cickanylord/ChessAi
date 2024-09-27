package ai_engine.board.pieces.enums

enum class PieceColor {
    BLACK, WHITE;

    fun oppositeColor(): PieceColor {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
        }
    }
}


