package bupt.cs.blog.service;

import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.vo.Result;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String pwd);

    Result getUserInfoByToken(String token);

    SysUser findUserByAccount(String account);

    //保存新用户
    void save(SysUser sysUser);
}
