package com.tristanwiley.githubmeet

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.koushikdutta.ion.Ion


class MainActivity : AppCompatActivity() {
    lateinit private var GITHUB_CLIENT_ID: String
    lateinit private var GITHUB_CLIENT_SECRET: String
    val REPO_NAME: String = "Meet-For-Github-Profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val token = prefs.getString("gh_token", "")

        GITHUB_CLIENT_ID = applicationContext.getString(R.string.GITHUB_CLIENT_ID)
        GITHUB_CLIENT_SECRET = applicationContext.getString(R.string.GITHUB_CLIENT_SECRET)
        Log.wtf("TOKEN", token)

        if (token != "" && isValidToken(token)) {
            if (isFirstTime(token)) {
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
        val response = Ion.with(applicationContext)
                .load("GET", "https://api.github.com/applications/$GITHUB_CLIENT_ID/tokens/$token")
                .setHeader("Accept", "application/json")
                .basicAuthentication(GITHUB_CLIENT_ID, GITHUB_CLIENT_SECRET)
                .asJsonObject()
                .get()
        return !response.getAsJsonObject("app").isJsonNull
    }

    fun isFirstTime(token: String): Boolean {
        val response = Ion.with(applicationContext)
                .load("https://api.github.com/repos/${getName(token)}/$REPO_NAME")
                .asJsonObject()
                .get()
        return !response.get("id").isJsonNull
    }

    fun getName(token: String): String {
        val response = Ion.with(applicationContext)
                .load("https://api.github.com/user")
                .setHeader("Authorization", "token $token")
                .asJsonObject()
                .get()
        return response.get("login").asString
    }
}
