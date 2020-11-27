package com.hk.es.repository;

import com.hk.es.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository  extends ElasticsearchRepository<Item,Long> {
    Page<Item> findByTitleOrCategoryOrBrand(String keyword, String keyword1, String keyword2, Pageable pageable);
}
