package com.project.restaurant.config;

import com.project.restaurant.file.FileReader;
import com.project.restaurant.util.MathUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by myelleswarapu on 26-01-2017.
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.project.restaurant"})
public class Application {

    @Autowired
    FileReader fileReader;

    private static final Logger LOG = Logger.getLogger("Application");

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(Application.class, args);
        appContext.registerShutdownHook();
        Application utils = appContext.getBean(Application.class);

        int exitCode = utils.run(args);
        appContext.stop();
        System.exit(exitCode);
    }

    /**
     *
     * @param args
     * @return
     */
    public int run(String[] args){
        String filePath;
        try {
            filePath = fileReader.getFilePathFromArgs(args);
            if(!FilenameUtils.getExtension(filePath).equals("txt")) {
                LOG.info("Invalid file");
                return 2;
            }
        } catch (IOException e) {
            LOG.info(e.toString());
            return 1;
        }
        List<int[]> data;
        try {
            data = fileReader.readDataFromFile(filePath);
        } catch (IOException e) {
            LOG.info(e.toString());
            return 1;
        } catch (NumberFormatException e){
            LOG.info(e.toString());
            return 100;
        }
        int maxTimeUnits = data.get(0)[0];
        int maxMenuItems = data.get(0)[1];
        int[] satisfactionValues = new int[data.size()-1];
        int[] timeUnits = new int[data.size()-1];
        for(int i=1;i<data.size();i++){
            satisfactionValues[i-1] = data.get(i)[0];
            timeUnits[i-1] = data.get(i)[1];
        }
        System.out.println("Maximum satisfaction for the given data : " + String.valueOf(process(maxTimeUnits, timeUnits, satisfactionValues, maxMenuItems)));
        return 0;
    }

    /**
     *
     * @param maxTimeUnits
     * @param timeUnits
     * @param satisfactionValues
     * @param maxMenuItems
     * @return
     */
    public int process(int maxTimeUnits, int timeUnits[], int satisfactionValues[], int maxMenuItems) {
        int i, j;
        int data[][] = new int[maxMenuItems+1][maxTimeUnits+1];
        // Build table data[][] in bottom up manner
        for (i = 0; i <= maxMenuItems; i++) {
            for (j = 0; j <= maxTimeUnits; j++) {
                if (i==0 || j==0)
                    data[i][j] = 0;
                else if (timeUnits[i-1] <= j)
                    data[i][j] = MathUtils.max(satisfactionValues[i-1] + data[i-1][j-timeUnits[i-1]],  data[i-1][j]);
                else
                    data[i][j] = data[i-1][j];
            }
        }
        return data[maxMenuItems][maxTimeUnits];
    }
}
