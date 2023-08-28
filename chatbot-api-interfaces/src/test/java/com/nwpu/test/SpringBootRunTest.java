package com.nwpu.test;

import com.alibaba.fastjson.JSON;
import com.nwpu.domain.ai.service.OpenAl;
import com.nwpu.domain.zsxq.IZsxqApi;
import com.nwpu.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.nwpu.domain.zsxq.model.vo.Topics;
import com.nwpu.domain.zsxq.service.ZsxqApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

public class SpringBootRunTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    private String groupId = "48884215821248";

    private String cookie = "zsxq_access_token=F7CE9936-3AC5-EC8D-61C5-66D62DA2A4EE_8918E3FD55884658; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2218a2698cff2cbd-0d2cbd1158fc47-26031f51-2073600-18a2698cff3cd5%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E5%BC%95%E8%8D%90%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fbugstack.cn%2F%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThhMjY5OGNmZjJjYmQtMGQyY2JkMTE1OGZjNDctMjYwMzFmNTEtMjA3MzYwMC0xOGEyNjk4Y2ZmM2NkNSJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%22%2C%22value%22%3A%22%22%7D%2C%22%24device_id%22%3A%2218a2698cff2cbd-0d2cbd1158fc47-26031f51-2073600-18a2698cff3cd5%22%7D; abtest_env=product; zsxqsessionid=59eeb5e3a07559c9e771a3b81469bc2b";

    @Test
    public void test_zsxq() throws IOException {

        ZsxqApi zsxqApi = new ZsxqApi();


        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
        logger.info("测试结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));

        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
        for (Topics topic : topics) {
            String topicId = topic.getTopic_id();
            String text = topic.getQuestion().getText();
            logger.info("topicId：{} text：{}", topicId, text);

            // 回答问题
            zsxqApi.answer(groupId, cookie, topicId, text, false);
        }

    }

    @Test
    public void test_openAi() throws IOException {
        OpenAl openAI = new OpenAl();
        String response = openAI.doChatGPT("鲁迅是谁");
        logger.info("测试结果：{}", response);
    }

}
