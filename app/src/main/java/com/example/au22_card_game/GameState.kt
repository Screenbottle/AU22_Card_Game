package com.example.au22_card_game

object GameState {
    var playerChips : Double = 5000.0
    var playerBet : Double = 0.0
    var playerBetReturn: Double = 0.0
    val dealerHand = Player()
    var roundStatus : String? = null

    fun playerWin() {
        roundStatus = "WIN"
    }

    fun playerTie() {
        roundStatus = "TIE"
    }

    fun playerLoss() {
        roundStatus = "LOSS"
    }

    fun roundOver() {
        roundStatus = "OVER"
    }

    fun playerChipUpdate(reward: Double) {
        playerChips += reward
    }

    fun newRound() {
        dealerHand.handOfCards.clear()
        roundStatus = null
    }
}