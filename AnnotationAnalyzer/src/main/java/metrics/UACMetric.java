package metrics;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UACMetric {

    ArrayList<Double> uacValues = new ArrayList<>();
    Multimap<String, String> annotationInClass;

    public void getUACMetric(List<File> javaFiles) {

        for(File javaFile: javaFiles){
            annotationInClass = ArrayListMultimap.create();

            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);

            Set<String> uniqueClass = annotationInClass.keySet();
            for(String className : uniqueClass){
                int num = annotationInClass.get(className).size();
                //System.out.println(className+": "+num);
                uacValues.add((double) num);
            }

        }

        //calculate percentile of each app
        if(!uacValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(uacValues, "UAC");
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {
        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg) {
            String className = getClassName(ma);
            getAnnotation(className, ma);
            super.visit(ma, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg) {
            String className = getClassName(na);
            getAnnotation(className, na);
            super.visit(na, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg) {
            String className = getClassName(sma);
            getAnnotation(className, sma);
            super.visit(sma, arg);
        }
    }

    private void getAnnotation(String className, AnnotationExpr expr){
        if(!className.equals("")){
            String value = expr.toString();
            if(!annotationInClass.containsEntry(className, value)){
                annotationInClass.put(className, value);
            }
        }
    }

    private String getClassName(Node annotationNode){
        String name = "";
        Node node = annotationNode;
        Node root = annotationNode.findRootNode();

        while((node.getParentNode().get() != root) && !node.getParentNode().get().getMetaModel().toString().equals("ClassOrInterfaceDeclaration")){
            node = node.getParentNode().get();
        }
        if(node.getParentNode().get().getMetaModel().toString().equals("ClassOrInterfaceDeclaration")){
            name = ((ClassOrInterfaceDeclaration)node.getParentNode().get()).getName().toString();
        }
        return name;
    }

}
