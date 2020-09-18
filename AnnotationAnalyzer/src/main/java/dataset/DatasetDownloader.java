package dataset;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class DatasetDownloader {

    public void downloadApps(String appsDir) throws IOException {
        if(!appsDir.endsWith(File.separator)){
            appsDir = appsDir+File.separator;
        }
        BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\ProjectsGit"));
        String repoUrl;
        while ((repoUrl = br.readLine()) != null){
            String cloneDirPath = appsDir+createPath(repoUrl);
            try {
                System.out.println("Cloning "+repoUrl);
                Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(Paths.get(cloneDirPath).toFile())
                        .call();
                Thread.sleep(5000);
            } catch (GitAPIException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private String createPath(String repoUrl){
        String path = repoUrl.substring(19, repoUrl.lastIndexOf("."));
        return path.replaceAll("/", ".");
    }
}
