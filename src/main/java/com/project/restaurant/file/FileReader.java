package com.project.restaurant.file;

import com.google.common.collect.Lists;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by myelleswarapu on 26-01-2017.
 */
@Service
public class FileReader {

    private static final Logger LOG = Logger.getLogger("FileReader");

    /**
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public List<int[]> readDataFromFile(String filePath) throws IOException, NumberFormatException {
        String data;
        List<int[]> dataList = Lists.newArrayList();
        data = new String(Files.readAllBytes(Paths.get(filePath)));
        Arrays.stream(data.split("\n"))
                .forEach(line -> dataList.add(
                        Arrays.stream(line.split(" "))
                                .mapToInt(Integer::parseInt)
                                .toArray()));
        return dataList;
    }

    /**
     *
     * @param args
     * @return
     * @throws IOException
     */
    public String getFilePathFromArgs(String[] args) throws IOException {
        Options options = new Options();
        options.addOption(new Option("FP", "filePath", true, "Location of the data file including the directory"));
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        String filePath = new File(this.getClass().getResource("/data/data.txt").getFile()).getAbsolutePath();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOG.info(e.toString());
        }
        if(cmd.hasOption("FP")){
            filePath = cmd.getOptionValue("FP");
        }
        return filePath;
    }
}
