package bupt.cs.blog.service.impl;

import bupt.cs.blog.dao.dos.Archives;
import bupt.cs.blog.dao.mapper.ArticleBodyMapper;
import bupt.cs.blog.dao.mapper.ArticleMapper;
import bupt.cs.blog.dao.mapper.ArticleTagMapper;
import bupt.cs.blog.dao.pojo.Article;
import bupt.cs.blog.dao.pojo.ArticleBody;
import bupt.cs.blog.dao.pojo.ArticleTag;
import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.service.*;
import bupt.cs.blog.utils.UserThreadLocal;
import bupt.cs.blog.vo.*;
import bupt.cs.blog.vo.params.ArticleParam;
import bupt.cs.blog.vo.params.PageParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ThreadService threadService;

    //因为需要用到查询year month的操作，而mybatisplus不支持此操作
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPagesize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true, true));
    }



//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1.分页查询article数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPagesize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//
//        if (pageParams.getCategoryId() != null){
//            // and category_id = #{category_id}
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//
//        if (pageParams.getTagId() != null) {
//            List<Long> articleIdList = new ArrayList<>();
//            //加入标签 条件查询
//            //article表中并没有tag字段 一篇文章有多个标签
//            //article_tag article_id 1 : n tag_id
//            if (pageParams.getTagId() != null) {
//                LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//                articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//                List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//                for (ArticleTag articleTag : articleTags) {
//                    articleIdList.add(articleTag.getArticleId());
//                }
//                if (articleIdList.size() > 0){
//                    //in [1,2,3,4]
//                    queryWrapper.in(Article::getId, articleIdList);
//                }
//
//            }
//        }
//
//        //按照是否置顶和创建时间进行排序
//        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//
//        //不能直接返回该record，不符合页面格式要求
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));

    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long id) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyid和categoryid 去关联查询
         */
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true, true, true);
        //查看完文章了，增加阅读数的问题
        //查看完文章之后，本应该直接返回数据，这时候做了一个更新操作，更新时加写锁，阻塞其他的
        //读操作，性能就会比较低
        //更新增加了此次接口的耗时 如果一旦更新出现问题 不能影响查看文章的操作
        //线程池 可以把更新操作 扔到线程池中去执行，和主线程就不相关了

        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    @Transactional
    @Override
    public Result publish(ArticleParam articleParam) {
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id 当前的登录用户
         * 3. 标签 要将标签加入到 关联列表当中
         * 4. body内容存储
         */
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        articleMapper.insert(article);

        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){

            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());

        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    //把相同的属性copy
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        //创建时间copy不过来
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-ddd HH:mm"));

        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
