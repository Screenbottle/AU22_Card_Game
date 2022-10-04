package com.example.au22_card_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var playerBetView: EditText
    private lateinit var playerCashView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startRoundButton = findViewById<Button>(R.id.startRoundButton)
        playerCashView = findViewById(R.id.playerCashView)
        playerCashView.text = String.format(getString(R.string.cash), "${GameState.playerChips}")

        playerBetView = findViewById(R.id.playerBet)

        startRoundButton.setOnClickListener {
            startButtonPressed()
        }
    }

    private fun startButtonPressed() {
        val playerBetText = playerBetView.text.toString()
        val playerBet = playerBetText.toDoubleOrNull()

        //checks if the player has given a valid input, displays error message toasts if that is not the case
        if (playerBet != null) {
            if ((playerBet < GameState.playerChips) && (playerBet > 0.0)) {
                GameState.playerBet = playerBet

                val intent = Intent(this, RoundActivity::class.java)
                startActivity(intent)
            }
            else if (playerBet < 5) {
                Toast.makeText(this, "Error: You must bet 5$ or more", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this,"Error: Not Enough Chips", Toast.LENGTH_LONG).show()

            }
        }
        else{
            Toast.makeText(this,"Error: No Bet Made", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        playerBetView.setHint(R.string.betEditTextHint)
        playerBetView.text = null
        GameState.playerBet = 0.0
        playerCashView.text = String.format(getString(R.string.cash), "${GameState.playerChips}")
    }
}