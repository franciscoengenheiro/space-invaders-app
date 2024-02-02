package spaceInvaders

import pt.isel.canvas.Canvas
import pt.isel.canvas.GREEN
import pt.isel.canvas.WHITE

// Constantes
const val ATRIBUTES_LINEHEIGHT: Int = HEIGHT - 35

// Classe de enumerados para instanciar todas as cores usadas para desenhar no Canvas
enum class Color(val hexa: Int) {
    GAMEBACKGROUND(0x000000), // Cor Preta
    GAMEOVER(0xFF0000), // Cor Vermelha
    GAMEWON(0x00FF00), // Cor Verde
    POINTS(0x00FF00), // Cor Verde
    ATTRIBUTES(0xffffff), // Cor Branca
    ALIEN_SHOT(0xFF0000), // Cor Vermelha
    SPACESHIP_SHOT(0xFFFDFF), // Cor Branca
    PROTECTION(0x00FF00), // Cor Verde
    INGAME(0x00FF00), // Cor Verde
    HITBOX(0x000000) // Cor Preta, mudar para WHITE (por exemplo) para ver as hitboxes na janela do jogo
}

// Função para desenhar o jogo completo
fun Canvas.drawGame(
    game: Game, alienSquid_spriteCoords: String, alienCrab_spriteCoords: String,
    alienOctopus_spriteCoords: String, alienOvni_spriteCoords: String
) {
    // Apagar sempre a janela antes de desenhar
    this.erase()
    // Desenhar a imagem de fundo do jogo
    this.drawImage("invadersbackground.png", 0, 0, WIDTH, HEIGHT)
    if (game.startscreen == true) { // Se o jogador está na janela inicial:
        // Desenhar a janela inicial
        this.drawStartScreen()
    } else { // Se o jogador está na janela do jogo principal:
        if (game.over == false) { // e o jogo não terminou:
            // Desenhar a janela do jogo principal
            this.drawInGameScreen(
                game, alienSquid_spriteCoords, alienCrab_spriteCoords,
                alienOctopus_spriteCoords, alienOvni_spriteCoords
            )
        } else if (game.over == true && game.won == true) { // e o jogo terminou porque o jogador ganhou:
            // Desenhar uma janela que representa a vitória do jogo
            this.drawWinScreen(game)
        } else if (game.over == true) { // e o jogo terminou:
            // Desenhar uma janela que representa a derrota do jogo
            this.drawGameOverScreen(game)
        }
    }
}

// Função para desenhar a janela inicial do jogo
fun Canvas.drawStartScreen() {
    // Janela Inicial
    this.drawRect(30, 30, WIDTH - 60, HEIGHT - 60, Color.INGAME.hexa, 1)
    this.drawText(230, 90, "Welcome to", GREEN, 40)
    this.drawText(170, 140, "SPACE INVADERS", GREEN, 40)
    this.drawText(235, 210, "Select Difficulty", WHITE, 28)
    this.drawText(310, 270, "ROGUE", WHITE, 22)
    this.drawText(300, 320, "CLASSIC", WHITE, 22)
    this.drawText(303, 370, "MASTER ", WHITE, 22)
    this.drawText(293, 420, "INHUMAN", WHITE, 22)
    this.drawRectsWithColor(Color.INGAME.hexa)
}

fun Canvas.drawRectsWithColor(color: Int) {
    this.drawRect(270, 245, 155, 35, color, 1)
    this.drawRect(270, 295, 155, 35, color, 1)
    this.drawRect(270, 345, 155, 35, color, 1)
    this.drawRect(270, 395, 155, 35, color, 1)
}

// Função para desenhar a janela do jogo principal
fun Canvas.drawInGameScreen(
    game: Game, alienSquid_spriteCoords: String, alienCrab_spriteCoords: String,
    alienOctopus_spriteCoords: String, alienOvni_spriteCoords: String
) {

    // Chamar a função para desenhar as hitboxes dos aliens consoante o tipo
    this.drawAliensHitBoxes(game.aliens_block.aliens)
    // Chamar a função para desenhar os aliens
    this.drawAliens(
        game.aliens_block.aliens, alienSquid_spriteCoords, alienCrab_spriteCoords,
        alienOctopus_spriteCoords
    )
    if (game.ovni != null) { // Se o ovni existir no jogo:
        // Chamar a função para desenhar a hitbox do ovni
        this.drawAlienOvniHitbox(game.ovni)
        // Chamar a função para desenhar o ovni
        this.drawAlienOvni(game.ovni, alienOvni_spriteCoords)
    }
    if (game.spaceship_shot != null) { // Se o tiro da nave existir:
        // Chamar a função para desenhar o tiro
        this.drawSpaceShipShot(game.spaceship_shot)
    }
    // Chamar a função para desenhar a hitbox da nave
    this.drawSpaceShipHitbox(game.ship)
    // Chamar a função para desenhar a nave
    this.drawSpaceShip(game.ship)
    // Chamar a função para desenhar os tiros dos aliens
    this.drawAlienShots(game.alien_shots)
    // Chamar a função para desenhar os tiros da nave
    this.drawSpaceShipShots(game.spaceship_shots)
    // Chamar a função para desenhar os pontos na janela do jogo
    this.drawInGameTotalPoints(game)
    // Chamar a função para desenhar a dificuldade escolhida na janela do jogo
    this.drawInGameDifficulty(game)
    // Chamar a função para desenhar as vidas restantes na janela do jogo
    this.drawInGameShipLives(game)
    // Desenhar uma linha verde na parte inferior da janela do jogo principal
    this.drawLine(0, ATRIBUTES_LINEHEIGHT, WIDTH, ATRIBUTES_LINEHEIGHT, Color.INGAME.hexa, 1)
    // Chamar a função para desenhar as proteções da nave
    this.drawInGameProtections(game.difficulty.ship_protections)
}

// Função que desenha uma janela após a vitória do jogo
fun Canvas.drawWinScreen(game: Game) {
    this.drawText(220, 140, "You Win!", Color.GAMEWON.hexa, 60)
    this.drawResultsBoard(game)
}

// Função que desenha uma janela após a derrota do jogo
fun Canvas.drawGameOverScreen(game: Game) {
    this.drawText(185, 140, "Game over", Color.GAMEOVER.hexa, 60)
    this.drawResultsBoard(game)
}

// Função que desenha os resultados do jogo
fun Canvas.drawResultsBoard(game: Game) {
    // Calcular as vidas perdidas no decorrer do jogo
    val lives_lost: Int = game.difficulty.ship_lives.onStart - game.difficulty.ship_lives.current
    // Se os retângulos onde estão as dificuldades descritas forem alterados, alterar também na função:
    // "chooseDifficultyOnMouseDown" no ficheiro Game.kt
    this.drawText(240, 220, "Press ESC to Return", Color.ATTRIBUTES.hexa, 22)
    this.drawText(260, 290, "Final Score ", Color.ATTRIBUTES.hexa, 19)
    this.drawText(380, 290, "${game.total_points}", Color.INGAME.hexa, 19)
    this.drawText(260, 320, "Lives Lost", Color.ATTRIBUTES.hexa, 19)
    this.drawText(370, 320, "${lives_lost}", Color.INGAME.hexa, 19)
    this.drawText(260, 350, "Time", Color.ATTRIBUTES.hexa, 19)
    this.drawText(320, 350, "${game.elapsed_time.min}m:${game.elapsed_time.sec}s", Color.INGAME.hexa, 19)
    this.drawText(260, 380, "Difficulty", Color.ATTRIBUTES.hexa, 19)
    this.drawText(360, 380, "${game.difficulty.diff}", Color.INGAME.hexa, 19)
}

// Função que desenha os Aliens presentes no bloco de Aliens principal
fun Canvas.drawAliens(
    aliens: List<Alien>, alienSquid_spriteCoords: String, alienCrab_spriteCoords: String,
    alienOctopus_spriteCoords: String
) {
    aliens.forEach { // Para cada alien existente na lista:
        when (it.type) {
            AlienType.ALIEN_SQUID -> { // Avaliar se é do tipo ALIEN_SQUID:
                // Se for, recortar a imagem Sprite conforme as coordenadas recebidas como parâmetros da função
                this.drawImage(
                    "invaders-sprite.png|$alienSquid_spriteCoords", it.position.x,
                    it.position.y, ActualAlienDimensionOnCanvas.WIDTH.value, ActualAlienDimensionOnCanvas.HEIGHT.value
                )
            }

            AlienType.ALIEN_CRAB -> { // Avaliar se é do tipo ALIEN_CRAB:
                // Se for, recortar a imagem Sprite conforme as coordenadas recebidas como parâmetros da função
                this.drawImage(
                    "invaders-sprite.png|$alienCrab_spriteCoords", it.position.x,
                    it.position.y, ActualAlienDimensionOnCanvas.WIDTH.value, ActualAlienDimensionOnCanvas.HEIGHT.value
                )
            }

            AlienType.ALIEN_OCTOPUS -> { // Avaliar se é do tipo ALIEN_OCTOPUS:
                // Se for, recortar a imagem Sprite conforme as coordenadas recebidas como parâmetros da função
                this.drawImage(
                    "invaders-sprite.png|$alienOctopus_spriteCoords", it.position.x,
                    it.position.y, ActualAlienDimensionOnCanvas.WIDTH.value, ActualAlienDimensionOnCanvas.HEIGHT.value
                )
            }
        }
    }
}

// Função para desenhar o Ovni através do recorte da imagem Sprite conforme as coordenadas recebidas
fun Canvas.drawAlienOvni(ovni: Alien, alienOvni_spriteCoords: String) =
    this.drawImage(
        "invaders-sprite.png|$alienOvni_spriteCoords", ovni.position.x, ovni.position.y,
        Ovni.WIDTH.value, Ovni.HEIGHT.value
    )

// Função para desenhar a nave na janela do jogo
fun Canvas.drawSpaceShip(ship: Spaceship) = this.drawImage(
    "spaceship.png",
    ship.position.x - ship.basewidth / 2, ship.position.y, ship.basewidth, ship.baseheight
)

// Função para desenhar o tiro da nave criado
fun Canvas.drawSpaceShipShot(shot: Shot) {
    return this.drawShot(shot, Color.SPACESHIP_SHOT.hexa)
}

// Função para desenhar todos os tiros dos aliens existentes no jogo
fun Canvas.drawAlienShots(alienshots: List<Shot?>) {
    alienshots.forEach { // Para cada tiro existente na lista de tiros dos aliens:
        if (it != null) { // Se existir:
            // Desenhá-lo
            this.drawShot(it, Color.ALIEN_SHOT.hexa)
        }
    }
}

fun Canvas.drawSpaceShipShots(spaceship_shots: List<Shot?>) {
    spaceship_shots.forEach { // Para cada tiro existente na lista de tiros dos nave:
        if (it != null) { // Se existir:
            // Desenhá-lo
            this.drawShot(it, Color.SPACESHIP_SHOT.hexa)
        }
    }
}

// Função para desenhar um tiro global
fun Canvas.drawShot(shot: Shot, color: Int) {
    return this.drawRect(
        shot.position.x - ShotAttributes.WIDTH.value / 2,
        shot.position.y - ShotAttributes.HEIGHT.value / 2, ShotAttributes.WIDTH.value,
        ShotAttributes.HEIGHT.value, color, 0
    )
}

// Função para desenhar as proteções da nave
fun Canvas.drawInGameProtections(protections: List<Protection>) {
    protections.forEach { Protection -> // Para cada proteção existente na lista
        Protection.protection.forEach { Rect -> // Para cada retângulo existente na proteção
            // Desenhar esse retângulo
            drawRect(Rect.position.x, Rect.position.y, Rect.width, Rect.height, Color.PROTECTION.hexa, 0)
        }
    }
}

// Função para desenhar no canto inferior esquerdo da janela do jogo os pontos atuais do jogador
fun Canvas.drawInGameTotalPoints(game: Game) {
    this.drawText(10, HEIGHT - 10, "SCORE ", Color.ATTRIBUTES.hexa, 19)
    this.drawText(90, HEIGHT - 10, "${game.total_points}", Color.INGAME.hexa, 19)
}

// Função para desenhar no centro da margem inferior da janela do jogo a dificuldade escolhida pelo jogador
fun Canvas.drawInGameDifficulty(game: Game) {
    this.drawText(265, 490, "Difficulty", Color.ATTRIBUTES.hexa, 19)
    this.drawText(365, 490, "${game.difficulty.diff}", Color.INGAME.hexa, 19)
}

// Função para desenhar no canto inferior direito da janela do jogo as vidas restantes do jogador
fun Canvas.drawInGameShipLives(game: Game) {
    this.drawText(WIDTH - 90, HEIGHT - 10, "LIVES ", Color.ATTRIBUTES.hexa, 19)
    this.drawText(WIDTH - 20, HEIGHT - 10, "${game.difficulty.ship_lives.current}", Color.INGAME.hexa, 19)
}

// Função cujo objetivo é ajudar a visualizar na janela do jogo a posição das hitboxes dos aliens
fun Canvas.drawAliensHitBoxes(aliens: List<Alien>) {
    aliens.forEach { // Para cada alien existente na lista:
        if (it.type == AlienType.ALIEN_SQUID) { // Se o seu tipo for ALIEN_SQUID:
            this.drawRect(
                it.position.x + AlienHitBoxOffset.ALIEN_SQUID.x,
                it.position.y + AlienHitBoxOffset.ALIEN_SQUID.y, width = AlienHitBoxOffset.ALIEN_SQUID.width,
                height = AlienHitBoxOffset.ALIEN_SQUID.height, Color.HITBOX.hexa, 0
            )
        } else if (it.type == AlienType.ALIEN_CRAB) { // Se o seu tipo for ALIEN_CRAB:
            this.drawRect(
                it.position.x + AlienHitBoxOffset.ALIEN_CRAB.x,
                it.position.y + AlienHitBoxOffset.ALIEN_CRAB.y, width = AlienHitBoxOffset.ALIEN_CRAB.width,
                height = AlienHitBoxOffset.ALIEN_CRAB.height, Color.HITBOX.hexa, 0
            )
        } else if (it.type == AlienType.ALIEN_OCTOPUS) { // Se o seu tipo for ALIEN_OCTOPUS:
            this.drawRect(
                it.position.x + AlienHitBoxOffset.ALIEN_OCTOPUS.x,
                it.position.y + AlienHitBoxOffset.ALIEN_OCTOPUS.y, width = AlienHitBoxOffset.ALIEN_OCTOPUS.width,
                height = AlienHitBoxOffset.ALIEN_OCTOPUS.height, Color.HITBOX.hexa, 0
            )
        }
    }
}

// Função cujo objetivo é ajudar a visualizar na janela do jogo a posição da hitbox do ovni
fun Canvas.drawAlienOvniHitbox(ovni: Alien) =
    this.drawRect(
        ovni.position.x + AlienHitBoxOffset.ALIEN_OVNI.x,
        ovni.position.y + AlienHitBoxOffset.ALIEN_OVNI.y, width = AlienHitBoxOffset.ALIEN_OVNI.width,
        height = AlienHitBoxOffset.ALIEN_OVNI.height, Color.HITBOX.hexa, 0
    )

// Função cujo objetivo é ajudar a visualizar na janela do jogo a posição da hitbox completa da nave
fun Canvas.drawSpaceShipHitbox(ship: Spaceship) {
    this.drawRect(
        ship.position.x - SpaceShipHitBoxOffset.RECT1.x,
        ship.position.y + SpaceShipHitBoxOffset.RECT1.y, width = SpaceShipHitBoxOffset.RECT1.width,
        height = SpaceShipHitBoxOffset.RECT1.height, Color.HITBOX.hexa
    )
    this.drawRect(
        ship.position.x - SpaceShipHitBoxOffset.RECT2.x,
        ship.position.y + SpaceShipHitBoxOffset.RECT2.y, width = SpaceShipHitBoxOffset.RECT2.width,
        height = SpaceShipHitBoxOffset.RECT2.height, Color.HITBOX.hexa
    )
    this.drawRect(
        ship.position.x - SpaceShipHitBoxOffset.RECT3.x,
        ship.position.y + SpaceShipHitBoxOffset.RECT3.y, width = SpaceShipHitBoxOffset.RECT3.width,
        height = SpaceShipHitBoxOffset.RECT3.height, Color.HITBOX.hexa
    )
    this.drawRect(
        ship.position.x - SpaceShipHitBoxOffset.RECT4.x,
        ship.position.y + SpaceShipHitBoxOffset.RECT4.y, width = SpaceShipHitBoxOffset.RECT4.width,
        height = SpaceShipHitBoxOffset.RECT4.height, Color.HITBOX.hexa
    )
}

