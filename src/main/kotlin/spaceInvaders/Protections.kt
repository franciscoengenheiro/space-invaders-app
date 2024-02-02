package spaceInvaders

// Construtores
data class Rect(val position: Position, val width: Int, val height: Int, val isDamaged: Boolean = false)
data class Protection(val protection: List<Rect>)

// Classe de enumerados que instância o canto superior esquerdo de cada proteção criada na janela do jogo, e que foi
// calculada conforme a quantidade de proteções e o número de retângulos que cada proteção tem por linha.
// Se for necessário alterar a quantidade de proteções, os espaços entre elas ou o número de retângulos dentro de cada
// uma, é necessário calcular novamente estas posições
enum class ProtectionStartPos(val x: Int, val y: Int) {
    BLOCK1(45, 350),
    BLOCK2(219, 361),
    BLOCK3(393, 372),
    BLOCK4(567, 383)
}

// Função auxiliar para a criação dos retângulos por linha, que compõem a proteção da nave
fun createRectRow(protection: List<Rect>, inicial_xpos: Int, inicial_ypos: Int): List<Rect> {
    // Guardar numa nova variável a lista de retângulos existente
    var existing_rectlist: List<Rect> = protection
    // Instanciar uma variável mutável do tipo Rect
    var new_Rect: Rect
    // Instanciar uma variável mutável que vai receber a posição x inicial
    var new_x: Int = inicial_xpos
    // Instanciar uma variável mutável que vai receber a posição y inicial
    val new_y: Int = inicial_ypos
    for (column in 1..11) { // Para cada coluna:
        // Guardar na variável mutável as alterações na posição x
        new_Rect = Rect(
            position = Position(new_x, new_y), width = ShotAttributes.WIDTH.value * 2,
            height = ShotAttributes.HEIGHT.value
        )
        // Guardar o novo retângulo na lista
        existing_rectlist += new_Rect
        // Calcular a nova posição x para criar o retângulo seguinte na linha
        new_x += (ShotAttributes.WIDTH.value * 2)
    }
    // Devolver a lista que vai sendo alterada
    return existing_rectlist
}

// Função para criar os retângulos que compõem cada proteção da nave
fun createProtection(inicial_xpos: Int): Protection {
    // Inicializar uma variável mutável que é inicialmente uma lista vazia do tipo Rect
    var rect_list: List<Rect> = emptyList()
    // Primeira linha de retângulos que compõem a proteção
    rect_list = createRectRow(rect_list, inicial_xpos, inicial_ypos = ProtectionStartPos.BLOCK1.y)
    // Segunda linha de retângulos que compõem a proteção
    rect_list = createRectRow(rect_list, inicial_xpos, inicial_ypos = ProtectionStartPos.BLOCK2.y)
    // Terceira linha de retângulos que compõem a proteção
    rect_list = createRectRow(rect_list, inicial_xpos, inicial_ypos = ProtectionStartPos.BLOCK3.y)
    // Quarta linha de retângulos que compõem a proteção
    rect_list = createRectRow(rect_list, inicial_xpos, inicial_ypos = ProtectionStartPos.BLOCK4.y)
    // Guardar as linhas de retângulos criadas num objeto do tipo Protection
    return Protection(protection = rect_list)
}

// Função que cria as várias proteções da nave
fun createProtections(): List<Protection> {
    // Inicializar uma variável mutável que é inicialmente uma lista vazia do tipo Protection
    var new_protections: List<Protection> = emptyList()
    // Primeira Proteção (a contar da esquerda)
    new_protections += createProtection(inicial_xpos = ProtectionStartPos.BLOCK1.x)
    // Segunda Proteção (a contar da esquerda)
    new_protections += createProtection(inicial_xpos = ProtectionStartPos.BLOCK2.x)
    // Terceira Proteção (a contar da esquerda)
    new_protections += createProtection(inicial_xpos = ProtectionStartPos.BLOCK3.x)
    // Quarta Proteção (a contar da esquerda)
    new_protections += createProtection(inicial_xpos = ProtectionStartPos.BLOCK4.x)
    // Devolver essa lista, que vai representar todas as proteções da nave existentes no jogo
    return new_protections
}
