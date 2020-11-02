package cc.mrbird.febs.common.weChat;

import cc.mrbird.febs.chaoyang3team.domain.OpenidModifyRecord;
import cc.mrbird.febs.chaoyang3team.service.OpenidModifyRecordService;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.authentication.JWTToken;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.ActiveUser;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.system.domain.LoginLog;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.LoginLogService;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author CHExN
 */
@Slf4j
@Validated
@RestController
@RequestMapping("weChatLogin")
public class WeChatLogin extends BaseController {

    private static final String APPID = "wx43b2f24732c20897";
    private static final String SECRET = "331ec343c41e9fe42af03d4faabc49a6";
    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session";

    // oAmL05HDAW_346Ov6f2pSNzUCICc

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private FebsProperties properties;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OpenidModifyRecordService openidModifyRecordService;

    @PostMapping("login")
    @Limit(key = "login", period = 60, count = 15, name = "登录接口", prefix = "limit")
    public FebsResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String type,
            @NotBlank(message = "{required}") String code, HttpServletRequest request) throws Exception {
        // 不为null则将字符串转换为小写
        username = StringUtils.lowerCase(username);
        password = MD5Util.encrypt(username, password);
        final String ERROR = "用户名或密码错误";
        User user = this.userManager.getUser(username);

        if (user == null)
            throw new FebsException(ERROR);
        if (!(user.getType().equals(User.TYPE_UNIVERSAL) || user.getType().equals(type)))
            throw new FebsException("账号非本系统，请切换系统");
        if (!StringUtils.equals(user.getPassword(), password))
            throw new FebsException(ERROR);
        if (User.STATUS_LOCK.equals(user.getStatus()))
            throw new FebsException("账号已被锁定,请联系管理员");

        // 更新用户登录时间
        this.userService.updateLoginTime(username);
        // 保存登录记录
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        this.loginLogService.saveLoginLog(loginLog);

        // 进行网络请求,访问url接口
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("appid", APPID);
        paramMap.put("secret", SECRET);
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("js_code", code);

        String result = HttpUtil.get(URL, paramMap);
        JSONObject resultObject = JSONObject.fromObject(result);
        Map<String, Object> signInData = (Map<String, Object>) JSONObject.toBean(resultObject, Map.class);

        String openId = signInData.get("openid").toString();
        // 检验openid是否与原先的一致，如果不一样，则更新openid，并删除之前的登陆信息
        if (!openId.equals(user.getOpenId())) {
            // 增加修改openid的记录
            OpenidModifyRecord openidModifyRecord = new OpenidModifyRecord();
            openidModifyRecord.setUsername(username);
            openidModifyRecord.setOpenIdLow(user.getOpenId());
            openidModifyRecord.setOpenIdNew(openId);
            openidModifyRecordService.createOpenidModifyRecord(openidModifyRecord);
            // 更新openid
            user.setOpenId(openId);

            this.userService.updateOpenId(username, openId);
            // 删除之前的登陆信息（踢掉登陆状态）
            this.kickOutByUsername(username);
        }

        String encryptToken = FebsUtil.encryptToken(JWTUtil.sign(username, password, type, openId));
        String expireTimeStr = DateUtil.formatFullTime(LocalDateTime.now().plusSeconds(3600)); // 一次登陆只持续1小时

        JWTToken jwtToken = new JWTToken(encryptToken, expireTimeStr);
        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        return new FebsResponse().message("认证成功").data(this.generateWxUserInfo(jwtToken, user, signInData));
    }

    @PostMapping("goon")
    public FebsResponse goon(@NotBlank(message = "{required}lo") String code, HttpServletRequest request) throws Exception {

        log.info("goon");

        String token = FebsUtil.decryptToken(request.getHeader("Authentication"));
        if (StringUtils.isBlank(token))
            throw new FebsException("TOKEN is empty");

        String username = JWTUtil.getUsername(token);
        User user = this.userManager.getUser(username);

        if (User.STATUS_LOCK.equals(user.getStatus()))
            throw new FebsException("账号已被锁定,请联系管理员");

        //进行网络请求,访问url接口
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("appid", APPID);
        paramMap.put("secret", SECRET);
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("js_code", code);

        String result = HttpUtil.get(URL, paramMap);
        JSONObject resultObject = JSONObject.fromObject(result);
        Map<String, Object> signInData = (Map<String, Object>) JSONObject.toBean(resultObject, Map.class);

        String openId = JWTUtil.getOpenId(token);
        // 检验openid是否与原先的一致，如果不一样，则提示重新登陆绑定
        if (!signInData.get("openid").toString().equals(openId))
            throw new FebsException("账号已绑定其他微信，请重新登陆");

        String encryptToken = FebsUtil.encryptToken(JWTUtil.sign(username, user.getPassword(), user.getType(), openId));
        String expireTimeStr = DateUtil.formatFullTime(LocalDateTime.now().plusSeconds(3600)); // 一次续时只延续1小时

        JWTToken jwtToken = new JWTToken(encryptToken, expireTimeStr);
        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        return new FebsResponse().message("续时成功").data(this.generateWxUserInfo(jwtToken, user, signInData));
    }

    private void kickOutByUsername(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        List<ActiveUser> kickoutUserList = new ArrayList<>();
        List<String> kickoutUserStringList = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.equals(activeUser.getUsername(), username) && (activeUser.getLoginType() != null && activeUser.getLoginType() == 2)) {
                kickoutUserList.add(activeUser);
                kickoutUserStringList.add(userOnlineString);
            }
        }
        if (kickoutUserList.isEmpty() || kickoutUserStringList.isEmpty()) return;
        for (int i = 0; i < kickoutUserList.size(); i++) {
            // 删除 zset中的记录
            redisService.zrem(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserStringList.get(i));
            // 删除对应的 token缓存
            redisService.del(FebsConstant.TOKEN_CACHE_PREFIX + kickoutUserList.get(i).getToken() + "." + kickoutUserList.get(i).getIp());
        }
    }

    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setType(user.getType());
        activeUser.setLoginType(2);
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExpireAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(FebsConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);
        return activeUser.getId();
    }

    /**
     * 生成小程序需要的用户信息，包括：
     * 1. token
     * 2. openId
     * 3. 用户角色
     * 4. 用户权限
     *
     * @param token    token
     * @param user     用户信息
     * @param userInfo 原有的map
     * @return UserInfo
     */
    private Map<String, Object> generateWxUserInfo(JWTToken token, User user, Map<String, Object> userInfo) {
        String username = user.getUsername();
        userInfo.put("token", token.getToken());
        userInfo.put("expireTime", token.getExpireAt());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        user.setOpenId("it's a secret");
        user.setPassword("it's a secret");
        userInfo.put("user", user);
        return userInfo;
    }


    @PostMapping("getCredentials")
    @Limit(key = "getCredentials", period = 60, count = 120, name = "登录接口", prefix = "limit")
    public Map<String, Object> getCredentials(@NotBlank(message = "{required}") String code) {
        // 进行网络请求,访问url接口
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("appid", "wx443585b2b57e7137");
        paramMap.put("secret", "b5fcbeec316eea66d5e4ec2df5ebca22");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("js_code", code);

        String result = HttpUtil.get(URL, paramMap);
        JSONObject resultObject = JSONObject.fromObject(result);
        Map<String, Object> signInData = (Map<String, Object>) JSONObject.toBean(resultObject, Map.class);
        System.out.println(signInData);
        return signInData;
    }

}