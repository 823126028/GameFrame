package com.quanmin.test;

import java.util.List;
import org.springframework.stereotype.Component;

import com.quanmin.mybatis.wrapper.BaseDao;
import com.quanmin.mybatis.wrapper.Params;

@Component("playerVipDao")
public class PlayerVipDao extends BaseDao<PlayerVip>
{
  public PlayerVip read(int vId)
  {
    return (PlayerVip)getSqlSession().selectOne("com.reign.gcld.pay.domain.PlayerVip.read", Integer.valueOf(vId));
  }

  public PlayerVip readForUpdate(int vId)
  {
    return (PlayerVip)getSqlSession().selectOne("com.quanmin.test.PlayerVip.readForUpdate", Integer.valueOf(vId));
  }

  public List<PlayerVip> getModels()
  {
    return getSqlSession().selectList("com.quanmin.test.PlayerVip.getModels");
  }

  public int getModelSize()
  {
    return ((Integer)getSqlSession().selectOne("com.quanmin.test.PlayerVip.getModelSize")).intValue();
  }

  public int create(PlayerVip playerVip)
  {
    return getSqlSession().insert("com.quanmin.test.PlayerVip.create", playerVip);
  }

  public int deleteById(int vId)
  {
    return getSqlSession().delete("com.quanmin.test.PlayerVip.deleteById", Integer.valueOf(vId));
  }

  public String getVipStatus(int playerId)
  {
    return (String)getSqlSession().selectOne("com.quanmin.test.PlayerVip.getVipStatus", Integer.valueOf(playerId));
  }

  public int setVipStatus(int playerId, String vipStatus)
  {
    Params params = new Params();
    params.addParam("playerId", Integer.valueOf(playerId));
    params.addParam("vipStatus", vipStatus);
    return getSqlSession().update("com.quanmin.test.PlayerVip.setVipStatus", params);
  }

  public String getVipRemainingTimes(int playerId)
  {
    return (String)getSqlSession().selectOne("com.quanmin.test.PlayerVip.getVipRemainingTimes", Integer.valueOf(playerId));
  }

  public int setVipRemainingTimes(int playerId, String remainingTimes)
  {
    Params params = new Params();
    params.addParam("playerId", Integer.valueOf(playerId));
    params.addParam("remainingTimes", remainingTimes);
    return getSqlSession().update("com.quanmin.test.PlayerVip.setVipRemainingTimes", params);
  }
}