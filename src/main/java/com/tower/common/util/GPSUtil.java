package com.tower.common.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.tower.common.bean.MapPoint;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class GPSUtil {

	private static String ak = "nrLvZh6mP1XLZ4Hne6BujGlV5212ZGeQ";

	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	private static final double pi = 3.14159265358979324;
	private static final double a = 6378245.0;
	private static final double ee = 0.00669342162296594323;

	/**
	 * @param args
	 */
	public static Map getAddressByGps(MapPoint mapPoint) {
		Map map=new HashMap();
		if (mapPoint.getBaidu() == 0) {
			mapPoint = google_bd_encrypt(mapPoint.getLat(),
					mapPoint.getLng());
		}
		String url = String
				.format("http://api.map.baidu.com/geocoder/v2/?ak=%s&callback=renderReverse&location=%s,%s&output=json&pois=0",
						ak, mapPoint.getLat(), mapPoint.getLng());
		String str = HttpRequst.sendGet(url);
		System.out.println(str);
		String sematic_description = null;

		String temp = "{\"result\":[{" + str.substring(51, str.length() - 2)
				+ "]}";
		try {
			JSONObject jo =JSONObject.fromObject(temp);
			JSONArray jsonArray = (JSONArray) jo.get("result");
			JSONObject lo=jsonArray.getJSONObject(0);
			JSONObject address=lo.getJSONObject("addressComponent");
			String province=address.getString("province");
			String city=address.getString("city");
			String district=address.getString("district");
			map.put("province", province);
			map.put("cityname",city);
			map.put("areaname", district);
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (sematic_description != null) {
		}

		return map;
	}

	/**
	 * gg_lat 纬度 gg_lon 经度 GCJ-02转换BD-09 Google地图经纬度转百度地图经纬度
	 * */
	public static MapPoint google_bd_encrypt(double gg_lat, double gg_lon) {
		MapPoint point = new MapPoint();
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		point.setLat(bd_lat);
		point.setLng(bd_lon);
		return point;
	}
	
	 /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     * 
     * @param gg_lat
     * @param gg_lon
     */
    public static MapPoint gcj02_To_Bd09(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        MapPoint point = new MapPoint();
    	point.setLat(bd_lat);
		point.setLng(bd_lon);
        return point;
    }
    
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     * 
     * @param gg_lat
     * @param gg_lon
     */
	public static MapPoint GpsToBaidu(String gg_lat, String gg_lon) {
    	String url="http://api.gpsspg.com/convert/latlng/?oid=2277&key=E06A7FFE259BB29CD2918F99C4114816&from=0&to=2&latlng="+gg_lat+","+gg_lon;
        String result=HttpRequst.sendGet(url);
       if(result==null||result.equals("")){
    	   return null;
       }
       JSONObject object=JSONObject.fromObject(result);
       if(!object.get("status").equals("200"))
    	   return null;
       JSONArray results=object.getJSONArray("result");
       MapPoint mapPoint=new MapPoint();
       mapPoint.setLat(results.getJSONObject(0).getDouble("lat"));
       mapPoint.setLng(results.getJSONObject(0).getDouble("lng"));
        return mapPoint;
    }

	 /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     * 
     * @param gg_lat
     * @param gg_lon
     */
	public static List<MapPoint> GpsToBaidu(List<Map> list) {
		
    	String url="http://api.gpsspg.com/convert/latlng/?oid=2277&key=E06A7FFE259BB29CD2918F99C4114816&from=0&to=2&latlng=";
    	
    	for (Map map : list) {
    		url+=map.get("gg_lat").toString();
    		url+=",";
    		url+=map.get("gg_lon").toString();
    		url+=";";
		}
    	
    	if(url.endsWith(";"))
    		url=url.substring(0,url.length()-1);
        String result=HttpRequst.sendGet(url);
       if(result==null||result.equals("")){
    	   return null;
       }
       JSONObject object=JSONObject.fromObject(result);
       if(!object.get("status").equals("200"))
    	   return null;
       JSONArray results=object.getJSONArray("result");
       List<MapPoint> poits=new ArrayList<MapPoint>();
       for (int i=0;i<results.size();i++) {
    	   MapPoint mapPoint=new MapPoint();
    	   mapPoint.setLat(results.getJSONObject(i).getDouble("lat"));
           mapPoint.setLng(results.getJSONObject(i).getDouble("lng"));
           poits.add(mapPoint);
       	}
        return poits;
    }


}