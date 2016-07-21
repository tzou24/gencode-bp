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

package com.cckjcg.adsense.ws;

import com.cckjcg.adsense.entity.*;
import com.cckjcg.adsense.service.*;
import com.frameworkset.util.RListInfo;
import com.frameworkset.util.ListInfo;
import java.util.List;
import javax.jws.WebService;

/**
 * <p>Title: WSAdViewRecodeServiceImpl</p> <p>Description:
 * 广告访问记录webservice服务和hessian服务实现类. </p> <p>福建长潮科技</p> <p>Copyright (c)
 * 2007</p> @Date 2016-03-30 15:30:03 @author zouyaobin @version v1.0
 */
@WebService(name = "WSAdViewRecodeService", targetNamespace = "com.cckjcg.adsense.ws")
public class WSAdViewRecodeServiceImpl implements WSAdViewRecodeService {

	private AdViewRecodeService adViewRecodeService;
	public void addAdViewRecode(AdViewRecode adViewRecode) throws AdViewRecodeException {
		// webservice服务
		try {
			adViewRecodeService.addAdViewRecode(adViewRecode);
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("add AdViewRecode failed:", e);
		}

	}
	public void deleteAdViewRecode(String id) throws AdViewRecodeException {
		// webservice服务
		try {
			adViewRecodeService.deleteAdViewRecode(id);
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("delete AdViewRecode failed::id=" + id, e);
		}

	}
	public void deleteBatchAdViewRecode(String... ids) throws AdViewRecodeException {
		// webservice服务
		try {
			adViewRecodeService.deleteBatchAdViewRecode(ids);
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("batch delete AdViewRecode failed::ids=" + ids, e);
		}

	}
	public void updateAdViewRecode(AdViewRecode adViewRecode) throws AdViewRecodeException {
		// webservice服务
		try {
			adViewRecodeService.updateAdViewRecode(adViewRecode);

		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("update AdViewRecode failed::", e);
		}

	}
	public AdViewRecode getAdViewRecode(String id) throws AdViewRecodeException {
		// webservice服务
		try {
			AdViewRecode adViewRecode = adViewRecodeService.getAdViewRecode(id);
			return adViewRecode;
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("get AdViewRecode failed::id=" + id, e);
		}

	}
	public RListInfo queryListInfoAdViewRecodes(AdViewRecodeCondition conditions, long offset, int pagesize)
			throws AdViewRecodeException {
		// webservice服务
		try {

			ListInfo adViewRecodes = adViewRecodeService.queryListInfoAdViewRecodes(conditions, offset, pagesize);
			return new RListInfo(adViewRecodes);
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("pagine query AdViewRecode failed:", e);
		}

	}
	public List<AdViewRecode> queryListAdViewRecodes(AdViewRecodeCondition conditions) throws AdViewRecodeException {
		// webservice服务
		try {
			List<AdViewRecode> adViewRecodes = adViewRecodeService.queryListAdViewRecodes(conditions);
			return adViewRecodes;
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("query AdViewRecode failed:", e);
		}

	}
}