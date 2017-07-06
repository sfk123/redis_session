package com.sheng.example;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * Created by Administrator on 2017/6/30 0030.
 */
public class MyRealm extends AuthorizingRealm {
    /**
     * 授权方法:
     * 1. 实际返回的是 SimpleAuthorizationInfo 类的实例
     * 2. 可以调用 SimpleAuthorizationInfo 的 addRole 来添加当前登录 user 的权限信息.
     * 3. 可以调用 PrincipalCollection 参数的 getPrimaryPrincipal() 方法来获取用户信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Object principal = principalCollection.getPrimaryPrincipal();
//        User user = userService.getAdmin(principal.toString());
//		if("admin".equals(principal)){
//        info.addRole(user.getRole().getName());
//			System.out.println("用户角色admin");
//			info.addRole("list");
//		}
//		if("user".equals(principal)){
//			info.addRole("list");
//		}

//		info.addRole("user");

        return info;
    }

    /**
     * 认证方法
     * 1. 编写表单: 表单的 action、和 username、password 的参数都是什么 ?
     * 回答: 提交到你想提交的地方, username 和 password 也参数名称都任意.
     * 2. 例如, 提交到了一个 SpringMVC 的 handler:
     * 1). 获取用户名、密码
     * 2).
     * Subject currentUser = SecurityUtils.getSubject();
     * UsernamePasswordToken token = new UsernamePasswordToken(username, password);
     * currentUser.login(token);
     * 3. 当 Subject 调用 login 方法时, 即会触发当前的 doGetAuthenticationInfo 方法. 且把
     * UsernamePasswordToken 对象传入, 然后再该方法中执行真正的认证: 访问数据库进行比对.
     * 1). 获取用户名和密码
     * 2).
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        System.out.println("doGetAuthenticationInfo------->"+token.getPrincipal());
        System.out.println("doGetAuthenticationInfo------->"+token.getCredentials());

        //1. 从 token 中获取登录的 username! 注意不需要获取 password.

        //2. 利用 username 查询数据库得到用户的信息.

        //3. 创建 SimpleAuthenticationInfo 对象并返回. 注意该对象的凭证式从数据库中查询得到的.
        //而不是页面输入的. 实际的密码校验可以交由 Shiro 来完成

        //4. 关于密码加密的事: shiro 的密码加密可以非常非常的复杂, 但实现起来却可以非常简单.
        //1). 可以选择加密方式: 在当前的 realm 中编写一个 public 类型的不带参数的方法, 使用 @PostConstruct
        //注解进行修饰, 在其中来设置密码的匹配方式.
        //2). 设置盐值: 盐值一般是从数据库中查询得到的.
        //3). 调用 new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName)
        //构造器返回 SimpleAuthenticationInfo 对象:  credentialsSalt 为
        //ByteSource credentialsSalt = new Md5Hash(source);

        //登陆的主要信息: 可以是一个实体类的对象, 但该实体类的对象一定是根据 token 的 username 查询得到的.
        Object principal = token.getPrincipal();//获取登录名
        //认证信息: 从数据库中查询出来的信息. 密码的比对交给 shiro 去进行比较
        String credentials = "789ecdde95405b37ffafd9c4e460b4a9";
        //设置盐值:
        String source = "abcdefg";
        ByteSource credentialsSalt = new Md5Hash(source);
//		System.out.println(credentialsSalt);
//        User atx_admin=null;
//        try {
//            atx_admin = userService.getAdmin(token.getPrincipal().toString());
//            if(atx_admin.getRole().getName().equals("park_station")){//数据库不加密前台人员密码
//                atx_admin.setPassword(strToMD5(atx_admin.getPassword()));
//            }
////			System.out.println("数据库密码：" + atx_admin.getPassword());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if(atx_admin==null)
//            throw new AuthenticationException("用户名不存在");
        //当前 Realm 的 name
        String realmName = getName();
//		System.out.println("realmName:"+realmName);
        SimpleAuthenticationInfo info =
                new SimpleAuthenticationInfo(principal, credentials,
                        credentialsSalt, realmName);

        return info;
    }

    //@PostConstruct: 相当于 bean 节点的 init-method 配置.
    public void setCredentialMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();

        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);

        setCredentialsMatcher(credentialsMatcher);
    }
    public static String strToMD5(String str){
        String saltSource = "abcdefg";

        String hashAlgorithmName = "MD5";//加密方式
        Object salt = new Md5Hash(saltSource);//盐值
        int hashIterations = 1024;//加密次数

        //加密后的密码
        Object result = new SimpleHash(hashAlgorithmName, str, salt, hashIterations);
        return result.toString();
    }
    public static void main(String[] args) {
        String saltSource = "abcdefg";

        String hashAlgorithmName = "MD5";//加密方式
        String credentials = "123";//密码
        Object salt = new Md5Hash(saltSource);//盐值
        int hashIterations = 1024;//加密次数

        //加密后的密码
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
        System.out.println("68f3139a38b232392cc9d3b6ddd762f7".equals(result.toString()));
    }
}
