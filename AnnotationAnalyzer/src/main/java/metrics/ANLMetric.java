package metrics;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ANLMetric {

    ArrayList<Double> anlValues = new ArrayList<>();

    public void getANLMetric(List<File> javaFiles) {
        for(File javaFile: javaFiles){
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);
        }

        //calculate percentile of each app
        if(!anlValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(anlValues, "ANL");
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {
        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg) {
			int num = getNestingLevel(ma);
			anlValues.add((double) num);
			//System.out.println(ma.getName()+": "+num);
            super.visit(ma, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg) {
			int num = getNestingLevel(na);
			anlValues.add((double) num);
			//System.out.println(na.getName()+": "+num);
            super.visit(na, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg) {
			int num = getNestingLevel(sma);
			anlValues.add((double) num);
			//System.out.println(sma.getName()+": "+num);
            super.visit(sma, arg);
        }
    }

    private int getNestingLevel(AnnotationExpr node){
        int nestingLevel = 0;

        Node parentNode = node.getParentNode().get();
        Node rootNode = node.findRootNode();

        while(parentNode != rootNode){
            String nodeType = parentNode.getMetaModel().getTypeName();
            if(nodeType.equals("SingleMemberAnnotationExpr") || nodeType.equals("NormalAnnotationExpr")
                    || nodeType.equals("MarkerAnnotationExpr")){
                nestingLevel++;
            }
            parentNode = parentNode.getParentNode().get();
        }
        return nestingLevel;
    }

}
