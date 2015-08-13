package com.bin.test.rabbitmqtest;
import java.net.MalformedURLException;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class SystemPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private String				propertyName		= "application.properties";
	private Properties			properties;
	public static final String	APPSERVER_ID		= "appserver.id";
	protected Resource[]		resources;
	private boolean				createAppserverId	= true;
	
	Logger logger = LoggerFactory.getLogger(SystemPropertyPlaceholderConfigurer.class);
	
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		try{
			properties = mergeProperties();
		}
		catch (Exception exception){
		}
	}
	
	public void setLocations(Resource[] locations) {
		String applicationProperties = System.getProperty(propertyName);
		System.out.println(String.valueOf(applicationProperties));
		logger.debug(String.valueOf(applicationProperties));
		if(!StringUtils.isEmpty(applicationProperties)){
			try{
				
				String[] paths = applicationProperties.split(",");
				resources = new Resource[paths.length];
				
				for (int i = 0; i < paths.length; i++){
					resources[i] = new UrlResource("file:" + paths[i].trim());
				}
				
				super.setLocations(resources);
			}
			catch (MalformedURLException e){
				super.setLocations(locations);
			}
		}
		else{
			super.setLocations(locations);
		}
	}
	
	public String getPropertyName() {
		
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getValue(String key) {
		if(properties == null)
			return null;
		return properties.get(key) + "";
	}

	public boolean isCreateAppserverId() {
		return createAppserverId;
	}

	public void setCreateAppserverId(boolean createAppserverId) {
		this.createAppserverId = createAppserverId;
	}
}
