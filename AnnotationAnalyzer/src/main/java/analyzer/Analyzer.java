package analyzer;

import dataset.DatasetDownloader;
import density.AnnotationDensity;
import metrics.AnnotationMetrics;
import org.apache.commons.io.FilenameUtils;
import type.AnnotationDec;
import type.AnnotationType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Analyzer {

    static Map<String, Integer> fileNum = new HashMap<>();
    public static Map<String, Integer> annotationType = new HashMap<>();
    public static ArrayList <String> temp = new ArrayList<>();
    private static List<File> javaFiles;
    public static Map<String, ArrayList<ArrayList<Double>>> percentile = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String appsDir = getAppsDir();
        //download apps used in the experiment
        if(useExperimentalDataset()) new DatasetDownloader().downloadApps(appsDir);

        extractAndAnalyzeApps(appsDir);
        //get average of percentile for all the apps
        getAveragePercentile();

		System.out.println("Number of annotated files: ");
		for(String key : fileNum.keySet()){
			System.out.println(key+"   "+fileNum.get(key));
		}

		System.out.println("Annotation types: ");
		for(Map.Entry<String, Integer> entry : annotationType.entrySet()){
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
    }

    private static String getAppsDir(){
        String appsDir = null;
        Properties prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            appsDir = prop.getProperty("appsDir");
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if(!new File(appsDir).exists()) {
            System.out.println("Invalid apps directory.");
            System.exit(1);
        }

        return appsDir;
    }

    private static boolean useExperimentalDataset(){
        boolean use = false;
        Properties prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            if(prop.getProperty("downloadExperimentalDataset").equals("true"))
                use = true;
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return use;
    }

    public static void extractAndAnalyzeApps(String appsDir){
        File file = new File(appsDir);
        for(File f : file.listFiles()){
            if(f.isDirectory()){
                try {
                    analyzeAnnotation(f);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void analyzeAnnotation(File f) throws IOException{
        javaFiles = new ArrayList<>();
        listJavaFiles(f);

        AnnotationExtractor extractor = new AnnotationExtractor();
        extractor.listAnnotations(f, javaFiles);

        AnnotationDensity density = new AnnotationDensity();
        density.getDensity(f, javaFiles);

        AnnotationMetrics metrics = new AnnotationMetrics();
        metrics.getMetrics(f, javaFiles);

        AnnotationType type = new AnnotationType();
        type.getType(javaFiles);

        AnnotationDec declaration = new AnnotationDec();
        declaration.getDeclaration(f, javaFiles);
    }

    //calculate average of percentiles
    private static void getAveragePercentile(){
        double p90, p95, p99;
        for(Map.Entry<String, ArrayList<ArrayList<Double>>> entry : percentile.entrySet()){
            String metricName = entry.getKey();
            p90 = getAverage(entry.getValue().get(0));
            p95 = getAverage(entry.getValue().get(1));
            p99 = getAverage(entry.getValue().get(2));
            System.out.println("Average Percentile Rank of "+metricName+" metric: very frequent: "+p90+" frequent: "+p95+" less frequent: "+p99);
        }        

    }

    private static double getAverage(List<Double> list){
        double avg = 0, sum = 0;
        if(!list.isEmpty()){
            for(int i=0; i<list.size(); i++){
                sum += list.get(i);
            }
            avg = (sum/(double)list.size());
        }
        return Math.round(avg*100.0)/100.0;
    }

    private static void listJavaFiles(File f){
        if(f.listFiles() != null){
            for(final File file : Objects.requireNonNull(f.listFiles())){
                if(file.isDirectory()){
                    listJavaFiles(file);
                }else{
                    String extension = FilenameUtils.getExtension(file.getName());
                    if(extension.equals("java")){
                        javaFiles.add(file);
                    }
                }
            }
        }
    }
}
