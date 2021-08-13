package bupt.cs.blog.service.impl;

import bupt.cs.blog.dao.mapper.CategoryMapper;
import bupt.cs.blog.dao.pojo.Category;
import bupt.cs.blog.service.CategoryService;
import bupt.cs.blog.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setCategoryName(category.getCategoryName());
        categoryVo.setId(category.getId());
        categoryVo.setAvatar(category.getAvatar());
        return categoryVo;
    }
}
