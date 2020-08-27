package me.erika.retrofitexample.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.erika.retrofitexample.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_nutrients_btn.setOnClickListener{
            val intent = Intent(this, NutritionActivity::class.java)
            startActivity(intent)
        }

        main_drink_water_btn.setOnClickListener{
            val intent = Intent(this, DrinkWaterActivity::class.java)
            startActivity(intent)
        }
    }
}
