package automation.library.dbUtils;

import automation.library.common.Property;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ElasticHelper {

	RestHighLevelClient client;

	public ElasticHelper() {
		this(Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", "ESurl"),
				Integer.parseInt(Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", "ESport")));
	}
	
	public ElasticHelper(String hostname, int port) {
		client = new RestHighLevelClient(
			        RestClient.builder(new HttpHost(hostname, port, "http"))
			    );		
	}
	
	/**
	 * Closes the connection if one exist
	 */
	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			System.err.println("Was unable to close connect to ElasticSearch: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * This gets a search results for the search
	 */
	public SearchResponse search(SearchRequest searchRequest) throws IOException {
		SearchResponse searchResponse = client.search(searchRequest);
		return searchResponse;
	}
	
	/**
	 * Gets a single document
	 */
	public GetResponse getDocument(String index, String type, String id) throws IOException {
		GetResponse searchResponse = client.get(new GetRequest(index,type,id));
		return searchResponse;
	}
	
	/**
	 * Updates a document using a JSON input
	 */
	public UpdateResponse updateDocument(String index, String type, String id, String json) throws IOException {
		UpdateRequest ur = new UpdateRequest(index, type, id);
		ur.doc(json, XContentType.JSON);
		return client.update(ur);
	}
	
	/**
	 * Creates a document in the index using the generated id
	 */
	public IndexResponse insertDocumenet(String index, String type, String json) throws IOException {
		IndexRequest ir = new IndexRequest(index, type);
		ir.source(json, XContentType.JSON);
		return client.index(ir);
	}
	
	/**
	 * Creates a document in the index and setting the id
	 */
	public IndexResponse insertDocumenet(String index, String type, String id, String json) throws IOException {
		IndexRequest ir = new IndexRequest(index, type, id);
		ir.source(json, XContentType.JSON);
		return client.index(ir);
	}
	
	/**
	 * Deletes a document using the id
	 */
	public DeleteResponse deleteDocument(String index, String type, String id) throws IOException {
		DeleteRequest request = new DeleteRequest(index, type, id);
		return client.delete(request);
	}
	
	/**
	 * Creates a new index with no settings
	 */
	public CreateIndexResponse createIndex(String index) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(index);
		return client.indices().create(request);
	}
	
	/**
	 * Creates a new index passing in the index request
	 */
	public CreateIndexResponse createIndex(CreateIndexRequest request) throws IOException {
		return client.indices().create(request);
	}
	
	/**
	 * Delete index using request
	 */
	public DeleteIndexResponse deleteIndex(DeleteIndexRequest  request) throws IOException {
		return client.indices().delete(request);
	}
	
	/**
	 * Delete index using request
	 */
	public DeleteIndexResponse deleteIndex(String index) throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(index);
		return client.indices().delete(request);
	}
	
	/**
	 * Returns the clinet so it can be used in more complex ways, such as scroll search
	 */
	public RestHighLevelClient getClient() {
		return client;
	}
}
