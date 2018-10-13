package com.hommyiot.guillermorosales.hommyiot

import android.app.Activity
import android.os.Bundle
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException

class MainActivity : Activity() {
    private var mLedstrip: Gpio? = null
    private var mDisplay: AlphanumericDisplay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLedstrip = RainbowHat.openLedRed()
        mDisplay = RainbowHat.openDisplay()
        mDisplay?.setEnabled(true)

        val reference = FirebaseDatabase.getInstance().reference
        reference.child("lights").child("on").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    mDisplay?.display(if (dataSnapshot.value as Boolean) "on" else "off")
                    mLedstrip?.setValue((dataSnapshot.value as Boolean?)!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
