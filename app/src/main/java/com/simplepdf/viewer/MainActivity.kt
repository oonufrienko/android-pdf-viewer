package com.simplepdf.viewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView
    private lateinit var emptyView: TextView
    private var currentUri: Uri? = null
    private var currentName: String = "document.pdf"

    private val openDocument =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) openPdf(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfView = findViewById(R.id.pdfView)
        emptyView = findViewById(R.id.emptyView)
        emptyView.setOnClickListener { pickFile() }

        val uri = intent?.data
        if (uri != null) openPdf(uri)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { openPdf(it) }
    }

    private fun pickFile() {
        openDocument.launch(arrayOf("application/pdf"))
    }

    private fun openPdf(uri: Uri) {
        currentUri = uri
        currentName = queryDisplayName(uri) ?: "document.pdf"
        title = currentName
        emptyView.visibility = View.GONE
        pdfView.visibility = View.VISIBLE

        pdfView.fromUri(uri)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)   // подвійний тап — швидкий zoom
            .enableAntialiasing(true)
            .spacing(8)
            .onError {
                Toast.makeText(this, R.string.open_error, Toast.LENGTH_LONG).show()
                emptyView.visibility = View.VISIBLE
                pdfView.visibility = View.GONE
            }
            .load()
        // Pinch-zoom (два пальці) працює у PDFView "з коробки",
        // межі масштабування: minZoom=1, maxZoom=10 (за замовчуванням 3)
        pdfView.maxZoom = 10f
    }

    private fun queryDisplayName(uri: Uri): String? {
        if (uri.scheme == "file") return uri.lastPathSegment
        return try {
            contentResolver.query(uri, null, null, null, null)?.use { c ->
                val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0 && c.moveToFirst()) c.getString(idx) else null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun sharePdf() {
        val uri = currentUri ?: run {
            Toast.makeText(this, R.string.nothing_to_share, Toast.LENGTH_SHORT).show()
            return
        }
        try {
            // Копіюємо у кеш і ділимося через FileProvider — наданий нам
            // content-URI не можна передати третьому додатку напряму
            val outDir = File(cacheDir, "shared").apply { mkdirs() }
            val outFile = File(outDir, currentName)
            contentResolver.openInputStream(uri)!!.use { input ->
                outFile.outputStream().use { output -> input.copyTo(output) }
            }
            val shareUri = FileProvider.getUriForFile(
                this, "com.simplepdf.viewer.fileprovider", outFile
            )
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, shareUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(send, getString(R.string.share_via)))
        } catch (e: Exception) {
            Toast.makeText(this, R.string.share_error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun openDonatePage() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/river44")))
        } catch (e: Exception) {
            // на пристрої немає браузера — нічого не робимо
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open -> { pickFile(); true }
            R.id.action_share -> { sharePdf(); true }
            R.id.action_donate -> { openDonatePage(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
