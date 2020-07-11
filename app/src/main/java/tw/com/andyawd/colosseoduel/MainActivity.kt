package tw.com.andyawd.colosseoduel

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import tw.com.andyawd.andyawdlibrary.AWDLog

class MainActivity : AppCompatActivity() {

    private val mainMenuFragment = MainMenuFragment()
    private val duelScoreFragment = DuelScoreFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setWindowBackground()

        initComponent()
        initListener()
    }

    private fun setWindowBackground() {
        window.decorView.background = ActivityCompat.getDrawable(this, R.color.colorAccent)
    }

    private fun initComponent() {
        supportFragmentManager.inTransaction {
            replace(R.id.clAmGroup, mainMenuFragment)
        }
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        mainMenuFragment.fragmentOpenListener = fragmentOpenListener
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).func()
            .commitAllowingStateLoss()
    }

    private val fragmentOpenListener = object : FragmentOpenListener() {
        override fun onOpenDuelScore() {
            super.onOpenDuelScore()

            AWDLog.d("onOpenDuelScore")

            if (duelScoreFragment.isAdded) {
                supportFragmentManager.inTransaction {
                    show(duelScoreFragment)
                    addToBackStack(BaseConstants.DUEL_SCORE_FRAGMENT)
                }
            } else {
                supportFragmentManager.inTransaction {
                    add(R.id.clAmGroup, duelScoreFragment)
                    addToBackStack(BaseConstants.DUEL_SCORE_FRAGMENT)
                }
            }
        }

        override fun onOpenHistoryRecord() {
            super.onOpenHistoryRecord()

            AWDLog.d("onOpenHistoryRecord")
        }
    }
}
