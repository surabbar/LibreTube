package com.github.libretube.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import com.github.libretube.DONATE_URL
import com.github.libretube.GITHUB_URL
import com.github.libretube.LICENSE_URL
import com.github.libretube.PIPED_GITHUB_URL
import com.github.libretube.R
import com.github.libretube.WEBLATE_URL
import com.github.libretube.WEBSITE_URL
import com.github.libretube.databinding.ActivityAboutBinding
import com.github.libretube.extensions.BaseActivity
import com.github.libretube.extensions.getStyledSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class AboutActivity : BaseActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.website.setOnClickListener {
            openLinkFromHref(WEBSITE_URL)
        }
        binding.website.setOnLongClickListener {
            onLongClick(WEBSITE_URL)
            true
        }

        binding.piped.setOnClickListener {
            openLinkFromHref(PIPED_GITHUB_URL)
        }
        binding.piped.setOnLongClickListener {
            onLongClick(PIPED_GITHUB_URL)
            true
        }

        binding.translate.setOnClickListener {
            openLinkFromHref(WEBLATE_URL)
        }
        binding.translate.setOnLongClickListener {
            onLongClick(WEBLATE_URL)
            true
        }

        binding.donate.setOnClickListener {
            openLinkFromHref(DONATE_URL)
        }
        binding.donate.setOnLongClickListener {
            onLongClick(DONATE_URL)
            true
        }

        binding.github.setOnClickListener {
            openLinkFromHref(GITHUB_URL)
        }
        binding.github.setOnLongClickListener {
            onLongClick(GITHUB_URL)
            true
        }

        binding.license.setOnClickListener {
            showLicense()
        }
        binding.license.setOnLongClickListener {
            onLongClick(LICENSE_URL)
            true
        }
    }

    private fun openLinkFromHref(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW).setData(uri)
        startActivity(intent)
    }

    private fun onLongClick(href: String) {
        // copy the link to the clipboard
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.copied), href)
        clipboard.setPrimaryClip(clip)
        // show the snackBar with open action
        val snackBar = binding.root.getStyledSnackBar(R.string.copied_to_clipboard)
        snackBar.setAction(R.string.open_copied) {
            openLinkFromHref(href)
        }
        snackBar.animationMode = Snackbar.ANIMATION_MODE_FADE
        snackBar.show()
    }

    private fun showLicense() {
        val licenseString = assets
            ?.open("gpl3.html")
            ?.bufferedReader()
            .use {
                it?.readText()
            }

        val licenseHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(licenseString.toString(), 1)
        } else {
            Html.fromHtml(licenseString.toString())
        }

        MaterialAlertDialogBuilder(this)
            .setPositiveButton(getString(R.string.okay)) { _, _ -> }
            .setMessage(licenseHtml)
            .create()
            .show()
    }
}
