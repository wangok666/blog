package bupt.cs.blog.service;

import bupt.cs.blog.vo.CategoryVo;
import bupt.cs.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    //下拉所有标签
    Result findall();

}
