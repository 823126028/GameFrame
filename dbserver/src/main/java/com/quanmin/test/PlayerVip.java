package com.quanmin.test;

import com.quanmin.mybatis.wrapper.IModel;

public class PlayerVip
  implements IModel
{
  private static final long serialVersionUID = 1L;
  private Integer vId;
  private Integer playerId;
  private String vipStatus;
  private String vipRemainingTimes;

  public Integer getVId()
  {
    return this.vId;
  }

  public void setVId(Integer vId) {
    this.vId = vId;
  }

  public Integer getPlayerId() {
    return this.playerId;
  }

  public void setPlayerId(Integer playerId) {
    this.playerId = playerId;
  }

  public String getVipStatus() {
    return this.vipStatus;
  }

  public void setVipStatus(String vipStatus) {
    this.vipStatus = vipStatus;
  }

  public String getVipRemainingTimes() {
    return this.vipRemainingTimes;
  }

  public void setVipRemainingTimes(String vipRemainingTimes) {
    this.vipRemainingTimes = vipRemainingTimes;
  }
}