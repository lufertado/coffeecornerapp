package com.coffeecorner.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductCode {
    private final Integer code;
    private List<Integer> extraCodes = new ArrayList<>();

    public ProductCode(Integer code, List<Integer> extraCodes) {
        this.code = code;
        if(extraCodes != null) {
            this.extraCodes = extraCodes;
        }
    }

    public ProductCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ProductCode)) {
            return false;
        }
        return code.equals(((ProductCode)o).getCode());
    }

    @Override
    public int hashCode() {
        return code * 37;
    }

    public Integer getCode() {
        return code;
    }

    public List<Integer> getExtraCodes() {
        return extraCodes;
    }
}
