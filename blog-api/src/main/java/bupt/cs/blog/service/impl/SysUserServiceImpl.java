package bupt.cs.blog.service.impl;

import bupt.cs.blog.dao.mapper.SysUserMapper;
import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public SysUser findUserById(Long id) {
       SysUser sysUser = sysUserMapper.selectById(id);
       if (sysUser == null) {
           sysUser = new SysUser();
           sysUser.setNickname("西土城首富王广鱼");
       }
       return sysUser;
    }
}
