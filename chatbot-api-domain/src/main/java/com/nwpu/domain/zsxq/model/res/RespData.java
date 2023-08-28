package com.nwpu.domain.zsxq.model.res;

import com.nwpu.domain.zsxq.model.vo.Topics;

import java.util.List;

/**
 * 返回数据
 */
public class RespData {
    private List<Topics> topics;

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }
}
