package bupt.cs.blog.service;

import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);
}
