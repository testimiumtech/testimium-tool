package com.testimium.tool.datasource.connector.dao;

import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.datasource.connector.register.DataSourceRegistry;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.DBException;
import com.testimium.tool.logging.LogUtil;
import org.hibernate.*;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.transform.Transformers;

import java.util.List;
import java.util.Map;

/**
 *  Generic DAO Class to execute hibernate SQL queries.
 *  
 * @author SAndeep Agrawal
 *
 */

public class VividDataDaoImpl implements IVividDataDao{

	/*@Autowired
	private ConnectorRegistry connectorRegistry;*/
	/*@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDataList(String queryString) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createSQLQuery(queryString);

		return query.list();
	}*/
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> executeQuey(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException {
		LogUtil.logTestCaseMsg("ConnectorKey========="+connectorKey);
		Session session = null;
		List<Map<String, Object>>  list = null;
		try {
			SessionFactory sessionFactory = (SessionFactory) DataSourceRegistry.getConnector(connectorKey);
			session = sessionFactory.openSession();
			//session.setFlushMode(FlushMode.MANUAL);
			//ManagedSessionContext.bind(session);
			//session.beginTransaction();
			Query query = session.createSQLQuery(sqlQuery);
			list =  query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			//ManagedSessionContext.unbind(sessionFactory);
			
		} catch (SQLGrammarException gex) {
			LogUtil.logTestCaseErrorMsg("SQLGrammarException in VividDataDaoImpl class: " + gex.getMessage(), gex);
			throw new DBException("SQLGrammarException for sqlQuery: " + sqlQuery + "\n" + gex.getMessage(), gex);
		} catch (HibernateException hex){
			LogUtil.logTestCaseErrorMsg("HibernateException in VividDataDaoImpl class: " + hex.getMessage(), hex);
			throw new DBException("HibernateException for sqlQuery: " + sqlQuery + "\n" + hex.getMessage(), hex);
		} finally {
			//session.flush();
			try {
				session.close();
			} catch( Exception ex){
				LogUtil.logTestCaseErrorMsg("WhRile Closing inside finally block in VividDataDaoImpl class: " + ex.getMessage(), ex);
			}
		}
		return list;
	}

	@Override
	public int executeUpdate(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException {
		LogUtil.logTestCaseMsg("ConnectorKey========="+connectorKey);
		Session session = null;
		int result = 0;
		List<Map<String, Object>>  list = null;
		try {
			SessionFactory sessionFactory = (SessionFactory) DataSourceRegistry.getConnector(connectorKey);
			session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.MANUAL);
			ManagedSessionContext.bind(session);
			session.beginTransaction();
			Query query = session.createSQLQuery(sqlQuery);
			result =  query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).executeUpdate();
			ManagedSessionContext.unbind(sessionFactory);
		} catch (SQLGrammarException gex) {
			LogUtil.logTestCaseErrorMsg("SQLGrammarException in VividDataDaoImpl class: " + gex.getMessage(), gex);
			throw new DBException("SQLGrammarException for sqlQuery: " + sqlQuery + "\n" + gex.getMessage(), gex);
		} catch (HibernateException hex){
			LogUtil.logTestCaseErrorMsg("HibernateException in VividDataDaoImpl class: " + hex.getMessage(), hex);
			throw new DBException("HibernateException for sqlQuery: " + sqlQuery + "\n" + hex.getMessage(), hex);
		} finally {
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
		return result;
	}

	@Override
	public void execute(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException {
		LogUtil.logTestCaseMsg("ConnectorKey========="+connectorKey);
		Session session = null;
		try {
			SessionFactory sessionFactory = (SessionFactory) DataSourceRegistry.getConnector(connectorKey);
			session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.MANUAL);
			ManagedSessionContext.bind(session);
			session.beginTransaction();
			Query query = session.createSQLQuery(sqlQuery);
			query.executeUpdate();
			ManagedSessionContext.unbind(sessionFactory);
		} catch (SQLGrammarException gex) {
			LogUtil.logTestCaseErrorMsg("SQLGrammarException in VividDataDaoImpl.executeSQL() method: " + gex.getMessage(), gex);
			throw new DBException("SQLGrammarException for sqlQuery: " + sqlQuery + "\n" + gex.getMessage(), gex);
		} catch (HibernateException hex){
			LogUtil.logTestCaseErrorMsg("HibernateException in VividDataDaoImpl.executeSQL() method: " + hex.getMessage(), hex);
			throw new DBException("HibernateException for sqlQuery: " + sqlQuery + "\n" + hex.getMessage(), hex);
		} finally {
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
	}

	/*@Override
	public List<Map<String, Object>> executeExcelQuery(String sheetName, String sqlQuery, ConnectorKey connectorKey) throws Exception {
		System.setProperty("ROW", "1");
		System.setProperty("COLUMN", "0");//Table start column
		Connection connection = ((Fillo)ConnectorRegistry.getConnector(connectorKey)).getConnection("C:\\Sandeep\\Projects\\testimium\\POC\\Selenium\\codebase\\SmartTestingToolFramework\\sttf\\SmartTestingFramework\\testcases\\Customers.xlsx");
		String strQuery="Select Name from Customer";
		connection.executeQuery(strQuery);
		*//*while(recordset.next()){
			System.out.println(recordset.getField("TestCase Name"));
		}
		recordset.close();*//*
		connection.close();
		return null;
	}*/

	public static void main(String[] args) throws Exception {
		ConnectorKey key = new ConnectorKey();
		key.setServiceName("testimium");
		key.setConnectorName("sql.server.dev");
		DataSourceRegistry.buildConnectorMap();
		//new VividDataDaoImpl().executeExcelQuery("Employee", "", key);
		//List<Map<String, Object>> list = new VividDataDaoImpl().executeSQLQuey("Select * from TB_MODULE", key);
		List<Map<String, Object>> list = new VividDataDaoImpl().executeQuey("SELECT FIRST_NAME, LAST_NAME FROM TB_CUSTOMERS", key);
		//List<Map<String, Object>> list = new VividDataDaoImpl().executeQuey("Update TB_MODULE set IS_USED='N' WHERE MODULE_NAME = 'MIN_MAX'", key);
		System.out.println("=================="+list.size());
		Thread.sleep(30000);
		list = new VividDataDaoImpl().executeQuey("SELECT FIRST_NAME, LAST_NAME FROM TB_CUSTOMERS", key);
		//List<Map<String, Object>> list = new VividDataDaoImpl().executeQuey("Update TB_MODULE set IS_USED='N' WHERE MODULE_NAME = 'MIN_MAX'", key);
		System.out.println("=================="+list.size());
	}


}
