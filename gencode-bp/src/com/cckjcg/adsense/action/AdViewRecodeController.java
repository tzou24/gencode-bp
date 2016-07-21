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

package com.cckjcg.adsense.action;

import com.cckjcg.adsense.entity.*;
import com.frameworkset.util.ListInfo;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;
import com.frameworkset.util.StringUtil;
import com.cckjcg.adsense.service.*;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.MapKey;

/**
 * <p>Title: AdViewRecodeController</p> <p>Description: 广告访问记录管理控制器处理类 </p>
 * <p>福建长潮科技</p> <p>Copyright (c) 2007</p> @Date 2016-03-30 15:30:03 @author
 * zouyaobin @version v1.0
 */
public class AdViewRecodeController {

	private static Logger log = Logger.getLogger(AdViewRecodeController.class);

	private AdViewRecodeService adViewRecodeService;
	public @ResponseBody String addAdViewRecode(AdViewRecode adViewRecode) {
		// 控制器
		try {
			adViewRecodeService.addAdViewRecode(adViewRecode);
			return "success";
		} catch (AdViewRecodeException e) {
			log.error("add AdViewRecode failed:", e);
			return StringUtil.formatBRException(e);
		} catch (Throwable e) {
			log.error("add AdViewRecode failed:", e);
			return StringUtil.formatBRException(e);
		}

	}
	public @ResponseBody String deleteAdViewRecode(String id) {
		try {
			adViewRecodeService.deleteAdViewRecode(id);
			return "success";
		} catch (AdViewRecodeException e) {
			log.error("delete AdViewRecode failed:", e);
			return StringUtil.formatBRException(e);
		} catch (Throwable e) {
			log.error("delete AdViewRecode failed:", e);
			return StringUtil.formatBRException(e);
		}

	}
	public @ResponseBody String deleteBatchAdViewRecode(String... ids) {
		try {
			adViewRecodeService.deleteBatchAdViewRecode(ids);
			return "success";
		} catch (Throwable e) {
			log.error("delete Batch ids failed:", e);
			return StringUtil.formatBRException(e);
		}

	}
	public @ResponseBody String updateAdViewRecode(AdViewRecode adViewRecode) {
		try {
			adViewRecodeService.updateAdViewRecode(adViewRecode);
			return "success";
		} catch (Throwable e) {
			log.error("update AdViewRecode failed:", e);
			return StringUtil.formatBRException(e);
		}

	}
	public String getAdViewRecode(String id, ModelMap model) throws AdViewRecodeException {
		try {
			AdViewRecode adViewRecode = adViewRecodeService.getAdViewRecode(id);
			model.addAttribute("adViewRecode", adViewRecode);
			return "path:getAdViewRecode";
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("get AdViewRecode failed::id=" + id, e);
		}

	}
	public String queryListInfoAdViewRecodes(AdViewRecodeCondition conditions,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, ModelMap model)
					throws AdViewRecodeException {
		// Constant.component_type_actionimpl
		try {

			ListInfo adViewRecodes = adViewRecodeService.queryListInfoAdViewRecodes(conditions, offset, pagesize);
			model.addAttribute("adViewRecodes", adViewRecodes);
			return "path:queryListInfoAdViewRecodes";
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Exception e) {
			throw new AdViewRecodeException("pagine query AdViewRecode failed:", e);
		}

	}
	public String queryListAdViewRecodes(AdViewRecodeCondition conditions, ModelMap model)
			throws AdViewRecodeException {
		try {
			List<AdViewRecode> adViewRecodes = adViewRecodeService.queryListAdViewRecodes(conditions);
			model.addAttribute("adViewRecodes", adViewRecodes);
			return "path:queryListAdViewRecodes";
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Exception e) {
			throw new AdViewRecodeException("query AdViewRecode failed:", e);
		}

	}
	public String toUpdateAdViewRecode(String id, ModelMap model) throws AdViewRecodeException {
		try {
			AdViewRecode adViewRecode = adViewRecodeService.getAdViewRecode(id);
			model.addAttribute("adViewRecode", adViewRecode);
			return "path:updateAdViewRecode";
		} catch (AdViewRecodeException e) {
			throw e;
		} catch (Throwable e) {
			throw new AdViewRecodeException("get AdViewRecode failed::id=" + id, e);
		}

	}
	public String toAddAdViewRecode() {
		return "path:addAdViewRecode";
	}
	public String index() {
		return "path:index";

	}
}