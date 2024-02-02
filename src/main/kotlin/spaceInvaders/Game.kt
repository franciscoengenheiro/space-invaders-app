package spaceInvaders

import pt.isel.canvas.MouseEvent
import pt.isel.canvas.playSound

// Construtores
data class Area(val width: Int, val height: Int)
data class Lives(val onStart: Int, val current: Int = 0, val pointsLostOnNextLifeUsed: Int = 0)
data class Game(
    val area: Area,
    val alien_shots: List<Shot> = emptyList(),
    val spaceship_shot: Shot? = null,
    val spaceship_shots: List<Shot> = emptyList(),
    val aliens_block: Aliens,
    val ovni: Alien? = null,
    val ship: Spaceship,
    val total_points: Int = 0,
    val elapsed_time: Time = Time(0, 0),
    val difficulty: Difficulty,
    val startscreen: Boolean = true,
    val over: Boolean = false,
    val won: Boolean = false,
    // Este parâmetro é responsável pela alteração do movimento dos aliens. Neste caso
    // representa que, inicialmente, a velocidade do movimento é 6x mais lenta do que
    // a velocidade mais rápida instanciada no enumerado Period.Aliens neste ficheiro
    val module: Int = 6
)

// Classe de enumerados para instanciar as dificuldades presentes no jogo
enum class GameDiff {
    CLASSIC,
    ROGUE,
    MASTER,
    INHUMAN,
}

// Classe de enumerados para instanciar as teclas do teclado que vão ser utilizadas no jogo
enum class Keyboard(val key: Int) {
    ESCAPE(27),
    SPACEBAR(32),
}

// Classe de enumerados para instanciar as diferentes constantes que as funções do tipo onTimeProgress vão receber
enum class Period(val milliseconds: Int) {
    FRAMES(14),
    SHOT(250),
    SECOND(1000),
    ALIEN(80) // Este enumerado vai representar o período mais rápido a que os aliens se podem mover
}

// Função que altera a velocidade do bloco central de aliens consoante o número total ainda existente na janela do jogo
fun Game.increaseAlienSpeedOnNumberOfAliensRemaining(): Game {
    when (this.aliens_block.aliens.size) { // Avaliar quantos aliens estão na lista que corresponde ao bloco central:
        40 -> return this.copy(module = 5)
        30 -> return this.copy(module = 4)
        20 -> return this.copy(module = 3)
        15 -> return this.copy(module = 2)
        10 -> return this.copy(module = 1)
    }
    return this
}

// Função para criar novos tiros da nave, se a dificuldade escolhida o permitir
fun Game.enableUnlimitedSpaceShipShotsOnDiff(): Game {
    if (this.difficulty.enable_unlimitedshots) { // Se a difficuldade permitir tiros da nave infinitos:
        // Guardar numa variável a posição x da nave onde foi feito o disparo
        val inicialshot_xpos: Int = this.ship.position.x
        // Criar uma variável mutável que vair receber os tiros da nave existentes
        var spaceship_shots: List<Shot> = this.spaceship_shots
        // Criar um tiro na posição onde foi feito o disparo
        spaceship_shots += createSpaceShipShot(inicialshot_xpos)
        // Atualizar o jogo
        return this.copy(spaceship_shots = spaceship_shots)
    }
    return this
}

// Função que retorna o jogo depois de uma vida perdida se ainda existirem vidas disponíveis para perder
fun Game.returnGameOnLifeLost(): Game {
    // Criar uma variável que vai representar as vidas no início do jogo
    val livesOnStart: Int = this.difficulty.ship_lives.onStart
    // Criar uma variável que vai representar as vidas que o jogador ainda possui durante o decorrer do jogo
    val currentlives: Int = this.difficulty.ship_lives.current
    // Criar uma variável que vai representar os pontos que irão ser retirados ao jogador quando este perder
    // a vida seguinte durante o decorrer do jogo
    val pointsLostOnNextLifeUsed: Int = this.difficulty.ship_lives.pointsLostOnNextLifeUsed
    // Criar uma variável que vai representar os pontos acumulados desde o início do jogo
    val currentpoints: Int = this.total_points
    if (this.over == true) { // Se o jogo acabou, ou seja, quando o jogador perde uma vida:
        when (this.difficulty.ship_lives.current) {
            3 -> { // Se tiver 3 vidas:
                playSound("shipdamaged")
                // Atualizar o jogo retirando uma vida, entre outras alterações
                return this.copy(
                    alien_shots = emptyList(), ovni = null,
                    difficulty = this.difficulty.copy(
                        ship_lives = Lives(
                            livesOnStart, currentlives - 1,
                            150
                        )
                    ), total_points = currentpoints - pointsLostOnNextLifeUsed, over = false
                )
            }

            2 -> { // Se tiver 2 vidas:
                playSound("shipdamaged")
                // Atualizar o jogo retirando uma vida, entre outras alterações
                return this.copy(
                    alien_shots = emptyList(), ovni = null,
                    difficulty = this.difficulty.copy(
                        ship_lives = Lives(
                            livesOnStart, currentlives - 1,
                            0
                        )
                    ), total_points = currentpoints - pointsLostOnNextLifeUsed, over = false
                )
            }

            1 -> { // Se tiver 1 vida:
                // Acabar o jogo, retirando a última vida, entre outras alterações
                playSound("gameover")
                return this.copy(
                    alien_shots = emptyList(), ovni = null,
                    difficulty = this.difficulty.copy(
                        ship_lives = Lives(
                            livesOnStart, currentlives - 1,
                            0
                        )
                    ), total_points = currentpoints - pointsLostOnNextLifeUsed, over = true
                )
            }
        }
    }
    return this
}

// Função que constrói um novo jogo cada vez que o botão esquerdo ou direito do rato é premido
fun Game.enableSpaceShipShotOnMouseDown(): Game {
    // Se o tiro não existir na janela do jogo:
    if (this.spaceship_shot == null) {
        // Guardar numa variável a posição x da nave onde foi feito o disparo
        val inicialshot_xpos: Int = this.ship.position.x
        // Criar um tiro na posição onde foi feito o disparo
        val ship_shot: Shot = createSpaceShipShot(inicialshot_xpos)
        playSound("shotfired")
        // Guardar o tiro criado nos dados do jogo
        return this.copy(spaceship_shot = ship_shot)
    }
    // Se o tiro existir, não é permitido o disparo enquanto o tiro permanecer na janela do jogo
    return this
}

// Função que constrói um novo jogo cada vez que o cursor do rato é movido dentro da janela do jogo
fun Game.moveSpacehShipOnMouseMove(mouse: MouseEvent): Game {
    // Se a coordenada x do rato estiver dentro dos parâmetros onde a nave não saí da janela do jogo:
    if (mouse.x in this.ship.basewidth / 2..(WIDTH - this.ship.basewidth / 2)) {
        // Guardar na variável "ship", que representa uma nave, uma cópia do jogo onde é alterado a posição da nave
        // consoante a coordenada x do rato
        val new_ship = this.ship.copy(position = Position(mouse.x, this.ship.position.y))
        // Guardar a nave criada nos dados do jogo
        return this.copy(ship = new_ship)
    }
    return this
}

// Função para mover os aliens no jogo e o ovni se este existir
fun Game.buildGameOnAlienPeriod(): Game {
    playSound("invadersmoving")
    return this.copy(
        aliens_block = this.aliens_block.copy(aliens = this.aliens_block.aliens).moveAliens(this),
        ovni = this.createAlienOvni().ovni
    )
}

// Função que recebe outras funções de extensão Game e constrói o jogo
fun Game.buildGameOnFramesPeriod(): Game {
    return this
        .buildGameOnAlienHit()
        .buildGameOnOvniHit()
        .buildGameOnAlienShotDamaged()
        .moveGameShots()
        .checkIfAlienShotsCollidedWithSpaceShip()
        .moveAlienOvni()
        .checkIfSpaceShipShotCollidedWithAlienShots()
        .checkIfSpaceShipShotCollidedWithProtections()
        .checkIfAlienShotsCollidedWithProtections()
        .checkIfAliensCollidedWithProtections()
        .checkIfAliensReachedSpaceShipMoveHeight()
        .checkIfAllAliensWereKilled()
        .returnGameOnLifeLost()
        .increaseAlienSpeedOnNumberOfAliensRemaining()
        .disableNegativeTotalPointsOnGameLost()
}

// Função para desabilitar o aparecimento de pontos negativos quando o jogo é terminado.
fun Game.disableNegativeTotalPointsOnGameLost(): Game {
    if (this.over == true) { // Quando o jogo acabar:
        if (this.total_points < 0) { // Se os pontos acumulados forem negativos:
            // Colocar o acumulador a zero
            return this.copy(total_points = 0)
        }
    }
    return this
}

// Função para avaliar se o jogador eliminou os aliens do bloco de aliens principal
fun Game.checkIfAllAliensWereKilled(): Game {
    if (this.aliens_block.aliens.isEmpty()) { // Se a lista de aliens estiver vazia:
        if (this.difficulty.enable_loop) { // Verificar se a dificuldade escolhida apresenta o jogo em ciclo infinito
            // Dependendo da dificuldade escolhida, retornar ou não as proteções da nave
            val ship_protections = when (this.difficulty.diff) {
                GameDiff.ROGUE -> createProtections()
                GameDiff.CLASSIC -> createProtections()
                GameDiff.MASTER -> emptyList()
                GameDiff.INHUMAN -> emptyList()
            }
            // Voltar o jogo ao estado inicial, mantendo certos parâmetros
            return this.copy(
                alien_shots = emptyList(), spaceship_shots = emptyList(), spaceship_shot = null,
                aliens_block = createAliens(), difficulty = this.difficulty.copy(ship_protections = ship_protections),
                over = false, module = 6
            )
        } else {
            playSound("wingame")
            // Se o jogo não estiver em ciclo infinito, acabar o jogo porque o jogador ganhou
            return this.copy(over = true, won = true)
        }
    }
    // Se a lista de aliens ainda conter aliens, continuar o jogo
    return this
}

// Função para avaliar se algum Alien do bloco principal de Aliens atingiu a altura a que a nave se movimenta
fun Game.checkIfAliensReachedSpaceShipMoveHeight(): Game {
    this.aliens_block.aliens.forEach { // Para cada alien existente no bloco principal:
        // Se o alien atingiu a altura a que a nave se move no jogo:
        if (it.position.y + ActualAlienDimensionOnCanvas.HEIGHT.value > SPACESHIP_MOVE_HEIGHT) {
            playSound("gameover")
            // Acabar o jogo
            return this.copy(over = true)
        }
    }
    return this
}

// Função para atualizar o jogo em cada movimento dos tiros, tanto da nave como dos aliens
fun Game.moveGameShots(): Game {
    // Inicializar uma variável mutável do tipo Shot? que vai representar o tiro da nave e, se este existir,
    // fazê-lo mover na janela do jogo
    val ship_shot: Shot? = if (this.spaceship_shot != null) this.spaceship_shot.moveSpaceShipShot()
    else this.spaceship_shot
    val aliens_shots: List<Shot> = this.alien_shots.mapNotNull { // Para cada tiro dos aliens existente na lista:
        // Fazer mover esses tiros
        it.moveAlienShot()
    }
    val spaceship_shots = this.spaceship_shots.mapNotNull { // Para cada tiro da nave existente na lista:
        // Fazer mover esses tiros
        it.moveSpaceShipShot()
    }
    return this.copy(alien_shots = aliens_shots, spaceship_shots = spaceship_shots, spaceship_shot = ship_shot)
}

// Função para criar novos tiros de alien segundo uma função de probabilidade de sucesso
fun Game.buildNewAlienShotsOnShotPeriod(): Game {
    // Inicializar uma variável mutável que vai receber inicialmente a lista de tiros dos aliens existente
    var alien_shots: List<Shot> = this.alien_shots
    if (this.shotProb()) { // Ao chamar a função de probabilidade, se esta retornar verdade:
        // Juntar um tiro à lista de tiros dos aliens
        alien_shots += getRandomAlienShot(this)
    }
    // Guardar a nova lista nos dados do jogo
    return this.copy(alien_shots = alien_shots)
}

// Função para mover o Ovni na janela principal do jogo, se este existir
fun Game.moveAlienOvni(): Game {
    // Inicializar uma variável mutável do tipo Shot? que vai indicar se o Ovni existe ou não na janela do jogo
    val newalien_ovni: Alien? = if (this.ovni != null) {
        playSound("ufo_lowpitch")
        playSound("ufo_highpitch")
        this.ovni.moveAlienOvni()
    } else this.ovni
    // Guardar nos dados do jogo as alterações feitas ao Ovni
    return this.copy(ovni = newalien_ovni)
}

// Função para determinar os pontos resultantes da eliminação de um alien consoante o seu tipo
fun Alien.calculatePointsIfAlienWasHit(): Int {
    return when (this.type) {
        AlienType.ALIEN_SQUID -> {
            AlienType.ALIEN_SQUID.pointsOnDeath
        }

        AlienType.ALIEN_CRAB -> {
            AlienType.ALIEN_CRAB.pointsOnDeath
        }

        AlienType.ALIEN_OCTOPUS -> {
            AlienType.ALIEN_OCTOPUS.pointsOnDeath
        }

        AlienType.ALIEN_OVNI -> {
            AlienType.ALIEN_OVNI.pointsOnDeath
        }
    }
}

// Função para construir o jogo sempre o Ovni é atingido por um tiro da nave
fun Game.buildGameOnOvniHit(): Game {
    if (this.ovni != null) { // Se o ovni existir no jogo:
        // Perceber se existe ou não colisão com o tiro da nave ou tiros, dependendo da dificuldade escolhida,
        // e guardar o resultado numa nova variável
        val ovni = this.ovni.checkIfSpaceShipShotCollidedWithAlien(this)
            .checkIfSpaceShipShotsCollidedWithAlien(this)
        // Se existir colisão, a função auxiliar está encarregue de eliminar o ovni, o tiro e atualizar os pontos
        return this.copy(ovni = ovni).removeOvniIfCollisionWasDetected()
    } else {
        return this
    }
}

// Função para construir o jogo sempre que um alien é atingido por um tiro da nave
fun Game.buildGameOnAlienHit(): Game {
    val new_alienslist = this.aliens_block.aliens.map { // Para cada alien existente na lista:
        // Perceber se existe ou não colisão com o tiro da nave ou tiros, dependendo da dificuldade escolhida,
        // e guardar o resultado numa nova variável
        val alien = it.checkIfSpaceShipShotCollidedWithAlien(this)
            .checkIfSpaceShipShotsCollidedWithAlien(this)
        alien
    }
    val new_spaceshipshots = this.spaceship_shots.map { // Para cada tiro da nave existente na lista:
        // Perceber se existe ou não colisão com os aliens da lista e guardar o resultado numa nova variável
        val shot = it.checkIfSpaceShipShotsCollidedWithAlien(this)
        shot
    }
    // Se existir colisão, a função auxiliar está encarregue de eliminar o alien, o tiro e atualizar os pontos
    return this.copy(
        spaceship_shots = new_spaceshipshots,
        aliens_block = this.aliens_block.copy(aliens = new_alienslist)
    )
        .removeAlienIfCollisionWasDetected()
}

// Função para construir o jogo sempre que um tiro dos aliens é atingido por um tiro da nave
fun Game.buildGameOnAlienShotDamaged(): Game {
    val new_alienslist = this.alien_shots.map { // Para cada alien existente na lista:
        // Perceber se existe ou não colisão com o tiro da nave ou tiros, dependendo da dificuldade escolhida,
        // e guardar o resultado numa nova variável
        val alien = it.checkIfAlienShotCollidedSpaceShipShots(this)
        alien
    }
    // Se existir colisão, a função auxiliar está encarregue de eliminar o alien, o tiro e atualizar os pontos
    return this.copy(alien_shots = new_alienslist)
        .removeAlienShotIfCollisionWasDetected()
}
