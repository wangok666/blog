package bupt.cs.blog.service;

import bupt.cs.blog.dao.pojo.SysUser;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String pwd);
}
