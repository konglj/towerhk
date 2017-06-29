package com.tower.common.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.tower.common.Config;
import com.tower.common.util.ParamerUtil;
import com.tower.mapper.TowersMapper;
import com.tower.service.OrderService;
import com.tower.service.TowerService;

@Component
public class TaskCancel {

	@Autowired
	private OrderService orderService;

	@Autowired
	private TowerService towerService;

	@Scheduled(cron = "0/60 * * * * ? ")
	public void task() {
		List<Map> orders = orderService.getCheckCancelOrder();
		if (orders != null && orders.size() > 0) {
			for (Map map : orders) {
				orderService.updateOrderCancel(map);
			}

		}

		System.out.println("111");
	}

	@Scheduled(cron = "0 0 12 * * ?")
	public void getUnOrderTower() {
		try {

			List<Map> towers = towerService.getUnOrderTower();
			if (towers != null && towers.size() > 0) {
				for (Map map : towers) {

					
					String time="";
					if (map.get("lastordertime") == null) {
						String visitime = map.get("towervisibletime")==null?null:map.get("towervisibletime").toString();
						// 判读日志
						time=visitime;
						

					} else {
						time=map.get("lastordertime").toString();

					}
					if(ParamerUtil.isUpdateTowerLevel(time)){
					   Map mapLeve=new HashMap();
					   mapLeve.put("towerid", map.get("towerid"));
					   mapLeve.put("towerlevel", Config.jjtowerlevel);
					   int count=towerService.updateTowerLevel(mapLeve);
					}
				
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("22");
	}

	
	@Scheduled(cron = "0 0 12 * * ?")
	public void getPosterOrderUndo() {
		try {

			List<Map> orders = orderService.getCheckPostUnDo(Config.postundoday);
			if (orders != null && orders.size() > 0) {
				for (Map map : orders) {
					try {
						
						orderService.updateSendMessage(map);
						
					} catch (Exception e) {
						e.printStackTrace();
					
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("33");
	}

}
