package com.tower.common.bean;

public class MapPoint {

	private int baidu;
	
	private double lat;// 纬度
	
	private double lng;// 经度

	
	public MapPoint() {
	}

	public MapPoint(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MapPoint) {
			MapPoint bmapPoint = (MapPoint) obj;
			return (bmapPoint.getLng() == lng && bmapPoint.getLat() == lat) ? true
					: false;
		} else {
			return false;
		}
	}

	
	public int getBaidu() {
		return baidu;
	}

	public void setBaidu(int baidu) {
		this.baidu = baidu;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Point [lat=" + lat + ", lng=" + lng + "]";
	}
}
