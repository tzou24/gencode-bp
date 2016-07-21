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

/**
 * <p>Title: AdViewRecodeCondition</p> <p>Description: 广告访问记录管理查询条件实体类 </p>
 * <p>福建长潮科技</p> <p>Copyright (c) 2007</p> @Date 2016-03-30 15:30:03 @author
 * zouyaobin @version v1.0
 */
public class AdViewRecodeCondition implements java.io.Serializable {
	/**
	 * 关键词
	 */
	private String keyWord;
	private String markAddress;
	/**
	 * 用户操作系统
	 */
	private String optionSys;
	public AdViewRecodeCondition() {
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

	public void setOptionSys(String optionSys) {
		this.optionSys = optionSys;
	}

	public String getOptionSys() {
		return optionSys;
	}

}