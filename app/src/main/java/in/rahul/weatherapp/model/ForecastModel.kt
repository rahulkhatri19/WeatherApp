package `in`.rahul.weatherapp.model

class ForecastModel(val date:String, val climateImage:String, val maxTemp:String, val minTemp:String, val weatherData:MutableList<WeatherDetailModel>) {
}