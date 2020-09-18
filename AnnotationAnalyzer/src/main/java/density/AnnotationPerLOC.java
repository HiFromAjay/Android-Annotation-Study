package density;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class AnnotationPerLOC {

    public void getAnnotationPerLOC(List<File> javaFiles) throws IOException{
        final int[] annotationCount = new int[1];
        for(File javaFile: javaFiles){
            annotationCount[0] = 0;

            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(MarkerAnnotationExpr ma, Object arg) {
                    annotationCount[0]++;
                    super.visit(ma, arg);
                }

                @Override
                public void visit(NormalAnnotationExpr na, Object arg) {
                    annotationCount[0]++;
                    super.visit(na, arg);
                }

                @Override
                public void visit(SingleMemberAnnotationExpr sma, Object arg) {
                    annotationCount[0]++;
                    super.visit(sma, arg);
                }
            }, null);

            double annotationPerLoc = getAnoPerLoc(annotationCount[0], getLOCFile(javaFile));
            System.out.println("Annotation per LOC in a file named "+javaFile.getName()+": "+annotationPerLoc);
        }
    }

    private int getLOCFile(File f) throws IOException{
        int count = 0;
        BufferedReader reader = new BufferedReader(new FileReader(f));
        while(reader.readLine() != null) count++;
        return count;
    }

    private double getAnoPerLoc(int annCount, int loc){
        double anoPerLoc = (double) annCount/loc;
        return Math.round(anoPerLoc*100.0)/100.0;
    }

}
