package bupt.cs.blog.service.impl;

import bupt.cs.blog.dao.mapper.SysUserMapper;
import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.service.LoginService;
import bupt.cs.blog.service.SysUserService;
import bupt.cs.blog.vo.ErrorCode;
import bupt.cs.blog.vo.LoginUserVo;
import bupt.cs.blog.vo.Result;
import bupt.cs.blog.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;
    @Override
    public SysUser findUserById(Long id) {
       SysUser sysUser = sysUserMapper.selectById(id);
       if (sysUser == null) {
           sysUser = new SysUser();
           sysUser.setNickname("西土城首富王广鱼");
       }
       return sysUser;
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("西土城首富王广鱼");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findUser(String account, String pwd) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, pwd);
        queryWrapper.select(SysUser::getId, SysUser::getAccount,
                SysUser::getAvatar, SysUser::getNickname);
        //限制为只查询一次，如果查到了就不再继续查下去,相当于在sql的最后加上limit语句
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;


    }

    /**
     * 1. token合法性校验
     *  是否为空 解析是否成功 redis是否存在
     * 2. 如果校验失败，返回错误
     * 3. 如果成功，返回对应的结果 LoginUserVo
     * @param token
     * @return
     */
    @Override
    public Result getUserInfoByToken(String token) {
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        //注意 默认生成的id 是分布式id 采用了雪花算法
        this.sysUserMapper.insert(sysUser);
    }
}
