package com.example.tick_tack_toe
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tick_tack_toe.databinding.ActivityGameBinding
import kotlin.random.Random


class Game : AppCompatActivity() {

    // ērtas piekļuves activity_game.xml saistīšana
    private lateinit var binding: ActivityGameBinding


    // Pirmais spēlētājs iet pirmais
    private var FirstPlayerIsCross = true


    // Spēles stāvoklis - pabeigts vai nē
    private var stateAbsolute = false


    // rādīt spēlētāja pozīciju un gaijiena pārslēgšana
    private fun checkPlayerMove(button : Button){
        if (FirstPlayerIsCross){
            button.setBackgroundColor(Color.BLUE)
            FirstPlayerIsCross = !FirstPlayerIsCross
        }
        else{
            button.setBackgroundColor(Color.GREEN)
            FirstPlayerIsCross = !FirstPlayerIsCross
        }
    }

    //MutableList pievienotu elementu
    private fun setPos(): String {
        return if (FirstPlayerIsCross)
            "X"
        else
            "O"
    }


    // Kurš spēlētājs tiks grafiski parādīts kā pašreizējais
    private fun setName(p1Name: String?,p2Name: String?): String? {
        return if (FirstPlayerIsCross)
            p1Name
        else
            p2Name
    }

    // Kurš spēlētājs tiks grafiski parādīts kā uzvarētājs
    private fun winName(p1Name: String?,p2Name: String?): String? {
        return if (!FirstPlayerIsCross)
            p1Name
        else
            p2Name
    }

    // pēc spēles beigām izslēgt iespēju izmantot pogas
    private fun disableButtons(gameButtons: List<Button>){
        for (i in gameButtons){
            i.isEnabled = false
        }
    }

    // Pārbaude ja neizšķirts pārbaudot vai ir palikuši "" elementi 2D MutableList
    private fun checkDraw(board: List<List<String>>): Boolean {
        return "" !in board.flatten()
    }

    // Uzvaras pārbaude
    /* Šaja Koda daļa(checkWin) ideja modificeta no
    https://www.reddit.com/r/codehs/comments/jgalvz/i_need_help_for_919_tic_tac_toe_in_codehs/
    Autors: u/Dawn_Kang
    Datums: 17.04.2025 */
    private fun checkWin(board: MutableList<MutableList<String>>): Boolean {

        // Pārbaudīt pēc rindas
        for (i in 0..2) {
            if ((board[i][0] != "") && ((board[i][0] == board[i][1]) && (board [i][1] == board[i][2]))) {
                return true
            }
        }

        // Pārbaudīt pēc kollonas
        for (i in 0..2){
            if ((board[0][i] != "") && ((board[0][i] == board[1][i]) && (board [1][i] == board[2][i]))) {
                return true
            }
        }

        // Pārbaudīt pēc [0][0],[1][1],[2][2] diagonāle
        if ((board[0][0] != "") && ((board[0][0] == board[1][1]) && (board [1][1] == board[2][2]))) {
            return true
        }

        // Pārbaudīt pēc [0][2],[1][1],[2][0] diagonāle
        if ((board[0][2] != "") && ((board[0][2] == board[1][1]) && (board [1][1] == board[2][0]))) {
            return true
        }
        return false
    }


    // Pilna gaijena apstrāde
    private fun buttonDo(binding : Button,boardState : MutableList<MutableList<String>>,
                         ButtonList: MutableList<Button>,currPlayer : TextView,p1Name: String?,
                         p2Name: String?,winPlayerText : TextView,WinScreen : ConstraintLayout,i:Int,n:Int, ){

        // Ja pozīcija nav aizņemta, aizņemt pozīciju ar spēlētāju
        if (boardState[i][n] == ""){
            boardState[i][n] = setPos()
            checkPlayerMove(binding)
            currPlayer.text = setName(p1Name,p2Name)
        }

        // Apstrādāt kāda spēlētāja uzvaru
        if (checkWin(boardState)){
            disableButtons(ButtonList)
            val strName = winName(p1Name,p2Name)
            currPlayer.text = "GAME OVER!"
            val finalName = "$strName Won!!!!"
            winPlayerText.text = finalName
            WinScreen.visibility = View.VISIBLE
            stateAbsolute = true
        }

        //  Apstrādāt neizšķirtu spēli
        if (checkDraw(boardState) && !checkWin(boardState)){
            disableButtons(ButtonList)
            currPlayer.text = "GAME OVER!"
            winPlayerText.text = "DRAW"
            WinScreen.visibility = View.VISIBLE
            stateAbsolute = true
        }

    }

    // random elementa atlase PVC
    private fun pcLogic(isPvC:Boolean,TrackButtons : MutableList<Button>){
        if (isPvC && !FirstPlayerIsCross && TrackButtons.size>1) {
            val size = TrackButtons.size
            var randIndex = Random.nextInt(0,size-1)
            // simulēt otra spēlētāja atlasi
            TrackButtons[randIndex].callOnClick()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        //iegūt objektu koku
        binding = ActivityGameBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Izņemam datus no iepriekšējās aktivitātes
        val p1Name = intent.getStringExtra("P1")
        val p2Name = intent.getStringExtra("P2")
        val isPvC = intent.getBooleanExtra("isPvC",false)
        binding.currPlayer.text = p1Name

        // Spēles stāvokļa saglabāšanai
        val boardState = mutableListOf(
            mutableListOf("","",""),
            mutableListOf("","",""),
            mutableListOf("","",""))

        // Iespējamās pozīcijas
        val ButtonList = mutableListOf(
            binding.button00,binding.button01,binding.button02,
            binding.button10,binding.button11,binding.button12,
            binding.button20,binding.button21,binding.button22)

        // ButtonList kopija atlikušo PVC gājienu izsekošanai
        val TrackButtons = ButtonList

        /*Gaidām nospiešanu visam 9 pogam, izpildām kodu ar līdzīgu loģiku:
         1.) Noņemt no iespējamajiem gajieniem
         2.) Gaitas apstrāde
         3.) Ja spēle ir nepabeigta, PC veic gajienus Ja spēle ir pabeigta, PC neveic gajienus
        */

        binding.button00.setOnClickListener {
            // Noņemt no iespējamajiem gajieniem
            TrackButtons.remove(binding.button00)
            // Gaitas apstrāde
            buttonDo(binding.button00,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,0,0)
            // Ja spēle ir nepabeigta, PC veic gajienus Ja spēle ir pabeigta, PC neveic gajienus
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))

        }
        binding.button01.setOnClickListener {
            TrackButtons.remove(binding.button01)
            buttonDo(binding.button01,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,0,1)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button02.setOnClickListener {
            TrackButtons.remove(binding.button02)
            buttonDo(binding.button02,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,0,2)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button10.setOnClickListener {
            TrackButtons.remove(binding.button10)
            buttonDo(binding.button10,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,1,0)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button11.setOnClickListener {
            TrackButtons.remove(binding.button11)
            buttonDo(binding.button11,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,1,1)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button12.setOnClickListener {
            TrackButtons.remove(binding.button12)
            buttonDo(binding.button12,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,1,2)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button20.setOnClickListener {
            TrackButtons.remove(binding.button20)
            buttonDo(binding.button20,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,2,0)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button21.setOnClickListener {
            TrackButtons.remove(binding.button21 )
            buttonDo(binding.button21,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,2,1)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }
        binding.button22.setOnClickListener {
            TrackButtons.remove(binding.button22)
            buttonDo(binding.button22,boardState,ButtonList,binding.currPlayer,p1Name,p2Name, binding.winPlayerText,binding.WinScreen,2,2)
            if (!stateAbsolute)(pcLogic(isPvC,TrackButtons))
        }

        //Atgriezties galvenajā izvēlnē
        binding.backFromWinButton.setOnClickListener{
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