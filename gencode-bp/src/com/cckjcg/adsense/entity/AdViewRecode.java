/**
 *  Copyright 2008-2010 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cckjcg.adsense.entity;

import com.frameworkset.orm.annotation.PrimaryKey;
import java.sql.Timestamp;
import org.frameworkset.util.annotations.RequestParam;
/**
 * <p>Title: AdViewRecode</p> <p>Description: 广告访问记录管理服务实体类 </p> <p>福建长潮科技</p>
 * <p>Copyright (c) 2007</p> @Date 2016-03-30 15:30:03 @author
 * zouyaobin @version v1.0
 */
public class AdViewRecode implements java.io.Serializable {
	@PrimaryKey
	private String id;
	/**
	 * 广告内容
	 */
	private String adContentId;
	/**
	 * 广告位
	 */
	private String adsenseId;
	private String areaCity;
	/**
	 * 用户所在区域
	 */
	private String areaProvince;
	/**
	 * 浏览器
	 */
	private String browser;
	/**
	 * 用户cookies
	 */
	private String cookie;
	private String deliveryId;
	/**
	 * 上网设备
	 */
	private String device;
	/**
	 * 关键词
	 */
	private String keyWord;
	private String markAddress;
	/**
	 * 网络服务提供商
	 */
	private String netService;
	/**
	 * 用户操作系统
	 */
	private String optionSys;
	/**
	 * 用户手机号码
	 */
	private String phoneNum;
	/**
	 * 屏幕分辨率
	 */
	private String screenRes;
	/**
	 * 离开时间
	 */
	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp viewEDate;
	/**
	 * 访问端IP
	 */
	private String viewIp;
	/**
	 * 访问来源
	 */
	private String viewResouce;
	/**
	 * 访问时间
	 */
	@RequestParam(dateformat = "yyyy-MM-dd HH:mm:ss")
	private Timestamp viewSDate;
	public AdViewRecode() {
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAdContentId(String adContentId) {
		this.adContentId = adContentId;
	}

	public String getAdContentId() {
		return adContentId;
	}

	public void setAdsenseId(String adsenseId) {
		this.adsenseId = adsenseId;
	}

	public String getAdsenseId() {
		return adsenseId;
	}

	public void setAreaCity(String areaCity) {
		this.areaCity = areaCity;
	}

	public String getAreaCity() {
		return areaCity;
	}

	public void setAreaProvince(String areaProvince) {
		this.areaProvince = areaProvince;
	}

	public String getAreaProvince() {
		return areaProvince;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowser() {
		return browser;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return device;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setMarkAddress(String markAddress) {
		this.markAddress = markAddress;
	}

	public String getMarkAddress() {
		return markAddress;
	}

	public void setNetService(String netService) {
		this.netService = netService;
	}

	public String getNetService() {
		return netService;
	}

	public void setOptionSys(String optionSys) {
		this.optionSys = optionSys;
	}

	public String getOptionSys() {
		return optionSys;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setScreenRes(String screenRes) {
		this.screenRes = screenRes;
	}

	public String getScreenRes() {
		return screenRes;
	}

	public void setViewEDate(Timestamp viewEDate) {
		this.viewEDate = viewEDate;
	}

	public Timestamp getViewEDate() {
		return viewEDate;
	}

	public void setViewIp(String viewIp) {
		this.viewIp = viewIp;
	}

	public String getViewIp() {
		return viewIp;
	}

	public void setViewResouce(String viewResouce) {
		this.viewResouce = viewResouce;
	}

	public String getViewResouce() {
		return viewResouce;
	}

	public void setViewSDate(Timestamp viewSDate) {
		this.viewSDate = viewSDate;
	}

	public Timestamp getViewSDate() {
		return viewSDate;
	}

}