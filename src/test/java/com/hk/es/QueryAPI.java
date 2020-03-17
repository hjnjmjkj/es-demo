package com.hk.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class QueryAPI {
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
    public void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("field","baz"));
        searchRequest.source(searchSourceBuilder);

        searchRequest.indices("posts");
        searchRequest.types("doc");
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(searchResponse);
    }

    @Test
    public void testAggr() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_age").field("age");
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).aggregation(aggregationBuilder);
        searchRequest.source(searchSourceBuilder);

        searchRequest.indices("test");
        searchRequest.types("user");
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Terms terms = searchResponse.getAggregations().get("by_age");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            Object key = bucket.getKey();
            long docCount = bucket.getDocCount();
            System.out.println(key+":"+docCount);
        }
        System.out.println(searchResponse);
    }


}
