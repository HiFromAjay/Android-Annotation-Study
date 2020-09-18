package metrics;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LOCADMetric {

    ArrayList<Double> locadValues = new ArrayList<>();

    public void getLOCADMetric(List<File> javaFiles) {
        for(File javaFile: javaFiles){
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);
        }

        //calculate percentile of each app
        if(!locadValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(locadValues, "LOCAD");
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg) {
            locadValues.add((double) getLOC(ma));
            //System.out.println(ma.getName()+": "+getLOC(ma));
            super.visit(ma, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg) {
            locadValues.add((double) getLOC(na));
            //System.out.println(na.getName()+": "+getLOC(na));
            super.visit(na, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg) {
            locadValues.add((double) getLOC(sma));
            //System.out.println(sma.getName()+": "+getLOC(sma));
            super.visit(sma, arg);
        }

        private int getLOC(AnnotationExpr node){
            int begin = node.getRange().get().begin.line;
            int end = node.getRange().get().end.line;
            return (end-begin)+1;
        }
    }

}
