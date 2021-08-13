package bupt.cs.blog.service;

import bupt.cs.blog.vo.ArticleVo;
import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.params.ArticleParam;
import bupt.cs.blog.vo.params.PageParams;

public interface ArticleService {

    //分页查询文章列表
    Result listArticle(PageParams pageParams);

    //最热文章
    Result hotArticle(int limit);

    //最新文章
    Result newArticles(int limit);

    //文章归档
    Result listArchives();

    //查询文章详情
    Result findArticleById(Long id);

    //发布文章
    Result publish(ArticleParam articleParam);
}
