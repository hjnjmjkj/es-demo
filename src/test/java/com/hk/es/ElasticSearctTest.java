package com.hk.es;

import com.hk.es.entity.Item;
import com.hk.es.entity.Person;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author john
 * @date 2019/12/8 - 14:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearctTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    public void testCreate() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        elasticsearchOperations.createIndex(Item.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        elasticsearchOperations.putMapping(Item.class);
    }

    @Test
    public void testsave() throws InterruptedException {
        for (int i = 0; i < 6 ; i++) {
            Thread.sleep(1000*10);
            Item item = new Item();
            item.setId(1L+i);
            item.setTitle("我们都是中国人"+i);
            item.setPrice(1.1);

            if(!elasticsearchRestTemplate.indexExists(item.getClass())){
                System.out.println("create index at "+ DateUtil.now());
                elasticsearchRestTemplate.createIndex(item.getClass());
                elasticsearchRestTemplate.putMapping(item.getClass());
            }
            System.out.println("save doc at "+DateUtil.now());
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(String.valueOf(item.getId()))
                    .withObject(item)
                    .build();
            String documentId = elasticsearchOperations.index(indexQuery);
            System.out.println("-----save-----");
        }
    }

    // 新增一条文档
    @Test
    public void createItem(){
        Item item = new Item();
        item.setId(1L);
        item.setTitle("我们都是中国人");
        item.setPrice(1.1);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(String.valueOf(item.getId()))
                .withObject(item)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery);
        System.out.println(documentId);
    }

    // 根据id查询一条文档
    @Test
    public void getPersonById() {
        Person p = elasticsearchRestTemplate.queryForObject(GetQuery.getById(String.valueOf(1)), Person.class);
        System.out.println(p);
    }

    // 新增一条文档
    @Test
    public void createPerson() {
        Person person = new Person();
        person.setId(1);
        person.setAge(2);
        person.setName("huangkai");
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(String.valueOf(person.getId()))
                .withObject(person)
                .build();
        String documentId = elasticsearchRestTemplate.index(indexQuery);
        System.out.println(documentId);
    }
}