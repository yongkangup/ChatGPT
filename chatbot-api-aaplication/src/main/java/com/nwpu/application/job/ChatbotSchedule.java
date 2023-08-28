package com.nwpu.application.job;


import com.alibaba.fastjson.JSON;
import com.nwpu.domain.ai.IOpenAI;
import com.nwpu.domain.zsxq.IZsxqApi;
import com.nwpu.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.nwpu.domain.zsxq.model.vo.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

@Configuration
@EnableScheduling
public class ChatbotSchedule {
  private Logger logger = LoggerFactory.getLogger(ChatbotSchedule.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;
    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;
    @Resource
    private IOpenAI openAI;

    @Scheduled(cron = "0/60 * * * * ?")
    public void run(){
        try {
            //防止chatGPT封号
            if(new Random().nextBoolean()){
                logger.info("随机打烊中");
                return;
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            int hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 22 || hour < 7){
                logger.info("工作时间为早7:00到22:00，休息中！");
                return;
            }
            // 1. 检索问题
            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("检索结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            if (null == topics || topics.isEmpty()){
                logger.info("本次检索未查询到待回答问题！");
                return;
            }
            // 2. AI 回答
            Topics topic = topics.get(0);
            String answer = openAI.doChatGPT(topic.getQuestion().getText().trim());
            boolean status = zsxqApi.answer(groupId, cookie, topic.getTopic_id(), answer, false);
            logger.info("编号：{} 问题：{} 回答：{} 状态：{}", topic.getTopic_id(), topic.getQuestion().getText(), answer, status);
        } catch (IOException e) {
            logger.error("自动回答问题异常", e);
        }
    }
}
