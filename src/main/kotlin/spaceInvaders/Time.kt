package spaceInvaders

// Construtor
data class Time(val min: Int, val sec: Int)

// Função para contar o tempo decorrido no jogo em segundos
fun countInGameElapsedTime(sec: Int): Int = sec + 1

// Função para calcular o tempo decorrido no jogo em minutos e segundos, usando por isso um objeto do tipo Time
fun Game.calculateResultTimeOnSecondPeriod(sec: Int): Game {
    // Criar uma variável mutável que vai representar os minutos
    var min: Int = 0
    // Criar uma variável mutável que vai receber os segundos que passaram durante o decorrer do jogo
    var new_sec: Int = sec
    while (new_sec > 59) { // Se os segundos ultrapassarem 60:
        // Incrementar o contador dos minutos como 1 minuto
        min++
        // Retirar no total dos segundos os 60 segundos convertidos num minuto anteriormente
        new_sec -= 60
    }
    return this.copy(elapsed_time = Time(min = min, sec = new_sec))
}
