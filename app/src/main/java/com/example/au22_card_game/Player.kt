package com.example.au22_card_game

class Player(
    var handOfCards: MutableList<PlayingCard> = mutableListOf(),
    var points : Int = 0,
    var frontCardIndex : Int = 0,
    var backCardIndex : Int = 0
) {
    fun pointUpdate() {
        //calculates the players current point total, and checks for aces
        points = 0
        var hasAce = false
        for (card in handOfCards) {
            points += card.value
            if (card.value == 11) {
                hasAce = true
            }
        }
        if(hasAce && points > 21) {
            points = 0
            for (card in handOfCards) {
                if (card.value == 11) {
                    card.value = 1
                    points += card.value
                }
                else {
                    points += card.value
                }
            }
        }
    }
}