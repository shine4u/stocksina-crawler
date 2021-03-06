package com.wbximy.crawler.dao;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wbximy.crawler.exception.TableNotExistException;
import com.wbximy.crawler.stocksina.domain.Stock;
import com.wbximy.crawler.stocksina.domain.StockHolder;
import com.wbximy.crawler.stocksina.mapping.StockHolderMapper;
import com.wbximy.crawler.stocksina.mapping.StockMapper;
import com.wbximy.crawler.stocksina.mapping.TableMapper;

import lombok.Setter;

public class StocksinaDAO {
	Logger logger = Logger.getLogger(StocksinaDAO.class);

	@Setter private TableMapper tableMapper;
	@Setter private StockMapper stockMapper;
	@Setter private StockHolderMapper stockHolderMapper;
	
	private static Set<String> checkedTables = new HashSet<String>();
	
	private boolean existTable(String tablename) {
		return tableMapper.exist(tablename) > 0;
	}
	
	private void checkTableExist(String tablename) {
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
				try {
					throw new TableNotExistException(tablename);
				} catch (TableNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		
	}
	
	public void writeStock(Stock stock) throws TableNotExistException {
		checkTableExist(Stock.class.getSimpleName());
		
		if (stockMapper.selectOne(stock.getStockCode()) != null) {
			logger.info("write stock but exists, ignore." + stock);
			return ;
		}
		logger.info("insert stock " + stock);
		stockMapper.insert(stock);
	}

	public void writeStockHolder(StockHolder holder) {
		// TODO Auto-generated method stub
		checkTableExist(StockHolder.class.getSimpleName());
		if (stockHolderMapper.selectOne(holder.getStockCode(), holder.getHolderName()) 	!= null) {
			logger.info("write stockHolder but exists, ignore." + holder.getStockCode() + " " + holder.getHolderName());
			return ;
		}
		logger.info("insert stockHolder " + holder);
		stockHolderMapper.insert(holder);
	}
	
}
