package com.quanmin.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quanmin.test.PlayerVipDao;
@Component
public class ServerInitManager {
	@Autowired
	PlayerVipDao pvd;
	
	public void init(){
		pvd.deleteById(1);
		pvd.getVipRemainingTimes(1);
	}
}
