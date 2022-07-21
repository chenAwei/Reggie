package com.bdqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bdqn.common.R;
import com.bdqn.utils.ValidateCodeUtils;
import com.bdqn.pojo.User;
import com.bdqn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        log.info(user.toString());
        if (phone != null){
            String code = ValidateCodeUtils.generateValidateCode4String(6).toString();
            log.info("code={}",code);
            session.setAttribute(phone,code);
            return R.success("短信发送成功！");
        }
        return R.error("短信发送失败！");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object checkSession = session.getAttribute(phone);
        if (checkSession != null && code.equals(checkSession)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败！");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session){
        Long user = (Long) session.getAttribute("user");
        if (user != null){
            session.removeAttribute("user");
        }
        return R.success("退出登录成功！");
    }

}
