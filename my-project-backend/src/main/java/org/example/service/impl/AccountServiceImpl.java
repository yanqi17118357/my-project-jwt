package org.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.entity.dto.Account;
import org.example.entity.vo.request.EmailRegisterVO;
import org.example.mapper.AccountMapper;
import org.example.service.AccountService;
import org.example.utils.Const;
import org.example.utils.FlowUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    FlowUtils flowUtils;

    @Resource
    PasswordEncoder encoder;

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User.withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimits(ip))
                return "请求频繁，请稍后再试";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = Const.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码错误，请重新输入";
        if (this.existAccountByEmail(email)) return "此电子邮件已被注册，请更换电子邮件";
        if (this.existAccountByUsername(username)) return "此用户名已被使用，请更换用户名";
        String password =encoder.encode(vo.getPassword());
        Account account = new Account(null, vo.getUsername(), password, email, "user", new Date());
        if (this.save(account)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    private boolean existAccountByEmail(String email) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }

    private boolean existAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", username));
    }

    public Account findAccountByNameOrEmail(String text) {
        return this.query()
                .eq("username", text).or()
                .eq("email", text)
                .one();
    }

    private boolean verifyLimits(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }
}
