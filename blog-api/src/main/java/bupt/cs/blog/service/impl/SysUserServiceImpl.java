package bupt.cs.blog.service.impl;

import bupt.cs.blog.dao.mapper.SysUserMapper;
import bupt.cs.blog.dao.pojo.SysUser;
import bupt.cs.blog.service.LoginService;
import bupt.cs.blog.service.SysUserService;
import bupt.cs.blog.vo.ErrorCode;
import bupt.cs.blog.vo.LoginUserVo;
import bupt.cs.blog.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        return Result.success(loginUserVo);
    }
}
