package com.github.vakk.webviewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView

class MainActivity : AppCompatActivity() {

    val webview : WebView get() = findViewById(R.id.webview)
    var searchView : SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val searchItem = menu!!.findItem(R.id.item_search)
        searchView = searchItem.actionView as SearchView?
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.item_next -> {
                webview.findNext(false)
                true
            }
            R.id.item_prev -> {
                webview.findNext(true)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
