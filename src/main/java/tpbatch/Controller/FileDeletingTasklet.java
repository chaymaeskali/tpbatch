package tpbatch.Controller;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class FileDeletingTasklet implements Tasklet, InitializingBean {

    private Resource[] resources;
    LocalDate localDate = LocalDate.now();

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        for(Resource r: resources) {
            File file = r.getFile();
            String path = "src/main/resources/archive/";
            Files.copy(file.toPath(),
                    (new File(path + localDate + "_"+ file.getName())).toPath(),
                    //Paths.get(path + file.getName()),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Dosya başarılı bir şekilde "+ path +" e arşivlendi.");

            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("Dosya silenirken bir hata oluştu. Lütfen kontol ediniz!");

                throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
            }
            System.out.println("Folderda bulunan dosya başarılı bir şekilde silindi.");
        }
        return RepeatStatus.FINISHED;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resources, "directory must be set");
    }

}