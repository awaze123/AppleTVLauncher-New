package com.awaze.appletvlauncher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 12, 24)
    private val white = Color.rgb(245, 247, 255)
    private val muted = Color.rgb(164, 176, 201)
    private lateinit var root: LinearLayout

    data class AppItem(val title: String, val pkg: String, val symbol: String, val c1: Int, val c2: Int)
    private val apps = listOf(
    AppItem("YouTube", "com.google.android.youtube.tv", "▶", Color.rgb(250, 40, 55), Color.rgb(130, 0, 15)),
    AppItem("NetMirror TV", "app.netmirror.newtv", "N", Color.rgb(120, 78, 220), Color.rgb(48, 24, 100)),
    AppItem("Cricfy TV", "app.cricfy.tv", "C", Color.rgb(44, 115, 235), Color.rgb(20, 40, 115)),
    AppItem("JioHotstar", "in.startv.hotstar", "✦", Color.rgb(40, 120, 245), Color.rgb(15, 30, 100))
)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        buildHome()
    }

    private fun buildHome() {
        val scroll = ScrollView(this).apply { isFillViewport = true }
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(30), dp(22), dp(30), dp(24))
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(Color.rgb(8, 15, 34), Color.rgb(15, 24, 48), Color.rgb(5, 8, 18)))
        }
        scroll.addView(root)
        setContentView(scroll)

        val top = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        top.addView(label("TV+  |  STREAMING", 20, white, true), LinearLayout.LayoutParams(0, dp(40), 1f))
        top.addView(label(SimpleDateFormat("EEE, d MMM  •  h:mm a", Locale.ENGLISH).format(Date()), 13, muted, false))
        root.addView(top)

        val hero = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(28), dp(22), dp(24), dp(22))
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.rgb(30, 75, 135), Color.rgb(25, 35, 80), Color.rgb(20, 24, 48))).apply {
                cornerRadius = dp(22).toFloat()
                setStroke(dp(1), Color.argb(90, 130, 175, 255))
            }
            isFocusable = true
        }
        hero.addView(label("A WORLD OF ENTERTAINMENT", 11, Color.rgb(185, 211, 255), true))
        hero.addView(label("Your screen. Your stories.", 27, white, true).apply { setPadding(0, dp(7), 0, dp(6)) })
        hero.addView(label("Movies, live television and favourites — all in one place.", 14, muted, false))
        hero.addView(label("↓   EXPLORE YOUR APPS", 12, white, true).apply { setPadding(0, dp(18), 0, 0) })
        hero.setOnClickListener { root.findViewWithTag<View>("dock0")?.requestFocus() }
        root.addView(hero, LinearLayout.LayoutParams(-1, dp(190)).apply { topMargin = dp(22) })

        root.addView(label("Trending on your screen", 19, white, true).apply { setPadding(0, dp(22), 0, dp(12)) })
        val cards = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val names = listOf("Cinematic nights", "Live & local", "Your next favourite")
        val colors = listOf(
            intArrayOf(Color.rgb(80, 50, 125), Color.rgb(20, 30, 65)),
            intArrayOf(Color.rgb(20, 105, 115), Color.rgb(12, 38, 65)),
            intArrayOf(Color.rgb(150, 65, 60), Color.rgb(45, 25, 55))
        )
        names.forEachIndexed { i, name ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.BOTTOM
                setPadding(dp(16), dp(14), dp(12), dp(14))
                background = GradientDrawable(GradientDrawable.Orientation.TL_BR, colors[i]).apply { cornerRadius = dp(16).toFloat(); setStroke(dp(1), Color.argb(55, 255, 255, 255)) }
                isFocusable = true
            }
            card.addView(label(listOf("✦", "◉", "▶")[i], 25, white, true))
            card.addView(label(name, 14, white, true).apply { setPadding(0, dp(10), 0, 0) })
            cards.addView(card, LinearLayout.LayoutParams(0, dp(110), 1f).apply { if (i > 0) marginStart = dp(12) })
        }
        root.addView(cards)

        root.addView(label("Your apps", 19, white, true).apply { setPadding(0, dp(22), 0, dp(12)) })
        val dock = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(10), dp(10), dp(10), dp(10))
            background = GradientDrawable().apply { setColor(Color.argb(110, 35, 45, 70)); cornerRadius = dp(20).toFloat(); setStroke(dp(1), Color.argb(80, 150, 175, 220)) }
        }
        apps.forEachIndexed { index, app ->
            val tile = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                isFocusable = true
                contentDescription = app.title
                background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(app.c1, app.c2)).apply { cornerRadius = dp(14).toFloat() }
                setOnFocusChangeListener { v, focused ->
                    v.scaleX = if (focused) 1.08f else 1f
                    v.scaleY = if (focused) 1.08f else 1f
                    (v.background as? GradientDrawable)?.setStroke(dp(if (focused) 3 else 0), if (focused) Color.WHITE else Color.TRANSPARENT)
                }
                setOnClickListener { openApp(app) }
                tag = "dock$index"
            }
            tile.addView(label(app.symbol, 25, white, true))
            tile.addView(label(app.title, 11, white, true).apply { setPadding(dp(2), dp(5), dp(2), 0) })
            dock.addView(tile, LinearLayout.LayoutParams(0, dp(82), 1f).apply { if (index > 0) marginStart = dp(10) })
        }
        root.addView(dock)
        root.findViewWithTag<View>("dock0")?.requestFocus()
    }

    private fun openApp(app: AppItem) {
        val launch = packageManager.getLeanbackLaunchIntentForPackage(app.pkg) ?: packageManager.getLaunchIntentForPackage(app.pkg)
        if (launch == null) {
            Toast.makeText(this, "${app.title} installed ਨਹੀਂ ਹੈ", Toast.LENGTH_SHORT).show()
            return
        }
        try { startActivity(launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
        catch (_: Exception) { Toast.makeText(this, "${app.title} ਖੁੱਲ੍ਹ ਨਹੀਂ ਸਕੀ", Toast.LENGTH_SHORT).show() }
    }

    private fun label(text: String, size: Int, color: Int, bold: Boolean): TextView = TextView(this).apply {
        this.text = text
        textSize = size.toFloat()
        setTextColor(color)
        if (bold) setTypeface(typeface, Typeface.BOLD)
        gravity = Gravity.CENTER_VERTICAL
        maxLines = 2
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
