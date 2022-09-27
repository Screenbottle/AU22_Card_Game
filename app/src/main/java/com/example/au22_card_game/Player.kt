package com.example.au22_card_game

class Player(
    var handOfCards: MutableList<PlayingCard> = mutableListOf(),
    var points : Int = 0,
    var frontCardIndex : Int = 0,
    var backCardIndex : Int = 0
) {

}