package com.example.au22_card_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class RoundResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_result)

        val resultView = findViewById<TextView>(R.id.resultText)
        val betView = findViewById<TextView>(R.id.betText)
        val rewardView = findViewById<TextView>(R.id.rewardText)

        //Changes the 3 textviews depending on whether the player won, lost or tied the round
        //Updates the players chip amount based on the result
        when (GameState.roundStatus) {
            "WIN" -> {
                val betReturn = GameState.playerBet * GameState.playerBetReturn
                GameState.playerChipUpdate(betReturn)

                resultView.setText(R.string.winText)
                betView.text = String.format(getString(R.string.betText), "${GameState.playerBet}")
                rewardView.text = String.format(getString(R.string.rewardText), "$betReturn")
            }
            "TIE" -> {
                resultView.setText(R.string.tieText)
                betView.text = String.format(getString(R.string.betText), "${GameState.playerBet}")
                rewardView.setText(R.string.tieRewardText)
            }
            "LOSS" -> {
                val betLoss = -GameState.playerBet
                GameState.playerChipUpdate(betLoss)

                resultView.setText(R.string.lossText)
                betView.text = String.format(getString(R.string.betText), "${GameState.playerBet}")
                rewardView.setText(R.string.lossRewardText)
            }
        }

        val cashView = findViewById<TextView>(R.id.playerCashView)

        cashView.text = String.format(getString(R.string.cash), "${GameState.playerChips}")

        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {
            GameState.roundOver()
            finish()
        }
    }
}