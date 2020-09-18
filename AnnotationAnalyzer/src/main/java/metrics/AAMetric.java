package metrics;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AAMetric {

    ArrayList<Double> aaValues = new ArrayList<>();

    public void getAAMetric(List<File> javaFiles) {
        for(File javaFile: javaFiles){
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);
        }
        //calculate percentile of each app
        if(!aaValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(aaValues, "AA");
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {
        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg) {
            aaValues.add((double) 0);
            //System.out.println(ma.getName()+": 0");
            super.visit(ma, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg) {
            aaValues.add((double) na.getPairs().size());
            //System.out.println(na.getName()+": "+na.getPairs().size());
            super.visit(na, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg) {
            aaValues.add((double) 1);
            //System.out.println(sma.getName()+": 1");
            super.visit(sma, arg);
        }
    }

}
