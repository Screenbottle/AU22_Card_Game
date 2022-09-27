package com.example.au22_card_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.au22_card_game.GameState.dealerHand

class RoundActivity : AppCompatActivity() {

    private lateinit var cardDeck : MutableList<PlayingCard>
    private val playerHand = Player()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round)

        val playerCount = 1

        val handList = arrayOf(playerHand, dealerHand)

        makeDeck()

        for (i in 1..2) {
            for (j in 0..playerCount) {
                val drawnCard = drawCard()
                handList[j].handOfCards.add(drawnCard)
            }
        }

        val hitButton = findViewById<Button>(R.id.hitButton)

        hitButton.setOnClickListener {

            playerHand.handOfCards.add(drawCard())
            playerPointUpdate(playerHand)

            setCardView()

            if (playerHand.points > 21) {
                //todo
                //lose round
            }
            else if (playerHand.points == 21) {
                //go to dealers play
                dealerPlay()
            }
        }

        val standButton = findViewById<Button>(R.id.standButton)

        standButton.setOnClickListener {
            dealerPlay()
        }

        val dealerButton = findViewById<Button>(R.id.dealerButton)

        dealerButton.setOnClickListener {
            val intent = Intent(this, DealerHandActivity::class.java)
            intent.putExtra("status", "show")

            startActivity(intent)
        }

        val leftButton = findViewById<ImageButton>(R.id.cardLeftButton)

        leftButton.setOnClickListener {
            setCardView("left")
        }

        val rightButton = findViewById<ImageButton>(R.id.cardRightButton)

        rightButton.setOnClickListener {
            setCardView(("right"))
        }

        playerPointUpdate(playerHand)
        playerPointUpdate(dealerHand)


        setCardView()

        if (playerHand.points == 21 && dealerHand.points != 21) {
            //todo
            //player wins
        }
        else if (playerHand.points == 21 && dealerHand.points == 21) {
            //todo
            //stand off (tie)
        }
        else if (dealerHand.points == 21) {
            //todo
            //house wins
        }
    }

    private fun drawCard() : PlayingCard {
        //draws a random card and removes it from the deck
        val randomCard = (0 until cardDeck.size).random()
        return cardDeck.removeAt(randomCard)
    }

    private fun dealerPlay() {

        while (dealerHand.points < 17) {
            dealerHand.handOfCards.add(drawCard())
            playerPointUpdate(dealerHand)
        }

        if (dealerHand.points <= 21 && dealerHand.points < playerHand.points && playerHand.points <= 21) {
            //todo
            //player wins
        }
        else if (dealerHand.points <= 21 && dealerHand.points == playerHand.points) {
            //todo
            //stand off (tie)
        }
        else if (dealerHand.points <= 21 && (playerHand.points < dealerHand.points || playerHand.points > 21)) {
            //todo
            //house wins
        }

    }

    private fun playerPointUpdate(player : Player) {
        //calculates the players current point total, and checks for aces
        player.points = 0
        var hasAce = false
        for (card in player.handOfCards) {
            player.points += card.value
            if (card.value == 11) {
                hasAce = true
            }
        }
        if(hasAce && player.points > 21) {
            player.points = 0
            for (card in player.handOfCards) {
                if (card.value == 11) {
                    card.value = 1
                    player.points += card.value
                }
                else {
                    player.points += card.value
                }
            }
        }
    }

    private fun setCardView(direction : String? = null) {
        //one function for all changes in card views

        val cardViewBack = findViewById<ImageView>(R.id.cardViewBack)
        val cardViewFront = findViewById<ImageView>(R.id.cardViewFront)

        when (direction) {
            "left" -> {
                //moves "left" through the players hand,
                // if the current cards have the index of 2 in the front and 1 in the back,
                // it changes it to 1 in the front and 0 in the back
                playerHand.frontCardIndex = playerHand.backCardIndex
                if (playerHand.backCardIndex - 1 < 0) {
                    playerHand.backCardIndex = playerHand.handOfCards.lastIndex
                }
                else {
                    playerHand.backCardIndex = playerHand.backCardIndex - 1
                }

                cardViewFront.setImageResource(playerHand.handOfCards[playerHand.frontCardIndex].imageID)
                cardViewBack.setImageResource(playerHand.handOfCards[playerHand.backCardIndex].imageID)

            }
            "right" -> {
                //moves "right" through the players hand,
                // if the current cards have the index of 1 in the front and 2 in the back,
                // it changes it to 2 in the front and 0 in the back
                playerHand.backCardIndex = playerHand.frontCardIndex
                if (playerHand.frontCardIndex + 1 >= playerHand.handOfCards.size) {
                    playerHand.frontCardIndex = 0
                }
                else {
                    playerHand.frontCardIndex = playerHand.frontCardIndex + 1
                }

                cardViewFront.setImageResource(playerHand.handOfCards[playerHand.frontCardIndex].imageID)
                cardViewBack.setImageResource(playerHand.handOfCards[playerHand.backCardIndex].imageID)
            }
            else -> {
                //defaults to showing the last card in the players hand in the front, and the card before that in the back

                playerHand.frontCardIndex = playerHand.handOfCards.lastIndex
                playerHand.backCardIndex = playerHand.handOfCards.lastIndex - 1

                cardViewFront.setImageResource(playerHand.handOfCards[playerHand.frontCardIndex].imageID)
                cardViewBack.setImageResource(playerHand.handOfCards[playerHand.backCardIndex].imageID)

                val pointView = findViewById<TextView>(R.id.pointView)

                pointView.text = getString(R.string.points, playerHand.points)

            }
        }
    }

    private fun makeDeck() {
        // generates a new deck of cards and links each card object to the corresponding image
        cardDeck = mutableListOf()
        val suiteList = listOf("hearts", "clubs", "diamond", "spades")
        val courtList = listOf("a", "j", "q", "k")
        val courtValue = listOf(11, 10)

        var drawableId : Int
        for (i in 0..3) {
            for (j in 0..12) {
                if (j == 0) {
                    drawableId = this.resources.getIdentifier("${suiteList[i]}_${courtList[0]}", "drawable", this.packageName)
                    val card = PlayingCard(drawableId, "${suiteList[i]}_${courtList[j]}",  courtValue[0], suiteList[i])
                    cardDeck.add(card)
                }
                else if (j > 9){
                    drawableId = this.resources.getIdentifier("${suiteList[i]}_${courtList[j-9]}", "drawable", this.packageName)
                    val card = PlayingCard(drawableId, "${suiteList[i]}_${courtList[j-9]}",  courtValue[1], suiteList[i])
                    cardDeck.add(card)
                }
                else {
                    drawableId = this.resources.getIdentifier("${suiteList[i]}_${j+1}", "drawable", this.packageName)
                    val card = PlayingCard(drawableId, "${suiteList[i]}_${j+1}",  j+1, suiteList[i])
                    cardDeck.add(card)
                }
            }
        }
    }

}