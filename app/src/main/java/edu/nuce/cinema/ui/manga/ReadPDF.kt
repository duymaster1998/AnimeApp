package edu.nuce.cinema.ui.manga

import android.app.ActionBar
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.fragment.navArgs
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.cinema.R
import edu.nuce.cinema.databinding.FragmentMangaBinding
import edu.nuce.cinema.ui.base.BaseFragment
import edu.nuce.cinema.ui.common.constants.Base
import kotlinx.android.synthetic.main.fragment_manga.*
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class MangaFragments : BaseFragment(R.layout.fragment_manga){
    private val args by navArgs<MangaFragmentArgs>()
    private val _binding by viewBinding<FragmentMangaBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e(args.manga.file)
        FileLoader.with(requireContext())
            .load(Base.URL_IMAGE + args.manga.file,true)
            .fromDirectory("PDFFile",FileLoader.DIR_INTERNAL)
            .asFile(object :FileRequestListener<File>{
                override fun onLoad(request: FileLoadRequest?, response: FileResponse<File?>?) {
                    val pdfFile = response!!.body
                    _binding.pdfView.fromFile(pdfFile)
                        .defaultPage(0)
                        .swipeHorizontal(true)
                        .pageSnap(true)
                        .autoSpacing(true)
                        .pageFling(true)
                        .pageFitPolicy(FitPolicy.HEIGHT)
                        .load()

                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    Timber.e(t.toString())
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}

