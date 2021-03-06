package com.publiccms.common.database;

import static com.publiccms.common.tools.VerificationUtils.base64Decode;
import static com.publiccms.common.tools.VerificationUtils.decrypt;
import static java.lang.Integer.parseInt;
import static com.publiccms.common.constants.CommonConstants.ENCRYPT_KEY;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.publiccms.common.datasource.MultiDataSource;

/**
 *
 * CmsDataSource
 * 
 */
public class CmsDataSource extends MultiDataSource {
    /**
     * 
     */
    public static final String DATABASE_CONFIG_FILENAME = "/database.properties";
    /**
     * 
     */
    public static final String DATABASE_CONFIG_TEMPLATE = "config/database-template.properties";
    private static CmsDataSource cmsDataSource;
    private String dbconfigFilePath;

    private Map<Object, Object> dataSources = new HashMap<>();

    /**
     * @param filePath
     */
    public CmsDataSource(String filePath) {
        dbconfigFilePath = filePath;
        cmsDataSource = this;
    }

    /**
     * @param configFilePath
     * @return database config
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Properties loadDatabaseConfig(String configFilePath) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        File file = new File(configFilePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        }
        return properties;
    }

    /**
     * @param properties
     * @return database source
     * @throws IOException
     * @throws PropertyVetoException
     */
    public static DataSource initDataSource(Properties properties) throws IOException, PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
        dataSource.setUser(properties.getProperty("jdbc.username"));
        String password = properties.getProperty("jdbc.password");
        String encryptPassword = properties.getProperty("jdbc.encryptPassword");
        if (null != encryptPassword) {
            password = decrypt(base64Decode(encryptPassword), ENCRYPT_KEY);
        }
        dataSource.setPassword(password);
        dataSource.setAutoCommitOnClose(Boolean.parseBoolean(properties.getProperty("cpool.autoCommitOnClose")));
        dataSource.setCheckoutTimeout(parseInt(properties.getProperty("cpool.checkoutTimeout")));
        dataSource.setInitialPoolSize(parseInt(properties.getProperty("cpool.minPoolSize")));
        dataSource.setMinPoolSize(parseInt(properties.getProperty("cpool.minPoolSize")));
        dataSource.setMaxPoolSize(parseInt(properties.getProperty("cpool.maxPoolSize")));
        dataSource.setMaxIdleTime(parseInt(properties.getProperty("cpool.maxIdleTime")));
        dataSource.setAcquireIncrement(parseInt(properties.getProperty("cpool.acquireIncrement")));
        dataSource.setMaxIdleTimeExcessConnections(parseInt(properties.getProperty("cpool.maxIdleTimeExcessConnections")));
        return dataSource;
    }

    /**
     * @throws IOException
     * @throws PropertyVetoException
     */
    public static void initDefautlDataSource() throws IOException, PropertyVetoException {
        Properties properties = loadDatabaseConfig(cmsDataSource.dbconfigFilePath);
        DataSource dataSource = initDataSource(properties);
        cmsDataSource.getDataSources().put("default", dataSource);
        cmsDataSource.setTargetDataSources(cmsDataSource.getDataSources());
        cmsDataSource.setDefaultTargetDataSource(dataSource);
        cmsDataSource.init();
    }

    @Override
    public void afterPropertiesSet() {

    }

    /**
     * 
     */
    public void init() {
        super.afterPropertiesSet();
    }

    /**
     * @param name
     * @param dataSource
     */
    public void put(Object name, Object dataSource) {
        dataSources.put(name, dataSource);
    }

    /**
     * @return database source map
     */
    public Map<Object, Object> getDataSources() {
        return dataSources;
    }
}
