package spaceInvaders

// Classe de enumerados para instanciar os parâmetros dos tiros existentes no jogo
enum class ShotAttributes(val value: Int) {
    WIDTH(4),
    HEIGHT(11),
    SPACESHIPSHOT_SPEED(4)
}

// Construtores
data class Position(val x: Int, val y: Int)
data class OffsetVector(val dx: Int, val dy: Int)
data class Shot(val position: Position, val offsetVector: OffsetVector, val isDamaged: Boolean = false)

// Função que recebe uma posição inicial onde foi feito o disparo, e retorna um tiro criado a partir dessa mesma posição
fun createSpaceShipShot(inicialshot_xpos: Int): Shot {
    return Shot(
        position = Position(inicialshot_xpos, SPACESHIP_MOVE_HEIGHT),
        offsetVector = OffsetVector(0, ShotAttributes.SPACESHIPSHOT_SPEED.value)
    )
}

// Função que recebe um tiro do tipo Shot e adiciona-o a uma lista de tiros dos aliens, retornando essa lista
fun createAlienShots(shot: Shot): List<Shot> {
    var alienshots: List<Shot> = listOf(shot)
    alienshots += shot
    return alienshots
}

// Função de extensão de Shot para mover os tiros da nave criados
fun Shot.moveSpaceShipShot(): Shot? {
    if (this.position.y < 0) { // Se o tiro da nave ultrapassar a margem superior da janela do jogo:
        // Eliminá-lo para poder ser criado outro
        return null
    }
    return this.copy(position = (Position(this.position.x, this.position.y - this.offsetVector.dy)))
}

// Função de extensão de Shot para mover os tiros dos aliens criados
fun Shot.moveAlienShot(): Shot? {
    if (this.position.y > ATRIBUTES_LINEHEIGHT - 8) { // Se o tiro dos aliens ultrapassar a margem inferior da janela do jogo:
        // Eliminá-lo para limpar a lista de tiros que não estejam na janela do jogo
        return null
    }
    return this.copy(position = (Position(this.position.x, this.position.y + this.offsetVector.dy)))
}

// Função que recebe o bloco de aliens do jogo e cria um tiro do tipo Shot em qualquer posição aleatória onde estes
// se encontrem na janela do jogo
fun getRandomAlienShot(game: Game): Shot {
    // Se ainda existirem aliens na lista:
    if (game.aliens_block.aliens.size != 0) {
        // Guardar numa variável um alien da lista de aliens escolhido aleatoriamente
        val random_alien = game.aliens_block.aliens.random()
        // Guardar nas variáveis x e y, as coordenadas da posição do alien
        val (x, y) = random_alien.position
        // Criar um tiro com a posição atual do alien e com os parâmetros de velocidade aleatórios, dentro de um
        // intervalo que vai depender da dificuldade escolhida no início do jogo
        return Shot(
            position = Position(
                x + ActualAlienDimensionOnCanvas.WIDTH.value / 2,
                y + ActualAlienDimensionOnCanvas.HEIGHT.value / 2
            ), offsetVector =
            OffsetVector(0, (game.difficulty.alien_shotspeed.min..game.difficulty.alien_shotspeed.max).random())
        )
        // Senão existirem aliens, o jogo acaba e por isso o próximo tiro é criado fora da janela do jogo,
        // e mais tarde eliminado quando chegar à margem inferior da janela do jogo pela função de movimento
    } else {
        return Shot(position = Position(-50, 0), offsetVector = OffsetVector(0, 100))
    }
}

// Função que retorna um valor booleano consoante o cálculo de uma probabilidade de sucesso. A percentagem de sucesso
// vai depender da dificuldade escolhida no início do jogo e tem como objetivo controlar o aparecimento dos tiros
// dos aliens na janela do jogo
fun Game.shotProb(): Boolean {
    val shotprob = (0..100).random()
    if (shotprob < this.difficulty.alien_shotchance) {
        return true
    } else {
        return false
    }
}

