# Space Invaders

| ![Space Invaders](/src/main/resources/invaders.png) |
|:---------------------------------------------------:|
|                  *Space Invaders*                   |

> The purpose of this project was to develop a game in Kotlin using
> the [CanvasLib](https://github.com/palex65/CanvasLib) library, which supports canvas-based graphics programming.

> The project was developed in the context of the *Programming* course of
> the [CSE](https://www.isel.pt/en/curso/bsc-degree/computer-science-and-computer-engineering) undergraduate degree
> of [ISEL](https://www.isel.pt/en), which took place in the first semester of the 2021/2022 academic year.

> The code contains several non-game related issues mentioned in the [critical analysis](#critical-analysis) and should
> not be used as a reference. It was
> solely published with the intent of documenting my progress as a software developer student, highlighting my mistakes
> during its development.

## Table of Contents

- [Introduction](#introduction)
- [Download](#download)
- [Requirements](#requirements)
- [Features](#features)
- [How to Play](#how-to-play)
- [Application Design](#application-design)
    - [Points](#points)
    - [Difficulties](#difficulties)
    - [Animations](#animations)
    - [Hitboxes](#hitboxes)
    - [Spaceship](#spaceship)
    - [Aliens](#aliens)
    - [Bunkers](#bunkers)
    - [Sounds](#sounds)
- [Critical Analysis](#critical-analysis)

## Download

Download the JAR file [here](https://mega.nz/file/ETY00Rjb#xKRBa4X7eSalATP-KGgzViVEgEKPMHrmXPU12Yt2MzU)

## Requirements

- JRE 1.4.0 or above (for running the application);
- JDK 17 or above (for development purposes).

## Features

Besides what is present in the original game, this version includes the following features:

- [x] Four difficulty levels: `Rogue`, `Classic`, `Master`, and `InHuman`. Each difficulty has its own
  unique [settings](#difficulties) such as spaceship lives, projectiles, alien speed, and more;
- [x] Custom points awarded for destroying different alien types, points are deducted when a life is lost;
- [x] Unique sprite animations;
- [x] Various sounds for spaceship projectiles, alien deaths, movement, and more.

## Introduction

[Space Invaders](https://en.wikipedia.org/wiki/Space_Invaders)
is a legendary arcade game created by Tomohiro Nishikado in 1978. Regarded as one of the most influential and iconic
video games of all time, it revolutionized the gaming industry and paved the way for the future of
interactive entertainment.

In this game, players control a spaceship, at the bottom of the screen, tasked with defending Earth from waves of
descending alien invaders. Armed with a laser cannon, players must skillfully shoot down the relentless extraterrestrial
enemies while dodging their retaliatory fire.
Players could also take cover behind destructible bunkers for protection from enemy fire.

The objective is to score as many points as possible by shooting down waves of invading aliens while avoiding
their
projectiles.
The game is progressively increased in difficulty as the alien formations descended closer to the player's
spaceship.
The goal is to survive for as long as possible and achieve the highest score.

## How to Play

In the main menu, the player can choose between four different game difficulties, which are
differentiated by the speed of the aliens, number of lives and the speed of their projectiles,
as explained in later detail over in [this](#difficulties) section.

| ![Space Invaders](/src/main/resources/menu.png) |
|:-----------------------------------------------:|
|             *Difficulty Selection*              |

To move the spaceship, the player must move the mouse cursor horizontally within the game window. The spaceship can only
move horizontally and is always at the bottom of the screen.

Firing the cannon from this spaceship can be done with the **mouse right-click** or the **spacebar**.
The player is only allowed to fire if the previous projectile has already disappeared from the screen.
Although, it can depend on the difficulty chosen, as some enable the player to fire multiple projectiles at once.

The alien and the spaceship projectiles have a **destructive effect** on the **bunkers**,
which are placed at the bottom of the screen.
So, the player must be careful not to destroy the bunkers, as they provide protection from enemy fire.

A spaceship projectile can destroy an enemy projectile, which can be useful in some situations.

The player is **awarded points** for each alien destroyed, depending on its type.

The **current score**, **difficulty** and **current number of lives** are displayed at the bottom of the screen.

The game gets **progressively harder** depending on the number of aliens still alive,
since the fewer aliens there are, the faster they move towards the player's spaceship.

Whenever a life is lost and the player still has lives left:

- all projectiles disappear from the screen in order to give the player a chance to recover and reposition properly;
- if a UFO (**red** bonus alien) is present on the screen, it will also disappear.
- the points lost on a life loss are incremented in order to further penalize the player for allowing
  the spaceship to take damage.

In the course of the game, if the player does not eliminate the aliens from the screen,
or if it loses the lives it has, depending on the difficulty chosen,
the game ends.
In such an event, the player is presented with the results' screen, which includes:

- the **final score**;
- the **lives lost**;
- the **time elapsed**;
- the **difficulty chosen**.

The mentioned screen can be seen in the following image:

| ![Game Over Screen](/src/main/resources/game-over-screen.png) |
|:-------------------------------------------------------------:|
|                      *Game Over Screen*                       |

## Application Design

### Points

There are **55** aliens initially in the central block:

- 2 lines at the top with 11 aliens each of the type **SQUID**
  (in *yellow*);
- 2 lines in the middle with 11 aliens each of the type **CRAB**
  (in *light blue*);
- 1 line at the bottom with 11 aliens of the type **OCTOPUS** (in *light green*).

There is also a **UFO** (in *red*) type, which appears randomly at the top of the screen throughout the game.

Each type of alien has a different score value, as shown in the following table:

| Alien Type | Points |
|:----------:|:------:|
|    UFO     |  100   |
|    CRAB    |   20   |
|  OCTOPUS   |   10   |
|   SQUID    |   30   |

When the player loses a life, it also loses some of the accumulated points.
The number of the points lost is dependent on the number of lives the player has left.
If the player has 3 lives:

- **1st life lost** - loses 100 points;
- **2nd life lost** - loses 150 points;
- **3rd life lost** - loses 0 points, since it is the last life.

Each time the spaceship projectile passes the top of the screen, the player is awarded **1 point**. If the player loses
the game with negative points, the score is set to 0.

### Difficulties

There are four **different game difficulties** available, which are:

- **Rogue**;
- **Classic**;
- **Master**;
- **InHuman**.

The differences between them are the following:

|          Characteristic           | Rogue  | Classic | Master | InHuman  |
|:---------------------------------:|:------:|:-------:|:------:|:--------:|
|        **Spaceship Lives**        |   3    |    3    |   2    |    1     |
|     **Spaceship Projectiles**     | Single | Single  | Single | Multiple |
| **Alien's Shooting Speed Range**  |  1-4   |   3-6   |  5-8   |   7-10   |
|   **Alien's Horizontal Speed**    |   4    |    4    |   7    |    7     |
| **Probability of Alien fire (%)** |   50   |   60    |   75   |    90    |
|       **Bunkers Presence**        |  Yes   |   Yes   |   No   |    No    |
|      **Infinite Game Cycle**      |   No   |   Yes   |  Yes   |   Yes    |

### Animations

To enhance the game's visual appeal and create a more dynamic experience, animations were implemented. These animations
utilize a [sprite sheet](https://gamedevelopment.tutsplus.com/an-introduction-to-spritesheet-animation--gamedev-13099t),
which is a composite image containing multiple frames. By displaying the frames sequentially
with a specific delay, the illusion of movement is created.

Each frame in the sprite sheet represents an animation step, and it advances as the animation updates. Multiple
animation steps can be defined for a single image, allowing for different animations to be displayed using the same
image, as is the case for the aliens.

When the desired animations for an image are completed, the animation step is **resetted to 0**. For instance, if the
sprite sheet has two columns representing different animations for a **SQUID type**, with the first animation step being
0 and the second being 1, the third animation step will go back to 0. This cycling behavior is achieved by using the
**modulus operator**, which wraps the animation step based on the number of columns in the sprite sheet.

The following image shows the sprite
sheet used for the aliens' animation with the respective dimensions of each frame.

|      ![Aliens Sprite Sheet](/src/main/resources/invaders-sprite.png)      |
|:-------------------------------------------------------------------------:|
|                           *Aliens Sprite Sheet*                           |

### Hitboxes

In order to **detect collisions** between the different entities of the game, hitboxes were created.
A **hitbox** is a defined **area that surrounds an entity**, which is used to determine if it has collided with another
entity.

| ![Game Hitboxes](/src/main/resources/hitboxes.png) |
|:--------------------------------------------------:|
|                  *Game Hitboxes*                   |

Note that the bunker hitboxes are not visile in the previous image because of their rectangular shape
that perfectly matches the several components that make up the bunker, as described in [this](#bunkers) section.

### Spaceship

The spaceship's hitbox **is composed of 4 rectangles**, which are used to detect collisions with the aliens'
projectiles.

Below, on the left, is the image of the spaceship used in the game, and on the right, the hitbox created for it.

| ![Spaceship Hitbox](/src/main/resources/spaceship-hitbox.png) | 
|:-------------------------------------------------------------:|
|                      *Spaceship Hitbox*                       |

To create the rectangles, it was necessary to know the x and y coordinates of the upper left corner of each one,
as well as the width and length.
The coordinates of the ship are represented in the previous image by the intersection of two straight lines that were
used
to find them.

| ![Spaceship Offsets](/src/main/resources/spaceship-offsets.png) |
|:---------------------------------------------------------------:|
|                       *Spaceship Offsets*                       |

### Aliens

When drawing the aliens in the game via the sprite sheet,
it was necessary to shorten the image by half (length and width),
so the offsets of the alien's hitboxes would directly depend on the type and size of the cropped image.

Since each alien represents an object of type Alien,
it is possible to access the coordinates of the upper left corner of each one, in order to add the necessary offset.
This way,
each alien type will have an hitbox that is circumscribed to its image.
As such, during the course of the game, it is easier to hit the aliens that are closer to the player because they have a
larger hitbox than the ones that are further away.

The UFO represents a specific case,
since it does not move at the same pace as the central alien block,
and therefore has a larger hitbox to compensate for the speed at which it moves.

```kotlin
data class Alien(val position: Position, val type: AlienType, val isHit: Boolean = false) 
```

### Bunkers

Each bunker is a set of rectangles with 4 rows and 11 columns.
Each rectangle was drawn with the same width as the shots (11 pixels) and twice as long as the shots (8 pixels).
With this information,
the ideal distances from the first bunker (leftmost) to the left edge of the game were calculated and found to be 45 pixels,
so the last bunker (rightmost) would be the same length from the right edge of the game.
In order for there to be 4 properly spaced bunkers, the size of each and the distance between them were calculated using the following equation:

```math
(700 - (88 x 4) - (45 x 2)) / 3 = x <=> x = 86
```

Where:
- 700 corresponds to the length of the game window; 
- 88 corresponds to 11 rectangles with a length of 8;
- 4 is the total number of bunkers;
- 3 corresponds to the spaces to be calculated.

The following imagem shows the bunker's constitution, which was then used to create the hitboxes.

| ![Bunkers Hitboxes](/src/main/resources/bunker-constitution.png) |
|:----------------------------------------------------------------:|
|                        *Bunkers Hitboxes*                        |
    
### Sounds

The [CanvasLib](https://github.com/palex65/CanvasLib) library provides functions for playing and loading sounds. These
sounds are used to enhance the
player's perception of the game state, making it more immersive and enjoyable.

The sounds used in this game include:

- **Difficulty selection** - played when the player selects a difficulty;
- **Spaceship projectile** - played when the spaceship fires a projectile;
- **Alien death** - played when an alien is destroyed;
- **Alien block movement** - played when the alien block moves across the screen;
- **UFO movement** - played when the UFO moves across the screen;
- **Spaceship damage** - played when the spaceship is hit by an alien projectile;
- **Game Win** - played when the player wins the game;
- **Game Over** - played when the player loses the game.

## Critical Analysis

Upon a light review of the project, I have identified several noteworthy flaws that severely impact its maintainability
and scalability. The most relevant ones are:

- The game is **not optimized** as it **prioritizes immutability over performance**, resulting in the creation and
  destruction
  of multiple copies of the same object every frame.
- The game **lacks scalability** in terms of window size as it is hardcoded to a specific resolution.
- There are **numerous occurrences of magic numbers** and **hardcoded values** throughout the code, making it
  challenging to
  modify the game's behavior and appearance.
- The project **lacks organization and a clear structure**. Most files, if not all, are **unnecessarily large and
  complex**, and
  they assume responsibilities that should be clearly defined and separated.
- The code contains **excessive comments**, which hinders readability.
  The comments are also **inconsistent** and written in a language that is not universally understood.

It's worth mentioning that the project was developed in a short period of time, and it was not intended to be
maintained or scaled, as the main goal was to learn and practice the Kotlin programming language as a way to introduce
programming concepts to students.
Nevertheless, such flaws should be addressed in a future version of the game if one is ever developed.

## Authors

Francisco Engenheiro - 49428

--- 

Instituto Superior de Engenharia de Lisboa<br>
Programming<br>
Winter Semester of 2021/2022