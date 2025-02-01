package com.jinny.springbatch.config;

import com.jinny.springbatch.event.JobsListener;
import com.jinny.springbatch.event.TriggersListener;
import com.jinny.springbatch.job.QuartzJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.Name;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final QuartzProperties quartzProperties;
    private final DataSource dataSource;
    private final JobsListener jobsListener;
    private final TriggersListener triggersListener;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JobDetail QuartzJobDetail() {
        return JobBuilder.newJob(QuartzJob.class).withIdentity("name", "group")
                .withDescription("Quartz Job")
                .usingJobData("datakey", "value")

                .storeDurably()
                .build();
    }

    @Bean
    public CronTrigger QuartzJobTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/15 * * * * ?");

        return TriggerBuilder.newTrigger().forJob(QuartzJobDetail()).withIdentity("sampleTrigger").withSchedule(cronScheduleBuilder).build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        schedulerFactoryBean.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", quartzProperties.getSchedulerName());

        properties.putAll(quartzProperties.getProperties());
        schedulerFactoryBean.setGlobalTriggerListeners(triggersListener);
        schedulerFactoryBean.setGlobalJobListeners(jobsListener);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setTransactionManager(platformTransactionManager);
        schedulerFactoryBean.setTriggers(applicationContext.getBean("QuartzJobTrigger", Trigger.class));
        //Trigger quartzJobTrigger = applicationContext.getBean("QuartzJobTrigger", Trigger.class);
        //JobDetail quartzJobDetail = applicationContext.getBean("QuartzJobDetail", JobDetail.class);
        return schedulerFactoryBean;
    }
}
