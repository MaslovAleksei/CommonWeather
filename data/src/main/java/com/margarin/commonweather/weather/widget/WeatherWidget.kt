package com.margarin.commonweather.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.margarin.commonweather.ACTIVITY_REFERENCE
import com.margarin.commonweather.CONDITION
import com.margarin.commonweather.ICON
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.TEMP
import com.margarin.commonweather.loadFromDataStore
import com.margarin.data.R
import kotlinx.coroutines.runBlocking

class WeatherWidget : AppWidgetProvider() {

    private var temp: String? = null
    private var condition: String? = null
    private var icon: Int? = null
    private var location: String? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            runBlocking {
                temp = loadFromDataStore(context, TEMP, "")
                condition = loadFromDataStore(context, CONDITION, "")
                location = loadFromDataStore(context, LOCATION, "")
                icon = loadFromDataStore(context, ICON, (R.drawable.clear_day).toString()).toInt()
            }

            val intent = Intent(context, Class.forName(ACTIVITY_REFERENCE))
            val pendingIntent = PendingIntent.getActivity(
                context,
                40,
                intent,
                PendingIntent.FLAG_MUTABLE)

            val views = RemoteViews(context.packageName, R.layout.weather_widget)
            views.setTextViewText(R.id.tv_temp_widget, "$temp°C")
            views.setTextViewText(R.id.tv_condition_widget, condition)
            views.setTextViewText(R.id.tv_city_widget, location)
            views.setImageViewResource(R.id.iv_icon_widget, icon ?: R.drawable.clear_day)
            views.setOnClickPendingIntent(appWidgetId, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}