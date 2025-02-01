package com.jinny.springbatch.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 스케줄러 중복 실행 방지
 */
@Slf4j
@DisallowConcurrentExecution
public class LongTimeJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("START TEST2==================================================");
            Thread.sleep(19000);
            log.info("END TEST2================================================================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}