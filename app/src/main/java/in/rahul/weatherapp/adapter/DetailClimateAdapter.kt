package `in`.rahul.weatherapp.adapter

import `in`.rahul.weatherapp.R
import `in`.rahul.weatherapp.model.DetailClimateModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_details_layout.view.*

class DetailClimateAdapter(val context: Context, val climateList: MutableList<DetailClimateModel>) :
    RecyclerView.Adapter<DetailClimateAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivDetail = view.iv_detail
        val tvUnitName = view.tv_unit_name
        val tvUnitValue = view.tv_unit_value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.weather_details_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return climateList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = climateList.get(position)

        holder.ivDetail.setImageResource(listItem.climateImage)
        holder.tvUnitName.text = listItem.unitName
        holder.tvUnitValue.text = listItem.unitValue
    }
}