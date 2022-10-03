package com.example.au22_card_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

const val ROUND_STATUS_KEY = "ROUND_STATUS"
const val STATUS_NOT_SET = false

class DealerHandActivity : AppCompatActivity() {

    private var cardBackImageID = this.resources.getIdentifier("peter_river", "drawable", this.packageName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer_hand)

        val roundStatus = intent.getBooleanExtra(ROUND_STATUS_KEY, STATUS_NOT_SET)

        setCardView(null, roundStatus)

        val leftButton = findViewById<ImageButton>(R.id.cardLeftButton)
        val rightButton = findViewById<ImageButton>(R.id.cardRightButton)
        val returnButton = findViewById<Button>(R.id.dealerButton)

        leftButton.setOnClickListener {
            setCardView("left", roundStatus)
        }
        rightButton.setOnClickListener {
            setCardView("right", roundStatus)
        }

        if (!roundStatus) {
            leftButton.visibility = View.INVISIBLE
            rightButton.visibility = View.INVISIBLE

            returnButton.setOnClickListener {
                finish()
            }
        }
        else {
            leftButton.visibility = View.VISIBLE
            rightButton.visibility = View.VISIBLE

            returnButton.setText(R.string.roundResultButton)

            returnButton.setOnClickListener {

            }
        }
    }

    private fun setCardView(direction : String? = null, roundStatus : Boolean?) {
        //one function for all changes in card views

        val cardViewBack = findViewById<ImageView>(R.id.cardViewBack)
        val cardViewFront = findViewById<ImageView>(R.id.cardViewFront)

        if (roundStatus == true) {
            when (direction) {
                "left" -> {
                //moves "left" through the players hand,
                // if the current cards have the index of 2 in the front and 1 in the back,
                // it changes it to 1 in the front and 0 in the back
                GameState.dealerHand.frontCardIndex = GameState.dealerHand.backCardIndex
                if (GameState.dealerHand.backCardIndex - 1 < 0) {
                    GameState.dealerHand.backCardIndex = GameState.dealerHand.handOfCards.lastIndex
                }
                else {
                    GameState.dealerHand.backCardIndex = GameState.dealerHand.backCardIndex - 1
                }

                cardViewFront.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.frontCardIndex].imageID)
                cardViewBack.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.backCardIndex].imageID)

            }
                "right" -> {
                //moves "right" through the players hand,
                // if the current cards have the index of 1 in the front and 2 in the back,
                // it changes it to 2 in the front and 0 in the back
                GameState.dealerHand.backCardIndex = GameState.dealerHand.frontCardIndex
                if (GameState.dealerHand.frontCardIndex + 1 >= GameState.dealerHand.handOfCards.size) {
                    GameState.dealerHand.frontCardIndex = 0
                }
                else {
                    GameState.dealerHand.frontCardIndex = GameState.dealerHand.frontCardIndex + 1
                }

                cardViewFront.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.frontCardIndex].imageID)
                cardViewBack.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.backCardIndex].imageID)
            }
                else -> {
                //defaults to showing the last card in the players hand in the front, and the card before that in the back

                GameState.dealerHand.frontCardIndex = GameState.dealerHand.handOfCards.lastIndex
                GameState.dealerHand.backCardIndex = GameState.dealerHand.handOfCards.lastIndex - 1

                cardViewFront.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.frontCardIndex].imageID)
                cardViewBack.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.backCardIndex].imageID)

                val pointView = findViewById<TextView>(R.id.pointView)

                pointView.text = getString(R.string.points, GameState.dealerHand.points)

                }
            }
        }
        else {
            val shownPoints = GameState.dealerHand.handOfCards[0].value
            GameState.dealerHand.frontCardIndex = 0
            GameState.dealerHand.backCardIndex = cardBackImageID

            cardViewFront.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.frontCardIndex].imageID)
            cardViewBack.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.backCardIndex].imageID)

            val pointView = findViewById<TextView>(R.id.pointView)

            pointView.text = getString(R.string.points, shownPoints)
        }
    }
}