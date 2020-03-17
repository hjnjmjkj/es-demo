package com.hk.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DocumentAPIs {
    RestHighLevelClient client;

    @Before
    public void before(){
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

    }
    @After
    public void after() throws IOException {
        client.close();
    }

    @Test
    public void testIndex() throws IOException {

        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        indexResponse.getId();
    }
    @Test
    public void testIndex2() throws IOException {

        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "1");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
                .source(jsonMap);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
    }
    @Test
    public void testIndex3() throws IOException {

        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "1");
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
                .source(builder);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    public void testGet() throws IOException {
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSource());
    }

    @Test
    public void testExist() throws IOException {
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    public void testUpdate() throws IOException {
        UpdateRequest request = new UpdateRequest("posts", "doc", "1");
        String jsonString = "{" +
                "\"updated\":\"2017-01-01\"," +
                "\"reason\":\"daily update\"" +
                "}";
        request.doc(jsonString, XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        System.out.println(updateResponse);
    }

    @Test
    public void testDelete() throws IOException {
        DeleteRequest request = new DeleteRequest(
                "posts",
                "doc",
                "1");
        DeleteResponse deleteResponse = client.delete(
                request, RequestOptions.DEFAULT);

        System.out.println(deleteResponse);
    }

    @Test
    public void testBulk() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("posts", "doc", "1")
                .source(XContentType.JSON,"field", "foo"));
        request.add(new IndexRequest("posts", "doc", "2")
                .source(XContentType.JSON,"field", "bar"));
        request.add(new IndexRequest("posts", "doc", "3")
                .source(XContentType.JSON,"field", "baz"));

        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

        System.out.println(bulkResponse.hasFailures());


        System.out.println(bulkResponse);
    }
}
