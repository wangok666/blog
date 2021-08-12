package bupt.cs.blog.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysUser {
    //不选用数据库自增类型是因为以后用户数量多了要进行分表操作，id需要用分布式id
//    @TableId(type = IdType.ASSIGN_ID)//默认id类型
//    @TableId(type = IdType.AUTO)//数据库自增类型id

    private Long id;

    private String account;

    private Integer admin;

    private String avatar;

    private Long createDate;

    private Integer deleted;

    private String email;

    private Long lastLogin;

    private String mobilePhoneNumber;

    private String nickname;

    private String password;

    private String salt;

    private String status;
}