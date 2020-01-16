package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author CHExN
 */
@Data
@AllArgsConstructor
@TableName("p_wc_file")
public class WcFile implements Serializable {

    private static final long serialVersionUID = -3947372515211826907L;

    private Long wcId;

    private Long fileId;

}
