package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    enum class Turn { NOUGHT, CROSS }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS
    private var boardList = mutableListOf<Button>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
    }

    private fun initBoard() {
        boardList = mutableListOf(
            binding.a1, binding.a2, binding.a3,
            binding.b1, binding.b2, binding.b3,
            binding.c1, binding.c2, binding.c3
        )

        for (button in boardList) {
            button.setOnClickListener { boardTapped(it) }
        }
    }

    private fun boardTapped(view: View) {
        if (view !is Button || view.text != "") return

        val isCross = currentTurn == Turn.CROSS
        val symbol = getString(if (isCross) R.string.player_x else R.string.player_o)
        val color = ContextCompat.getColor(this, if (isCross) R.color.player_x else R.color.player_o)

        currentTurn = if (isCross) Turn.NOUGHT else Turn.CROSS

        view.text = symbol
        view.setTextColor(color)
        setTurnLabel()

        if (checkForVictory(symbol)) {
            result(getString(R.string.victory_message, symbol))
        } else if (fullBoard()) {
            result(getString(R.string.draw_message))
        }
    }

    private fun checkForVictory(s: String): Boolean {
        val winPaths = listOf(
            listOf(binding.a1, binding.a2, binding.a3),
            listOf(binding.b1, binding.b2, binding.b3),
            listOf(binding.c1, binding.c2, binding.c3),
            listOf(binding.a1, binding.b1, binding.c1),
            listOf(binding.a2, binding.b2, binding.c2),
            listOf(binding.a3, binding.b3, binding.c3),
            listOf(binding.a1, binding.b2, binding.c3),
            listOf(binding.a3, binding.b2, binding.c1)
        )

        return winPaths.any { path ->
            path.all { it.text == s }
        }
    }

    private fun result(title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(R.string.reset_button) { _, _ -> resetBoard() }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard() {
        boardList.forEach { it.text = "" }
        firstTurn = Turn.CROSS
        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean = boardList.none { it.text == "" }

    private fun setTurnLabel() {
        val player = if (currentTurn == Turn.CROSS) getString(R.string.player_x) else getString(R.string.player_o)
        binding.turnTV.text = "Player $player's Turn"
    }
}