package cc.mrbird.febs.chaoyang3team.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 在这里不论添加什么字段，都得去StoreroomOutAdd.vue里，在转换JSON的时候把新加的字段给去除
 * @author CHExN
 */
@Data
@TableName("cy_storeroom")
@Excel("库存物资信息表")
public class Storeroom implements Serializable {

    private static final long serialVersionUID = -407562502955655527L;

    /**
     * 库房物品id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    private transient String ids;
    private transient String[] idArr;

    @TableField("NAME")
    @ExcelField(value = "物品名称")
    private String name;
    private transient String[] names;

    @TableField("TYPE")
    @ExcelField(value = "型号")
    private String type;

    @TableField("UNIT")
    @ExcelField(value = "UNIT")
    private String unit;
    private transient String unitOriginal;

    /**
     * 物品入/出库数量
     */
    @TableField("AMOUNT")
    @ExcelField(value = "库存")
    private BigDecimal amount;
    private transient BigDecimal amountOriginal;

    /**
     * 收据
     */
    @TableField("RECEIPT")
    private String receipt;

    /**
     * 物品入库时价格
     */
    @TableField("MONEY")
    @ExcelField(value = "单价")
    private BigDecimal money;
    private transient BigDecimal moneyOriginal;

    /**
     * 物品总价格
     */
    @ExcelField(value = "总价")
    private transient BigDecimal totalPrice;


    /**
     * 物资类别 1保洁物品 2劳保物品 3办公室用品 4维修用品 5固定资产 6工会用品 7技安用品 8食堂用品 9其他
     */
    @TableField("TYPE_APPLICATION")
    @ExcelField(value = "物资类别", writeConverterExp = "1=保洁物品,2=劳保物品,3=办公室用品,4=维修用品,5=固定资产,6=工会用品,7=技安用品,8=食堂用品,9=其他")
    private String typeApplication;
    private transient String typeApplicationToDict;
    private transient String typeApplicationAuthority;

    @TableField("SUPPLIER")
    private String supplier;
    @ExcelField(value = "供应商")
    private transient String supplierToDict;

    /**
     * 出/入库日期
     */
    @TableField("DATE")
    @ExcelField(value = "入库日期")
    private String date;

    @TableField("REMARK")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 是否是库房数据 0库房数据 1入库数据 2出库数据
     */
    @TableField("IS_IN")
    private String isIn;

    /**
     * 出库到哪个部门的id
     */
    @TableField("TO_DEPT_ID")
    private Long toDeptId;
    private transient String toDeptName;
    private transient String toDeptIds;

    /**
     * 入库信息对应的库房ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 此字段只适用于typeApplication为保洁物品、劳保物品、维修用品的出库记录，用于判断出库到各分队的物品是否有被分配到公厕里。0 未分配 1已分配
     */
    @TableField("STATUS")
    private String status;

    /**
     * 剩余分配的数量（同上，此字段只适用于typeApplication为保洁物品、劳保物品、维修用品的出库记录）
     */
    @TableField("AMOUNT_DIST")
    private BigDecimal amountDist;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient BigDecimal storeroomCount;
    private transient String putNum;
    private transient String outNum;

    private transient UnitConversion unitConversion;

}
