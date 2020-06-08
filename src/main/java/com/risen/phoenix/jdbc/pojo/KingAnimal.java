package com.risen.phoenix.jdbc.pojo;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;

public class KingAnimal {

    @PhxId(PhxDataTypeEnum.INTEGER)
    private Integer kingUuid;
    @PhxField(value = PhxDataTypeEnum.VARCHAR, length = 20)
    private String kingName;
    @PhxField(PhxDataTypeEnum.VARCHAR)
    private String kingType;

    public Integer getKingUuid() {
        return kingUuid;
    }

    public void setKingUuid(Integer kingUuid) {
        this.kingUuid = kingUuid;
    }

    public String getKingName() {
        return kingName;
    }

    public void setKingName(String kingName) {
        this.kingName = kingName;
    }

    public String getKingType() {
        return kingType;
    }

    public void setKingType(String kingType) {
        this.kingType = kingType;
    }

    @Override
    public String toString() {
        return "KingAnimal{" +
                "kingUuid=" + kingUuid +
                ", kingName='" + kingName + '\'' +
                ", kingType='" + kingType + '\'' +
                '}';
    }
}
