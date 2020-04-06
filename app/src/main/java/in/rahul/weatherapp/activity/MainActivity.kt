package `in`.rahul.weatherapp.activity

import `in`.rahul.weatherapp.R
import `in`.rahul.weatherapp.adapter.DetailClimateAdapter
import `in`.rahul.weatherapp.adapter.ForecastAdapter
import `in`.rahul.weatherapp.model.DetailClimateModel
import `in`.rahul.weatherapp.model.ForecastModel
import `in`.rahul.weatherapp.model.WeatherDetailModel
import `in`.rahul.weatherapp.utils.ApiUrlHelper
import `in`.rahul.weatherapp.utils.ClientHelperInterface
import `in`.rahul.weatherapp.utils.CommonUtils.Companion.logMessage
import `in`.rahul.weatherapp.utils.CommonUtils.Companion.toastShortMessage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

class MainActivity : AppCompatActivity() {

//    var stCurrentTemp = "0"
//    var stMaxTemp = "0"
//    var stMinTemp = "0"
//    var stDate = "Friday, 28 1:00 pm"
    var stCity = ""
//    var stFeelsLikeTemp = "0"
    val searchCity = "Bangalore"
    lateinit var flexboxLayoutManager: FlexboxLayoutManager
    lateinit var weatherDetailList: MutableList<WeatherDetailModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherDetailList = mutableListOf()

        flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND

        loadWeatherData()
    }

    private fun loadWeatherData() {
        weatherDetailList.clear()

        ClientHelperInterface.create()
            .getForcast(searchCity, ApiUrlHelper.openweatherAppId, "metric")
            .enqueue(object :
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
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
                    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+0530")

                    for (i in 0 until weatherList.length()) {
                        val weatherObjectI = weatherList.getJSONObject(i)
                        val dateTime = weatherObjectI.getLong("dt")
                        val mainWeatherData = weatherObjectI.getJSONObject("main")
                        val windDetail = weatherObjectI.getJSONObject("wind")
                        val weatherIconData = weatherObjectI.getJSONArray("weather")
                        val currentTemp = mainWeatherData.getDouble("temp")
                        val feelsLikeTemp = mainWeatherData.getDouble("feels_like")
                        val minTemp = mainWeatherData.getDouble("temp_min")
                        val maxTemp = mainWeatherData.getDouble("temp_max")
                        val pressure = mainWeatherData.getDouble("pressure")
                        val humidity = mainWeatherData.getDouble("humidity")
                        var windSpeed = windDetail.getDouble("speed")
                        val dateData = simpleDateFormat.format(dateTime * 1000)
                        val dateObject = simpleDateFormat.parse(dateData)
                        val date = dateObject?.time

                        stCity = cityDetail.getString("name")
                        var iconUrl = ""
                        var iconDescription = ""
                        for (j in 0 until weatherIconData.length()) {
                            val weatherIconObject = weatherIconData.getJSONObject(j)
                            val iconData = weatherIconObject.getString("icon")
                            iconDescription = weatherIconObject.getString("description")
                            iconUrl = "http://openweathermap.org/img/wn/$iconData@2x.png"
//                        iconUrl = "http://openweathermap.org/img/wn/"+iconData+"@2x.png"
                        }

                        val decimalFormat = DecimalFormat("#.#")
//                        minTemp -= 273.15
//                        maxTemp -= 273.15
//                        currentTemp -= 273.15
//                        feelsLikeTemp -= 273.15
                        windSpeed *= 3.6
                        val simpleDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
//                        stCurrentTemp = decimalFormat.format(currentTemp)
//                        stMaxTemp = decimalFormat.format(maxTemp)
//                        stMinTemp = decimalFormat.format(minTemp)
//                        stFeelsLikeTemp = decimalFormat.format(feelsLikeTemp)

                        weatherDetailList.add(
                            WeatherDetailModel(
                                date.toString(),
                                simpleDate.format(date),
                                decimalFormat.format(currentTemp),
                                decimalFormat.format(feelsLikeTemp),
                                decimalFormat.format(minTemp),
                                decimalFormat.format(maxTemp),
                                pressure.toString(),
                                humidity.toString(),
                                decimalFormat.format(windSpeed),
                                iconUrl,
                                iconDescription
                            )
                        )

                        val stLog =
                            "date: $date currentTemp: ${currentTemp} feelLike: ${(
                                feelsLikeTemp
                            )} minTemp: ${(minTemp)} maxTemp: ${(
                                maxTemp
                            )} pressure: $pressure humidity: $humidity windSpeed: $windSpeed"

                        logMessage("mainAct", stLog)
                    }
                    weatherValuesInit()
                    next5DaysData(weatherDetailList)
                }

            })
    }

    private fun weatherValuesInit() {

        val listData = weatherDetailList.get(0)

        val calendar = GregorianCalendar()
        var dayOfWeek = ""
        calendar.timeInMillis = listData.date.toLong()
        val simpleDateFormat = SimpleDateFormat("dd MMM hh:mm a", Locale.ENGLISH)
        val formatedDate = simpleDateFormat.format(calendar.timeInMillis)
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> dayOfWeek = "Sunday"
            2 -> dayOfWeek = "Monday"
            3 -> dayOfWeek = "Tuesday"
            4 -> dayOfWeek = "Wednesday"
            5 -> dayOfWeek = "Thursday"
            6 -> dayOfWeek = "Friday"
            7 -> dayOfWeek = "Saturday"
        }
//        Friday, 28 1:00 pm
        val currentDate = "$dayOfWeek, $formatedDate"
        toolbar.title = stCity
        Glide.with(this).load(listData.icon).fitCenter().into(iv_current_temp)
//        iv_current_temp.setImageResource(R.drawable.rain)
        tv_current_temp.text = "${listData.currentTemp} °C"
        tv_max_temp.text = "${listData.maxTemp} °C"
        tv_min_temp.text = "${listData.minTemp} °C"
        tv_date.text = currentDate

        val climateDetailList = mutableListOf<DetailClimateModel>()
        climateDetailList.add(DetailClimateModel(R.drawable.ic_thermometer, "Feels Like", "${listData.feelLike} °C"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_wind_speed, "Wind Speed", "${listData.windSpeed} km/hr"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_humidity, "Humidity", "${listData.humidity} %"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_pressure, "Pressure", "${listData.pressure} hPa"))

//        recycleViewDetail.layoutManager = GridLayoutManager(this, 2)
        recycleViewDetail.layoutManager = flexboxLayoutManager
        recycleViewDetail.adapter = DetailClimateAdapter(this, climateDetailList)

        iv_current_temp.setOnClickListener {
            toastShortMessage(this, "Weather condition : ${listData.iconDescription}")
        }
    }

    fun next5DaysData(weatherDetailList:MutableList<WeatherDetailModel>){
        val dayOneList:MutableList<WeatherDetailModel> = mutableListOf()
        val dayTwoList:MutableList<WeatherDetailModel> = mutableListOf()
        val dayThreeList:MutableList<WeatherDetailModel> = mutableListOf()
        val dayFourList:MutableList<WeatherDetailModel> = mutableListOf()
        val dayFiveList:MutableList<WeatherDetailModel> = mutableListOf()
        val daySixList:MutableList<WeatherDetailModel> = mutableListOf()

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar = GregorianCalendar()
        calendar.timeInMillis = weatherDetailList[0].date.toLong()

        val date1 = simpleDateFormat.format(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val date2 = simpleDateFormat.format(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val date3 = simpleDateFormat.format(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val date4 = simpleDateFormat.format(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val date5 = simpleDateFormat.format(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val date6 = simpleDateFormat.format(calendar.timeInMillis)

//        logMessage("MainAct","date1: $date1, date2:$date2, date3:$date3, date4:$date4, date5:$date5, date6:$date6")
        for (k in weatherDetailList.indices){
            logMessage("MainAct", "date: ${weatherDetailList[k].dateFormated}")
            when(weatherDetailList[k].dateFormated){
                date1 -> {dayOneList.add( WeatherDetailModel(weatherDetailList[k].date, weatherDetailList[k].dateFormated, weatherDetailList[k].currentTemp, weatherDetailList[k].feelLike, weatherDetailList[k].minTemp, weatherDetailList[k].maxTemp, weatherDetailList[k].pressure, weatherDetailList[k].humidity, weatherDetailList[k].windSpeed, weatherDetailList[k].icon, weatherDetailList[k].iconDescription))}
                date2 -> {dayTwoList.add( WeatherDetailModel(weatherDetailList[k].date, weatherDetailList[k].dateFormated, weatherDetailList[k].currentTemp, weatherDetailList[k].feelLike, weatherDetailList[k].minTemp, weatherDetailList[k].maxTemp, weatherDetailList[k].pressure, weatherDetailList[k].humidity, weatherDetailList[k].windSpeed, weatherDetailList[k].icon, weatherDetailList[k].iconDescription))}
                date3 -> {dayThreeList.add( WeatherDetailModel(weatherDetailList[k].date, weatherDetailList[k].dateFormated, weatherDetailList[k].currentTemp, weatherDetailList[k].feelLike, weatherDetailList[k].minTemp, weatherDetailList[k].maxTemp, weatherDetailList[k].pressure, weatherDetailList[k].humidity, weatherDetailList[k].windSpeed, weatherDetailList[k].icon, weatherDetailList[k].iconDescription))}
                date4 -> {dayFourList.add( WeatherDetailModel(weatherDetailList[k].date, weatherDetailList[k].dateFormated, weatherDetailList[k].currentTemp, weatherDetailList[k].feelLike, weatherDetailList[k].minTemp, weatherDetailList[k].maxTemp, weatherDetailList[k].pressure, weatherDetailList[k].humidity, weatherDetailList[k].windSpeed, weatherDetailList[k].icon, weatherDetailList[k].iconDescription))}
                date5 -> {dayFiveList.add( WeatherDetailModel(weatherDetailList[k].date, weatherDetailList[k].dateFormated, weatherDetailList[k].currentTemp, weatherDetailList[k].feelLike, weatherDetailList[k].minTemp, weatherDetailList[k].maxTemp, weatherDetailList[k].pressure, weatherDetailList[k].humidity, weatherDetailList[k].windSpeed, weatherDetailList[k].icon, weatherDetailList[k].iconDescription))}
                date6 -> {daySixList.add( WeatherDetailModel(weatherDetailList[k].date, weatherDetailList[k].dateFormated, weatherDetailList[k].currentTemp, weatherDetailList[k].feelLike, weatherDetailList[k].minTemp, weatherDetailList[k].maxTemp, weatherDetailList[k].pressure, weatherDetailList[k].humidity, weatherDetailList[k].windSpeed, weatherDetailList[k].icon, weatherDetailList[k].iconDescription))}
            }
        }

        val forecastList = mutableListOf<ForecastModel>()
        calendar.timeInMillis = dayOneList[0].date.toLong()
        val simpleDateForecast = SimpleDateFormat("EEE, dd/MM", Locale.ENGLISH)
//        forecastList.add(ForecastModel("Tomorrow 29/02", R.drawable.rain, "32 °C", "19 °C"))
//        forecastList.add(ForecastModel("Sun, 01/03", R.drawable.sun, "34 °C", "19 °C"))
//        forecastList.add(ForecastModel("Mon, 02/03", R.drawable.storm, "33 °C", "20 °C"))
        forecastList.add(ForecastModel(simpleDateForecast.format(dayTwoList[0].date.toLong()), dayTwoList[0].icon, "${dayTwoList[0].maxTemp} °C", "${dayTwoList[0].minTemp} °C", dayTwoList))

        forecastList.add(ForecastModel(simpleDateForecast.format(dayThreeList[0].date.toLong()), dayThreeList[0].icon, "${dayThreeList[0].maxTemp} °C", "${dayThreeList[0].minTemp} °C", dayThreeList))

        forecastList.add(ForecastModel(simpleDateForecast.format(dayFourList[0].date.toLong()), dayFourList[0].icon, "${dayFourList[0].maxTemp} °C", "${dayFourList[0].minTemp} °C", dayFourList))

        forecastList.add(ForecastModel(simpleDateForecast.format(dayFiveList[0].date.toLong()), dayFiveList[0].icon, "${dayFiveList[0].maxTemp} °C", "${dayFiveList[0].minTemp} °C", dayFiveList))

        forecastList.add(ForecastModel(simpleDateForecast.format(daySixList[0].date.toLong()), daySixList[0].icon, "${daySixList[0].maxTemp} °C", "${daySixList[0].minTemp} °C", daySixList))
//        logMessage("Main Act: hi", "cal:${calendar.timeInMillis}, data:${dayOneList[0].date},")

        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = ForecastAdapter(this, forecastList)
    }
}
