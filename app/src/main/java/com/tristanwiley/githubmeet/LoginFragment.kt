package com.tristanwiley.githubmeet

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion

class LoginFragment : Fragment() {
    val OAUTH_REQUEST_CODE = 1
    private val GITHUB_CLIENT_ID = context.getString(R.string.GITHUB_CLIENT_ID)
    private val GITHUB_CLIENT_SECRET = context.getString(R.string.GITHUB_CLIENT_SECRET)

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable bundle: Bundle?): View? {
        super.onCreateView(inflater, null, bundle)
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent) {
        super.onActivityResult(requestCode, resultCode, result)
        val accessToken = result.extras["result"].toString()
        if (resultCode == OAUTH_REQUEST_CODE) {
            Log.wtf("CODE", accessToken)
            val json = JsonObject()
            json.addProperty("client_id", GITHUB_CLIENT_ID)
            json.addProperty("client_secret", GITHUB_CLIENT_SECRET)
            json.addProperty("code", accessToken)

            Ion.with(activity)
                    .load("POST", "https://github.com/login/oauth/access_token?client_id=$GITHUB_CLIENT_ID&client_secret=$GITHUB_CLIENT_SECRET&code=$accessToken")
                    .setHeader("Accept", "application/json")
                    .asJsonObject()
                    .setCallback { _, result ->
                        if (result != null) {
                            setOAuthToken(result["access_token"].asString)
                        } else {
                            Toast.makeText(activity, "There was a problem authenticating, try again?", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val loginButton = activity.findViewById(R.id.githubLogin) as? Button
        loginButton?.setOnClickListener {
            val browserIntent = Intent(activity, OAuthCallback::class.java)
            startActivityForResult(browserIntent, OAUTH_REQUEST_CODE)
        }
    }

    fun setOAuthToken(token: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = prefs.edit().apply {
            putString("gh_token", token)
        }
        editor.apply()
    }
}
