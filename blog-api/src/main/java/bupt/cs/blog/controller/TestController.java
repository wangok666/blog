package bupt.cs.blog.controller;

import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.utils.UserThreadLocal;
import bupt.cs.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}