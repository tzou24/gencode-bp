package org.frameworkset.gencode.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.formatter.CodeFormatterApplication;
import org.frameworkset.gencode.core.ui.GenAddJsp;
import org.frameworkset.gencode.core.ui.GenI8N;
import org.frameworkset.gencode.core.ui.GenListInfoJsp;
import org.frameworkset.gencode.core.ui.GenListJsp;
import org.frameworkset.gencode.core.ui.GenMainJsp;
import org.frameworkset.gencode.core.ui.GenUpdateJsp;
import org.frameworkset.gencode.core.ui.GenViewJsp;
import org.frameworkset.gencode.entity.AnnoParam;
import org.frameworkset.gencode.entity.Annotation;
import org.frameworkset.gencode.entity.ConditionField;
import org.frameworkset.gencode.entity.ControlInfo;
import org.frameworkset.gencode.entity.ControlParam;
import org.frameworkset.gencode.entity.Field;
import org.frameworkset.gencode.entity.Method;
import org.frameworkset.gencode.entity.MethodParam;
import org.frameworkset.gencode.entity.ModuleMetaInfo;
import org.frameworkset.gencode.entity.SortField;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.orm.engine.EngineException;
import com.frameworkset.orm.engine.model.NameFactory;
import com.frameworkset.orm.engine.model.NameGenerator;
import com.frameworkset.util.FileUtil;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.VelocityUtil;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

/**
 * 生成代码业务组件
 * @author yinbp
 *
 */
public class GencodeServiceImpl {
	private static final Logger log = Logger.getLogger(GencodeServiceImpl.class);
	public static String DEFAULT_SOURCEPATH;
	public static String SQLITEPATH;
	private ControlInfo controlInfo ;
	/**
	 * 代码存放目录
	 */
	private File rootdir;
	private File resourcedir;
	private File javaSourceDir;
	private File javaEntiySourceDir;
	private File javaWSSourceDir;
	private File javaServiceSourceDir;
	private File javaActionSourceDir;
	private File jspSourceDir;
	private String relativePath;//jsp访问相对地址
	private String namespacei18n;
	private File mvcConfDir;
	private File mvcconf ;
	private File i18nzh_CN ;
	private File i18nen_US ;
	private File readme ;
	private File mainJsp ;
	private File listJsp ;
	private File listInfoJsp ;
	private File addJsp ;
	private File updateJsp;
	private File viewJsp;
	private File webxmlFile;
	private String jsppath;
	private ModuleMetaInfo moduleMetaInfo;
	private String exceptionName;
	private String entityName;
	private String entityParamName;
	private String conditionEntityName;
	private String conditionEntityParamName;
	private String sqlfilepath;
	private SQLBuilder SQLBuilder ; 
	private List<Field> sortFields;
	
	private Field primaryField ;
	private Field defaultSortField ;
	private String primaryKeyName;
	private String modulePackage ;
	private String javamodulePackage ;
	private String serviceClass;
	private String serviceInfType;
	private String serviceParamName;
	private String controlClass;
	private boolean pagineWithDBRownumberOver = false;
	/**
	 * 需要作为查询条件的字段
	 */
	private List<Field> conditions;
	
	/**
	 * 列表中出现的字段
	 */
	private List<Field> listShowFields;
	private List<Field> listHiddenFields;
	
	private List<Field> viewShowFields;
	private List<Field> viewHiddenFields;
	
	private List<Field> editShowFields;
	private List<Field> editHiddenFields;
	
	private List<Field> addShowFields;
	private List<Field> addHiddenFields;
	private List<SQL> sqls;
	/**
	 * ui界面上是否需要引入日期控件
	 */
	private boolean needDateComponent;
	/**
	 * 所有字段
	 */
//	private List<Field> allfields;
	
	private List<Field> entityFields;
	private ControlParam controlParam;
	private boolean genfromWeb;
	public GencodeServiceImpl()
	{
		this(false);
	}
	
	public GencodeServiceImpl(boolean genfromWeb)
	{
		controlParam = new ControlParam();
		this.genfromWeb = genfromWeb;
	}
	public void setTheme(String theme) {
		this.controlParam.setTheme(theme);
	}
	
	public String getTheme() {
		return controlParam.getTheme();
	}
	
	public String genCode()
	{
//		this.moduleMetaInfo = moduleMetaInfo; 
		
		this.init();
		genJavaSource();
		genPersistentConfigfile();
		genUI();
		 genMVCConf();
		 if(this.controlParam.isGenI18n())
		 {
			 genI18N();
		 }
		 genReadme();
		 formatcode();
		 genzipfile();
		 
		return "success";
	}
	
	private void formatcode()  
	{
		String path = "";
		 try {
			 path = new File(this.moduleMetaInfo.getSourcedir(), this.moduleMetaInfo.getModuleName()).getCanonicalPath();
			 CodeFormatterApplication c = new CodeFormatterApplication();
				c.starts(path);
		} catch (Exception e) {
			log.error("format java code in path["+path+"] failed：",e);
		}
	}
	private void genzipfile()
	{
		 File zip = new File(this.moduleMetaInfo.getSourcedir(), this.moduleMetaInfo.getModuleName() + ".zip");
			if(zip.exists())
				zip.delete();
			FileUtil.zip(new File(this.moduleMetaInfo.getSourcedir(), this.moduleMetaInfo.getModuleName()), zip);
	}
	
	private void genI18N()
	{
		GenI8N genI8N = new GenI8N(this,false);//en_US
		genI8N.gen();
		genI8N = new GenI8N(this,true);//zh_CN
		genI8N.gen();
	}
	
	/**
	 * 生成前端jsp ui界面
	 */
	private void genUI()
	{
		this.mainJsp = new File(jspSourceDir,"main.jsp");
		this.listJsp = new File(jspSourceDir,this.entityParamName +"List.jsp");
		this.listInfoJsp = new File(jspSourceDir,this.entityParamName +"ListInfo.jsp");
		addJsp = new File(jspSourceDir,this.entityParamName +"Add.jsp");
		viewJsp = new File(jspSourceDir,this.entityParamName +"View.jsp");
		updateJsp = new File(jspSourceDir,this.entityParamName +"Edit.jsp");
		GenMainJsp genMainJsp = new GenMainJsp(this);
		genMainJsp.gen();
		
		GenListJsp genListJsp = new GenListJsp(this);
		genListJsp.gen();		
		GenListInfoJsp genListInfoJsp = new GenListInfoJsp(this);
		genListInfoJsp.gen();
		GenAddJsp genAddJsp = new GenAddJsp(this);
		genAddJsp.gen();
		
		GenViewJsp genViewJsp = new GenViewJsp(this);
		genViewJsp.gen();
		
		GenUpdateJsp genUpdateJsp = new GenUpdateJsp(this);
		genUpdateJsp.gen();
	}
	
	/**
	 * 生成前端jsp ui界面
	 */
	private void genMVCConf()
	{
		GenMVCConf conf = new GenMVCConf(this);
		conf.gen();
	}
	
	/**
	 * 生成前端jsp ui界面
	 */
	private void genReadme()
	{
		GenReadme conf = new GenReadme(this);
		conf.gen();
	}
	
	
	
	private void init()
	{
		if(this.controlInfo != null)
		{
			pagineWithDBRownumberOver = controlInfo.getPagineWithDBRownumberOver() > 0 ;
			if(pagineWithDBRownumberOver)
			{
				if(this.sortFields == null || sortFields.size() == 0)
					throw new java.lang.IllegalArgumentException("采用Rownumber Over分析函数分页时，必须指定一个排序字段!");
			}
		}
		SQLBuilder = new SQLBuilder(this);
		File f = new File(moduleMetaInfo.getSourcedir(),this.moduleMetaInfo.getModuleName());
		if(!f.exists())
		{
			f.mkdirs();
		}
		else if(moduleMetaInfo.isClearSourcedir())
		{
			FileUtil.deleteSubfiles(f.getAbsolutePath());
			f.mkdirs();
		}
		this.jsppath = moduleMetaInfo.getJsppath();
		if(jsppath != null )
		{
			jsppath = jsppath.trim();
			if( !jsppath.startsWith("/"))
				jsppath = "/"+jsppath;
			
			if( jsppath.endsWith("/"))
				jsppath = jsppath.substring(0, jsppath.length() - 1);
			
			
		}
		this.rootdir = f;
		modulePackage = moduleMetaInfo.getPackagePath().replace(".", "/") ;
//		modulePackage = modulePackage.endsWith("/") ? modulePackage + moduleMetaInfo.getModuleName():
//			modulePackage + "/" + moduleMetaInfo.getModuleName();
//		javamodulePackage = !moduleMetaInfo.getPackagePath().endsWith(".")? 
//						moduleMetaInfo.getPackagePath() + "." +moduleMetaInfo.getModuleName()
//						:moduleMetaInfo.getPackagePath() +moduleMetaInfo.getModuleName();
		javamodulePackage = moduleMetaInfo.getPackagePath();
		javaSourceDir = new File(this.rootdir,"src/"+modulePackage);
		
		if(!javaSourceDir.exists())
		{
			javaSourceDir.mkdirs();
		}
		
		javaEntiySourceDir = new File(this.javaSourceDir,  "entity");
		if(!javaEntiySourceDir.exists())
		{
			javaEntiySourceDir.mkdirs();
		}
		javaWSSourceDir = new File(this.javaSourceDir,  "ws");
		if(!javaWSSourceDir.exists())
		{
			javaWSSourceDir.mkdirs();
		} 
		javaServiceSourceDir = new File(this.javaSourceDir,"service");
		if(!javaServiceSourceDir.exists())
		{
			javaServiceSourceDir.mkdirs();
		}
		javaActionSourceDir = new File(this.javaSourceDir,"action");
		if(!javaActionSourceDir.exists())
		{
			javaActionSourceDir.mkdirs();
		}
		resourcedir = new File(this.rootdir,"resources");
		if(!resourcedir.exists())
		{
			resourcedir.mkdirs();
		}
//		if(this.moduleMetaInfo.getSystem() == null || this.moduleMetaInfo.getSystem().equals(""))
//			jspSourceDir = new File(this.rootdir,"WebRoot/"+ moduleMetaInfo.getModuleName());
//		else
//			jspSourceDir = new File(this.rootdir,"WebRoot/"+ this.moduleMetaInfo.getSystem() + "/"+moduleMetaInfo.getModuleName());
		if(jsppath == null )
		{
			if(this.moduleMetaInfo.getSystem() == null || this.moduleMetaInfo.getSystem().equals(""))
			{
				jspSourceDir = new File(this.rootdir,"WebRoot/"+ moduleMetaInfo.getModuleName());
				this.relativePath =  "/"+moduleMetaInfo.getModuleName();
			}
			else
			{
				this.relativePath =   "/"+this.moduleMetaInfo.getSystem() + "/"+moduleMetaInfo.getModuleName();
				jspSourceDir = new File(this.rootdir,"WebRoot/"+ this.moduleMetaInfo.getSystem() + "/"+moduleMetaInfo.getModuleName());
			}
		}
		else
		{
			this.relativePath = jsppath;
			jspSourceDir = new File(this.rootdir,SimpleStringUtil.getRealPath("WebRoot", jsppath));
		}
		if(!jspSourceDir.exists())
		{
			jspSourceDir.mkdirs();
		}
		if(this.moduleMetaInfo.getSystem() == null || this.moduleMetaInfo.getSystem().equals(""))
		{
			
			this.namespacei18n = moduleMetaInfo.getModuleName();
		}
		else
		{
			
			namespacei18n =this.moduleMetaInfo.getSystem() + "."+moduleMetaInfo.getModuleName();
		}
		
		if(this.moduleMetaInfo.getSystem() == null || this.moduleMetaInfo.getSystem().equals(""))
			mvcConfDir = new File(this.rootdir,"WebRoot/WEB-INF/conf/"+ moduleMetaInfo.getModuleName());
		else
			mvcConfDir = new File(this.rootdir,"WebRoot/WEB-INF/conf/"+ this.moduleMetaInfo.getSystem());
		
		if(!mvcConfDir.exists())
		{
			mvcConfDir.mkdirs();
		}
		mvcconf = new File(getMvcConfDir(),"bboss-"+getModuleMetaInfo().getModuleName()+".xml");
		if(this.isGenI18n())
		{
			this.i18nen_US = new File(getMvcConfDir(),"messages_"+getModuleMetaInfo().getModuleName()+"_en_US.properties");
			this.i18nzh_CN = new File(getMvcConfDir(),"messages_"+getModuleMetaInfo().getModuleName()+"_zh_CN.properties");
		}
		readme = new File(this.rootdir,"readme.txt");
		webxmlFile = new File(this.rootdir,"WebRoot/WEB-INF/web.xml");
//		if(!webxmlFile.exists())
//		{
//			try {
//				webxmlFile.createNewFile();
//			} catch (IOException e) {
//				log.error("Create web.xml failed:"+webxmlFile.getAbsolutePath(),e);
//			}
//		}
		if(!genfromWeb)
		{
			TableMetaData tableMeta = DBUtil.getTableMetaData(this.moduleMetaInfo.getDatasourceName(), this.moduleMetaInfo.getTableName());
			if(this.entityFields == null)
			{
				List<Field> fields = getFields( tableMeta);
				 this.entityFields = fields;
				 for(Field field:entityFields)
				 {
					 field.setFieldCNName(field.getFieldName());
					 if(this.controlParam.isGenI18n())
						 field.setFieldAsciiCNName(field.getFieldName());
				 }
			}
			else
			{
				if(this.controlParam.isGenI18n())
				{
					handleI18n();
				}
			}
			initConditions();
			
			this.addShowFields = this.entityFields;
			this.viewShowFields = this.entityFields;
			this.editShowFields = this.entityFields;
			this.listShowFields = this.entityFields;
		}
		initSortFields(listShowFields);
		 
	}
	private void handleI18n()
	{
		if(this.entityFields != null && this.entityFields.size() > 0)
		{
			for(Field f:this.entityFields)
			{
				if(f.getFieldCNName() != null)
					f.setFieldAsciiCNName(SimpleStringUtil.native2ascii(f.getFieldCNName()));
				else
					f.setFieldAsciiCNName(f.getFieldName());
			}
		}
	}
	private void initConditions()
	{
		if(this.conditions != null )
		 {
			 for(Field field:conditions)
			 {
				 
				 for(Field dbfield:this.entityFields)
				 {
					 if(field.getColumnname().equalsIgnoreCase(dbfield.getColumnname()))
					 {
						 field.setType(dbfield.getType());
						 field.setFieldName(dbfield.getFieldName());
						 field.setFieldCNName(dbfield.getFieldCNName());
						 field.setMfieldName(dbfield.getMfieldName());
						 field.setColumntype(dbfield.getColumntype());
						 if(dbfield.getColumntype().equals("TIMESTAMP"))
			 					addConditionImport("java.sql.Timestamp");
			 				else if(dbfield.getColumntype().equals("DATE"))
			 					addConditionImport("java.sql.Date");
					 }
				 }
			 }
		 }
	}
	private void initSortFields(List<Field> listFields)
	{
		
		if(this.sortFields != null )
		 {
			 for(Field field:sortFields)
			 {
				 
				 for(Field dbfield:listFields)
				 {
					 if(field.getColumnname().equalsIgnoreCase(dbfield.getColumnname()))
					 {
						 if(!this.genfromWeb)
						 {
							 field.setType(dbfield.getType());
							 field.setFieldName(dbfield.getFieldName());
							 field.setFieldCNName(dbfield.getFieldCNName());
							 field.setMfieldName(dbfield.getMfieldName());
						 }
						 dbfield.setSortField(true);
						 dbfield.setDesc(field.isDesc());
						 if(field.isDefaultSortField())
						 {
							 this.defaultSortField = field;
						 }
					 }
				 }
			 }
		 }
	}
	
	
	private List<Field> getFields(TableMetaData tableMeta)
	{
		Set<ColumnMetaData> columns = tableMeta.getColumns();
		if(columns.size() > 0)
		{
			 
			List<Field> fs = new ArrayList<Field>();
			
			for(ColumnMetaData c:columns)
			{
				Field f = new Field();
				f.setType(Util.convertType(c.getSchemaType().getJavaType()));
				if(c.getRemarks() != null)
				{
					f.setFieldCNName(c.getRemarks());
					f.setFieldAsciiCNName(SimpleStringUtil.native2ascii(c.getRemarks()));
				}
				
				boolean isp = false;
				if(this.moduleMetaInfo.isAutogenprimarykey() && tableMeta.getPrimaryKeyMetaData(c.getColumnName()) != null)
				{
					Annotation anno = new Annotation();
					anno.setName("PrimaryKey");
					if(SimpleStringUtil.isNotEmpty(this.moduleMetaInfo.getPkname()))
					{
						anno.addAnnotationParam("pkname", this.moduleMetaInfo.getPkname(),AnnoParam.V_STRING);
					}
					f.addAnnotation(anno);
					isp = true;
					entityImport.add("com.frameworkset.orm.annotation.PrimaryKey");
				}
				if(c.getDataType() == java.sql.Types.CLOB)
				{
					Annotation anno = new Annotation();
					anno.setName("Column");
					anno.addAnnotationParam("type", "clob",AnnoParam.V_STRING);
					f.addAnnotation(anno);
					entityImport.add("com.frameworkset.orm.annotation.Column");
				}
				else if(c.getDataType() == java.sql.Types.BLOB)
				{
					Annotation anno = new Annotation();
					anno.setName("Column");
					anno.addAnnotationParam("type", "blob",AnnoParam.V_STRING);
					entityImport.add("com.frameworkset.orm.annotation.Column");
					f.addAnnotation(anno);
				}
				
		         try
		         {
		        	 List<String> inputs = new ArrayList<String>(2);
			         inputs.add(c.getColumnName().toLowerCase());
			         inputs.add( NameGenerator.CONV_METHOD_JAVANAME);
		        	 String mfieldName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
		                                                 inputs,false);
		        	 inputs = new ArrayList<String>(2);
			         inputs.add(c.getColumnName().toLowerCase());
			         inputs.add( NameGenerator.CONV_METHOD_JAVAFIELDNAME);
		        	 String fieldName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                             inputs,false);
		        	 f.setMfieldName(mfieldName);
		        	 f.setFieldName(fieldName);
		        	 f.setColumnname(c.getColumnName());
		        	 f.setColumntype(c.getTypeName());
		        	 if(isp)
		        	 {
		        		 this.primaryField = f;
		        		 this.primaryKeyName = f.getFieldName();
		        		 fs.add(0, f);
		        	 }
		        	 else
		        	 {
		        		 fs.add(f);
		        	 }
		         }
		         catch (EngineException e)
		         {
		             log.error(e.getMessage(), e);
		         }
		         
		         
		 				
 				if(f.getColumntype().startsWith("TIMESTAMP"))
 					addEntityImport("java.sql.Timestamp");
 				else if(f.getColumntype().equals("DATE"))
 					addEntityImport("java.sql.Date");
		 			 
		 		 
				
			}
			if(this.primaryKeyName == null)
			{
				this.primaryKeyName = "id";
			}
			return fs;
			
		}
		return null;
	}
	
	
	/**
	 * 生成持久层sql配置文件
	 * @throws Exception 
	 */
	private void genPersistentConfigfile() 
	{
		File sqlconfig = new File(this.javaServiceSourceDir,this.entityParamName + ".xml");
		if(sqlconfig.exists())
		{
			sqlconfig.delete();
		}
		this.sqlfilepath = this.modulePackage + "/service/"+this.entityParamName + ".xml";
		try {
			sqlconfig.createNewFile();
			 Template persistentsqltempalte = VelocityUtil.getTemplate("gencode/conf/persistentsql.vm");
			 VelocityContext context = new VelocityContext();
			
			 context.put("sqls", this.sqls);
			 context.put("description",this.moduleMetaInfo.getModuleCNName()+"管理sql配置文件");
			 context.put("company", this.moduleMetaInfo.getCompany());
			 context.put("gendate", this.moduleMetaInfo.getDate());
			 context.put("author", this.moduleMetaInfo.getAuthor());
			 context.put("version", this.moduleMetaInfo.getVersion());
			 context.put("filename", this.entityParamName + ".xml");
			 
			
			 writFile(context,persistentsqltempalte,sqlconfig,this.moduleMetaInfo.getEncodecharset());
		} catch (Exception e) {
			log.error("gen Persistent Config file failed:"+sqlconfig.getAbsolutePath(),e);
		}
	}
	/**
	 * 生成java源代码
	 */
	private void genJavaSource()
	{
		
		
		
		
		String entityName = this.moduleMetaInfo.getEntityName(); 
		if(SimpleStringUtil.isEmpty(entityName))
		{
			 
			 List inputs = new ArrayList(2);
	         inputs.add(moduleMetaInfo.getTableName().toLowerCase());
	         inputs.add( NameGenerator.CONV_METHOD_JAVANAME);
	         
	         try
	         {
	        	 entityName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
	                                                 inputs,this.moduleMetaInfo.isIgnoreEntityFirstToken());
	             
	         }
	         catch (EngineException e)
	         {
	             log.error(e.getMessage(), e);
	         }
		}
		String entityJavaName = entityName + ".java";
		String conditionEntityJavaName = entityName + "Condition.java";
		String serviceInfJavaName = entityName + "Service.java";
		String serviceImplJavaName = entityName + "ServiceImpl.java";
		String controllerJavaName = entityName + "Controller.java";
		String exceptionJavaName = entityName + "Exception.java";
		
		this.exceptionName = entityName + "Exception";
		this.entityName = entityName;
		this.conditionEntityName = entityName+"Condition";
		
		this.entityParamName = (entityName.charAt(0)+"").toLowerCase()  + entityName.substring(1);
		this.conditionEntityParamName = entityParamName+"Condition";
		this.serviceClass = this.javamodulePackage + ".service."+ entityName + "ServiceImpl";
		this.controlClass = this.javamodulePackage + ".action."+entityName + "Controller"; 
		File entity = new File(this.javaEntiySourceDir,entityJavaName);
		if(entity.exists())
		{
			entity.delete();
		}
		
		File exception = new File(this.javaServiceSourceDir,exceptionJavaName);
		if(exception.exists())
		{
			exception.delete();
		}
		File serviceInf = new File(this.javaServiceSourceDir,serviceInfJavaName);
		if(serviceInf.exists())
		{
			serviceInf.delete();
		}
		File serviceImpl = new File(this.javaServiceSourceDir,serviceImplJavaName);
		if(serviceImpl.exists())
		{
			serviceImpl.delete();
		}
		File controller = new File(this.javaActionSourceDir,controllerJavaName);
		if(controller.exists())
		{
			controller.delete();
		}
		try {
			this.serviceInfType = entityName + "Service";
			this.serviceParamName = this.entityParamName + "Service";
			entity.createNewFile();
			serviceInf.createNewFile();
			serviceImpl.createNewFile();
			controller.createNewFile();
			exception.createNewFile();

			 genEntity(  entityName,   this.moduleMetaInfo.getModuleCNName()+"管理服务实体类",entity);

			 
				 
				 genConditionEntity(  conditionEntityName,   this.moduleMetaInfo.getModuleCNName()+"管理查询条件实体类",conditionEntityJavaName);
			 
			  
			 genServiceInf(  this.serviceInfType, this.moduleMetaInfo.getModuleCNName()+"管理服务接口", serviceInf);
			 genException(entityName + "Exception", this.moduleMetaInfo.getModuleCNName()+"管理异常处理类",exception);
			 genServiceImpl(entityName + "ServiceImpl",serviceInfType ,this.moduleMetaInfo.getModuleCNName()+"管理业务处理类",serviceImpl);
			 genActionCode(entityName+"Controller",null,serviceInfType,  this.moduleMetaInfo.getModuleCNName()+"管理控制器处理类",controller);
			 if(this.controlParam.isGenRPCservice())
			 {
				 genRPC();
			 }
		} catch (Exception e) {
			log.error("gen java source file failed:",e);
		}
		
	}
	private String wsclassimpl ;
	private String wsclassinf ;
	private String wsserviceInf;
	private String wsserviceimpl;
	
	private void genRPC()
	{
		
		wsclassimpl = this.javamodulePackage + ".ws.WS"+ entityName + "ServiceImpl";		 
		wsclassinf = this.javamodulePackage + ".ws.WS"+ entityName + "Service";
		wsserviceInf = "WS"+ entityName + "Service";
		wsserviceimpl = "WS"+ entityName + "ServiceImpl";
		File serviceinf = new File(this.javaWSSourceDir,wsserviceInf+".java");
		if(serviceinf.exists())
		{
			serviceinf.delete();
		}
		File serviceimpl = new File(this.javaWSSourceDir,wsserviceimpl+".java");
		if(serviceimpl.exists())
		{
			serviceimpl.delete();
		}
		 String entityPackageInfo = javamodulePackage+".ws";
		try {
			genWSServiceInf(wsserviceInf,entityPackageInfo,serviceinf, this.moduleMetaInfo.getModuleCNName() + "webservice服务和hessian服务接口.") ;
			genWSServiceImpl(wsserviceimpl,wsserviceInf, this.moduleMetaInfo.getModuleCNName() + "webservice服务和hessian服务实现类.",  entityPackageInfo ,serviceimpl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void genEntity(String entityName, String description,File entity) throws Exception
	{
		 
		 List<String> imports = evalImport(false);
		 Template entitytempalte = VelocityUtil.getTemplate("gencode/java/entityjava.vm");
		 VelocityContext context = new VelocityContext();
		 context.put("fields", this.entityFields);
		 String entityPackageInfo = javamodulePackage+".entity";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", entityName);
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("iscondition", false);
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 writFile(context,entitytempalte,entity,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	private void genConditionEntity(String entityName ,String description,String conditionEntityJavaName) throws Exception
	{
		 File conditionEntity = new File(this.javaEntiySourceDir,conditionEntityJavaName);
		if(conditionEntity.exists())
		{
			conditionEntity.delete();
		}
		if(!needconditionsortbean())
 
			 return;
 
		
		conditionEntity.createNewFile(); 
 
		 List<String> imports = evalImport( true);
		 Template entitytempalte = VelocityUtil.getTemplate("gencode/java/entityjava.vm");
		 VelocityContext context = new VelocityContext();
		 List<Field> _conditions = new ArrayList<Field>();
		 if(conditions != null && conditions.size() > 0)
		 _conditions.addAll(conditions);
		 if(this.needsort())
		 {
			 Field sort = new Field();
			 sort.setFieldName("sortKey");
			 sort.setMfieldName("SortKey");
			 sort.setType("String");
			 _conditions.add(sort);
			 Field desc = new Field();
			 desc.setFieldName("sortDesc");
			 desc.setMfieldName("SortDesc");
			 desc.setType("boolean");
			 _conditions.add(desc);
		 }
		 context.put("fields", _conditions);
		 String entityPackageInfo = javamodulePackage+".entity";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", entityName);
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("iscondition", true);
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 writFile(context,entitytempalte,conditionEntity,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	public boolean needcondition()
	{
		return this.getConditions() != null && this.getConditions().size() > 0;
	}
	
	public boolean needconditionsortbean()
	{
		if((conditions == null || conditions.size() == 0) && (this.sortFields == null || this.sortFields.size() == 0))
			return false;
		else
			return true;
	}
	
	public boolean needsort()
	{
		return this.sortFields != null && this.sortFields.size() > 0;
	}
	
	private void genServiceInf(String serviceinfName,  String description,File serviceInf) throws Exception
	{
		 
		 List<String> imports = evalServiceInfImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceinfjava.vm");
		 VelocityContext context = new VelocityContext();
		 context.put("fields", null);
		 String entityPackageInfo = javamodulePackage+".service";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", serviceinfName);
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 context.put("needcondition",this.needconditionsortbean());
		 List<Method> methods = getMethods(0);
		 context.put("methods", methods);
	 
		 writFile(context,serviceinftempalte,serviceInf,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	private void genWSServiceInf(String serviceinfName, String entityPackageInfo ,File serviceInf,String description) throws Exception
	{
		 
		 List<String> imports = evalWSServiceInfImport();
		
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceinfjava.vm");
		 VelocityContext context = new VelocityContext();
		 List<Annotation> classannos = new ArrayList<Annotation>();
		 Annotation anno = new Annotation();
		 anno.setName("WebService");
		 anno.addAnnotationParam("name", serviceinfName);
		 anno.addAnnotationParam("targetNamespace", entityPackageInfo);
		 classannos.add(anno);
		 context.put("classannos", classannos);
		 context.put("package", entityPackageInfo);
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", serviceinfName);
		 context.put("description",description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 context.put("needcondition",this.needconditionsortbean());
		 List<Method> methods = getMethods(Constant.component_type_wsserviceinf);
		 context.put("methods", methods);
	 
		 writFile(context,serviceinftempalte,serviceInf,this.moduleMetaInfo.getEncodecharset());
		
	}
	private List<Field> getServiceImplFields(String serviceName)
	{
		List<Field> fields = new ArrayList<Field>();
		Field log4j = new Field(); 
		log4j.setStaticed(true);
		log4j.setFieldName("log");
		log4j.setType("Logger");
		String entityPackageInfo = javamodulePackage+".service";
		log4j.setDefaultValue("Logger.getLogger("+entityPackageInfo + "."+serviceName+".class)");
		fields.add(log4j);
		
		Field dao = new Field(); 
		
		dao.setFieldName("executor");
		dao.setType("ConfigSQLExecutor");
		fields.add(dao);
		return fields; 
	}
	private List<Field> getWSServiceImplFields()
	{
		List<Field> fields = new ArrayList<Field>();
	
		
		Field service = new Field(); 
		
		service.setFieldName(this.serviceParamName);
		service.setType(this.serviceInfType);
		fields.add(service);
		return fields; 
	}
	private void genServiceImpl(String serviceName,String serviceInfname, String description,File service) throws Exception
	{
		 
		 List<String> imports = evalServiceImplImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceimpljava.vm");
		 VelocityContext context = new VelocityContext();
		 List<Field> fields = getServiceImplFields(serviceName);
		 context.put("fields", fields);
		 String entityPackageInfo = javamodulePackage+".service";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", serviceName);
		 context.put("interfaceclassname", serviceInfname);
		 
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("componentType",Constant.component_type_serivceimpl);
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 context.put("needcondition",this.needconditionsortbean());
		 List<Method> methods = getMethods(Constant.component_type_serivceimpl);
		 context.put("methods", methods);
		 
	 
		 writFile(context,serviceinftempalte,service,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	private void genWSServiceImpl(String serviceName,String serviceInfname, String description, String entityPackageInfo ,File service) throws Exception
	{
		 
		 List<String> imports = evalWSServiceImplImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceimpljava.vm");
		 VelocityContext context = new VelocityContext();
		 List<Field> fields = getWSServiceImplFields();
		 List<Annotation> classannos = new ArrayList<Annotation>();
		 Annotation anno = new Annotation();
		 anno.setName("WebService");
		 anno.addAnnotationParam("name", serviceInfname);
		 anno.addAnnotationParam("targetNamespace", entityPackageInfo);
		 classannos.add(anno);
		 context.put("classannos", classannos);
		 context.put("fields", fields);
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", serviceName);
		 context.put("interfaceclassname", serviceInfname);
		 
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("componentType",Constant.component_type_wsserivceimpl);
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 context.put("needcondition",this.needconditionsortbean());
		 List<Method> methods = getMethods(Constant.component_type_wsserivceimpl);
		 context.put("methods", methods);
		 
	 
		 writFile(context,serviceinftempalte,service,this.moduleMetaInfo.getEncodecharset());
		
	}
	
	private void genActionCode(String actionName,String actionInfName,String serviceInfName, String description,File action) throws Exception
	{
		 
		 List<String> imports = evalActionImplImport();
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/serviceimpljava.vm");
		 VelocityContext context = new VelocityContext();
		 String serviceName = (serviceInfName.charAt(0)+"").toLowerCase() + serviceInfName.substring(1);
		 List<Field> fields = getActionImplFields(actionName,serviceInfName,serviceName);
		 context.put("fields", fields);
		 String entityPackageInfo = javamodulePackage+".action";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", actionName);
		 context.put("interfaceclassname", actionInfName);
		 
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 context.put("componentType",2);
		 context.put("moduleCNName",this.moduleMetaInfo.getModuleCNName());
		 List<Method> methods = getMethods(2);
		 context.put("methods", methods);
		 context.put("needcondition",this.needcondition());
		 context.put("needsort",this.needsort());
		 context.put("conditionFields", this.conditions);
	 
		 writFile(context,serviceinftempalte,action,this.moduleMetaInfo.getEncodecharset());
		
	}
	private List<Field> getActionImplFields(String actionName,String serviceinfclassName,String serviceName) {
		List<Field> fields = new ArrayList<Field>();
		Field log4j = new Field(); 
		log4j.setStaticed(true);
		log4j.setFieldName("log");
		log4j.setType("Logger");
	
		log4j.setDefaultValue("Logger.getLogger("+actionName+".class)");
		fields.add(log4j);
		
		Field service = new Field(); 
		
		service.setFieldName(serviceName);
		service.setType(serviceinfclassName);
		fields.add(service);
		return fields; 
	}
	
	public static final Map<String,MethodBodyGenerate> methodBodyGenerates = new HashMap<String,MethodBodyGenerate>();
	static{
		methodBodyGenerates.put(Constant.add, new AddMethodBodyGenerate());
		methodBodyGenerates.put(Constant.update, new UpdateMethodBodyGenerate());
		methodBodyGenerates.put(Constant.delete, new DeleteMethodBodyGenerate());
		methodBodyGenerates.put(Constant.deletebatch, new DeleteBatchMethodBodyGenerate());
		methodBodyGenerates.put(Constant.get, new GetMethodBodyGenerate());
		methodBodyGenerates.put(Constant.query, new QueryMethodBodyGenerate());
		methodBodyGenerates.put(Constant.paginequery, new PagineQueryMethodBodyGenerate());
		
		methodBodyGenerates.put(Constant.toAdd, new ToAddMethodBodyGenerate());
		methodBodyGenerates.put(Constant.toUpdate, new ToUpdateMethodBodyGenerate());
		methodBodyGenerates.put(Constant.index, new IndexMethodBodyGenerate());
	}
	private void setMethodBody(Method method,String methodtype,String entityName,String paramName,String encodecharset,String exception,int componenttype) throws Exception
	{
		MethodBodyGenerate methodBodyGenerate = methodBodyGenerates.get(methodtype);
		methodBodyGenerate.gen(method,entityName,entityParamName,paramName,encodecharset,exception, componenttype,this);
	}
	private List<Method> getActionMethods(int classtype ) throws Exception {
		List<String> exceptions = new ArrayList<String>();
		exceptions.add(exceptionName);
		
		List<Method> methods = new ArrayList<Method>();
		Method add = new Method();//定义添加方法
		add.setMethodname("add"+entityName);
		add.setReturntype("String");
		add.addAnnotation(new Annotation("ResponseBody"));
	
		List<MethodParam> params = new ArrayList<MethodParam>();
		MethodParam param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		params.add(param);
		add.setParams(params);
		setMethodBody(add,Constant.add,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName, Constant.component_type_actionimpl);
		
		methods.add(add);
		
		
		
		
		Method delete = new Method();//定义删除方法
		delete.setMethodname("delete"+entityName);
		delete.addAnnotation(new Annotation("ResponseBody"));
		delete.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
			 this.primaryKeyName = "id";
		}
		params.add(param);
		
		delete.setParams(params);
		setMethodBody(delete,Constant.delete,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(delete);
		
		Method deletebatch = new Method();//定义删除方法
		deletebatch.setMethodname("deleteBatch"+entityName);
		deletebatch.setReturntype("String");	
		deletebatch.addAnnotation(new Annotation("ResponseBody"));
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType() +"...");
			param.setName(primaryField.getFieldName()+"s");
		}
		else
		{
			param.setType("String...");
			param.setName("ids");
		}
		params.add(param);
		
		deletebatch.setParams(params);
		setMethodBody(deletebatch,Constant.deletebatch,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		
		methods.add(deletebatch);
		
		Method update = new Method();//定义修改方法
		update.setMethodname("update"+entityName);
		update.setReturntype("String");		
		update.addAnnotation(new Annotation("ResponseBody"));
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		
		params.add(param);
		update.setParams(params);
		
		setMethodBody(update,Constant.update,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(update);
		
		Method get = new Method();//定义获取方法
		get.setMethodname("get"+entityName);
		get.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		String defaultSort = null;
		String defaultSortField = null;
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
			defaultSort = primaryField.getColumnname();
			
		}
		else
		{
			param.setType("String");
			param.setName("id");
			defaultSort = "id";
		}
		defaultSortField = param.getName();
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		get.setExceptions(exceptions);		
		get.setParams(params);
		
		setMethodBody(get,Constant.get,entityName,defaultSortField,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(get);
		
		
		
		Method paginequery = new Method();//定义获取方法
		paginequery.setMethodname("queryListInfo"+entityName+"s");
		paginequery.setReturntype("String");	
		params = new ArrayList<MethodParam>();
		if(this.needcondition())
		{
		
			param = new MethodParam();
			
	//		param.addAnnotation(new Annotation("MapKey").addAnnotationParam("pattern","condition_*"));
			param.setType(this.conditionEntityName);
			param.setName("conditions");
			params.add(param);
		}
		
		if(this.needsort())
		{
			param = new MethodParam();		
			param.setType("String");
			param.setName("sortKey");
			param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.SORT",AnnoParam.V_CONTAST).addAnnotationParam("defaultvalue",this.defaultSortField == null?defaultSort:this.defaultSortField.getColumnname()));
			params.add(param);
			
			param = new MethodParam();
			param.setType("boolean");
			param.setName("desc");
			param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.DESC",AnnoParam.V_CONTAST).addAnnotationParam("defaultvalue", this.defaultSortField == null?"true":this.defaultSortField.isDesc()+"", AnnoParam.V_STRING));
			params.add(param);
		}	
		
		param = new MethodParam();		
		param.setType("long");
		param.setName("offset");
		param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.OFFSET",AnnoParam.V_CONTAST));
		
		params.add(param);
		param = new MethodParam();
		param.setType("int");
		param.setName("pagesize");
		param.addAnnotation(new Annotation("PagerParam").addAnnotationParam("name","PagerParam.PAGE_SIZE",AnnoParam.V_CONTAST).addAnnotationParam("defaultvalue", "10", AnnoParam.V_STRING));
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		paginequery.setExceptions(exceptions);		
		paginequery.setParams(params);
		 
		setMethodBody(paginequery,Constant.paginequery,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(paginequery);
		
		Method query = new Method();//定义获取方法
		query.setMethodname("queryList"+entityName+"s");
		query.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		if(this.needconditionsortbean())
		{
			param = new MethodParam();
			
			
			param.setType(this.conditionEntityName);
			param.setName("conditions");
			params.add(param);
		}
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		query.setExceptions(exceptions);		
		query.setParams(params);
		 
		setMethodBody(query,Constant.query,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(query);
		
		Method toUpdate = new Method();//定义获取方法
		toUpdate.setMethodname("toUpdate"+entityName);
		toUpdate.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
		}
		else
		{
			param.setType("String");
			param.setName("id");
		}
	
		params.add(param);
		param = new MethodParam();
		param.setName("model");
		param.setType("ModelMap");
		params.add(param);
		toUpdate.setExceptions(exceptions);		
		toUpdate.setParams(params);
		
		setMethodBody(toUpdate,Constant.toUpdate,entityName,defaultSortField,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(toUpdate);
		
		Method toAdd = new Method();//定义获取方法
		toAdd.setMethodname("toAdd"+entityName);
		toAdd.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		
		toAdd.setParams(params);
		
		setMethodBody(toAdd,Constant.toAdd,entityName,defaultSortField,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(toAdd);
		
		Method index = new Method();//定义获取方法
		index.setMethodname("index");
		index.setReturntype("String");		
		params = new ArrayList<MethodParam>();
		
		index.setParams(params);
		
		setMethodBody(index,Constant.index,entityName,defaultSortField,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_actionimpl);
		
		methods.add(index);
//		Method count = new Method();//定义获取方法
//		query.setMethodname("queryList"+entityName+"s");
//		query.setReturntype("List<"+entityName+">");		
//		params = new ArrayList<MethodParam>();
//		param = new MethodParam();
//		
//		
//		param.setType("Map");
//		param.setName("conditions");
//		params.add(param);
//		query.setExceptions(exceptions);		
//		query.setParams(params);
//		methods.add(query);
		return methods;
	}
	private List<Method> getMethods(int classtype ) throws Exception {
		if(classtype == Constant.component_type_actionimpl || classtype == Constant.component_type_actioninf)
			return getActionMethods(classtype );
		List<String> exceptions = new ArrayList<String>();
		exceptions.add(exceptionName);
		
		List<Method> methods = new ArrayList<Method>();
		Method add = new Method();//定义添加方法
		add.setMethodname("add"+entityName);
		add.setReturntype("void");
		
		add.setExceptions(exceptions);
		List<MethodParam> params = new ArrayList<MethodParam>();
		MethodParam param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebParam");
			 anno.addAnnotationParam("name", entityParamName);
			 anno.addAnnotationParam("partName", "part"+entityName);
			 param.addAnnotation(anno);
		}
		params.add(param);
		add.setParams(params);
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(add,Constant.add,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			sqls = new ArrayList<SQL>();
			SQL sql = new SQL();
			sql.setOptype(Constant.add);
			sql.setName("add" + this.entityName);
			evalsql(sql);
			this.sqls.add(sql);
		}
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(add,Constant.add,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
			
		}
		
		methods.add(add);		
		
		Method delete = new Method();//定义删除方法
		delete.setMethodname("delete"+entityName);
		delete.setReturntype("void");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", param.getName());
				 anno.addAnnotationParam("partName", "part"+primaryField.getMfieldName());
				 param.addAnnotation(anno);
			}
		}
		else
		{
			param.setType("String");
			param.setName("id");
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", param.getName());
				 anno.addAnnotationParam("partName", "partId");
				 param.addAnnotation(anno);
			}
		}
		params.add(param);
		delete.setExceptions(exceptions);		
		delete.setParams(params);
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(delete,Constant.delete,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.delete);
			sql.setName("deleteByKey");
			evalsql(sql);
			this.sqls.add(sql);
		}
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(delete,Constant.delete,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
			
		}
		methods.add(delete);
		
		Method deletebatch = new Method();//定义删除方法
		deletebatch.setMethodname("deleteBatch"+entityName);
		deletebatch.setReturntype("void");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		if(primaryField != null)
		{
			param.setType(primaryField.getType() +"...");
			param.setName(primaryField.getFieldName()+"s");
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", param.getName());
				 anno.addAnnotationParam("partName", "part"+primaryField.getMfieldName()+"s");
				 param.addAnnotation(anno);
			}
		}
		else
		{
			param.setType("String...");
			param.setName("ids");
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", param.getName());
				 anno.addAnnotationParam("partName", "partIds");
				 param.addAnnotation(anno);
			}
		}
		params.add(param);
		deletebatch.setExceptions(exceptions);		
		deletebatch.setParams(params);
		if(classtype == Constant.component_type_serivceimpl)
			setMethodBody(deletebatch,Constant.deletebatch,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(deletebatch,Constant.deletebatch,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
			
		}
		 
		
		methods.add(deletebatch);
		
		Method update = new Method();//定义修改方法
		update.setMethodname("update"+entityName);
		update.setReturntype("void");		
		params = new ArrayList<MethodParam>();
		param = new MethodParam();
		param.setType(this.entityName);
		param.setName(this.entityParamName);
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebParam");
			 anno.addAnnotationParam("name", param.getName());
			 anno.addAnnotationParam("partName", "part"+entityName);
			 param.addAnnotation(anno);
		}
		params.add(param);
		update.setExceptions(exceptions);		
		update.setParams(params);
		
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(update,Constant.update,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.update);
			sql.setName("update"+this.entityName);
			evalsql(sql);
			this.sqls.add(sql);
		}
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(update,Constant.update,entityName,entityParamName,this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
			
		}
		methods.add(update);
		
		Method get = new Method();//定义获取方法
		get.setMethodname("get"+entityName);
		get.setReturntype(entityName);		
		params = new ArrayList<MethodParam>();
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebResult");
			 anno.addAnnotationParam("name", this.entityParamName);
			 anno.addAnnotationParam("partName", "part"+this.entityName);
			 get.addAnnotation(anno);
		}
		param = new MethodParam();
		
		
		if(primaryField != null)
		{
			param.setType(primaryField.getType());
			param.setName(primaryField.getFieldName());
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", param.getName());
				 anno.addAnnotationParam("partName", "part"+primaryField.getMfieldName());
				 param.addAnnotation(anno);
			}
		}
		else
		{
			param.setType("String");
			param.setName("id");
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", param.getName());
				 anno.addAnnotationParam("partName", "partId");
				 param.addAnnotation(anno);
			}
		}
		params.add(param);
		get.setExceptions(exceptions);		
		get.setParams(params);
		
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(get,Constant.get,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			SQL sql = new SQL();
			sql.setOptype(Constant.get);
			sql.setName("selectById");
			evalsql(sql);
			this.sqls.add(sql);
		}
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(get,Constant.get,entityName,param.getName(),this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
			
		}
		methods.add(get);
		
		Method paginequery = new Method();//定义获取方法
		paginequery.setMethodname("queryListInfo"+entityName+"s");
		if(classtype == Constant.component_type_wsserviceinf || classtype == Constant.component_type_wsserivceimpl)
			paginequery.setReturntype("RListInfo");		
		else
			paginequery.setReturntype("ListInfo");
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebResult");
			 anno.addAnnotationParam("name", this.entityParamName+"s");
			 anno.addAnnotationParam("partName", "part"+entityName+"s");
			 paginequery.addAnnotation(anno);
		}
		params = new ArrayList<MethodParam>();
		if(this.needconditionsortbean())
		{
			param = new MethodParam();
			
			
			param.setType(this.conditionEntityName);
			param.setName("conditions");
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", "conditions");
				 anno.addAnnotationParam("partName", "partConditions");
				 param.addAnnotation(anno);
			}
			params.add(param);
		}
		
		param = new MethodParam();
		param.setType("long");
		param.setName("offset");
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebParam");
			 anno.addAnnotationParam("name", "offset");
			 anno.addAnnotationParam("partName", "partOffset");
			 param.addAnnotation(anno);
		}
		params.add(param);
		param = new MethodParam();
		param.setType("int");
		param.setName("pagesize");
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebParam");
			 anno.addAnnotationParam("name", "pagesize");
			 anno.addAnnotationParam("partName", "partPagesize");
			 param.addAnnotation(anno);
		}
		params.add(param);
		paginequery.setExceptions(exceptions);		
		paginequery.setParams(params);
		 
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(paginequery,Constant.paginequery,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
			if(!this.isPagineWithDBRownumberOver())
			{
				SQL sql = new SQL();
				sql.setOptype(Constant.paginequery);
				sql.setName("queryList"+this.entityName);
				evalsql(sql);
				this.sqls.add(sql);
			}
			else
			{
				SQL sql = new SQL();
				sql.setOptype(Constant.listquery);
				sql.setName("queryList"+this.entityName);
				evalsql(sql);
				this.sqls.add(sql);
				 sql = new SQL();
				sql.setOptype(Constant.paginequery);
				sql.setName("queryPagine"+this.entityName);
				evalsql(sql);
				this.sqls.add(sql);
				sql = new SQL();
				sql.setOptype(Constant.pagineOrderBy);
				sql.setName(this.entityName+"Orderby");
				evalsql(sql);
				this.sqls.add(sql);
				
			}
		}
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(paginequery,Constant.paginequery,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
			
		}
		
		
		methods.add(paginequery);
		
		Method query = new Method();//定义获取方法
		query.setMethodname("queryList"+entityName+"s");
		query.setReturntype("List<"+entityName+">");	
		if(classtype == Constant.component_type_wsserviceinf)
		{
			/**
			 * name = "appid", partName = "partAppid"
			 */
			 Annotation anno = new Annotation();
			 anno.setName("WebResult");
			 anno.addAnnotationParam("name", this.entityParamName+"s");
			 anno.addAnnotationParam("partName", "part"+entityName+"s");
			 query.addAnnotation(anno);
		}
		params = new ArrayList<MethodParam>();
		if(this.needconditionsortbean())
		{
			param = new MethodParam();
			
			
			param.setType(this.conditionEntityName);
			param.setName("conditions");
			if(classtype == Constant.component_type_wsserviceinf)
			{
				/**
				 * name = "appid", partName = "partAppid"
				 */
				 Annotation anno = new Annotation();
				 anno.setName("WebParam");
				 anno.addAnnotationParam("name", "conditions");
				 anno.addAnnotationParam("partName", "partConditions");
				 param.addAnnotation(anno);
			}
			params.add(param);
		}
		query.setExceptions(exceptions);		
		query.setParams(params);
		 
		if(classtype == Constant.component_type_serivceimpl)
		{
			setMethodBody(query,Constant.query,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_serivceimpl);
		}
		else if(classtype == Constant.component_type_wsserivceimpl)
		{
			setMethodBody(query,Constant.query,entityName,"conditions",this.moduleMetaInfo.getEncodecharset(),exceptionName,Constant.component_type_wsserivceimpl);
		}
		methods.add(query);
		return methods;
	}
	
	private void evalsql(SQL sql) {
		SQLBuilder.buidlersql(sql);
		
	}

	public static void writFile(  VelocityContext context,Template tempalte,File file,String charset)throws Exception
	{
		FileOutputStream out = null;
		 OutputStreamWriter writer = null;
		 try
		 {
			 out = new FileOutputStream(file);
			 writer = new OutputStreamWriter(out,charset);
			 tempalte.merge(context, writer);
			 writer.flush();
		 }
		 catch(Exception e)
		 {
			 throw e;
		 }
		 finally
		 {
			 if(out != null)
				try {
					out.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(writer != null)
				try {
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		 
	}
	
	public static String writetostring(  VelocityContext context,Template tempalte,String charset)throws Exception
	{
		
		StringWriter writer = null;
		 try
		 {
			  writer = new StringWriter();
			 
			 tempalte.merge(context, writer);
			 writer.flush();
			 return writer.toString();
		 }
		 catch(Exception e)
		 {
			 throw e;
		 }
		 finally
		 {
			 			 if(writer != null)
				try {
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		 
	}

	private void genException(String exceptionJavaName, String description,File exception) throws Exception
	{
		 
		 List<String> imports = null;
		 Template serviceinftempalte = VelocityUtil.getTemplate("gencode/java/exception.vm");
		 VelocityContext context = new VelocityContext();
		 context.put("fields", null);
		 String entityPackageInfo = javamodulePackage+".service";
		 context.put("package", entityPackageInfo);
		 context.put("imports", imports);
		 context.put("classname", exceptionJavaName);
		 context.put("description", description);
		 context.put("company", this.moduleMetaInfo.getCompany());
		 context.put("gendate", this.moduleMetaInfo.getDate());
		 context.put("author", this.moduleMetaInfo.getAuthor());
		 context.put("version", this.moduleMetaInfo.getVersion());
		 writFile(context,serviceinftempalte,exception,this.moduleMetaInfo.getEncodecharset());
		 
		
	}
	
	private List<String> evalImport(boolean condition) {
		if(condition)
		{
			List<String> imports = new ArrayList<String>();
//			for(Field f:fields)
//			{
//				
//					
//		         if(f.getType().equals("Timestamp"))
//		         {
//		        	 if(!imports.contains("java.sql.Timestamp"))
//		        		 imports.add("java.sql.Timestamp");
//		         }
//		         
//			}
//			if(!condition )
//			{
				imports.addAll(this.conditionImport);
//			}
			return imports;
		}
		else
		{
			List<String> imports = new ArrayList<String>();
			imports.addAll(entityImport);
			return imports;
		}
	}
	private Set<String> entityImport = new TreeSet<String>();
	private Set<String> conditionImport = new TreeSet<String>();
	public void addConditionImport(String importType)
	{
		conditionImport.add(importType);
	}
	private List<String> evalServiceInfImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(javamodulePackage+".entity.*");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("java.util.List");		
		
		return imports;
	}
	
	private List<String> evalWSServiceInfImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(javamodulePackage+".entity.*");
		imports.add(javamodulePackage+".service.*");
		imports.add("com.frameworkset.util.RListInfo");
		imports.add("java.util.List");		
		imports.add("javax.jws.WebParam");
		imports.add("javax.jws.WebResult");
		imports.add("javax.jws.WebService");		
		
		return imports;
	}
	
	private List<String> evalServiceImplImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(javamodulePackage+".entity.*");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("com.frameworkset.common.poolman.ConfigSQLExecutor");
		imports.add("org.apache.log4j.Logger");		
		imports.add("java.util.List");
		if(this.isPagineWithDBRownumberOver())
			imports.add("com.frameworkset.common.poolman.ConfigPagineOrderby");
		
		imports.add("com.frameworkset.orm.transaction.TransactionManager");
		
		return imports;
	}
	
	private List<String> evalWSServiceImplImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(javamodulePackage+".entity.*");
		imports.add(javamodulePackage+".service.*");
		imports.add("com.frameworkset.util.RListInfo");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("java.util.List");
		imports.add("javax.jws.WebService");	
		
		return imports;
	}
	
	private List<String> evalActionImplImport( ) {
		List<String> imports = new ArrayList<String>();
		imports.add(javamodulePackage+".entity.*");
		imports.add("com.frameworkset.util.ListInfo");
		imports.add("org.apache.log4j.Logger");		
		imports.add("java.util.List");
		imports.add("java.util.Map");
		imports.add("com.frameworkset.util.StringUtil");
		imports.add(javamodulePackage+".service.*");
		imports.add("org.frameworkset.util.annotations.ResponseBody");
		imports.add("org.frameworkset.web.servlet.ModelMap");
		imports.add("org.frameworkset.util.annotations.PagerParam");
		imports.add("org.frameworkset.util.annotations.MapKey");
		/**
		 * import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.demo.appbom.entity.AppBom;
import org.frameworkset.demo.appbom.service.AppBomException;
import org.frameworkset.demo.appbom.service.AppBomService;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
		 */
		return imports;
	}


	public static void main(String[] args)
	{
		String jsppath = "aaa/";
		jsppath =		jsppath.substring(0, jsppath.length() - 1);
		 List inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_UNDERSCORE);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
		
           inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_UNDERSCORE_OMIT_SCHEMA);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
         inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_JAVANAME);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
         inputs = new ArrayList(2);
         inputs.add("td_tableinfo_name");
         inputs.add( NameGenerator.CONV_METHOD_NOCHANGE);
         try
         {
             String javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                 inputs,false);
             System.out.println(javaName);
         }
         catch (EngineException e)
         {
             log.error(e, e);
         }
	}

	public List<Field> getConditions() {
		return conditions;
	}

//	public List<Field> getAllfields() {
//		return allfields;
//	}

	public ModuleMetaInfo getModuleMetaInfo() {
		return moduleMetaInfo;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getEntityParamName() {
		return entityParamName;
	}

	public Field getPrimaryField() {
		return primaryField;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	
	public String getPrimaryKeyColumnName() {
		return this.primaryField != null?this.primaryField.getColumnname():this.getPrimaryKeyName();
	}

	public List<Field> getSortFields() {
		return sortFields;
	}

	public void setSortFields(List<Field> sortFields) {
		this.sortFields = sortFields;
	}
	
	public void addCondition(ConditionField cf)
	{
		if(this.conditions == null)
		{
			conditions = new ArrayList<Field>();
		}
		conditions.add(cf);
	}
	
	public void addSortField(SortField sf)
	{
		if(this.sortFields == null)
		{
			sortFields = new ArrayList<Field>();
		}
		sortFields.add(sf);
	}

	public File getMvcConfDir() {
		return mvcConfDir;
	}


	
	public String getServiceClass() {
		// TODO Auto-generated method stub
		return serviceClass;
	}
	
	public String getControlClass() {
		// TODO Auto-generated method stub
		return controlClass;
	}

	public String getSqlfilepath() {
		return sqlfilepath;
	}

	public File getMvcconf() {
		return mvcconf;
	}
	public File getMainJsp() {
		return this.mainJsp;
	}
	public File getListJsp() {
		return this.listJsp;
	}

	public void setMvcconf(File mvcconf) {
		this.mvcconf = mvcconf;
	}

	public String getConditionEntityName() {
		return conditionEntityName;
	}

	public void setConditionEntityName(String conditionEntityName) {
		this.conditionEntityName = conditionEntityName;
	}

	public String getConditionEntityParamName() {
		return conditionEntityParamName;
	}

	public void setConditionEntityParamName(String conditionEntityParamName) {
		this.conditionEntityParamName = conditionEntityParamName;
	}

	

	public File getListInfoJsp() {
		return listInfoJsp;
	}

	public File getAddJsp() {
		// TODO Auto-generated method stub
		return addJsp;
	}
	
	public File getUpdateJsp() {
		// TODO Auto-generated method stub
		return this.updateJsp;
	}
	
	public File getViewJsp() {
		// TODO Auto-generated method stub
		return this.viewJsp;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public File getReadme() {
		return readme;
	}

	public void setReadme(File readme) {
		this.readme = readme;
	}

	public String getWsclassimpl() {
		return wsclassimpl;
	}

	public void setWsclassimpl(String wsclassimpl) {
		this.wsclassimpl = wsclassimpl;
	}

	public String getWsclassinf() {
		return wsclassinf;
	}

	public void setWsclassinf(String wsclassinf) {
		this.wsclassinf = wsclassinf;
	}

	public String getServicePort() {
		return serviceParamName;
	}
	
	public String getServiceParamName() {
		return serviceParamName;
	}

	public void setServiceParamName(String serviceParamName) {
		this.serviceParamName = serviceParamName;
	}

	public File getI18nzh_CN() {
		return i18nzh_CN;
	}

	public File getI18nen_US() {
		return i18nen_US;
	}

	public String getNamespacei18n() {
		return namespacei18n;
	}

	public List<Field> getListShowFields() {
		return listShowFields;
	}

	public List<Field> getListHiddenFields() {
		return listHiddenFields;
	}

	public List<Field> getEditShowFields() {
		return editShowFields;
	}

	public List<Field> getEditHiddenFields() {
		return editHiddenFields;
	}

	public List<Field> getAddShowFields() {
		return addShowFields;
	}

	public List<Field> getViewShowFields() {
		return viewShowFields;
	}

	public List<Field> getViewHiddenFields() {
		return viewHiddenFields;
	}
	public boolean isGenI18n() {
		return this.controlParam.isGenI18n();
	}
	public void setGenI18n(boolean genI18n) {
		this.controlParam.setGenI18n(genI18n);
	}
	public int getExcelVersion() {
		return this.controlParam.getExcelVersion();
	}
	public boolean isGenRPCservice() {
		return controlParam.isGenRPCservice();
	}
	public void setGenRPCservice(boolean genRPCservice) {
		this.controlParam.setGenRPCservice(genRPCservice);
	}
	public void setExcelVersion(int excelVersion) {
		this.controlParam.setExcelVersion(excelVersion);
	}
	
	public void setExportExcel(boolean exportExcel) {
		this.controlParam.setExportExcel(exportExcel);
	}
	public boolean isImportExcel() {
		return controlParam.isImportExcel();
	}
	public void setModuleMetaInfo(ModuleMetaInfo moduleMetaInfo) {
		this.moduleMetaInfo = moduleMetaInfo;
	}
	public void addEntityImport(String entityImport)
	{
		this.entityImport.add(entityImport);
	}
	public void setPrimaryField(Field primaryField) {
		this.primaryField = primaryField;
	}
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	public void setListShowFields(List<Field> listShowFields) {
		this.listShowFields = listShowFields;
	}
	public void setListHiddenFields(List<Field> listHiddenFields) {
		this.listHiddenFields = listHiddenFields;
	}
	public void setViewShowFields(List<Field> viewShowFields) {
		this.viewShowFields = viewShowFields;
	}
	public void setViewHiddenFields(List<Field> viewHiddenFields) {
		this.viewHiddenFields = viewHiddenFields;
	}
	public void setEditShowFields(List<Field> editShowFields) {
		this.editShowFields = editShowFields;
	}
	public void setAddShowFields(List<Field> addShowFields) {
		this.addShowFields = addShowFields;
	}
	public void setEditHiddenFields(List<Field> editHiddenFields) {
		this.editHiddenFields = editHiddenFields;
	}
	public List<Field> getEntityFields() {
		return entityFields;
	}
	public void setEntityFields(List<Field> entityFields) {
		this.entityFields = entityFields;
//		allfields = entityFields;
	}

	public Field getDefaultSortField() {
		return defaultSortField;
	}

	public void setDefaultSortField(Field defaultSortField) {
		this.defaultSortField = defaultSortField;
	}

	public boolean isNeedDateComponent() {
		return needDateComponent;
	}

	public void setNeedDateComponent(boolean needDateComponent) {
		this.needDateComponent = needDateComponent;
	}

	public String getJsppath() {
		return jsppath;
	}

	public void setJsppath(String jsppath) {
		this.jsppath = jsppath;
	}

	public List<Field> getAddHiddenFields() {
		return addHiddenFields;
	}

	public void setAddHiddenFields(List<Field> addHiddenFields) {
		this.addHiddenFields = addHiddenFields;
	}

	public ControlInfo getControlInfo() {
		return controlInfo;
	}

	public void setControlInfo(ControlInfo controlInfo) {
		this.controlInfo = controlInfo;
	}

	public boolean isPagineWithDBRownumberOver() {
		return pagineWithDBRownumberOver;
	}

	public void setPagineWithDBRownumberOver(boolean pagineWithDBRownumberOver) {
		this.pagineWithDBRownumberOver = pagineWithDBRownumberOver;
	}
	
	public String getDaoDBName()
	{
		return this.controlInfo != null && !controlInfo.getDaoDBName().equals("")?this.controlInfo.getDaoDBName():null;
	}
}
