package bupt.cs.blog.service;

import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findAllDetailById(long id);
}
