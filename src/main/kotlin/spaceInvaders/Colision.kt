package spaceInvaders

import pt.isel.canvas.playSound

// Função para avaliar se o tiro da nave colidiu com um Alien
fun Alien.checkIfSpaceShipShotCollidedWithAlien(game: Game): Alien {
    if (game.spaceship_shot != null) { // Se o tiro existir:
        if (this.getHitBox().intersectsWith(game.spaceship_shot.getHitBox())) { // Se houver colisão:
            return this.copy(isHit = true)
        } else {
            return this
        }
    }
    return this
}

// Função para avaliar se algum tiro da nave colidiu com um Alien
fun Alien.checkIfSpaceShipShotsCollidedWithAlien(game: Game): Alien {
    game.spaceship_shots.forEach { spaceship_shot -> // Para cada tiro da nave existente no jogo:
        if (spaceship_shot.getHitBox().intersectsWith(this.getHitBox())) { // Se houver colisão:
            return this.copy(isHit = true)
        }
    }
    return this
}

// Função para avaliar se algum tiro da nave colidiu com um Alien
fun Shot.checkIfSpaceShipShotsCollidedWithAlien(game: Game): Shot {
    game.aliens_block.aliens.forEach { alien -> // Para cada alien existente no jogo:
        if (this.getHitBox().intersectsWith(alien.getHitBox())) { // Se houver colisão:
            return this.copy(isDamaged = true)
        }
    }
    return this
}

// Função para avaliar se algum tiro da nave colidiu com um tiro dos aliens
fun Shot.checkIfAlienShotCollidedSpaceShipShots(game: Game): Shot {
    game.spaceship_shots.forEach { spaceship_shot -> // Para cada tiro da nave existente no jogo:
        if (spaceship_shot.getHitBox().intersectsWith(this.getHitBox())) { // Se houver colisão:
            return this.copy(isDamaged = true)
        }
    }
    return this
}

// Função para avaliar se o tiro da nave colidiu com um tiro dos Aliens
fun Shot.checkIfAlienShotCollidedWithSpaceShipShot(game: Game): Shot {
    if (game.spaceship_shot != null) { // Se o tiro existir:
        if (this.getHitBox().intersectsWith(game.spaceship_shot.getHitBox())) { // Se houver colisão:
            return this.copy(isDamaged = true)
        } else {
            return this
        }
    }
    return this
}

// Função para avaliar se o tiro da nave colidiu com algum retângulo que compõe uma proteção
fun Rect.checkIfSpaceShipShotCollidedWithRect(game: Game): Rect {
    if (game.spaceship_shot != null) { // Se o tiro existir:
        if (this.getHitBox().intersectsWith(game.spaceship_shot.getHitBox())) { // Se houver colisão:
            return this.copy(isDamaged = true)
        } else {
            return this
        }
    }
    return this
}

// Função para avaliar se um tiro dos aliens atingiu algum retângulo de uma proteção
fun Rect.checkIfAlienShotCollidedWithRect(game: Game): Rect {
    game.alien_shots.forEach { alien_shot -> // Para cada tiro dos aliens existente no jogo:
        if (alien_shot.getHitBox().intersectsWith(this.getHitBox())) { // Se houver colisão:
            return this.copy(isDamaged = true)
        }
    }
    return this
}

// Função para avaliar se um tiro dos aliens atingiu algum retângulo de uma proteção
fun Shot.checkIfAlienShotCollidedWithRect(game: Game): Shot {
    game.difficulty.ship_protections.forEach { protection -> // Para cada proteção existente no jogo:
        protection.protection.forEach { rect -> // Para cada retângulo existente:
            if (this.getHitBox().intersectsWith(rect.getHitBox())) { // Se houver colisão:
                return this.copy(isDamaged = true)
            }
        }
    }
    return this
}

// Função para avaliar se algum alien colidiu com algum retângulo de uma proteção
fun Rect.checkIfAliensCollidedWithRect(game: Game): Rect {
    game.aliens_block.aliens.forEach { alien -> // Para cada alien existente no jogo:
        if (alien.getHitBox().intersectsWith(this.getHitBox())) { // Se houver colisão:
            return this.copy(isDamaged = true)
        }
    }
    return this
}


// Função para avaliar se os tiros dos aliens atingiram alguma das proteções da nave existentes
fun Game.checkIfAlienShotsCollidedWithProtections(): Game {
    // Inicializar uma variável mutável que é uma lista vazia do tipo Protection
    var new_protections: List<Protection> = emptyList()
    // Inicializar uma variável mutável que vai servir como flag
    var shot_hitprotection: Boolean = false
    this.difficulty.ship_protections.forEach { protection -> // Para cada proteção existente no jogo:
        // Inicializar uma variável mutável que vai receber uma lista do tipo Rect que representa uma proteção da nave
        var new_protection: List<Rect> = protection.protection
        new_protection = new_protection.mapNotNull { // Para cada retângulo existente na proteção:
            // Perceber se existe ou não colisão com o tiro dos aliens e guardar o resultado numa variável
            val rect = it.checkIfAlienShotCollidedWithRect(this)
            if (rect.isDamaged) { // Se foi atingido:
                // Ativar a flag
                shot_hitprotection = true
                // Eliminar o retângulo que sofreu colisão da proteção
                null
            } else {
                rect
            }
        }
        // Guardar na lista instanciada anteriormente a nova proteção (que poderá ou não ter sido danificada)
        new_protections += Protection(protection = new_protection)
    }
    val new_alienshots = this.alien_shots.mapNotNull { // Para cada tiro dos aliens existente:
        // Perceber se existe ou não colisão com o tiro dos aliens e guardar o resultado numa variável
        val new_alienshot = it.checkIfAlienShotCollidedWithRect(this)
        if (new_alienshot.isDamaged) { // Se foi danificado:
            // Ativar a flag
            shot_hitprotection = true
            // Eliminar o tiro dos aliens que sofreu colisão
            null
        } else {
            new_alienshot
        }
    }
    if (shot_hitprotection == true) { // Se houver colisão:
        // Guardar os dados do jogo alterados
        return this.copy(
            alien_shots = new_alienshots,
            difficulty = this.difficulty.copy(ship_protections = new_protections)
        )
    }
    return this
}

// Função para avaliar se o tiro da nave atingiu alguma das proteções da nave existentes
fun Game.checkIfSpaceShipShotCollidedWithProtections(): Game {
    // Inicializar uma variável mutável que é uma lista vazia do tipo Protection
    var new_protections: List<Protection> = emptyList()
    // Inicializar uma variável mutável que vai servir como flag
    var shot_hitprotection: Boolean = false
    this.difficulty.ship_protections.forEach { protection -> // Para cada proteção existente no jogo:
        // Inicializar uma variável mutável que vai receber uma lista do tipo Rect e representa uma proteção da nave
        var new_protection: List<Rect> = protection.protection
        new_protection = new_protection.mapNotNull { // Para cada retângulo existente na proteção:
            // Perceber se existe ou não colisão com o tiro da nave e guardar o resultado numa variável
            val rect = it.checkIfSpaceShipShotCollidedWithRect(this)
            if (rect.isDamaged) { // Se foi atingido:
                // Ativar a flag
                shot_hitprotection = true
                // Eliminar o retângulo que sofreu colisão da proteção
                null
            } else {
                rect
            }
        }
        // Guardar na lista instanciada anteriormente a nova proteção (que poderá ou não ter sido danificada)
        new_protections += Protection(protection = new_protection)
    }
    if (shot_hitprotection == true) { // Se houver colisão:
        // Eliminar o tiro da nave e guardar os dados do jogo alterados
        return this.copy(
            spaceship_shot = null,
            difficulty = this.difficulty.copy(ship_protections = new_protections)
        )
    } else {
        return this
    }
}

// Função para remover os aliens que sofrerão colisão com o tiro da nave
fun Game.removeAlienIfCollisionWasDetected(): Game {
    // Inicializar uma variável mutável que vai servir como flag
    var alien_damaged: Boolean = false
    // Criar uma variável mutável que vai receber os pontos resultantes do decorrer do jogo
    var new_points: Int = this.total_points
    // Criar uma variável que vai transformar a lista de aliens existente
    val new_aliens: List<Alien> = this.aliens_block.aliens.mapNotNull { // Para cada alien existente na lista:
        if (it.isHit) { // Se foi atingido:
            // Não fazia sentido na última dificuldade reproduzir o som quando o alien é eliminado, visto que
            // o mesmo não tem tempo de se reproduzir na totalidade devido à rapidez que é permitido os disparos
            // ao jogador
            if (this.difficulty.diff != GameDiff.INHUMAN) {
                playSound("invaderkilled")
            }
            // Calcular os novos pontos a acrescentar ao acumulador total de pontos
            new_points += it.calculatePointsIfAlienWasHit()
            // Ativar a flag
            alien_damaged = true
            // Eliminar o alien que sofreu colisão
            null
        } else {
            it
        }
    }
    val spaceship_shots: List<Shot> = this.spaceship_shots.mapNotNull { // Para cada alien existente na lista:
        if (it.isDamaged) { // Se foi atingido: )
            // Ativar a flag
            alien_damaged = true
            // Eliminar o alien que sofreu colisão
            null
        } else {
            it
        }
    }
    if (alien_damaged == true) { // Se houver colisão:
        // Atualizar o jogo
        return this.copy(
            aliens_block = this.aliens_block.copy(aliens = new_aliens), spaceship_shot = null,
            spaceship_shots = spaceship_shots, total_points = new_points
        )
    } else {
        return this.copy(aliens_block = this.aliens_block.copy(aliens = new_aliens))
    }
}

// Função para remover o Ovni se existir colisão com o tiro da nave
fun Game.removeOvniIfCollisionWasDetected(): Game {
    if (this.ovni != null) { // Se o ovni existir:
        if (this.ovni.isHit) { // Se foi atingido:
            playSound("invaderkilled")
            // Chamar a função auxiliar para calcular os novos pontos
            val new_points: Int = this.total_points + ovni.calculatePointsIfAlienWasHit()
            // Atualizar o jogo
            return this.copy(ovni = null, spaceship_shot = null, total_points = new_points)
        } else {
            return this
        }
    }
    return this
}

// Função para remover o tiro dos aliens, se este for atingido por um disparo da nave
fun Game.removeAlienShotIfCollisionWasDetected(): Game {
    val alien_shots: List<Shot> = this.alien_shots.mapNotNull {
        if (it.isDamaged) { // Se foi atingido:
            // Eliminar o tiro da nave que sofreu colisão
            null
        } else {
            it
        }
    }
    // Atualizar o jogo
    return this.copy(alien_shots = alien_shots)
}

// Função para avaliar se o tiro da nave colidiu com algum tiro dos aliens
fun Game.checkIfSpaceShipShotCollidedWithAlienShots(): Game {
    // Criar uma nova lista somente com os tiros dos aliens que existam
    val newaliens_shots = this.alien_shots.mapNotNull {
        // Perceber se existe ou não colisão com o tiro da nave e guardar o resultado numa variável
        val new_alienshot: Shot = it.checkIfAlienShotCollidedWithSpaceShipShot(this)
            .checkIfAlienShotCollidedSpaceShipShots(this)
        if (new_alienshot.isDamaged) { // Se existir colisão:
            // Eliminar o tiro do alien
            null
        } else {
            new_alienshot
        }
    }
    // Atualizar os dados do jogo
    return this.copy(alien_shots = newaliens_shots)
}

// Função para avaliar se algum Alien do bloco principal de Aliens atingiu alguma das proteções da nave
fun Game.checkIfAliensCollidedWithProtections(): Game {
    this.difficulty.ship_protections.forEach { protection -> // Para cada proteção existente no jogo:
        protection.protection.forEach { rect -> // Para cada retângulo existente na proteção:
            // Criar uma variável que vai receber o resultado da colisão entre os Aliens e o retângulo no iterador
            val new_rect = rect.checkIfAliensCollidedWithRect(this)
            if (new_rect.isDamaged) { // Se o retângulo tiver sofrido colisão:
                // Eliminar todas as proteções da nave, visto que a sua utilidade deixa de ser necessária
                return this.copy(difficulty = this.difficulty.copy(ship_protections = emptyList()))
            }
        }
    }
    return this
}

// Função para avaliar se os tiros dos aliens existentes no jogo colidiram com a nave
fun Game.checkIfAlienShotsCollidedWithSpaceShip(): Game {
    this.alien_shots.forEach { // Para cada tiro dos aliens existente:
        // Avaliar se existe colisão com a nave
        this.ship.getHitBox().forEach { it2 -> // Para cada hitbox da nave (que é representada por 4 retângulos):
            if (it.getHitBox().intersectsWith(it2)) {
                // Se existir colisão, acabar o jogo
                return this.copy(over = true)
            }
        }
    }
    return this
}