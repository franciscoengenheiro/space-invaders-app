package spaceInvaders

// Constantes
const val ALIEN_VERTICALSHIFT: Int = 20

// Construtores
data class AlienShotSpeed(val min: Int, val max: Int)
data class Alien(val position: Position, val type: AlienType, val isHit: Boolean = false)
data class Aliens(val aliens: List<Alien>, val direction: AliensDirection = AliensDirection.RIGHT)

// Operadores necessários para operações com vetores
operator fun Position.plus(offset: OffsetVector) = Position(this.x + offset.dx, this.y + offset.dy)
operator fun Position.minus(offset: OffsetVector) = Position(this.x - offset.dx, this.y - offset.dy)

// Classe de enumerados para instanciar o comprimento e largura da imagem dos aliens na imagem Sprite associada
enum class AlienSpriteSheetDimension(val width: Int = 112, val height: Int = 80) {
    ALIEN_SQUID,
    ALIEN_CRAB,
    ALIEN_OCTOPUS,
    ALIEN_OVNI(224, 100)
}

// Classe de enumerados para instanciar a posição inicial dos aliens consoante a sua posição na imagem Sprite
enum class AlienStartPosOnSprite(val x: Int = 0, val y: Int = 0) {
    ALIEN_SQUID,
    ALIEN_CRAB(y = AlienSpriteSheetDimension.ALIEN_SQUID.height),
    ALIEN_OCTOPUS(y = AlienSpriteSheetDimension.ALIEN_SQUID.height * 2),
    ALIEN_OVNI(y = AlienSpriteSheetDimension.ALIEN_SQUID.height * 3)
}

// Classe de enumerados para instanciar a localização de cada tipo de alien na imagem Sprite associada. Apesar de
// a maioria destes enumerados não serem utilizados, considerou-se necessário incluí-los para explicar o aparecimento
// dos que são.
enum class AlienSpriteSheetLocation(val column: Int, val row: Int) {
    ALIEN_SQUID(2, 1),
    ALIEN_CRAB(2, 2),
    ALIEN_OCTOPUS(2, 3),
    ALIEN_OVNI(1, 4)
}

// Classe de enumerados para instanciar as dimensões reais que os aliens pertencentes ao bloco central ocupam no jogo
enum class ActualAlienDimensionOnCanvas(val value: Int) {
    WIDTH(AlienSpriteSheetDimension.ALIEN_SQUID.width / 2),
    HEIGHT(AlienSpriteSheetDimension.ALIEN_SQUID.height / 2)
}

// Classe de enumerados para instanciar cada tipo de alien existente no jogo
enum class AlienType(val pointsOnDeath: Int) {
    ALIEN_SQUID(30),
    ALIEN_CRAB(20),
    ALIEN_OCTOPUS(10),
    ALIEN_OVNI(100)
}

// Class de enumerados para instanciar os parâmetros do Ovni
enum class Ovni(val value: Int) {
    // Dimensões no canvas:
    WIDTH(80),
    HEIGHT(35),
    HORIZONTALSHIFT(2)
}

// Classe de enumerados para instanciar a direção dos aliens na janela do jogo
enum class AliensDirection {
    RIGHT,
    DOWN_AFTERRIGHT,
    LEFT,
    DOWN_AFTERLEFT
}

// Classe de enumerados para definir o offset da AlienHitbox consoante o tipo de alien. A sua utilização tem como
// objetivo criar uma hitbox que contenha somente os pixeis onde os aliens estão desenhados e não a imagem que os
// engloba. Se a imagem Sprite onde os aliens estão inseridos for alterada, os valores instanciados nesta classe
// terão que ser modificados, pois representam um offset em relação a essa imagem.
enum class AlienHitBoxOffset(val x: Int, val y: Int, val width: Int, val height: Int) {
    ALIEN_SQUID(10, 3, 35, 35),
    ALIEN_CRAB(6, 3, 45, 35),
    ALIEN_OCTOPUS(3, 3, 50, 35),
    ALIEN_OVNI(3, 3, 68, 30)
}


// Função auxiliar para a criação dos Aliens por linha
fun createAlienRow(aliens: List<Alien>, inicial_xpos: Int, inicial_ypos: Int, type: AlienType): List<Alien> {
    // Guardar numa nova variável a lista de aliens existente
    var existing_alienlist: List<Alien> = aliens
    // Instanciar uma variável mutável do tipo Alien
    var new_Alien: Alien
    // Instanciar uma variável mutável, que vai receber a posição x inicial
    var new_x: Int = inicial_xpos
    // Instanciar uma variável mutável, que vai receber a posição y inicial
    val new_y: Int = inicial_ypos
    for (column in 1..11) { // Para cada coluna:
        // Guardar na variável mutável as alterações na posição e no tipo de alien
        new_Alien = Alien(position = Position(new_x, new_y), type = type)
        // Guardar o novo alien criado na lista
        existing_alienlist += new_Alien
        // Calcular a nova posição x para desenhar o alien seguinte na linha
        new_x += ActualAlienDimensionOnCanvas.WIDTH.value
    }
    // Retornar a lista que vai sendo alterada
    return existing_alienlist
}

// Função para criar os aliens presentes no jogo
fun createAliens(): Aliens {
    // Inicializar uma variável mutável que é, inicialmente, uma lista vazia do tipo Alien
    var aliens_list: List<Alien> = emptyList()
    // Primeira linha de Aliens
    aliens_list = createAlienRow(
        aliens_list, 0, ActualAlienDimensionOnCanvas.HEIGHT.value,
        AlienType.ALIEN_SQUID
    )
    // Segunda linha de Aliens
    aliens_list = createAlienRow(
        aliens_list, 0, ActualAlienDimensionOnCanvas.HEIGHT.value * 2,
        AlienType.ALIEN_SQUID
    )
    // Terceira linha de Aliens
    aliens_list = createAlienRow(
        aliens_list, 0, ActualAlienDimensionOnCanvas.HEIGHT.value * 3,
        AlienType.ALIEN_CRAB
    )
    // Quarta linha de Aliens
    aliens_list = createAlienRow(
        aliens_list, 0, ActualAlienDimensionOnCanvas.HEIGHT.value * 4,
        AlienType.ALIEN_CRAB
    )
    // Quinta linha de Aliens
    aliens_list = createAlienRow(
        aliens_list, 0, ActualAlienDimensionOnCanvas.HEIGHT.value * 5,
        AlienType.ALIEN_OCTOPUS
    )
    return Aliens(aliens = aliens_list)
}

// Função de extensão de Aliens para fazer mover os aliens no jogo
fun Aliens.moveAliens(game: Game): Aliens {
    if (this.aliens.isEmpty()) { // Senão existirem aliens no jogo:
        return this
    } else {
        // Inicializar uma variável que vai receber a posição do alien que estiver mais à esquerda, e o alien
        // que estiver mais à direita na janela do jogo
        val (first_alienxpos, last_alienxpos) = findFirstAndLastAlienColumnWiseXPos(this.aliens)
        this.aliens.forEach { // Para cada alien existente na lista de aliens:
            if (this.direction == AliensDirection.RIGHT) { // Avaliar se a sua direção é para a direita:
                // Se o alien mais à direita tocar na margem do jogo:
                if (last_alienxpos + ActualAlienDimensionOnCanvas.WIDTH.value == WIDTH) {
                    // Mudar a direção para baixo
                    return changeAlienDirection(game = game, direction = AliensDirection.DOWN_AFTERRIGHT)
                }
                // Se for, continuar na mesma direção
                return changeAlienDirection(game = game, direction = AliensDirection.RIGHT)
            } else if (this.direction == AliensDirection.DOWN_AFTERRIGHT) { // Avaliar se a sua direção é para baixo:
                // Se for, mudar a direção para a esquerda
                return changeAlienDirection(game = game, direction = AliensDirection.LEFT)
            } else if (this.direction == AliensDirection.LEFT) { // Avaliar se a sua direção é para a esquerda:
                // Se o alien mais à esquerda tocar na margem do jogo:
                if (first_alienxpos == 0) {
                    // Mudar a direção para baixo
                    return changeAlienDirection(game = game, direction = AliensDirection.DOWN_AFTERLEFT)
                }
                // Se for, continuar na mesma direção
                return changeAlienDirection(game = game, direction = AliensDirection.LEFT)
            } else if (this.direction == AliensDirection.DOWN_AFTERLEFT) { // Avaliar se a sua direção é para baixo:
                // Se for, mudar a direção para a direita
                return changeAlienDirection(game = game, direction = AliensDirection.RIGHT)
            }
        }
    }
    return this
}

// Função de extensão de Aliens para auxiliar a função principal de movimento dos aliens
fun Aliens.changeAlienDirection(game: Game, direction: AliensDirection): Aliens {
    // Inicializar uma variável mutável do tipo Alien
    var moved_alien: Alien
    // Inicializar uma variável mutável que vai ser uma lista vazia do tipo Alien
    var new_aliens: List<Alien> = emptyList()
    // Inicializar uma variável mutável do tipo Aliens que vai receber os aliens existentes e a sua direção
    var aliens: Aliens = this
    this.aliens.forEach { // Para cada alien existente na lista de aliens:
        // Criar um vetor que vai ser diferente consoante a direção atual do alien
        val offsetVector: OffsetVector = when (this.direction) {
            AliensDirection.RIGHT -> OffsetVector(game.difficulty.alienblock_horizontalshift, 0)
            AliensDirection.LEFT -> OffsetVector(-game.difficulty.alienblock_horizontalshift, 0)
            AliensDirection.DOWN_AFTERRIGHT -> OffsetVector(0, ALIEN_VERTICALSHIFT)
            AliensDirection.DOWN_AFTERLEFT -> OffsetVector(0, ALIEN_VERTICALSHIFT)
        }
        // Somar o vetor resultante com a posição atual de cada alien e guardar numa variável
        val newPosition = it.position + offsetVector
        // Criar um novo alien com a posição alterada
        moved_alien = it.copy(position = Position(newPosition.x, newPosition.y))
        // Guardar o alien criado para a lista instanciada anteriormente
        new_aliens += moved_alien
        // Guardar essa lista numa variável, bem como a direção recebida como parâmetro, permitindo assim,
        // se necessário, alterar a direção
        aliens = this.copy(aliens = new_aliens, direction = direction)
    }
    return aliens
}

// Função para determinar as coordenadas x do alien mais à esquerda e do alien mais à direita na janela do jogo
private fun findFirstAndLastAlienColumnWiseXPos(aliens: List<Alien>): Pair<Int, Int> {
    // Ao início do jogo o alien mais à esquerda é sempre o primeiro da lista
    var min: Int = aliens[0].position.x
    // Ao início do jogo o alien mais à direita é sempre o último da lista
    var max: Int = aliens[aliens.size - 1].position.x
    // Percorrer a lista enquanto o index for menor que o tamanho da lista
    for (i in 1 until aliens.size) {
        // Ao longo do ciclo, comparar os aliens da lista com o valor que vai sendo guardado na variável "min"
        if (aliens[i].position.x < min) {
            // Se for menor, guardar esse valor na variável
            min = aliens[i].position.x
        }
        if (aliens[i].position.x > max) {
            // Se for maior, guardar esse valor na variável
            max = aliens[i].position.x
        }
    }
    return Pair(min, max)
}

// Função cujo objetivo é retornar um valor booleano consoante o cálculo de uma probabilidade de sucesso.
// Esta percentagem de sucesso está relacionado com o aparecimento do Ovni na janela do jogo
fun alienOvniProb(): Boolean {
    val alien_ovniprob = (0..100).random()
    if (alien_ovniprob < 1) {
        return true
    } else {
        return false
    }
}

// Função para criar um Ovni consoante o cálculo da sua probabilidade de aparecimento e se já existe ou não um
// ovni na janela do jogo
fun Game.createAlienOvni(): Game {
    if (alienOvniProb() == true && this.ovni == null) {
        return this.copy(ovni = Alien(position = Position(0, 5), type = AlienType.ALIEN_OVNI))
    }
    return this
}

// Função para mover o Ovni na janela do jogo
fun Alien.moveAlienOvni(): Alien? {
    // Guardar numa variável a posição x do Ovni
    val x: Int = this.position.x
    // Se o Ovni ultrapassar a margem direita da janela, torná-lo null para permitir a criação de outro
    if (x > WIDTH) {
        return null
    }
    // Guardar numa variável a nova posição x do Ovni que vai ser resultante da soma da posição x atual com
    // uma constante previamente definida
    val new_x: Int = x + Ovni.HORIZONTALSHIFT.value
    return this.copy(position = (Position(new_x, this.position.y)))
}

// Função cujo objetivo é retornar as Sprite Coords de um determinado tipo de alien sempre que é chamada
fun getAlienSpriteCoords(spriteRectX: Int, spriteRectY: Int, spriteWidth: Int, spriteHeigth: Int): String =
    "$spriteRectX,$spriteRectY,$spriteWidth,$spriteHeigth"

// Função auxiliar para calcular o próximo passo de animação. Visto que os 3 tipos de aliens partilham duas colunas
// na Sprite da mesma dimensão, só é necessário usar um atributo dos 3 existentes, pois são iguais
fun getNextAlienAnimationStep(animationStep: Int): Int =
    (animationStep + 1) % AlienSpriteSheetLocation.ALIEN_SQUID.column

// Função para calcular as novas coordenadas que formaram um retângulo, este irá ser recortado da imagem Sprite,
// consoante o tipo recebido e o passo da animação a realizar
fun calculateNewSpriteCoords(animationStep: Int, alien_type: AlienType): String {
    val new_spriteRectX: Int = animationStep * AlienSpriteSheetDimension.ALIEN_SQUID.width
    val y: Int = when (alien_type) {
        AlienType.ALIEN_SQUID -> AlienStartPosOnSprite.ALIEN_SQUID.y
        AlienType.ALIEN_CRAB -> AlienStartPosOnSprite.ALIEN_CRAB.y
        AlienType.ALIEN_OCTOPUS -> AlienStartPosOnSprite.ALIEN_OCTOPUS.y
        AlienType.ALIEN_OVNI -> AlienStartPosOnSprite.ALIEN_OVNI.y
    }
    return "$new_spriteRectX,$y,${AlienSpriteSheetDimension.ALIEN_SQUID.width},${AlienSpriteSheetDimension.ALIEN_SQUID.height}"
}









