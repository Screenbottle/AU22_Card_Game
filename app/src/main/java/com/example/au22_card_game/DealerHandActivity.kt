package com.example.au22_card_game

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlin.properties.Delegates

const val ROUND_STATUS_KEY = "ROUND_STATUS"
const val STATUS_NOT_SET = false

class DealerHandActivity : AppCompatActivity() {

    private var cardBackImageID by Delegates.notNull<Int>()
    private lateinit var cardViewBack: ImageView
    private lateinit var cardViewFront: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer_hand)

        val roundStatus = intent.getBooleanExtra(ROUND_STATUS_KEY, STATUS_NOT_SET)
        cardBackImageID = this.resources.getIdentifier("peter_river", "drawable", this.packageName)
        cardViewBack = findViewById(R.id.cardViewBack)
        cardViewFront = findViewById(R.id.cardViewFront)

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

        //if the round is over, the player can look at all of the dealers cards
        // otherwise only the first is visible and the player cannot cycle through them
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
                endRound()
            }
        }
    }

    private fun endRound() {
        val intent = Intent(this, RoundResultActivity::class.java)
        startActivity(intent)
    }

    private fun setCardView(direction : String?, roundStatus : Boolean) {
        //one function for all changes in card views

        if (roundStatus) {
            //if the round is over, the player is shown all cards in the dealer's hand, and can click through them in the same way as for the player's own hand
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

                pointView.text = String.format(getString(R.string.points), "${GameState.dealerHand.points}")

                }
            }
        }
        else {
            //if the round is not over, the player is only shown the first card and its value that the dealer drew
            val shownPoints = GameState.dealerHand.handOfCards[0].value
            GameState.dealerHand.frontCardIndex = 0

            cardViewFront.setImageResource(GameState.dealerHand.handOfCards[GameState.dealerHand.frontCardIndex].imageID)
            cardViewBack.setImageResource(cardBackImageID)

            val pointView = findViewById<TextView>(R.id.pointView)

            pointView.text = String.format(getString(R.string.points), "$shownPoints")
        }
    }

    override fun onResume() {
        if (GameState.roundStatus == "OVER") {
            finish()
        }
        super.onResume()
    }
}