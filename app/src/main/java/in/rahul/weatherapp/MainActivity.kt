package `in`.rahul.weatherapp

import `in`.rahul.weatherapp.adapter.DetailClimateAdapter
import `in`.rahul.weatherapp.adapter.ForecastAdapter
import `in`.rahul.weatherapp.model.DetailClimateModel
import `in`.rahul.weatherapp.model.ForecastModel
import `in`.rahul.weatherapp.utils.ApiUrlHelper
import `in`.rahul.weatherapp.utils.ClientHelperInterface
import `in`.rahul.weatherapp.utils.CommonUtils.Companion.logMessage
import `in`.rahul.weatherapp.utils.CommonUtils.Companion.toastShortMessage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTemp = "31"
        val maxTemp = "34"
        val minTemp = "19"

        toolbar.title = "Bangaluru"
        iv_current_temp.setImageResource(R.drawable.rain)
        tv_current_temp.text = "$currentTemp °C"
        tv_max_temp.text = "$maxTemp °C"
        tv_min_temp.text = "$minTemp °C"
        tv_date.text = "Friday, 28 1:00 pm"

        val forecastList= mutableListOf<ForecastModel>()
        forecastList.add(ForecastModel("Tomorrow 29/02", R.drawable.rain, "32 °C", "19 °C"))
        forecastList.add(ForecastModel("Sun, 01/03", R.drawable.sun, "34 °C", "19 °C"))
        forecastList.add(ForecastModel("Mon, 02/03", R.drawable.storm, "33 °C", "20 °C"))
        forecastList.add(ForecastModel("Tue, 03/03", R.drawable.storm, "33 °C", "20 °C"))

        val climateDetailList = mutableListOf<DetailClimateModel>()
        climateDetailList.add(DetailClimateModel(R.drawable.ic_thermometer, "Feels Like", "33 °C"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_wind_speed, "Wind Speed", "3 km/hr"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_humidity, "Humidity", "31 %"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_pressure, "Pressure", "1011 hPa"))

        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = ForecastAdapter(this, forecastList)

        recycleViewDetail.layoutManager = GridLayoutManager(this, 2)
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND
        recycleViewDetail.layoutManager = flexboxLayoutManager
        recycleViewDetail.adapter = DetailClimateAdapter(this, climateDetailList)
        loadWeatherData()
    }

    private fun loadWeatherData() {
        val city = "Bangalore"
        ClientHelperInterface.create().getForcast(city, ApiUrlHelper.openweatherAppId).enqueue(object :
            Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                logMessage("MainAct", "error: ${t.message}")
                toastShortMessage(this@MainActivity, "Please Try After some time !")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                logMessage("MainAct", "Success: ${response.body()}")
                val jsonMainObject = JSONObject(response.body().toString())
                val weatherList = jsonMainObject.getJSONArray("list")
                val cityDetail = jsonMainObject.getJSONObject("city")
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH)
                simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+0530")

                for (i in 0 until weatherList.length()){
                    val weatherObjectI = weatherList.getJSONObject(i)
                    val dateTime = weatherObjectI.getLong("dt")
                    val mainWeatherData = weatherObjectI.getJSONObject("main")
                    val windDetail = weatherObjectI.getJSONObject("wind")
                    var currentTemp = mainWeatherData.getDouble("temp")
                    var feelsLikeTemp = mainWeatherData.getDouble("feels_like")
                    var minTemp = mainWeatherData.getDouble("temp_min")
                    var maxTemp = mainWeatherData.getDouble("temp_max")
                    val pressure = mainWeatherData.getDouble("pressure")
                    val humidity = mainWeatherData.getDouble("humidity")
                    val windSpeed = windDetail.getDouble("speed")
                    val date= simpleDateFormat.format(dateTime*1000)

                    val decimalFormat = DecimalFormat("#.#")
                    minTemp -= 273.15
                    maxTemp -= 273.15
                    currentTemp -= 273.15
                    feelsLikeTemp -= 273.15


                    val stLog = "date: $date currentTemp: ${decimalFormat.format(currentTemp)} feelLike: ${decimalFormat.format(feelsLikeTemp)} minTemp: ${decimalFormat.format(minTemp)} maxTemp: ${decimalFormat.format(maxTemp)} pressure: $pressure humidity: $humidity windSpeed: $windSpeed"

                    logMessage("mainAct", stLog)
                }
            }

        })
    }
}
