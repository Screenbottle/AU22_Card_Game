package com.example.au22_card_game

object GameState {
    var playerChips : Int = 5000
    var playerBet : Int = 0
    val dealerHand = Player()
    lateinit var roundStatus : String

    fun playerWin() {
        roundStatus = "WIN"
    }

    fun playerTie() {
        roundStatus = "TIE"
    }

    fun playerLoss() {
        roundStatus = "LOSS"
    }
}