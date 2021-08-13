package bupt.cs.blog.service;

import bupt.cs.blog.vo.CategoryVo;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);
}
