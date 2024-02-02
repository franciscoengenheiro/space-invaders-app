package spaceInvaders

import pt.isel.canvas.Canvas
import pt.isel.canvas.KeyEvent
import pt.isel.canvas.MouseEvent
import pt.isel.canvas.loadSounds
import pt.isel.canvas.onFinish
import pt.isel.canvas.onStart

// Constantes
const val WIDTH: Int = 700
const val HEIGHT: Int = 500

fun main() {
    onStart { // Ao iniciar o Canvas:
        // Criar a área de jogo
        val playable_area: Area = Area(WIDTH, HEIGHT)
        // Criar a janela de jogo
        val frame = Canvas(
            width = playable_area.width, height = playable_area.height,
            background = Color.GAMEBACKGROUND.hexa
        )
        // Criar o centro da nave
        val ship_center: Position = Position(x = WIDTH / 2, y = SPACESHIP_MOVE_HEIGHT)
        // Inicializar a nave
        val ship = Spaceship(ship_center, SPACESHIPBASE_WIDTH, SPACESHIPBASE_HEIGHT)
        // Inicializar uma variável que vai receber todas as proteções da nave presentes no jogo inicialmente
        val ship_protections: List<Protection> = createProtections()
        // Inicializar uma variável que vai receber todos os aliens presentes no jogo inicialmente
        val aliens_block: Aliens = createAliens()
        // Inicializar uma variável mutável que vai representar a primeira animação dos aliens principais
        var animationStep: Int = 0
        // Inicializar uma dificuldade default do jogo
        val new_difficulty: Difficulty = Difficulty(
            diff = GameDiff.ROGUE,
            ship_lives = Lives(3, 3, 150),
            alien_shotspeed = AlienShotSpeed(min = 1, max = 4), alien_shotchance = 50,
            alienblock_horizontalshift = 4, ship_protections = ship_protections, enable_loop = false,
            enable_unlimitedshots = false
        )
        // Instanciar as coordenadas Sprite do tipo ALIEN_SQUID
        var alienSquid_spriteCoords: String = getAlienSpriteCoords(
            AlienStartPosOnSprite.ALIEN_SQUID.x,
            AlienStartPosOnSprite.ALIEN_SQUID.y, AlienSpriteSheetDimension.ALIEN_SQUID.width,
            AlienSpriteSheetDimension.ALIEN_SQUID.height
        )
        // Instanciar as coordenadas Sprite do tipo ALIEN_CRAB
        var alienCrab_spriteCoords: String = getAlienSpriteCoords(
            AlienStartPosOnSprite.ALIEN_CRAB.x,
            AlienStartPosOnSprite.ALIEN_CRAB.y, AlienSpriteSheetDimension.ALIEN_CRAB.width,
            AlienSpriteSheetDimension.ALIEN_CRAB.height
        )
        // Instanciar as coordenadas Sprite do tipo ALIEN_OCTOPUS
        var alienOctupus_spriteCoords: String = getAlienSpriteCoords(
            AlienStartPosOnSprite.ALIEN_OCTOPUS.x,
            AlienStartPosOnSprite.ALIEN_OCTOPUS.y, AlienSpriteSheetDimension.ALIEN_OCTOPUS.width,
            AlienSpriteSheetDimension.ALIEN_OCTOPUS.height
        )
        // Instanciar as coordenadas Sprite do tipo ALIEN_OVNI
        val alienOvni_spriteCoords: String = getAlienSpriteCoords(
            AlienStartPosOnSprite.ALIEN_OVNI.x,
            AlienStartPosOnSprite.ALIEN_OVNI.y, AlienSpriteSheetDimension.ALIEN_OVNI.width,
            AlienSpriteSheetDimension.ALIEN_OVNI.height
        )
        // Criar uma variável mutável que vai representar o tempo decorrido no jogo em segundos inicialmente
        var sec: Int = 0
        // Criar uma variável mutável que vai representar um contador que opera ao longo do periodo definido como o
        // periodo do movimento do bloco principal dos aliens
        var count_move: Int = 0
        // Criar uma variável mutável que vai representar o jogo inicialmente
        var game = Game(
            area = playable_area, alien_shots = emptyList(), aliens_block = aliens_block,
            ship = ship, difficulty = new_difficulty
        )
        // Inicializar as especificações de um tiro dos aliens
        val alien_firstshot: Shot = getRandomAlienShot(game)
        // Criar uma lista do tipo Shot? que vai receber um tiro dos aliens e juntá-lo a uma lista do mesmo tipo
        val alien_shots: List<Shot> = createAlienShots(alien_firstshot)
        // Guardar os novos parâmetros definidos no jogo
        game = game.copy(alien_shots = alien_shots)
        // Desenhar o jogo com os parâmetros inicializados anteriormente
        frame.drawGame(
            game, alienSquid_spriteCoords, alienCrab_spriteCoords, alienOctupus_spriteCoords,
            alienOvni_spriteCoords
        )
        // Carregar os sons do jogo
        loadSounds(
            "shipdamaged", "invadersmoving", "invaderkilled", "shotfired", "selectdiff", "gameover",
            "wingame", "ufo_highpitch", "ufo_lowpitch"
        )
        frame.onTimeProgress(Period.FRAMES.milliseconds) { // Ao fim de Period.Frames.milissegundos:
            // Se o jogador está na janela inicial:
            if (game.startscreen == true) {
                // Deixar o contador de tempo a 0 enquanto é escolhida uma dificuldade
                sec = 0
                // Se o jogador está na janela do jogo principal e o jogo não terminou:
            } else if (game.startscreen == false && game.over == false) {
                // Atualizar o jogo
                game = game.buildGameOnFramesPeriod()
                // Desenhar o jogo com os parâmetros alterados
                frame.drawGame(
                    game, alienSquid_spriteCoords, alienCrab_spriteCoords, alienOctupus_spriteCoords,
                    alienOvni_spriteCoords
                )
            }
        }
        frame.onTimeProgress(Period.SECOND.milliseconds) { // Ao fim de Period.SECOND.milissegundos:
            // Se o jogador está na janela do jogo principal e o jogo não terminou:
            if (game.startscreen == false && game.over == false) {
                // Incrementar o contador de tempo (em segundos)
                sec = countInGameElapsedTime(sec)
            }
            // Calcular o tempo decorrente no jogo num objeto do tipo Time, que engloba minutos e segundos
            // Guardar esse objeto no jogo
            game = game.calculateResultTimeOnSecondPeriod(sec)
        }
        frame.onTimeProgress(Period.SHOT.milliseconds) { // Ao fim de Period.SHOT.milissegundos:
            // Se o jogador está na janela do jogo principal e o jogo não terminou:
            if (game.startscreen == false && game.over == false) {
                // Atualizar o jogo
                game = game.buildNewAlienShotsOnShotPeriod()
                // Desenhar o jogo com os parâmetros alterados
                frame.drawGame(
                    game, alienSquid_spriteCoords, alienCrab_spriteCoords, alienOctupus_spriteCoords,
                    alienOvni_spriteCoords
                )
            }
        }
        frame.onTimeProgress(Period.ALIEN.milliseconds) { // Ao fim de Period.ALIEN.milissegundos:
            // Incrementar o contador com 1
            count_move++
            // Se a divisão do contador pelo módulo currente for 0:
            if (count_move % game.module == 0) {
                // Se o jogador está na janela do jogo principal e o jogo não terminou:
                if (game.startscreen == false && game.over == false) {
                    // Atualizar o jogo
                    game = game.buildGameOnAlienPeriod()
                    // Obter a animação dos aliens seguinte
                    animationStep = getNextAlienAnimationStep(animationStep)
                    // Calcular as novas coordenadas consoante o passo da animação e o tipo de alien associado
                    alienSquid_spriteCoords = calculateNewSpriteCoords(animationStep, AlienType.ALIEN_SQUID)
                    alienCrab_spriteCoords = calculateNewSpriteCoords(animationStep, AlienType.ALIEN_CRAB)
                    alienOctupus_spriteCoords = calculateNewSpriteCoords(animationStep, AlienType.ALIEN_OCTOPUS)
                    // Desenhar o jogo com os parâmetros alterados
                    frame.drawGame(
                        game, alienSquid_spriteCoords, alienCrab_spriteCoords, alienOctupus_spriteCoords,
                        alienOvni_spriteCoords
                    )
                }
            }
        }
        frame.onKeyPressed { keyEvent: KeyEvent ->
            // Se o jogador está na janela do jogo principal e o jogo não terminou:
            if (game.startscreen == false && game.over == false) {
                when (keyEvent.code) { // Quando é premido a barra do espaço:
                    // Permitir o disparo da nave somente se o tiro da nave não estiver presente na janela do jogo
                    Keyboard.SPACEBAR.key -> {
                        if (game.difficulty.enable_unlimitedshots) {
                            // Permitir vários disparos da nave em simultâneo
                            game = game.enableUnlimitedSpaceShipShotsOnDiff()
                        } else {
                            // Permitir o disparo da nave somente se o tiro da nave não estiver presente na janela do jogo
                            game = game.enableSpaceShipShotOnMouseDown()
                        }
                    }
                }
                // Se o jogador está na janela do jogo principal e o jogo terminou:
            } else if (game.startscreen == false && game.over == true) {
                when (keyEvent.code) {
                    Keyboard.ESCAPE.key -> { // Quando é premido a tecla ESC
                        // Recomeçar o jogo
                        game = game.copy(
                            alien_shots = emptyList(), spaceship_shots = emptyList(),
                            aliens_block = aliens_block, ovni = null, spaceship_shot = null, total_points = 0,
                            elapsed_time = Time(0, 0),
                            difficulty = game.difficulty.copy(ship_protections = ship_protections),
                            startscreen = true, over = false, won = false, module = 6
                        )
                    }
                }
                // Desenhar o jogo com os parâmetros alterados
                frame.drawGame(
                    game, alienSquid_spriteCoords, alienCrab_spriteCoords, alienOctupus_spriteCoords,
                    alienOvni_spriteCoords
                )
            }
        }
        frame.onMouseDown { mouse: MouseEvent ->
            // Se o botão do rato for premido:
            if (mouse.down == true) {
                // Enquanto o jogador estiver na janela inicial:
                if (game.startscreen == true) {
                    // É escolhida a dificuldade do jogo consoante a posição do rato
                    game = game.chooseDifficultyOnMouseDown(mouse, ship_protections)
                    // Enquanto o jogador estiver na janela do jogo principal e o jogo não terminou:
                } else if (game.startscreen == false && game.over == false) {
                    if (game.difficulty.enable_unlimitedshots) {
                        // Permitir vários disparos da nave em simultâneo
                        game = game.enableUnlimitedSpaceShipShotsOnDiff()
                    } else {
                        // Permitir o disparo da nave somente se o tiro da nave não estiver presente na janela do jogo
                        game = game.enableSpaceShipShotOnMouseDown()
                    }
                }
            }
        }
        frame.onMouseMove { mouse: MouseEvent ->
            // Se o jogador está na janela do jogo principal e o jogo não terminou:
            if (game.startscreen == false && game.over == false) {
                // Permitir o movimento da nave usando o cursor do rato enquanto este estiver dentro da janela
                // do jogo
                game = game.moveSpacehShipOnMouseMove(mouse)
            }
        }
    }
    onFinish { // Ao terminar o Canvas:
        // Mensagem na consola
        println("Closing the game...")
    }
}




