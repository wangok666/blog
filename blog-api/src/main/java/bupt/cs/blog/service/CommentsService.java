package bupt.cs.blog.service;

import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.params.CommentParam;

public interface CommentsService {

    //根据文章id查询评论
    Result commentsByArticleId(Long articleId);

    //对文章就行评论
    Result comment(CommentParam commentParam);
}
