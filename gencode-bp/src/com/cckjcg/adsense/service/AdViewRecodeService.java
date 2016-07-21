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

package com.cckjcg.adsense.service;

import com.cckjcg.adsense.entity.*;
import com.frameworkset.util.ListInfo;
import java.util.List;

/**
 * <p>Title: AdViewRecodeService</p> <p>Description: 广告访问记录管理服务接口 </p>
 * <p>福建长潮科技</p> <p>Copyright (c) 2015</p> @Date 2016-03-30 15:30:03 @author
 * zouyaobin @version v1.0
 */
public interface AdViewRecodeService {
	public void addAdViewRecode(AdViewRecode adViewRecode) throws AdViewRecodeException;
	public void deleteAdViewRecode(String id) throws AdViewRecodeException;
	public void deleteBatchAdViewRecode(String... ids) throws AdViewRecodeException;
	public void updateAdViewRecode(AdViewRecode adViewRecode) throws AdViewRecodeException;
	public AdViewRecode getAdViewRecode(String id) throws AdViewRecodeException;
	public ListInfo queryListInfoAdViewRecodes(AdViewRecodeCondition conditions, long offset, int pagesize)
			throws AdViewRecodeException;
	public List<AdViewRecode> queryListAdViewRecodes(AdViewRecodeCondition conditions) throws AdViewRecodeException;
}