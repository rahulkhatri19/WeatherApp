package `in`.rahul.weatherapp.adapter

import `in`.rahul.weatherapp.R
import `in`.rahul.weatherapp.model.ForecastTempModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.forcast_temp_layout.view.*

class ForecastTempAdapter(val context: Context, val forecastList: MutableList<ForecastTempModel>) :
    RecyclerView.Adapter<ForecastTempAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime = view.tv_time
        val ivClimate = view.iv_weather
        val tvCurrentTemp = view.tv_current_temp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.forcast_temp_layout,
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
        holder.tvTime.text = listItem.tvTime
        holder.tvCurrentTemp.text = listItem.tvCurrentTemp
        Glide.with(context).load(listItem.ivIcon).into(holder.ivClimate)
    }
}