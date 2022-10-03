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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("!!!", "startas ok")

        val startRoundButton = findViewById<Button>(R.id.startRoundButton)
        val playerCashView = findViewById<TextView>(R.id.playerCashView)
        playerCashView.text = String.format(getString(R.string.points), GameState.playerChips)

        playerBetView = findViewById(R.id.playerBet)

        startRoundButton.setOnClickListener {
            startButtonPressed()
        }
    }

    private fun startButtonPressed() {
        val playerBetText = playerBetView.text.toString()
        val playerBet = playerBetText.toIntOrNull()

        if (playerBet != null) {
            if ((playerBet < GameState.playerChips) && (playerBet > 0)) {
                GameState.playerBet = playerBet

                val intent = Intent(this, RoundActivity::class.java)
                startActivity(intent)
            }
            else if (playerBet < 0) {
                Toast.makeText(applicationContext, "Error: Invalid Bet", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(applicationContext,"Error: Not Enough Chips", Toast.LENGTH_LONG).show()

            }
        }
        else{
            Toast.makeText(applicationContext,"Error: No Bet Made", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        playerBetView.setText(R.string.betEditTextHint)
        GameState.playerBet = 0
    }
}