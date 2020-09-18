package density;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.List;

public class AnnotationPerApp {

    int getAnnotationPerApp(List<File> javaFiles) {
        final int[] annotationCount = {0};
        for(File javaFile: javaFiles){
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
        }
        return annotationCount[0];
    }

}
