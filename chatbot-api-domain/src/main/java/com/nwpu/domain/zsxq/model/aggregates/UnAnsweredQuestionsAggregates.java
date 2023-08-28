package com.nwpu.domain.zsxq.model.aggregates;

import com.nwpu.domain.zsxq.model.res.RespData;
//封装返回信息格式
public class UnAnsweredQuestionsAggregates {
    private boolean succeed;
    private RespData resp_data;

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public RespData getResp_data() {
        return resp_data;
    }

    public void setResp_data(RespData resp_data) {
        this.resp_data = resp_data;
    }
}
