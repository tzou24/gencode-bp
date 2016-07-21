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
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import org.apache.log4j.Logger;
import java.util.List;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * <p>Title: AdViewRecodeServiceImpl</p> <p>Description: 广告访问记录管理业务处理类 </p>
 * <p>福建长潮科技</p> <p>Copyright (c) 2007</p> @Date 2016-03-30 15:30:03 @author
 * zouyaobin @version v1.0
 */
public class AdViewRecodeServiceImpl implements AdViewRecodeService {

	private static Logger log = Logger.getLogger(com.cckjcg.adsense.service.AdViewRecodeServiceImpl.class);

	private ConfigSQLExecutor executor;
	public void addAdViewRecode(AdViewRecode adViewRecode) throws AdViewRecodeException {
		// 业务组件
		try {
			executor.insertBean("addAdViewRecode", adViewRecode);
		} catch (Throwable e) {
			throw new AdViewRecodeException("add AdViewRecode failed:", e);
		}

	}
	public void deleteAdViewRecode(String id) throws AdViewRecodeException {
		try {
			executor.delete("deleteByKey", id);
		} catch (Throwable e) {
			throw new AdViewRecodeException("delete AdViewRecode failed::id=" + id, e);
		}

	}
	public void deleteBatchAdViewRecode(String... ids) throws AdViewRecodeException {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteByKeys("deleteByKey", ids);
			tm.commit();
		} catch (Throwable e) {

			throw new AdViewRecodeException("batch delete AdViewRecode failed::ids=" + ids, e);
		} finally {
			tm.release();
		}

	}
	public void updateAdViewRecode(AdViewRecode adViewRecode) throws AdViewRecodeException {
		try {
			executor.updateBean("updateAdViewRecode", adViewRecode);
		} catch (Throwable e) {
			throw new AdViewRecodeException("update AdViewRecode failed::", e);
		}

	}
	public AdViewRecode getAdViewRecode(String id) throws AdViewRecodeException {
		try {
			AdViewRecode bean = executor.queryObject(AdViewRecode.class, "selectById", id);
			return bean;
		} catch (Throwable e) {
			throw new AdViewRecodeException("get AdViewRecode failed::id=" + id, e);
		}

	}
	public ListInfo queryListInfoAdViewRecodes(AdViewRecodeCondition conditions, long offset, int pagesize)
			throws AdViewRecodeException {
		ListInfo datas = null;
		try {
			datas = executor.queryListInfoBean(AdViewRecode.class, "queryListAdViewRecode", offset, pagesize,
					conditions);
		} catch (Exception e) {
			throw new AdViewRecodeException("pagine query AdViewRecode failed:", e);
		}
		return datas;

	}
	public List<AdViewRecode> queryListAdViewRecodes(AdViewRecodeCondition conditions) throws AdViewRecodeException {
		try {
			List<AdViewRecode> beans = executor.queryListBean(AdViewRecode.class, "queryListAdViewRecode", conditions);
			return beans;
		} catch (Exception e) {
			throw new AdViewRecodeException("query AdViewRecode failed:", e);
		}

	}
}