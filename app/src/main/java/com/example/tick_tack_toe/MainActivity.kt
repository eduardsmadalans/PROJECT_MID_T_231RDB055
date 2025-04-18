package com.example.tick_tack_toe
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tick_tack_toe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    // ērtas piekļuves activity_main.xml saistīšana
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        //iegūt objektu koku
        binding = ActivityMainBinding.inflate(layoutInflater)


        enableEdgeToEdge()

        setContentView(binding.root)

        //Gaidām nospiešanu, izpildām kodu ja ja
        binding.button.setOnClickListener {

            //iegūstam ievadīto spēlētāju vārdu
            var name1 = binding.p1Name.text.toString()
            var name2 = binding.p2Name.text.toString()

            //vai spēle ir PvC?
            val isPvC = false

            //Ja vārds nav ievadīts, izmantot placeholder
            if (name1.isEmpty()) name1 = "Player 1"
            if (name2.isEmpty()) name2 = "Player 2"

            val intent = Intent(this, Game::class.java)

            // Pārsūtīt informāciju uz citu aktivitāti
            intent.putExtra("P1", name1)
            intent.putExtra("P2", name2)
            intent.putExtra("isPvC", isPvC)


            startActivity(intent)
        }

        //Gaidām nospiešanu, izpildām kodu ja ja
        binding.buttonPvC.setOnClickListener {
            //iegūstam ievadīto spēlētāju vārdu
            var name1 = binding.p1Name.text.toString()
            val name2 = "PC"

            //vai spēle ir PvC?
            val isPvC  = true

            //Ja vārds nav ievadīts, izmantot placeholder
            if (name1.isEmpty()) name1 = "Player 1"
            val intent = Intent(this, Game::class.java)

            // Pārsūtīt informāciju uz citu aktivitāti
            intent.putExtra("P1", name1)
            intent.putExtra("P2", name2)
            intent.putExtra("isPvC",isPvC)

            startActivity(intent)
        }


        //Gaidām nospiešanu, iziet ja ja
        binding.buttonExit.setOnClickListener {
            finish()
        }

        // Palicis no Android Studio Empty Activity template
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}