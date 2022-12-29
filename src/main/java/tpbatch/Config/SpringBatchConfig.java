package tpbatch.Config;

import org.springframework.batch.core.Job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import tpbatch.Controller.FileDeletingTasklet;
import tpbatch.model.Transaction;


@Configuration
@EnableTransactionManagement
@EnableBatchProcessing
@EnableScheduling

public class SpringBatchConfig {
/*
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired private ItemReader<Transaction> TransactionReader;
    @Autowired private ItemWriter<Transaction> TransactionWriter;
    @Autowired private ItemProcessor<Transaction, Transaction> TransactionProcessor;
*/
    @Value("/ressources/data.csv")
    private Resource[] inputResource;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public Job job( JobBuilderFactory jobBuilderFactory,
                    StepBuilderFactory stepBuilderFactory,
                    ItemReader<Transaction> itemReader,
                    ItemProcessor<Transaction, Transaction> itemProcessor,
                    ItemWriter<Transaction> itemWriter )
    {

        Step step = stepBuilderFactory.get("readCsvFileStep")
                .<Transaction,Transaction>chunk(200)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                .skipLimit(5000)
                .skip(Exception.class)
                .build();
       // System.out.println("Employee reading step başarılı bir şekilde tamamlandı.");

        FileDeletingTasklet task = new FileDeletingTasklet();
        task.setResources(inputResource);
        Step step2 = stepBuilderFactory.get("deleteCsvFileStep")
                .tasklet(task)
                .build();

        System.out.println("After delete file");

        Job job = jobBuilderFactory.get("readCsvFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .next(step2)
                .build();
        System.out.println("Job");
        return job;
    }

    @Bean
    public LineMapper<Transaction> lineMapper() {
        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<Transaction>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setNames(new String[] { "idTransaction","idCompte","montant","dateTransaction"  });
        lineTokenizer.setDelimiter(",");
        //   lineTokenizer.setIncludedFields(new int[] { 0, 1, 2 });
        BeanWrapperFieldSetMapper<Transaction> fieldSetMapper = new BeanWrapperFieldSetMapper<Transaction>();
        fieldSetMapper.setTargetType(Transaction.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public FlatFileItemReader<Transaction> itemReader() {
        FlatFileItemReader<Transaction> flatFileItemReader = new FlatFileItemReader<Transaction>();
        String inputResource = "src/main/resources/data.csv";
        flatFileItemReader.setLineMapper(lineMapper());
        //itemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(new FileSystemResource(inputResource));
        return flatFileItemReader;
    }

/*
    @Bean
    public ItemProcessor<Transaction,Transaction> processor(){
        return new TransactionProcessor();
    }

    @Bean
    public ItemWriter<Transaction> writer(){
        return new TransactionWriter();
    }

    @Bean(name="importPersonnes")
    public Job importPersones(JobBuilderFactory jobs) {
        return jobs.get("importPersones")
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1")
                .<Transaction,Transaction>chunk(2)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }


    @Bean
    public JobRepository jobRepository() throws Exception  {
        return new MapJobRepositoryFactoryBean(getTransactionManager(getSessionFactory(getDataSource()))).getObject();
    }

    @Bean(name="jobLauncher")
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public BatchLauncher launchBatch(){
        return new BatchLauncher();
    }

    @Scheduled(cron = "0,10,20,30 * * * * *")
    public void scheduleFixedDelayTask() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        launchBatch().run();
    }*/


}