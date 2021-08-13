package bupt.cs.blog.dao.mapper;

import bupt.cs.blog.dao.dos.Archives;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import bupt.cs.blog.dao.pojo.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

}
