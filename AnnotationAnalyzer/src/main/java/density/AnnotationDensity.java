package density;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AnnotationDensity {

    public void getDensity(File f, List<File> javaFiles) throws IOException{
		//count annotation per app
		AnnotationPerApp apa = new AnnotationPerApp();
		System.out.println("Total annotations in the app: "+apa.getAnnotationPerApp(javaFiles));

		//count annotation per LOC (file unit)
		AnnotationPerLOC apl = new AnnotationPerLOC();
		apl.getAnnotationPerLOC(javaFiles);

        //count annotation per element
        AnnotationPerElement ape = new AnnotationPerElement();
        ape.getAnnotationPerElement(javaFiles);
    }

}
