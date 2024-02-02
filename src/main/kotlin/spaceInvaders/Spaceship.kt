package spaceInvaders

// Constantes
const val SPACESHIPBASE_WIDTH: Int = 50
const val SPACESHIPBASE_HEIGHT: Int = 30
const val SPACESHIP_MOVE_HEIGHT: Int = 420

// Construtor
data class Spaceship(val position: Position, val basewidth: Int, val baseheight: Int)

// Classe de enumerados para definir o offset da SpaceShipHitBox consoante o tipo de alien. A sua utilização tem como
// objetivo ajudar a criar uma hitbox que englobe somente os retângulos que compõem a nave. Se houver alteração do
// tamanho da nave os valores instanciados nesta classe terão que ser modificados.
enum class SpaceShipHitBoxOffset(val x: Int, val y: Int, val width: Int, val height: Int) {
    // Dividir a nave em 4 retângulos, de cima para baixo, respetivamente.
    // Cada retângulo vai ser considerado como uma hitbox na posterior deteção de colisões
    RECT1(2, 0, 4, 5),
    RECT2(6, 4, 12, 7),
    RECT3(20, 12, 41, 6),
    RECT4(25, 15, 50, 15)
}





