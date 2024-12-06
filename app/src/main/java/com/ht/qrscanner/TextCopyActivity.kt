package com.ht.qrscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ht.qrscanner.databinding.ActivityTextCopyBinding

class TextCopyActivity : AppCompatActivity() {
    companion object {
        private var scanedText: String = ""
        fun getCallingIntent(context: Context, text: String): Intent {
           this.scanedText = text
           return Intent(context, TextCopyActivity::class.java)
        }
    }
    lateinit var binding: ActivityTextCopyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextCopyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()

    }

    fun setupViews() {
        binding.textView.text = scanedText
        binding.copyButton.setOnClickListener {
            val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("text", scanedText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}