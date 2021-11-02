package automation.library.dbUtils;

import automation.library.common.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MongoDBHelper {

	static MongoClient mongoClient;
	static MongoDatabase database;
	static String dbNameAsString;

	private MongoDBHelper() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Create the db connection reading the config details from environment config file
	 * /src/test/resources/config/environments/env.properties
	 * read value from - MongoDBurl, MongoDBName
	 */
	public static void createConn() {
		if (mongoClient == null) {

			String propsPath = Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties";

			String uri = Property.getProperty(propsPath, "MongoDBurl");
			dbNameAsString = Property.getProperty(propsPath, "MongoDBName");
			MongoClientURI connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase(dbNameAsString);
		}
	}

	/**
	 * Create the db connection when uri and db name is provided
	 */
	public static void createConn(String uri, String mongoDbName) {
		if (mongoClient == null) {
			dbNameAsString = mongoDbName;
			MongoClientURI connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase(dbNameAsString);
		}
	}

	/**
	 * Create the db connection reading the config details from environment config file using the db prefix
	 * /src/test/resources/config/environments/env.properties
	 * read value from - dbPrefix_MongoDBurl, dbPrefix_MongoDBName
	 */
	public static void createConn(String dbPrefix) {
		if (mongoClient == null) {

			String propsPath = Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties";

			String uri = Property.getProperty(propsPath, dbPrefix+"_MongoDBurl");
			dbNameAsString = Property.getProperty(propsPath, dbPrefix+"_MongoDBName");
			MongoClientURI connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase(dbNameAsString);
		}
	}


	/**
	 * return the collection
	 */
	public static MongoCollection<Document> getCollection(String col) {
		return database.getCollection(col);
	}

	/**
	 * close the collection
	 */
	public static void closeConn() {
		mongoClient.close();
		mongoClient = null;
	}

	/**
	 * drop the input collection
	 */
	public static void dropCollection(List<String> collections) {
		for (String col : collections) {
			MongoCollection<Document> collection = getCollection(col);
			collection.drop();
		}
	}


	/**
	 * insert one document
	 * Note : connection to db must be established
	 */
	public static void insertOne(String col, Document doc) {
		database.getCollection(col).insertOne(doc);
	}


	/**
	 * insert one document
	 * Override method to existing, no need to have separate open/close connection method while using this method
	 * @param doc
	 * @param dbPrefix - db prefix as per config file
	 * @param col - collection name
	 * @param doc - document to be inserted
	 */
	public static void insertOne(String dbPrefix, String col, Document doc) {
		createConn(dbPrefix);
		database.getCollection(col).insertOne(doc);
		closeConn();
	}


	/**
	 * insert many documents
	 * Note : connection to db must be established
	 */
	public static void insertMany(String col, List<? extends Document> docs) {
		database.getCollection(col).insertMany(docs);
	}

	/**
	 * fine one document
	 * Note : connection to db must be established
	 */
	public static String findOne(String col, BasicDBObject query) {
		return database.getCollection(col).find(query).first().toJson();
	}

	/**
	 * return single document 
	 * override method to existing, no need to have separate open/close connection method while using this method
	 * @param dbPrefix - db prefix as per config file
	 * @param col - collection name
	 * @param query - the query filter
	 * @return string in json format
	 */
	public static String findOne(String dbPrefix, String col, BasicDBObject query) {
		try{
			createConn(dbPrefix);
			return database.getCollection(col).find(query).first().toJson();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			closeConn();
		}
	}

	/**
	 * return single document 
	 * @param dbPrefix - db prefix as per config file
	 * @param col - collection name
	 * @param query - the query filter
	 * @return Mongo document
	 */
	public static Document findOneDocument(String dbPrefix, String col, BasicDBObject query) {
		try{
			createConn(dbPrefix);
			return database.getCollection(col).find(query).first();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			closeConn();
		}
	}

	/**
	 * return many document found as per filter
	 */
	public static MongoIterable<Document> findMany(String col, BasicDBObject query) {
		return database.getCollection(col).find(query);
	}

	/**
	 * return all the document for a collection provided
	 */
	public static MongoIterable<Document> getDoc(String col) {
		return database.getCollection(col).find();
	}

	/**
	 * update one document as per query filter
	 */
	public static void updateOne(String col, BasicDBObject query, BasicDBObject updateObj) {
		database.getCollection(col).updateOne(query, updateObj);
	}

	/**
	 * override method to existing, no need to have separate open/close connection method while using this method
	 * @param dbPrefix - db prefix as per config file
	 * @param col - collection name
	 * @param query - the query filter
	 * @param updateObj - a document describing the updates, which may not be null. The update yp apply must include only update operator
	 */
	public static void updateOne(String dbPrefix, String col, BasicDBObject query, BasicDBObject updateObj) {
		database.getCollection(col).updateOne(query, updateObj);
	}

	/**
	 * update many document as per query filter
	 */
	public static void updateMany(String col, BasicDBObject query, BasicDBObject updateObj) {
		database.getCollection(col).updateMany(query, updateObj);
	}

	/**
	 * delete one document as per query filter
	 */
	public static void deleteOne(String col, BasicDBObject query) {
		database.getCollection(col).deleteOne(query);
	}

	/**
	 * delete many document as per query filter
	 */
	public static void deleteMany(String col, BasicDBObject query) {
		database.getCollection(col).deleteMany(query);
	}

	/**
	 * creates an Object to pass on as query (filter) in the mongo documnet methods. Equivalent to and condition in SQL
	 * @param jsonObject input json Object
	 * @return and query filter
	 */
	public BasicDBObject andQuery(JSONObject jsonObject){
		if(jsonObject !=null){
			BasicDBObject andQuery = new BasicDBObject();
			andQuery.put("$and", queryHelper(jsonObject));
			return andQuery;
		} else {
			return null;
		}
	}

	/**
	 * creates an Object to pass on to Bson update in the mongo document update method
	 * @param jsonObject input json Object
	 * @return and query filter
	 */
	public BasicDBObject setQuery(JSONObject jsonObject){
		if(jsonObject !=null){
			BasicDBObject setData = new BasicDBObject();
			setData.put("$set", setDataHelper(jsonObject));
			return setData;
		} else {
			return null;
		}
	}

	/**
	 * return the BasicDBObject created from input JSONObject
	 */
	public BasicDBObject setDataHelper(JSONObject jsonObject) {

		JSONObject obj2 = new JSONObject();
		obj2.put("foo","bar");

		BasicDBObject obj = new BasicDBObject();
		Iterator<?> keys = jsonObject.keys();
		while(keys.hasNext()){
			String key = (String) keys.next();
			obj.put(key,jsonObject.get(key));
		}
		return obj;
	}

	/**
	 * return the list of BasicDBObject created from JSONObject
	 */
	public List<BasicDBObject> queryHelper(JSONObject jsonObject) {
		List<BasicDBObject> obj = new ArrayList<>();
		Iterator<?> keys = jsonObject.keys();
		while(keys.hasNext()){
			String key = (String) keys.next();
			obj.add(new BasicDBObject(key,jsonObject.get(key)));
		}
		return obj;
	}
}
