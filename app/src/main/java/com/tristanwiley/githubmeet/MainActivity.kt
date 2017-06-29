package com.tristanwiley.githubmeet

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.koushikdutta.ion.Ion


class MainActivity : AppCompatActivity() {
    private val GITHUB_CLIENT_ID = applicationContext.getString(R.string.GITHUB_CLIENT_ID)
    private val GITHUB_CLIENT_SECRET = applicationContext.getString(R.string.GITHUB_CLIENT_SECRET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val token = prefs.getString("gh_token", "")
        Log.wtf("TOKEN", token)

        if (token != "" && isValidToken(token)) {
            if (isFirstTime()) {
                //TODO go to register fragment
            } else {
                //TODO go to dating fragment
            }
        } else {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, LoginFragment())
                    .commitAllowingStateLoss()
        }
    }

    fun isValidToken(token: String): Boolean {
        val test = Ion.with(applicationContext)
                .load("GET", "https://api.github.com/applications/$GITHUB_CLIENT_ID/tokens/$token")
                .setHeader("Accept", "application/json")
                .basicAuthentication(GITHUB_CLIENT_ID, GITHUB_CLIENT_SECRET)
                .asJsonObject()
                .get()
        return !test.getAsJsonObject("app").isJsonNull
    }

    fun isFirstTime(): Boolean {

        return true
    }
}
