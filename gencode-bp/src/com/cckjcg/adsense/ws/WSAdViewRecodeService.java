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
import java.util.List;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * <p>Title: WSAdViewRecodeService</p> <p>Description:
 * 广告访问记录webservice服务和hessian服务接口. </p> <p>福建长潮科技</p> <p>Copyright (c)
 * 2015</p> @Date 2016-03-30 15:30:03 @author zouyaobin @version v1.0
 */
@WebService(name = "WSAdViewRecodeService", targetNamespace = "com.cckjcg.adsense.ws")
public interface WSAdViewRecodeService {
	public void addAdViewRecode(
			@WebParam(name = "adViewRecode", partName = "partAdViewRecode") AdViewRecode adViewRecode)
					throws AdViewRecodeException;
	public void deleteAdViewRecode(@WebParam(name = "id", partName = "partId") String id) throws AdViewRecodeException;
	public void deleteBatchAdViewRecode(@WebParam(name = "ids", partName = "partIds") String... ids)
			throws AdViewRecodeException;
	public void updateAdViewRecode(
			@WebParam(name = "adViewRecode", partName = "partAdViewRecode") AdViewRecode adViewRecode)
					throws AdViewRecodeException;
	public @WebResult(name = "adViewRecode", partName = "partAdViewRecode") AdViewRecode getAdViewRecode(
			@WebParam(name = "id", partName = "partId") String id) throws AdViewRecodeException;
	public @WebResult(name = "adViewRecodes", partName = "partAdViewRecodes") RListInfo queryListInfoAdViewRecodes(
			@WebParam(name = "conditions", partName = "partConditions") AdViewRecodeCondition conditions,
			@WebParam(name = "offset", partName = "partOffset") long offset,
			@WebParam(name = "pagesize", partName = "partPagesize") int pagesize) throws AdViewRecodeException;
	public @WebResult(name = "adViewRecodes", partName = "partAdViewRecodes") List<AdViewRecode> queryListAdViewRecodes(
			@WebParam(name = "conditions", partName = "partConditions") AdViewRecodeCondition conditions)
					throws AdViewRecodeException;
}