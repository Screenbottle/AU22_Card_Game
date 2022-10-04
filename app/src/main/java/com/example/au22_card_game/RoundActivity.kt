package com.example.au22_card_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class RoundActivity : AppCompatActivity() {

    private lateinit var cardDeck : MutableList<PlayingCard>
    private val playerHand = Player()
    private lateinit var hitButton: Button
    private lateinit var standButton: Button
    private lateinit var dealerButton: Button
    private lateinit var cardViewBack: ImageView
    private lateinit var cardViewFront: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round)

        val playerCount = 1

        val handList = arrayOf(playerHand, GameState.dealerHand)

        makeDeck()
        GameState.newRound()

        for (i in 1..2) {
            for (j in 0..playerCount) {
                val drawnCard = drawCard()
                handList[j].handOfCards.add(drawnCard)
            }
        }

        cardViewBack = findViewById(R.id.cardViewBack)
        cardViewFront = findViewById(R.id.cardViewFront)

        hitButton = findViewById(R.id.hitButton)
        standButton = findViewById(R.id.standButton)
        dealerButton = findViewById(R.id.dealerButton)

        hitButton.setOnClickListener {

            playerHand.handOfCards.add(drawCard())
            playerHand.pointUpdate()

            setCardView()

            if (playerHand.points > 21) {
                GameState.playerLoss()
                dealerPlay()
                roundOver()
                dealerButton.setText(R.string.roundResultButton)
            }
            else if (playerHand.points == 21) {
                //go to dealers play
                dealerPlay()
                roundOver()
            }
        }

        standButton.setOnClickListener {
            roundOver()
            dealerPlay()
        }

        dealerButton.setOnClickListener {
            val intent = Intent(this, DealerHandActivity::class.java)
            startActivity(intent)
        }

        val leftButton = findViewById<ImageButton>(R.id.cardLeftButton)

        leftButton.setOnClickListener {
            setCardView("left")
        }

        val rightButton = findViewById<ImageButton>(R.id.cardRightButton)

        rightButton.setOnClickListener {
            setCardView("right")
        }

        playerHand.pointUpdate()
        GameState.dealerHand.pointUpdate()


        setCardView()
        //checks if either the player and/or the house has a Blackjack
        //if neither does, it proceeds as normal
        if (playerHand.points == 21 && GameState.dealerHand.points != 21) {
            GameState.playerWin()
            GameState.playerBetReturn = 1.5
            roundOver()
        }
        else if (playerHand.points == 21 && GameState.dealerHand.points == 21) {
            GameState.playerTie()
            roundOver()
        }
        else if (GameState.dealerHand.points == 21) {
            GameState.playerLoss()
            roundOver()
        }
        else {
            dealerButton.setOnClickListener {
                val intent = Intent(this, DealerHandActivity::class.java)
                intent.putExtra(ROUND_STATUS_KEY, false)

                startActivity(intent)
            }
        }
    }

    private fun drawCard() : PlayingCard {
        //draws a random card and removes it from the deck
        val randomCard = (0 until cardDeck.size).random()
        return cardDeck.removeAt(randomCard)
    }

    private fun roundOver() {
        //when the round is over, hides the hit and stand buttons, and changes the dealerButton listener
        hitButton.visibility = View.INVISIBLE
        standButton.visibility = View.INVISIBLE

        dealerButton.setText(R.string.roundResultButton)
        dealerButton.setOnClickListener {
            val intent = Intent(this, DealerHandActivity::class.java)
            intent.putExtra(ROUND_STATUS_KEY, true)
            startActivity(intent)
        }

    }

    private fun dealerPlay() {

        while (GameState.dealerHand.points < 17) {
            GameState.dealerHand.handOfCards.add(drawCard())
            GameState.dealerHand.pointUpdate()
        }

        if (((GameState.dealerHand.points < 21 && GameState.dealerHand.points < playerHand.points) || GameState.dealerHand.points > 21) && playerHand.points <= 21) {
            GameState.playerWin()
            GameState.playerBetReturn = 1.0
        }
        else if (GameState.dealerHand.points <= 21 && GameState.dealerHand.points == playerHand.points) {
            GameState.playerTie()
        }
        else if ((GameState.dealerHand.points <= 21 && playerHand.points < GameState.dealerHand.points) || playerHand.points > 21) {
            GameState.playerLoss()
        }

        Log.d("!!!", "dealer points: ${GameState.dealerHand.points}")

    }

    private fun setCardView(direction : String? = null) {
        //one function for all changes in card views

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

                pointView.text = String.format(getString(R.string.points), "${playerHand.points}")

            }
        }
    }

    private fun makeDeck() {
        // generates a new deck of cards and links each card object to the corresponding image
        cardDeck = mutableListOf()
        val suiteList = listOf("hearts", "clubs", "diamond", "spades")
        val courtList = listOf("a", "j", "q", "k")
        val courtValue = listOf(11, 10)

        //nested for loops to dynamically generate each card and add them to the deck, avoids hardcoding each card
        var drawableId : Int
        for (i in suiteList.indices) {
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

    override fun onResume() {
        if (GameState.roundStatus == "OVER") {
            finish()
        }
        super.onResume()
    }
}