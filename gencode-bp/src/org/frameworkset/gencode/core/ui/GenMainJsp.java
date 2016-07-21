package org.frameworkset.gencode.core.ui;

import java.io.File;

import org.frameworkset.gencode.core.AbstractGencode;
import org.frameworkset.gencode.core.GencodeServiceImpl;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.VelocityUtil;

public class GenMainJsp  extends AbstractGencode{

	public GenMainJsp(GencodeServiceImpl gencodeService) {
		super(gencodeService);
	}
	public void gen()
	{
		File conf = gencodeService.getMainJsp();
		if(conf.exists())
		{
			conf.delete();
		}
		try {
			conf.createNewFile();
			 Template conftempalte = VelocityUtil.getTemplate("/gencode/ui/"+gencodeService.getTheme()+"/main.vm");
			 VelocityContext context = new VelocityContext();
			
			 
			 context.put("company", gencodeService.getModuleMetaInfo().getCompany());
			 context.put("gendate", gencodeService.getModuleMetaInfo().getDate());
			 context.put("author", gencodeService.getModuleMetaInfo().getAuthor());
			 context.put("version", gencodeService.getModuleMetaInfo().getVersion());
			 
			 context.put("conditions", gencodeService.getConditions());
			 
			 context.put("moduleName", gencodeService.getModuleMetaInfo().getModuleName());
			 context.put("moduleCNName", gencodeService.getModuleMetaInfo().getModuleCNName());
			 context.put("entityVarName", gencodeService.getEntityParamName());
			 context.put("entityName", gencodeService.getEntityName());
			 context.put("primaryKeyName", gencodeService.getPrimaryKeyName());
			 context.put("relativePath", gencodeService.getRelativePath());
			 context.put("genI18n", gencodeService.isGenI18n());
			 context.put("namespacei18n", gencodeService.getNamespacei18n());
			 context.put("needDateComponent", gencodeService.isNeedDateComponent());
			 context.put("needcondition", gencodeService.needcondition());
			 gencodeService.writFile(context,conftempalte,conf,gencodeService.getModuleMetaInfo().getEncodecharset());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
