/*
* Log.kt
* © 2024 Fleetio
* */

package com.example.levysample.common

/**
 * To ease recognition in a logcat view, all log tags
 * have exactly four characters.
 *
 */
class Tag {
    companion object {
        val USER: String = "user" // user interface
        val NETW: String = "netw" // network
        val VIEW: String = "view" // view model
        val MAIN: String = "main" // MainActivity
    }
}
