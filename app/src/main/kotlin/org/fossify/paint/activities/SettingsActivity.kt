package org.fossify.paint.activities

import android.os.Bundle
import org.fossify.commons.extensions.beVisibleIf
import org.fossify.commons.extensions.getProperPrimaryColor
import org.fossify.commons.extensions.updateTextColors
import org.fossify.commons.extensions.viewBinding
import org.fossify.commons.helpers.NavigationIcon
import org.fossify.commons.helpers.isTiramisuPlus
import org.fossify.paint.databinding.ActivitySettingsBinding
import org.fossify.paint.extensions.config
import java.util.Locale

class SettingsActivity : SimpleActivity() {
    private val binding by viewBinding(ActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            setupEdgeToEdge(padBottomSystem = listOf(settingsNestedScrollview))
            setupMaterialScrollListener(binding.settingsNestedScrollview, binding.settingsAppbar)
        }
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.settingsAppbar, NavigationIcon.Arrow)

        setupCustomizeColors()
        binding.settingsRwUpdatesHolder.setOnClickListener {
            ch.rosenweg4303.updater.RosenwegUpdater.jetztPruefen(this)
        }
        setupUseEnglish()
        setupLanguage()
        setupNextcloud()
        setupPreventPhoneFromSleeping()
        setupBrushSize()
        setupAllowZoomingCanvas()
        setupRelativeBrushSize()
        setupForcePortraitMode()
        updateTextColors(binding.settingsHolder)

        arrayOf(
            binding.settingsColorCustomizationSectionLabel,
            binding.settingsGeneralSettingsLabel
        ).forEach {
            it.setTextColor(getProperPrimaryColor())
        }
    }

    private fun setupCustomizeColors() {
        binding.settingsColorCustomizationHolder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupUseEnglish() {
        binding.apply {
            settingsUseEnglishHolder.beVisibleIf((config.wasUseEnglishToggled || Locale.getDefault().language != "en") && !isTiramisuPlus())
            settingsUseEnglish.isChecked = config.useEnglish
            settingsUseEnglishHolder.setOnClickListener {
                settingsUseEnglish.toggle()
                config.useEnglish = settingsUseEnglish.isChecked
                System.exit(0)
            }
        }
    }

    /** Rosenweg: Anmeldung an der Nextcloud, damit Zeichnungen dort liegen. */
    private fun setupNextcloud() {
        val konto = ch.rosenweg4303.cloud.KontoSpeicher(this).laden()
        binding.settingsNextcloud.text = if (konto == null) {
            getString(org.fossify.paint.R.string.rw_nextcloud_aus)
        } else {
            getString(org.fossify.paint.R.string.rw_nextcloud_an, konto.benutzer)
        }
        binding.settingsNextcloudHolder.setOnClickListener {
            startActivity(android.content.Intent(this, ch.rosenweg4303.cloud.AnmeldenActivity::class.java))
        }
    }

    private fun setupLanguage() {
        binding.apply {
            settingsLanguage.text = Locale.getDefault().displayLanguage
            settingsLanguageHolder.beVisibleIf(isTiramisuPlus())
            settingsLanguageHolder.setOnClickListener {
                launchChangeAppLanguageIntent()
            }
        }
    }

    private fun setupPreventPhoneFromSleeping() {
        binding.apply {
            settingsPreventPhoneFromSleeping.isChecked = config.preventPhoneFromSleeping
            settingsPreventPhoneFromSleepingHolder.setOnClickListener {
                settingsPreventPhoneFromSleeping.toggle()
                config.preventPhoneFromSleeping = settingsPreventPhoneFromSleeping.isChecked
            }
        }
    }

    private fun setupBrushSize() {
        binding.apply {
            settingsShowBrushSize.isChecked = config.showBrushSize
            settingsShowBrushSizeHolder.setOnClickListener {
                settingsShowBrushSize.toggle()
                config.showBrushSize = settingsShowBrushSize.isChecked
            }
        }
    }

    private fun setupAllowZoomingCanvas() {
        binding.apply {
            settingsAllowZoomingCanvas.isChecked = config.allowZoomingCanvas
            settingsAllowZoomingCanvasHolder.setOnClickListener {
                settingsAllowZoomingCanvas.toggle()
                config.allowZoomingCanvas = settingsAllowZoomingCanvas.isChecked
            }
        }
    }

    private fun setupRelativeBrushSize() {
        binding.apply {
            settingsRelativeBrushSize.isChecked = config.relativeBrushSize
            settingsRelativeBrushSizeHolder.setOnClickListener {
                settingsRelativeBrushSize.toggle()
                config.relativeBrushSize = settingsRelativeBrushSize.isChecked
            }
        }
    }

    private fun setupForcePortraitMode() {
        binding.apply {
            settingsForcePortrait.isChecked = config.forcePortraitMode
            settingsForcePortraitHolder.setOnClickListener {
                settingsForcePortrait.toggle()
                config.forcePortraitMode = settingsForcePortrait.isChecked
            }
        }
    }
}
