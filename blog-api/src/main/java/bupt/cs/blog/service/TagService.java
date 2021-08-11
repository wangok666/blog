package bupt.cs.blog.service;

import bupt.cs.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

}
