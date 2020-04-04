package `in`.rahul.weatherapp.adapter

import `in`.rahul.weatherapp.R
import `in`.rahul.weatherapp.model.ForecastModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.forecast_layout.view.*

class ForecastAdapter(val context:Context, val forecastList:MutableList<ForecastModel>): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvDate = view.tv_date
        val ivClimate = view.iv_climate
        val tvMaxTemp = view.tv_max_temp
        val tvMinTemp = view.tv_min_temp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.forecast_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = forecastList.get(position)
        holder.tvDate.text = listItem.date
        holder.ivClimate.setImageResource(listItem.climateImage)
        holder.tvMaxTemp.text = listItem.maxTemp
        holder.tvMinTemp.text = listItem.minTemp
    }
}