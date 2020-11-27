package com.hk.es;

import com.hk.es.entity.Item;
import com.hk.es.entity.Person;
import com.hk.es.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author john
 * @date 2019/12/8 - 14:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testCreate() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Person.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        Document document = indexOperations.createMapping(Person.class);
        boolean index = indexOperations.create();
        System.out.println(index);
        Assert.assertTrue(index);
    }

    @Test
    public void deleteEsIndex() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Item.class);
        boolean deleteIndex = indexOperations.delete();
        System.out.println("删除索引结果是" + deleteIndex);
        Assert.assertTrue(deleteIndex);
    }


    @Test
    public void testsave() throws InterruptedException {
        for (int i = 0; i < 6 ; i++) {
            Item item = new Item();
            item.setId(1L+i);
            item.setTitle("我们都是中国人"+i);
            item.setBrand("中国");
            item.setCategory("大中国");
            /*item.setPrice(1.1);*/
            itemRepository.save(item);
        }
    }
    @Test
    public void search() {
        String keyword ="中国";
        Pageable pageable = PageRequest.of(0, 100);
        Page<Item> page = itemRepository.findByTitleOrCategoryOrBrand(keyword, keyword, keyword, pageable);
        System.out.println(page.getContent());
        System.out.println(page.getSize());
    }

}