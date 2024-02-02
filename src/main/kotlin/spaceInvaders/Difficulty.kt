package spaceInvaders

import pt.isel.canvas.MouseEvent
import pt.isel.canvas.playSound

data class Difficulty(
    val diff: GameDiff,
    val ship_lives: Lives,
    val alien_shotspeed: AlienShotSpeed,
    val alien_shotchance: Int,
    val alienblock_horizontalshift: Int,
    val ship_protections: List<Protection> = emptyList(),
    val enable_loop: Boolean,
    val enable_unlimitedshots: Boolean
)

// Função para criar um objeto do tipo Difficulty consoante a dificuldade escolhida
fun Game.createGameDifficulty(diff: GameDiff, ship_protections: List<Protection> = emptyList()): Game {
    val new_difficulty: Difficulty = when (diff) {
        GameDiff.ROGUE -> { // Dificuldade ROGUE escolhida:
            Difficulty(
                diff = diff, ship_lives = Lives(3, 3, 100),
                alien_shotspeed = AlienShotSpeed(min = 1, max = 4), alien_shotchance = 50,
                alienblock_horizontalshift = 4, ship_protections = ship_protections, enable_loop = false,
                enable_unlimitedshots = false
            )
        }

        GameDiff.CLASSIC -> { // Dificuldade CLASSIC escolhida:
            Difficulty(
                diff = diff, ship_lives = Lives(3, 3, 100),
                alien_shotspeed = AlienShotSpeed(min = 3, max = 6), alien_shotchance = 60,
                alienblock_horizontalshift = 4, ship_protections = ship_protections, enable_loop = true,
                enable_unlimitedshots = false
            )
        }

        GameDiff.MASTER -> { // Dificuldade MASTER escolhida:
            Difficulty(
                diff = diff, ship_lives = Lives(2, 2, 150),
                alien_shotspeed = AlienShotSpeed(min = 5, max = 8), alien_shotchance = 75,
                alienblock_horizontalshift = 7, enable_loop = true, enable_unlimitedshots = false
            )
        }

        GameDiff.INHUMAN -> { // Dificuldade INHUMAN escolhida:
            Difficulty(
                diff = diff, ship_lives = Lives(1, 1, 0),
                alien_shotspeed = AlienShotSpeed(min = 7, max = 10), alien_shotchance = 90,
                alienblock_horizontalshift = 7, enable_loop = true, enable_unlimitedshots = true
            )
        }
    }
    return this.copy(difficulty = new_difficulty, startscreen = false)
}

// Função para escolher a dificuldade na janela inicial consoante a posição do rato
fun Game.chooseDifficultyOnMouseDown(mouse: MouseEvent, ship_protections: List<Protection>): Game {
    // Coordenadadas do primeiro retângulo:
    // this.drawRect(270, 245, 155, 35, Color.INGAME.hexa, 1)
    if (mouse.x in 270..270 + 155 && mouse.y in 245..245 + 35) {
        playSound("selectdiff")
        return this.createGameDifficulty(GameDiff.ROGUE, ship_protections)
        // Coordenadadas do segundo retângulo:
        // this.drawRect(270, 295, 155, 35, Color.INGAME.hexa, 1)
    } else if (mouse.x in 270..270 + 155 && mouse.y in 295..295 + 35) {
        playSound("selectdiff")
        return this.createGameDifficulty(GameDiff.CLASSIC, ship_protections)
        // Coordenadadas do terceiro retângulo:
        // this.drawRect(270, 345, 155, 35, Color.INGAME.hexa, 1)
    } else if (mouse.x in 270..270 + 155 && mouse.y in 345..345 + 35) {
        playSound("selectdiff")
        return this.createGameDifficulty(GameDiff.MASTER)
        // Coordenadadas do quarto retângulo:
        // this.drawRect(270, 395, 155, 35, Color.INGAME.hexa, 1)
    } else if (mouse.x in 270..270 + 155 && mouse.y in 395..395 + 35) {
        playSound("selectdiff")
        return this.createGameDifficulty(GameDiff.INHUMAN)
    }
    return this
}
