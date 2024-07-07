package com.ryanverse.ojstar.esdao;

import com.ryanverse.ojstar.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author Haoran
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

	List<PostEsDTO> findByUserId (Long userId);
}