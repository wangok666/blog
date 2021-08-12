package bupt.cs.blog.service;

import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.params.LoginParam;

public interface LoginService {

    Result login(LoginParam loginParam);

    SysUser checkToken(String token);
}
