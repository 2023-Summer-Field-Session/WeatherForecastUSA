package biz.binarysolutions.weatherusa.components.forecast.workerthreads;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import biz.binarysolutions.weatherusa.components.MessageStatus;
import biz.binarysolutions.weatherusa.components.forecast.workerthreads.listeners.ForecastUpdaterListener;
import biz.binarysolutions.weatherusa.util.InternetUtil;

/**
 * 
 *
 */
public class ForecastUpdater extends Thread {
	
	private static final String URI = 
		"https://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php";

	private static final String APP_URL =
		"https://play.google.com/store/apps/details?id=biz.binarysolutions.weatherusa";

	private static final String APP_EMAIL =
		"support+weatherusa@binarysolutions.biz";

	public static final String USER_AGENT =
		"WeatherForecastUSA/v3.x (" + APP_URL + "; " + APP_EMAIL + ")";

	private static final SimpleDateFormat sdf = 
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

	private final Location location;
	private final Handler  handler;
	
	private String forecast = "";

	/**
	 * 
	 * @param location
	 * @return
	 */
	private String getParameters(Location location) {
		
		String parameters = "";
		try {
			StringBuffer sb = new StringBuffer()
			
				.append("?whichClient=NDFDgen")
				.append("&lat=")
				.append(location.getLatitude())
				.append("&lon=")
				.append(location.getLongitude())
				.append("&product=time-series&begin=")
				.append(URLEncoder.encode(sdf.format(new Date()), "UTF-8"))
				
				.append("&maxt=maxt")	// Maximum Temperature
				.append("&mint=mint")	// Minimum Temperature
//				.append("&temp=temp")	// 3 Hourly Temperature
				.append("&dew=dew")		// Dewpoint Temperature
				.append("&appt=appt")	// Apparent Temperature
					
//				.append("&pop12=pop12")	// 12 Hour Probability of Precipitation
//				.append("&qpf=qpf")		// Liquid Precipitation Amount
//				.append("&snow=snow")	// Snowfall Amount
//				.append("&sky=sky")		// Cloud Cover Amount
//				.append("&rh=rh")		// Relative Humidity 
					
//				.append("&wspd=wspd")	// Wind Speed
//				.append("&wdir=wdir")	// Wind Direction
					
				.append("&wx=wx")		// Weather
				.append("&icons=icons")	// Weather Icons
					
//				.append("&waveh=waveh")	// Wave Height					
						
/*
			request.addProperty("incw34", "false");	// Probabilistic Tropical Cyclone Wind Speed >34 Knots (Incremental) 
			request.addProperty("incw50", "false");	// Probabilistic Tropical Cyclone Wind Speed >50 Knots (Incremental)
			request.addProperty("incw64", "false");	// Probabilistic Tropical Cyclone Wind Speed >64 Knots (Incremental)
			request.addProperty("cumw34", "false");	// Probabilistic Tropical Cyclone Wind Speed >34 Knots (Cumulative) 
			request.addProperty("cumw50", "false");	// Probabilistic Tropical Cyclone Wind Speed >50 Knots (Cumulative)
			request.addProperty("cumw64", "false");	// Probabilistic Tropical Cyclone Wind Speed >64 Knots (Cumulative) 
			
			request.addProperty("wgust", "false");	// Wind Gust
			
			request.addProperty("conhazo", "false");		// Convective Hazard Outlook
			
			request.addProperty("ptornado",     "false");	// Probability of Tornadoes
			request.addProperty("phail",        "false");	// Probability of Hail
			request.addProperty("ptstmwinds",   "false");	// Probability of Damaging Thunderstorm Winds
			request.addProperty("pxtornado",    "false");	// Probability of Extreme Tornadoes
			request.addProperty("pxhail",       "false");	// Probability of Extreme Hail 
			request.addProperty("pxtstmwinds",  "false");	// Probability of Extreme Thunderstorm Winds
			request.addProperty("ptotsvrtstm",  "false");	// Probability of Severe Thunderstorms 
			request.addProperty("pxtotsvrtstm", "false");	// Probability of Extreme Severe Thunderstorms
			
			request.addProperty("tmpabv14d", "false");	// Probability of 8- To 14-Day Average Temperature Above Normal
			request.addProperty("tmpblw14d", "false");	// Probability of 8- To 14-Day Average Temperature Below Normal
			request.addProperty("tmpabv30d", "false");	// Probability of One-Month Average Temperature Above Normal
			request.addProperty("tmpblw30d", "false");	// Probability of One-Month Average Temperature Below Normal
			request.addProperty("tmpabv90d", "false");	// Probability of Three-Month Average Temperature Above Normal
			request.addProperty("tmpblw90d", "false");	// Probability of Three-Month Average Temperature Below Normal
			
			request.addProperty("prcpabv14d", "false");	// Probability of 8- To 14-Day Total Precipitation Above Median
			request.addProperty("prcpblw14d", "false");	// Probability of 8- To 14-Day Total Precipitation Below Median
			request.addProperty("prcpabv30d", "false");	// Probability of One-Month Total Precipitation Above Median
			request.addProperty("prcpblw30d", "false");	// Probability of One-Month Total Precipitation Below Median
			request.addProperty("prcpabv90d", "false");	// Probability of Three-Month Total Precipitation Above Median 
			request.addProperty("prcpblw90d", "false");	// Probability of Three-Month Total Precipitation Below Median
			
			request.addProperty("precipa_r", "false");	// Real-time Mesoscale Analysis Precipitation
			request.addProperty("sky_r",     "false");	// Real-time Mesoscale Analysis GOES Effective Cloud Amount
			request.addProperty("td_r",      "false");	// Real-time Mesoscale Analysis Dewpoint Temperature
			request.addProperty("temp_r",    "false");	// Real-time Mesoscale Analysis Temperature 
			request.addProperty("wdir_r",    "false");	// Real-time Mesoscale Analysis Wind Direction
*/
				.append("&wwa=wwa")		// Watches, Warnings, and Advisories
/*
			request.addProperty("wspd_r", "false");		// Real-time Mesoscale Analysis Wind Speed
	
*/					
				.append("&Submit=Submit");
			
			parameters = sb.toString();
			
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
					
		return parameters;
	}

	/**
	 * 
	 * @param location
	 * @param dialog
	 * @param listener 
	 */
	public ForecastUpdater
		(
				Location location, 
				final ProgressDialog dialog, 
				final ForecastUpdaterListener listener
		) {
		super();

		this.location = location;
		this.handler  = new Handler() {
			
			@Override
			public void handleMessage(Message message) {
				
				try {
					dialog.dismiss();
				} catch (Exception e) {
					// TODO: handle orientation change
					/*
					 * Currently, if orientation changes during the
					 * forecast update, update (seems) aborted and no
					 * forecast is shown.
					 * 
					 * What would happen if forecast update is started
					 * as new activity?
					 */
				}
				
				if (message.what == MessageStatus.OK) {
					listener.onForecastAvailable(forecast);
				} else {
					listener.onConnectionError();
				}
			}
		};
	}

	@Override
	public void run() {
		
		String url = URI + getParameters(location);
		forecast = InternetUtil.getGetResponse(url, USER_AGENT);
		
		handler.sendEmptyMessage(MessageStatus.OK);
	}
}
