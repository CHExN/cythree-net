package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.chaoyang3team.service.StoreroomPutService;
import cc.mrbird.febs.chaoyang3team.service.WcService;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.authentication.JWTToken;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.ActiveUser;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.system.dao.LoginLogMapper;
import cc.mrbird.febs.system.domain.LoginLog;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.LoginLogService;
import cc.mrbird.febs.system.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Validated
@RestController
public class LoginController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private WcService wcService;
    @Autowired
    private StoreroomPutService storeroomPutService;
    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private FebsProperties properties;
    @Autowired
    private ObjectMapper mapper;

    @PostMapping("login")
    @Limit(key = "login", period = 60, count = 15, name = "登录接口", prefix = "limit")
    public FebsResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String type, HttpServletRequest request) throws Exception {
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

        String token = FebsUtil.encryptToken(JWTUtil.sign(username, password, type));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(43200);
        String expireTimeStr = DateUtil.formatFullTime(expireTime);

        JWTToken jwtToken = new JWTToken(token, expireTimeStr);
        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new FebsResponse().message("认证成功").data(userInfo);
    }

    @GetMapping("loadData")
    @Limit(key = "loadData", period = 60, count = 15, name = "初始登陆加载数据")
    public FebsResponse index(String year, String date) {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
//        Long totalVisitCount = loginLogMapper.findTotalVisitCount();
//        data.put("totalVisitCount", totalVisitCount);
//        Long todayVisitCount = loginLogMapper.findTodayVisitCount();
//        data.put("todayVisitCount", todayVisitCount);
//        Long todayIp = loginLogMapper.findTodayIp();
//        data.put("todayIp", todayIp);
        List<BigDecimal> countList = loginLogMapper.findCountList();
        data.put("countList", countList);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = loginLogMapper.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        // 获取近期入库单数记录
        List<Map<String, Object>> storeroomPutDaysVisitCount = loginLogMapper.findStoreroomPutDaysVisitCount();
        data.put("storeroomPutDaysVisitCount", storeroomPutDaysVisitCount);
        // 获取所有月份的公厕消耗
        List<Map<String, Object>> allMonthWcConsumptionByYear = wcService.findAllMonthWcConsumptionByYear(year);
        data.put("allMonthWcConsumptionByYear", allMonthWcConsumptionByYear);
        // 所有分队前7消耗量排名
        List<Map<String, Object>> allOwnWcConsumptionByYear = wcService.findAllOwnWcConsumptionByYear(year);
        data.put("allOwnWcConsumptionByYear", allOwnWcConsumptionByYear);
        // 入库物资类别占比
        List<Map<String, Object>> storeroomPutTypeApplicationProportion = storeroomPutService.findStoreroomPutTypeApplicationProportion(date);
        data.put("storeroomPutTypeApplicationProportion", storeroomPutTypeApplicationProportion);
        // 入库供应商占比
        List<Map<String, Object>> storeroomPutSupplierProportion = storeroomPutService.findStoreroomPutSupplierProportion(date, null);
        data.put("storeroomPutSupplierProportion", storeroomPutSupplierProportion);
//        User param = new User();
//        param.setUsername(username);
//        List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastSevenDaysVisitCount(param);
//        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return new FebsResponse().data(data);
    }

    @RequiresPermissions("user:online")
    @GetMapping("online")
    public FebsResponse userOnline(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        List<ActiveUser> activeUsers = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            activeUser.setToken(null);
            if (StringUtils.isNotBlank(username)) {
                if (StringUtils.equalsIgnoreCase(username, activeUser.getUsername()))
                    activeUsers.add(activeUser);
            } else {
                activeUsers.add(activeUser);
            }
        }
        return new FebsResponse().data(activeUsers);
    }

    @DeleteMapping("kickout/{id}")
    @RequiresPermissions("user:kickout")
    public void kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        ActiveUser kickoutUser = null;
        String kickoutUserString = "";
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.equals(activeUser.getId(), id)) {
                kickoutUser = activeUser;
                kickoutUserString = userOnlineString;
            }
        }
        if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
            // 删除 zset中的记录
            redisService.zrem(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
            // 删除对应的 token缓存
            redisService.del(FebsConstant.TOKEN_CACHE_PREFIX + kickoutUser.getToken() + "." + kickoutUser.getIp());
        }
    }

    @GetMapping("logout/{id}")
    public void logout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        this.kickout(id);
    }

    @PostMapping("regist")
    public void regist(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws Exception {
        this.userService.regist(username, password);
    }

    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setType(user.getType());
        activeUser.setLoginType(1);
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
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("expireTime", token.getExpireAt());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        UserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        userInfo.put("config", userConfig);

        user.setPassword("it's a secret");
        userInfo.put("user", user);
        return userInfo;
    }
}
