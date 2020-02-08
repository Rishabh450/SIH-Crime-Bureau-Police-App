package com.sih.Utils;

import android.graphics.Color;
import android.media.Image;
import android.os.Build;

import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sih.policeapp.Activities.Weather;
import com.sih.policeapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.RequiresApi;


public class WeatherData  {
    LinearLayout layout;
    Weather activity;
    TextView current_time;
    TextView current_location;
    TextView current_phrase;
    // TextView hour_plus_1,hour_plus_2,hour_plus_3,hour_plus_4,hour_plus_5,hour_plus_6,hour_plus_7,hour_plus_8,hour_plus_9,hour_plus_10,hour_plus_11,hour_plus_12;
    //TextView hour_plus_1_temp,hour_plus_2_temp,hour_plus_3_temp,hour_plus_4_temp,hour_plus_5_temp,hour_plus_6_temp,hour_plus_7_temp,hour_plus_8_temp,hour_plus_9_temp,hour_plus_10_temp,hour_plus_11_temp,hour_plus_12_temp;
    //ImageView icon_plus_1,icon_plus_2,icon_plus_3,icon_plus_4,icon_plus_5,icon_plus_6,icon_plus_7,icon_plus_8,icon_plus_9,icon_plus_10,icon_plus_11,icon_plus_12;
    TextView day_plus_1,day_min_temp_plus_1,day_max_temp_plus_1,day_phrase_plus_1,day_plus_2,day_min_temp_plus_2,day_max_temp_plus_2,day_phrase_plus_2,day_plus_3,day_min_temp_plus_3,day_max_temp_plus_3,day_phrase_plus_3,day_plus_4,day_min_temp_plus_4,day_max_temp_plus_4,day_phrase_plus_4;
    ImageView icon_day_plus_1,icon_day_plus_2,icon_day_plus_3,icon_day_plus_4;
    TextView sunrise_time,sunset_time;
    ImageView sunrise_icon,sunset_icon,sunset_sunrise_icon,wind_icon,pressure_icon,visibility_icon,humidity_icon;
    TextView wind_direction,wind_speed;
    TextView pressure,visibilty,humidity;

    TextView[] hourlytime;
    ImageView[] icon_hourly;
    TextView[] temp_hourly;

    HashMap<Integer,Integer> icons;


    /* int[] icons= new int[]{R.drawable.icon1,R.drawable.icon2,R.drawable.icon3,R.drawable.icon4,R.drawable.icon5,R.drawable.icon6,
             R.drawable.icon7,R.drawable.icon8,R.drawable.icon11,R.drawable.icon12,R.drawable.icon13,R.drawable.icon14,R.drawable.icon15
             ,R.drawable.icon16,R.drawable.icon17,R.drawable.icon18,R.drawable.icon19,R.drawable.icon20,R.drawable.icon21,R.drawable.icon22
             ,R.drawable.icon23,R.drawable.icon24,R.drawable.icon25,R.drawable.icon26,R.drawable.icon29,R.drawable.icon30,R.drawable.icon31
             ,R.drawable.icon32,R.drawable.icon33,R.drawable.icon34,R.drawable.icon35,R.drawable.icon36,R.drawable.icon37,R.drawable.icon38,
             R.drawable.icon39,R.drawable.icon40,R.drawable.icon41,R.drawable.icon42,R.drawable.icon43,R.drawable.icon44};
      */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public WeatherData(Weather activity) throws NullPointerException, ParseException {

        icons=new HashMap<>();
        icons.put(1,R.drawable.icon1);
        icons.put(2, R.drawable.icon2);
        icons.put(3,R.drawable.icon3);
        icons.put(4,R.drawable.icon4);
        icons.put(5,R.drawable.icon5);
        icons.put(6,R.drawable.icon6);
        icons.put(7,R.drawable.icon7);
        icons.put(8,R.drawable.icon8);
        icons.put(11,R.drawable.icon11);
        icons.put(12,R.drawable.icon12);
        icons.put(13,R.drawable.icon13);
        icons.put(14,R.drawable.icon14);
        icons.put(15,R.drawable.icon15);
        icons.put(16,R.drawable.icon16);
        icons.put(17,R.drawable.icon17);
        icons.put(18,R.drawable.icon18);
        icons.put(19,R.drawable.icon19);
        icons.put(20,R.drawable.icon20);
        icons.put(21,R.drawable.icon21);
        icons.put(22,R.drawable.icon22);
        icons.put(23,R.drawable.icon23);
        icons.put(24,R.drawable.icon24);
        icons.put(25,R.drawable.icon25);
        icons.put(26,R.drawable.icon26);
        icons.put(29,R.drawable.icon29);
        icons.put(30,R.drawable.icon30);
        icons.put(31,R.drawable.icon31);
        icons.put(32,R.drawable.icon32);
        icons.put(33,R.drawable.icon33);
        icons.put(34,R.drawable.icon34);
        icons.put(35,R.drawable.icon35);
        icons.put(36,R.drawable.icon36);
        icons.put(37,R.drawable.icon37);
        icons.put(38,R.drawable.icon38);
        icons.put(39,R.drawable.icon39);
        icons.put(40,R.drawable.icon40);
        icons.put(41,R.drawable.icon41);
        icons.put(42,R.drawable.icon42);
        icons.put(43,R.drawable.icon43);
        icons.put(44,R.drawable.icon44);



        // View v=getLayoutInflater().inflate(R.layout.list,null,false);

        this.activity=activity;
        Date d=new Date();
        //String Time=String.valueOf(d.getTime());
        layout=activity.findViewById(R.id.layout);

        if (String.valueOf(activity.Current_IsDayTime).equals("true")) {
            layout.setBackgroundResource(R.drawable.sunshine5);


        }
        else
        {
            layout.setBackgroundResource(R.drawable.sunshine5);

        }

        if(activity.Current_TemperatureValue!=null) {
            TextView current_temp = (TextView) activity.findViewById(R.id.current_temp);
            current_temp.setText(activity.Current_TemperatureValue + "\u00b0");

            //Regex(activity.EffectiveDate);

           current_time = (TextView) activity.findViewById(R.id.current_time);
            current_time.setText("Last updated (" + activity.EffectiveDate + ")");


            current_location = (TextView) activity.findViewById(R.id.current_location);
            current_location.setText(activity.localizedName + ", " + activity.administrativeArea);

            current_phrase = (TextView) activity.findViewById(R.id.current_phrase);
            current_phrase.setText(activity.Current_WeatherText);
//HourlyTime
            hourlytime = new TextView[]{activity.findViewById(R.id.hour_plus_1), activity.findViewById(R.id.hour_plus_2), activity.findViewById(R.id.hour_plus_3), activity.findViewById(R.id.hour_plus_4), activity.findViewById(R.id.hour_plus_5), activity.findViewById(R.id.hour_plus_6),
                    activity.findViewById(R.id.hour_plus_7), activity.findViewById(R.id.hour_plus_8), activity.findViewById(R.id.hour_plus_9), activity.findViewById(R.id.hour_plus_10), activity.findViewById(R.id.hour_plus_11), activity.findViewById(R.id.hour_plus_12)};

//hourly time from array...
            for (int i = 0; i < 12; i++) {
                hourlytime[i].setText(Regex(activity.DateTime[i]));
            }
       /* hour_plus_1.setText(Regex(activity.DateTime[0]));
        hour_plus_2.setText(Regex(activity.DateTime[1]));
        hour_plus_3.setText(Regex(activity.DateTime[2]));
        hour_plus_4.setText(Regex(activity.DateTime[3]));
        hour_plus_5.setText(Regex(activity.DateTime[4]));
        hour_plus_6.setText(Regex(activity.DateTime[5]));
        hour_plus_7.setText(Regex(activity.DateTime[6]));
        hour_plus_8.setText(Regex(activity.DateTime[7]));
        hour_plus_9.setText(Regex(activity.DateTime[8]));
        hour_plus_10.setText(Regex(activity.DateTime[9]));
        hour_plus_11.setText(Regex(activity.DateTime[10]));
        hour_plus_12.setText(Regex(activity.DateTime[11]));
*/
//HourlyIcons....
            icon_hourly = new ImageView[]{activity.findViewById(R.id.icon_plus_1), activity.findViewById(R.id.icon_plus_2), activity.findViewById(R.id.icon_plus_3), activity.findViewById(R.id.icon_plus_4), activity.findViewById(R.id.icon_plus_5), activity.findViewById(R.id.icon_plus_6)
                    , activity.findViewById(R.id.icon_plus_7), activity.findViewById(R.id.icon_plus_8), activity.findViewById(R.id.icon_plus_9), activity.findViewById(R.id.icon_plus_10), activity.findViewById(R.id.icon_plus_11), activity.findViewById(R.id.icon_plus_12)};

            for (int k = 0; k < 12; k++) {

                icon_hourly[k].setBackgroundResource(icons.get(activity.HourlyIcon[k]));
            }

            //HourlyTemperature......
            temp_hourly = new TextView[]{activity.findViewById(R.id.hour_plus_1_temp), activity.findViewById(R.id.hour_plus_2_temp), activity.findViewById(R.id.hour_plus_3_temp), activity.findViewById(R.id.hour_plus_4_temp), activity.findViewById(R.id.hour_plus_5_temp), activity.findViewById(R.id.hour_plus_6_temp)
                    , activity.findViewById(R.id.hour_plus_7_temp), activity.findViewById(R.id.hour_plus_8_temp), activity.findViewById(R.id.hour_plus_9_temp), activity.findViewById(R.id.hour_plus_10_temp), activity.findViewById(R.id.hour_plus_11_temp), activity.findViewById(R.id.hour_plus_12_temp)};


            //putting values in hour data by using HourlyTempearature Array...and converting faraheniet into celsius...
            for (int j = 0; j < 12; j++) {
                temp_hourly[j].setText(String.valueOf(activity.HourlyTemperatureValue[j]) + "\u00b0");
            }


            //Days.....
            day_plus_1 = activity.findViewById(R.id.day_plus_1);
            day_plus_2 = activity.findViewById(R.id.day_plus_2);
            day_plus_3 = activity.findViewById(R.id.day_plus_3);
            day_plus_4 = activity.findViewById(R.id.day_plus_4);
            try {
                day_plus_1.setText(Week(activity.date1));
                day_plus_2.setText(Week(activity.date2));
                day_plus_3.setText(Week(activity.date3));
                day_plus_4.setText(Week(activity.date4));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            //Day Icons
            icon_day_plus_1 = activity.findViewById(R.id.icon_day_plus_1);
            icon_day_plus_2 = activity.findViewById(R.id.icon_day_plus_2);
            icon_day_plus_3 = activity.findViewById(R.id.icon_day_plus_3);
            icon_day_plus_4 = activity.findViewById(R.id.icon_day_plus_4);

            icon_day_plus_1.setBackgroundResource(icons.get(activity.DayIcon1));
            icon_day_plus_2.setBackgroundResource(icons.get(activity.DayIcon2));
            icon_day_plus_3.setBackgroundResource(icons.get(activity.DayIcon3));
            icon_day_plus_4.setBackgroundResource(icons.get(activity.DayIcon4));

            //Min Temperature of Diiferent Days.....
            day_min_temp_plus_1 = activity.findViewById(R.id.day_min_temp_plus_1);
            day_min_temp_plus_2 = activity.findViewById(R.id.day_min_temp_plus_2);
            day_min_temp_plus_3 = activity.findViewById(R.id.day_min_temp_plus_3);
            day_min_temp_plus_4 = activity.findViewById(R.id.day_min_temp_plus_4);

            //min temp for days
            day_min_temp_plus_1.setText(activity.TemperatureMin1 + "\u00b0");
            day_min_temp_plus_2.setText(activity.TemperatureMin2 + "\u00b0");
            day_min_temp_plus_3.setText(activity.TemperatureMin3 + "\u00b0");
            day_min_temp_plus_4.setText(activity.TemperatureMin4 + "\u00b0");


            //Max Temperature of Diiferent Days.....
            day_max_temp_plus_1 = activity.findViewById(R.id.day_max_temp_plus_1);
            day_max_temp_plus_2 = activity.findViewById(R.id.day_max_temp_plus_2);
            day_max_temp_plus_3 = activity.findViewById(R.id.day_max_temp_plus_3);
            day_max_temp_plus_4 = activity.findViewById(R.id.day_max_temp_plus_4);

            //max temp for days
            day_max_temp_plus_1.setText(activity.TemperatureMax1 + "\u00b0");
            day_max_temp_plus_2.setText(activity.TemperatureMax2 + "\u00b0");
            day_max_temp_plus_3.setText(activity.TemperatureMax3 + "\u00b0");
            day_max_temp_plus_4.setText(activity.TemperatureMax4 + "\u00b0");


            //Phrases of Different Days.........
            day_phrase_plus_1 = activity.findViewById(R.id.day_phrase_plus_1);
            day_phrase_plus_2 = activity.findViewById(R.id.day_phrase_plus_2);
            day_phrase_plus_3 = activity.findViewById(R.id.day_phrase_plus_3);
            day_phrase_plus_4 = activity.findViewById(R.id.day_phrase_plus_4);

            day_phrase_plus_1.setText(activity.DayIconPhrase1);
            day_phrase_plus_2.setText(activity.DayIconPhrase2);
            day_phrase_plus_3.setText(activity.DayIconPhrase3);
            day_phrase_plus_4.setText(activity.DayIconPhrase4);


            //Sunset & Sunrise..........
            sunrise_time = activity.findViewById(R.id.sunrise_time);
            sunset_time = activity.findViewById(R.id.sunset_time);
            sunrise_icon = activity.findViewById(R.id.sunrise_icon);
            sunset_icon = activity.findViewById(R.id.sunset_icon);
            sunset_sunrise_icon = activity.findViewById(R.id.sunset_sunrise_icon);

            //setting sunrise time and sunset time......image should be static...
            sunrise_time.setText(Regex(activity.SunRise));
            sunset_time.setText(Regex(activity.SunSet));


            //Wind Speed & Direction............
            wind_icon = activity.findViewById(R.id.wind_icon);
            wind_direction = activity.findViewById(R.id.wind_direction);
            wind_speed = activity.findViewById(R.id.wind_speed);

            //setting up wind speed and wind direction.......
            wind_speed.setText(activity.CurrentWindSpeed + " " + activity.CurrentWindSpeed_Unit);
            wind_direction.setText(activity.CurrentWindDirection + " " + activity.CurrentWindDirection_Unit);


            //Pressure......
            pressure = activity.findViewById(R.id.pressure);
            pressure_icon = activity.findViewById(R.id.pressure_icon);
            pressure.setText(activity.Current_Pressure + activity.Current_Prerssure_Unit);


            //Visibility........
            visibilty = activity.findViewById(R.id.visibility);
            visibility_icon = activity.findViewById(R.id.visibility_icon);
            visibilty.setText(activity.Current_Visibility + activity.Current_Visibility_Unit);

            //Humidity.........
            humidity = activity.findViewById(R.id.humidity);
            humidity_icon = activity.findViewById(R.id.humidity_icon);
            humidity.setText(activity.Current_Humidity + activity.Current_Humidity_Unit);

        }
    }

    private String Regex(String value)
    {   int i=0;
        int j = 0;
        String result = null;
        //2018-03-30T07:00:00+05:30
        if(value!=null){
            for(int n=0;n<value.length();n++)
            {
                if((value.charAt(n)=='T'))
                {
                    i=n;
                }
                if((value.charAt(n)=='+'))
                {
                    j=n;
                }
            }
            final String time =value.substring(i+1,j-2);

            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(time);
                System.out.println(dateObj);
                result=(new SimpleDateFormat("K:mm:a").format(dateObj));
            } catch (final ParseException e) {
                e.printStackTrace();
            }


            return result;
        }
        else
        {return " ";}
    }

    public String Week(String date) throws ParseException {
        if(date!=null)
        {
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
            Date dt1=format1.parse(date);
            DateFormat format2=new SimpleDateFormat("EEEE");
            return format2.format(dt1).substring(0,3);
        }
        else{
            return " ";
        }
    }



    /* CONVERSION OF TEMP FROM F TO C
    public String FtoC(int f)
    {
        int C = ((f - 32)*5)/9;
        return String.valueOf(C);
    }
    */
}