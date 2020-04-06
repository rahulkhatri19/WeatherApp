package `in`.rahul.weatherapp.activity

import `in`.rahul.weatherapp.R
import `in`.rahul.weatherapp.adapter.DetailClimateAdapter
import `in`.rahul.weatherapp.model.DetailClimateModel
import `in`.rahul.weatherapp.utils.CommonUtils.Companion.toastShortMessage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_forecast_detail.*

class ForecastDetailActivity : AppCompatActivity() {

    var stCity = ""
    var stCurrentTemp = ""
    var stMaxTemp = ""
    var stMinTemp = ""
    var stIcon = ""
    var stIconDescription = ""
    lateinit var flexboxLayoutManager:FlexboxLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_detail)
        toolbar.title = "Weather Forecast"

        flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND
        initView()
//        rv_Temp
//        rv_detail
        val climateDetailList = mutableListOf<DetailClimateModel>()
        climateDetailList.add(DetailClimateModel(R.drawable.ic_thermometer, "Feels Like", "${listData.feelLike} °C"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_wind_speed, "Wind Speed", "${listData.windSpeed} km/hr"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_humidity, "Humidity", "${listData.humidity} %"))
        climateDetailList.add(DetailClimateModel(R.drawable.ic_pressure, "Pressure", "${listData.pressure} hPa"))

//        recycleViewDetail.layoutManager = GridLayoutManager(this, 2)
        rv_detail.layoutManager = flexboxLayoutManager
        rv_detail.adapter = DetailClimateAdapter(this, climateDetailList)
//
        iv_current_temp.setOnClickListener {
            toastShortMessage(this, "Weather condition : $stIconDescription")
        }

    }

    private fun initView() {
        tv_city.text = stCity
        tv_current_temp.text = "$stCurrentTemp °C"
        Glide.with(this).load(stIcon).into(iv_current_temp)
        tv_max_temp.text = "$stMaxTemp °C"
        tv_min_temp.text = "$stMinTemp °C"
    }
}
