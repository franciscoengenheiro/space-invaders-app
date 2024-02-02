package spaceInvaders

// Construtor
data class HitBox(val corner: Position, val width: Int, val height: Int)

// Função de extensão de Shot para construir uma HitBox necessária para a posterior deteção de colisões
fun Shot.getHitBox(): HitBox = HitBox(
    corner = Position(
        this.position.x - ShotAttributes.WIDTH.value / 2,
        this.position.y - ShotAttributes.HEIGHT.value / 2
    ),
    width = ShotAttributes.WIDTH.value, height = ShotAttributes.HEIGHT.value
)

// Função de extensão de Spaceship para construir uma HitBox necessária para a posterior deteção de colisões
fun Spaceship.getHitBox(): List<HitBox> {
    return listOf(
        // A construção da hitbox da nave teve por base a criação de hitboxes individuais para cada retângulo que
        // compõe a estrutura da nave de cima para baixo, respetivamente
        HitBox(
            corner = Position(
                this.position.x - SpaceShipHitBoxOffset.RECT1.x,
                this.position.y + SpaceShipHitBoxOffset.RECT1.y
            ), width = SpaceShipHitBoxOffset.RECT1.width,
            height = SpaceShipHitBoxOffset.RECT1.height
        ),
        HitBox(
            corner = Position(
                this.position.x - SpaceShipHitBoxOffset.RECT2.x,
                this.position.y + SpaceShipHitBoxOffset.RECT2.y
            ), width = SpaceShipHitBoxOffset.RECT2.width,
            height = SpaceShipHitBoxOffset.RECT2.height
        ),
        HitBox(
            corner = Position(
                this.position.x - SpaceShipHitBoxOffset.RECT3.x,
                this.position.y + SpaceShipHitBoxOffset.RECT3.y
            ), width = SpaceShipHitBoxOffset.RECT3.width,
            height = SpaceShipHitBoxOffset.RECT3.height
        ),
        HitBox(
            corner = Position(
                this.position.x - SpaceShipHitBoxOffset.RECT4.x,
                this.position.y + SpaceShipHitBoxOffset.RECT4.y
            ), width = SpaceShipHitBoxOffset.RECT4.width,
            height = SpaceShipHitBoxOffset.RECT4.height
        ),
    )
}

// Função de extensão de Alien para construir uma HitBox necessária para a posterior deteção de colisões
fun Alien.getHitBox(): HitBox {
    // Retornar uma hitbox que, às coordenadas da imagem onde se encontra o alien acrescenta um offset de modo
    // a que a hitbox criada respeite a dimensão do alien consoante o seu tipo, criando assim a sensação
    // que é mais fácil acertar nos aliens que estão mais perto da nave porque têm uma hitbox maior do que os
    // aliens mais distantes da nave, pois estes apresentam um hitbox menor
    return when (this.type) {
        // Se for do tipo ALIEN_SQUID:
        AlienType.ALIEN_SQUID -> HitBox(
            corner = Position(
                this.position.x + AlienHitBoxOffset.ALIEN_SQUID.x,
                this.position.y + AlienHitBoxOffset.ALIEN_SQUID.y
            ), width = AlienHitBoxOffset.ALIEN_SQUID.width,
            height = AlienHitBoxOffset.ALIEN_SQUID.height
        )
        // Se for do tipo ALIEN_CRAB:
        AlienType.ALIEN_CRAB -> HitBox(
            corner = Position(
                this.position.x + AlienHitBoxOffset.ALIEN_CRAB.x,
                this.position.y + AlienHitBoxOffset.ALIEN_CRAB.y
            ), width = AlienHitBoxOffset.ALIEN_CRAB.width,
            height = AlienHitBoxOffset.ALIEN_CRAB.height
        )
        // Se for do tipo ALIEN_OCTOPUS:
        AlienType.ALIEN_OCTOPUS -> HitBox(
            corner = Position(
                this.position.x + AlienHitBoxOffset.ALIEN_OCTOPUS.x,
                this.position.y + AlienHitBoxOffset.ALIEN_OCTOPUS.y
            ), width = AlienHitBoxOffset.ALIEN_OCTOPUS.width,
            height = AlienHitBoxOffset.ALIEN_OCTOPUS.height
        )
        // Se for do tipo ALIEN_OVNI:
        AlienType.ALIEN_OVNI -> HitBox(
            corner = Position(
                this.position.x + AlienHitBoxOffset.ALIEN_OVNI.x,
                this.position.y + AlienHitBoxOffset.ALIEN_OVNI.y
            ), width = AlienHitBoxOffset.ALIEN_OVNI.width,
            height = AlienHitBoxOffset.ALIEN_OVNI.height
        )
    }
}

// Função de extensão de Rect para construir uma HitBox necessária para a posterior deteção de colisões
fun Rect.getHitBox(): HitBox = HitBox(
    corner = Position(this.position.x, this.position.y),
    width = this.width, height = this.height
)

// Função de extensão de HitBox que recebe outra HitBox e que retorna um valor booleano consoante o
// resultado da colisão entre as duas
fun HitBox?.intersectsWith(hitbox: HitBox?): Boolean {
    this?.let { // Se a primeira hitbox do tipo HitBox recebida existir:
        hitbox?.let { // Se a segunda hitbox do tipo HitBox recebida existir:
            val (x1, y1) = this.corner
            val (x2, y2) = it.corner
            if ((x1 + this.width) >= x2 && (x1 <= x2 + it.width) && (y1 + this.height >= y2) &&
                (y1 <= y2 + it.height)
            ) { // Se ambas existirem e chocarem:
                return true
            }
            // Se apenas uma das hitboxes existir, ou se as duas existirem e não chocarem:
            return false
        }
        // Se apenas uma das hitboxes existir:
        return false
    }
    // Se nenhuma das hitboxes existir:
    return false
}
