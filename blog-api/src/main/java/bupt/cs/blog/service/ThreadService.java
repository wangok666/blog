package bupt.cs.blog.service;

import bupt.cs.blog.dao.mapper.ArticleMapper;
import bupt.cs.blog.dao.pojo.Article;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    //期望此操作在线程池中进行，不会影响主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCount = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCount + 1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //设置一个 为了在多线程的环境下 线程安全
        updateWrapper.eq(Article::getViewCounts, viewCount);
        //update article set view_count=100 where view_count=99 and id=11
        articleMapper.update(articleUpdate, updateWrapper);
    }
}
