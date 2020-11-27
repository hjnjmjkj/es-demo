package com.hk.es.repository;

import com.hk.es.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository  extends ElasticsearchRepository<Item,Long> {
}
