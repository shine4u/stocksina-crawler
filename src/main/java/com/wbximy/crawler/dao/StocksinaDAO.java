package com.wbximy.crawler.dao;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wbximy.crawler.domain.stocksina.Stock;
import com.wbximy.crawler.exception.TableNotExistException;
import com.wbximy.crawler.mapping.stocksina.StockMapper;
import com.wbximy.crawler.mapping.stocksina.TableMapper;

import lombok.Setter;

public class StocksinaDAO {
	Logger logger = Logger.getLogger(StocksinaDAO.class);

	@Setter private TableMapper tableMapper;
	@Setter private StockMapper stockMapper;
	
	private static Set<String> checkedTables = new HashSet<String>();
	
	private boolean existTable(String tablename) {
		return tableMapper.exist(tablename) > 0;
	}
	
	public void writeStock(Stock stock) throws TableNotExistException {
		String tablename = Stock.class.getSimpleName();
		if (!checkedTables.contains(tablename)) {
			checkedTables.add(tablename);
			logger.info("checked for table creation tablename=" + tablename);
			if (!existTable(tablename)) {
				/*String sql = "CREATE TABLE " + tablename + "(";
				for (String col : Stock.fieldRules.keySet()) {
					sql += "\n" + col + " " + Stock.fieldRules.get(col) + ",";
				}
				sql += "\nPRIMARY KEY(" + Stock.primaryKeyRule + ")";
				sql += "\n);";
				logger.warn("It is not a proper way to create table in mybatis.");
				logger.warn("You should create a table before use it.");
				logger.info("create table sql=" + sql);
				tableMapper.create(sql);
				*/
				throw new TableNotExistException(tablename);
			}	
		}
		
		if (stockMapper.selectOne(stock.getStockCode()) != null) {
			logger.info("write stock but exists, ignore." + stock.getStockCode());
			return ;
		}
		logger.info("insert stock " + stock);
		stockMapper.insert(stock);
	}
	
}
