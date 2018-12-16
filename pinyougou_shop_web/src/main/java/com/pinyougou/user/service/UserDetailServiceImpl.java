package com.pinyougou.user.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全框架认证服务类 完成认证和授权操作
 */
public class UserDetailServiceImpl implements UserDetailsService{

   /* @Reference
    private SellerService sellerService;*/

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //基于商家用户名查询商家数据
        TbSeller seller = sellerService.findOne(username);

        if(seller!=null){
            //只有审核通过的商家才能够正常登陆
            if("1".equals(seller.getStatus())){
                //构建用户权限集合数据
                List<GrantedAuthority> authorities =new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                //参数一：用户名 参数二：密码  参数三：权限集合
                return new User(username,seller.getPassword(),authorities);
            }else {
                return null;
            }

        }else {
            return null;
        }

    }
}
