/**
 *
 *
 * @className:com.xtwl.jy.base.utils.ImageCacheUtils
 * @description:数据库管理类
 * 
 * @version:v1.0.0 
 * @author:SHENYANG
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-9-23     SHENYANG       v1.0.0        create
 *
 *
 */
/**
 * 
 */
package com.resmanager.client.system;

import java.util.ArrayList;

import com.resmanager.client.model.CustomerModel;
import com.resmanager.client.utils.ContactsUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	SQLiteDatabase db;
	private static final int version = 1;
	private static final String CUSTOMER_DB = "CREATE TABLE [customer] (customerid varchar NOT NULL PRIMARY KEY,customername varchar,sortLetters varchar)";

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		super(context, ContactsUtils.DB_NAME, null, version);
		// 得到数据库操作类
		db = getWritableDatabase();
	}

	/**
	 * 
	 * @Description:插入客户表
	 * @param typeKey
	 * @param searchName
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-6 上午10:25:51
	 */
	public void insertToCustomer(String customerid, String customername, String sortLetters) {
		// 先查询数据库是否已经存在该客户
		// Cursor cursor = db.query("customer", new String[] {"customerid"},
		// "customerid=?", new String[] { customerid }, null, null, null);
		// if (cursor.getCount() == 0) {
		ContentValues values = new ContentValues();
		values.put("customerid", customerid);
		values.put("customername", customername);
		values.put("sortLetters", sortLetters);
		db.insert("customer", null, values);
		// }
	}

	/**
	 * 
	 * @Title: getCustomerModels
	 * @Description: 查找客户
	 * @param @return 设定文件
	 * @return ArrayList<CustomerModel> 返回类型
	 * @throws
	 */
	public ArrayList<CustomerModel> getCustomerModels() {
		Cursor cursor = db.query("customer", new String[] { "customerid", "customername", "sortLetters" }, null, null, null, null, null, null);
		ArrayList<CustomerModel> customerModels = new ArrayList<CustomerModel>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CustomerModel customerModel = new CustomerModel();
			customerModel.setCustomerID(cursor.getString(cursor.getColumnIndex("customerid")));
			customerModel.setCustomerName(cursor.getString(cursor.getColumnIndex("customername")));
			customerModel.setSortLetters(cursor.getString(cursor.getColumnIndex("sortLetters")));
			customerModels.add(customerModel);
		}
		return customerModels;
	}

	public void deleteTable() {
		String sql = "delete from customer";
		db.execSQL(sql);
	}

	/**
	 * 
	 * @Description:删除搜索历史记录
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-28 下午2:17:04
	 */
	public void deleteSearchCache() {
		db.delete("search_history", null, null);
	}

	@Override
	public void onCreate(SQLiteDatabase _db) {
		_db.execSQL(CUSTOMER_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
		if (oldVersion < version) {
			_db.execSQL("DROP TABLE customer");
			_db.execSQL(CUSTOMER_DB);
		}
	}

	// 关闭数据库
	public void close() {
		db.close();
	}

	// 执行SQL语句
	public void execSql(String sql) {
		if (!sql.equals("")) {
			db.execSQL(sql);
		}
	}

}
