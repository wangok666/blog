package bupt.cs.blog.controller;

import bupt.cs.blog.common.aop.LogAnnotation;
import bupt.cs.blog.common.cache.Cache;
import bupt.cs.blog.vo.ArticleVo;
import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.params.ArticleParam;
import bupt.cs.blog.vo.params.PageParams;
import bupt.cs.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    //首页文章列表
    @PostMapping
    //加上此注解，代表要对此接口记录日志
    @LogAnnotation(module="文章", operator="获取文章列表")
    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    //首页最热文章
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000, name = "hot_article")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    //首页最新文章
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000, name = "news_article")
    public Result newArticles() {
        int limit = 5;
        return articleService.newArticles(limit);
    }

    //首页文章归档
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    //文章详情页
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        return articleService.findArticleById(id);
    }

    //文章发布页
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

}
