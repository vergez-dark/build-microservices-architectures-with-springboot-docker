package com.blog.authentications.model;

public class SystemError {
    private SrcRegionCode srcRegionCode;
    private SpecificErrorCode specificErrorCode;

    public SystemError(SrcRegionCode srcRegionCode, SpecificErrorCode specificErrorCode) {
        this.srcRegionCode = srcRegionCode;
        this.specificErrorCode = specificErrorCode;
    }

    public String getErrorCode() {
        return this.srcRegionCode.getCode() + "-" + this.specificErrorCode.getCode();
    }

    public String getErrorMessageCode() {
        return this.srcRegionCode.name() + "_" + this.specificErrorCode.name();
    }

    public SrcRegionCode getSrcRegionCode() {
        return srcRegionCode;
    }

    public void setSrcRegionCode(SrcRegionCode srcRegionCode) {
        this.srcRegionCode = srcRegionCode;
    }

    public SpecificErrorCode getSpecificErrorCode() {
        return specificErrorCode;
    }

    public void setSpecificErrorCode(SpecificErrorCode specificErrorCode) {
        this.specificErrorCode = specificErrorCode;
    }

}
