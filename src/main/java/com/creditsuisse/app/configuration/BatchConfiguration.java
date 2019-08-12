package com.creditsuisse.app.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.separator.JsonRecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.util.StringUtils;

import com.creditsuisse.app.domain.Event;

@Configuration
@EnableBatchProcessing
@EnableMBeanExport(registration=RegistrationPolicy.IGNORE_EXISTING)
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
	@Value("${chunk-size}")
	private int chunkSize;

	@Value("${max-threads}")
	private int maxThreads;
	
    @Value(value = "${logFilePath}")
    String logFile;

    @Bean
    FlatFileItemReader<Event> reader() {
        FlatFileItemReader<Event> reader = new FlatFileItemReader<>();
        if(logFile != null && !StringUtils.isEmpty(logFile)) {
        	reader.setResource(new FileSystemResource(logFile)); 
        } else {
        	// Take default file, if no file passed in args.
        	reader.setResource(new ClassPathResource("test.log"));
        }

        reader.setRecordSeparatorPolicy(new JsonRecordSeparatorPolicy());
        EventJsonLineMapper lineMapper = new EventJsonLineMapper();
        reader.setLineMapper(lineMapper);
        return reader;
    }
    
    @Bean
    public EventProcessor processor() {
        return new EventProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Event> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Event>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO event (id,state,duration,type,host,alert)"
                		+ " VALUES (:id,:state,:duration,:type,:host,:alert)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importEventJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importEventJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
    
	@Bean
	public TaskExecutor taskExecutor(){
	    SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("logProcessor");
	    asyncTaskExecutor.setConcurrencyLimit(maxThreads);
	    return asyncTaskExecutor;
	}

    @Bean
    public Step step1(JdbcBatchItemWriter<Event> writer) {
        return stepBuilderFactory.get("step1")
                .<Event, Event> chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .taskExecutor(taskExecutor())
				.throttleLimit(maxThreads)
                .build();
    }
}
