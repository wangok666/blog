package bupt.cs.blog.dao.mapper;

import bupt.cs.blog.dao.dos.Archives;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import bupt.cs.blog.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();
}
