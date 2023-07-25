package com.nahuel.weatherapp.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.nahuel.weatherapp.R
import com.nahuel.weatherapp.databinding.ActivityMainBinding
import com.nahuel.weatherapp.viewmodel.MainviewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: MainviewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET =  GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainviewModel::class.java)

        var cName = GET.getString("cityName", "ankara")
        binding.etCityName.setText(cName)

        viewmodel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.allDataView.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)
            binding.etCityName.setText(cityName)
            viewmodel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imgSearchCityName.setOnClickListener {
            val cityName = binding.etCityName.text.toString()
            SET.putString("cityName",cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            hideKeyBoard()

        }

        changeBackground()
    }

    private fun changeBackground() {

    }

    private fun hideKeyBoard() {
        val inm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun getLiveData() {

        viewmodel.weather_data.observe(this) { data ->
            data?.let {
                binding.allDataView.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
                binding.tvDegree.text = data.main.temp.toInt().toString() + "°C"
                binding.tvCountryCode.text =  data.sys.country.toString()
                binding.tvCityName.text = data.name.toString()
                binding.tvHumidity.text = ": " + data.main.humidity.toString() + "%"
                binding.tvSpeed.text = ": " + data.wind.speed.toString()
                binding.tvLat.text = ": " + data.coord.lat.toString()
                binding.tvLon.text = ": " + data.coord.lon.toString()



                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png")
                    //.load("https://openweathermap.org/img/wn/10d@2x.png")
                    .into(binding.imgWeatherIcon)

                var weatherBG = data.weather[0].icon

                when(weatherBG){
                    "01d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_02d)
                    "02d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_02d)
                    "03d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_03d)
                    "04d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_03d)
                    "9d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_02d)
                    "10d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_10d)
                    "11d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_11d)
                    "13d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_13d)
                    "50d" -> binding.swipeRefreshLayout.setBackgroundResource(R.drawable.ic_50d)
                }




            }
        }

        viewmodel.weather_load.observe(this){ load -> load?.let {
            if(it){
                binding.pbLoading.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
                binding.allDataView.visibility = View.GONE
            }else{
                binding.pbLoading.visibility = View.GONE
            }
        }}

        viewmodel.weather_error.observe(this){error -> error?.let {
            if(it){
                binding.tvError.visibility = View.VISIBLE
                binding.allDataView.visibility = View.GONE
                binding.pbLoading.visibility = View.GONE
            }else{
                binding.tvError.visibility = View.GONE
            }
        }}
    }
}


