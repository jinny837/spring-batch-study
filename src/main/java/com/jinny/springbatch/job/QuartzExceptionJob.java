package com.jinny.springbatch.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class QuartzExceptionJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // Your job logic goes here
        log.info("Caught exception");

        throw new JobExecutionException("Exception occurred in QuartzExceptionJob");
    }
}