package tw.com.andyawd.colosseoduel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.fragment_main_menu.*
import java.util.concurrent.TimeUnit


class MainMenuFragment : Fragment() {

    private var activity: Activity? = null

    var fragmentOpenListener: FragmentOpenListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initComponent()
        initClickListener()
    }

    private fun initComponent() {
        this.activity?.let { Glide.with(it).load(R.drawable.helmet_010101).into(acivFmmMenuIcon) }
    }

    @SuppressLint("CheckResult")
    private fun initClickListener() {
        mbFmmOpenVideoRecord.clicks()
            .throttleFirst(BaseConstants.CLICK_TIMER, TimeUnit.MILLISECONDS)
            .subscribe { fragmentOpenListener?.onOpenVideoRecordView() }

        mbFmmOpenVideoList.clicks()
            .throttleFirst(BaseConstants.CLICK_TIMER, TimeUnit.MILLISECONDS)
            .subscribe { fragmentOpenListener?.onOpenVideoListView() }
    }
}
