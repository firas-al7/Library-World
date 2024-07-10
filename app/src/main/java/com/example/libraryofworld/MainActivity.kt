package com.example.libraryofworld

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.libraryofworld.fragment.HomeFragment
import com.example.libraryofworld.fragment.MyHomePageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navBottom: BottomNavigationView

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navBottom = findViewById(R.id.bottom_navigation)


        switchFrag1()
        navBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> switchFrag1()
                R.id.page_2 -> switchFrag2()
                else -> {
                    switchFrag1()
                }
            }
            true
        }
    }

    private fun switchFrag1() {
        val manger = supportFragmentManager
        manger.beginTransaction().replace(R.id.frLy, HomeFragment()).commit()
    }

    private fun switchFrag2() {
        val manger = supportFragmentManager
        manger.beginTransaction().replace(R.id.frLy, MyHomePageFragment()).addToBackStack(null)
            .commit()
    }

}
