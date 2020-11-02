package cc.mrbird.febs.common.authentication;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * JSON Web Token
 */
@Data
public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 1282057025599826155L;
    /**
     * 保存了账号密码的加密token
     */
    private String token;

    /**
     * token过期时间
     */
    private String expireAt;

    public JWTToken(String token) {
        this.token = token;
    }

    public JWTToken(String token, String expireAt) {
        this.token = token;
        this.expireAt = expireAt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
