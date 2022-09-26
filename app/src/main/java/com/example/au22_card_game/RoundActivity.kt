package com.example.au22_card_game

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class RoundActivity : AppCompatActivity() {

    lateinit var cardDeck : MutableList<playingCard>
    lateinit var playerHand : MutableList<playingCard>
    lateinit var dealerHand : MutableList<playingCard>
    var playerPoints : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round)

        val playerCount = 1

        playerHand = mutableListOf()
        dealerHand = mutableListOf()

        val handList = arrayOf(playerHand, dealerHand)

        makeDeck()

        for (i in 1..2) {
            for (j in 0..playerCount) {
                val drawnCard = drawCard()
                handList[j].add(drawnCard)
            }
        }

        val hitButton = findViewById<Button>(R.id.hitButton)

        hitButton.setOnClickListener {
            val newCard = drawCard()
            playerHand.add(newCard)
            playerPointUpdate()

            if (playerPoints > 21) {
                //todo
                //lose round
            }
        }

        val standButton = findViewById<Button>(R.id.standButton)

        standButton.setOnClickListener {
            //todo
        }

        val dealerButton = findViewById<Button>(R.id.dealerButton)

        dealerButton.setOnClickListener {
            //todo
        }

        val leftButton = findViewById<ImageButton>(R.id.cardLeftButton)

        leftButton.setOnClickListener {
            setCardView("left")
        }

        val rightButton = findViewById<ImageButton>(R.id.cardRightButton)

        rightButton.setOnClickListener {
            setCardView(("right"))
        }

        playerPointUpdate()
        setCardView()


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
                    val card = playingCard(drawableId, "${suiteList[i]}_${courtList[j]}",  courtValue[0], suiteList[i])
                    cardDeck.add(card)
                }
                else if (j > 9){
                    drawableId = this.resources.getIdentifier("${suiteList[i]}_${courtList[j-9]}", "drawable", this.packageName)
                    val card = playingCard(drawableId, "${suiteList[i]}_${courtList[j-9]}",  courtValue[1], suiteList[i])
                    cardDeck.add(card)
                }
                else {
                    drawableId = this.resources.getIdentifier("${suiteList[i]}_$j", "drawable", this.packageName)
                    val card = playingCard(drawableId, "${suiteList[i]}_$j",  j, suiteList[i])
                    cardDeck.add(card)
                }
            }
        }
    }

    private fun drawCard() : playingCard {
        //draws a random card and removes it from the deck
        val randomCard = (1..cardDeck.size).random()

        val drawnCard : playingCard = cardDeck[randomCard]

        cardDeck.removeAt(randomCard)

        return drawnCard
    }

    private fun playerPointUpdate() {
        for (i in (0 until playerHand.size)) {
            playerPoints += playerHand[i].value
        }

        val pointView = findViewById<TextView>(R.id.pointView)

        pointView.text = getString(R.string.points, playerPoints)
    }

    private fun setCardView(direction : String? = null) {

        val cardViewBack = findViewById<ImageView>(R.id.cardViewBack)
        val cardViewFront = findViewById<ImageView>(R.id.cardViewFront)

        if (direction == "left") {
            //todo
        }
        else if (direction == "right") {
            //todo
        }
        else {
            cardViewFront.setImageResource(playerHand[0].imageID)
            cardViewBack.setImageResource(playerHand[1].imageID)
        }
    }


}