package metrics;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ACMetric {

    int classCount = 0;
    ArrayList<Double> acValues = new ArrayList<>();
    Map<String, Integer> annotationInClass;

    public void getACMetric(List<File> javaFiles) {

        for(File javaFile: javaFiles){
            annotationInClass = new HashMap<>();
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);

            for(String className : annotationInClass.keySet()){
                //System.out.println("Class Name: "+className+" No. of Annotations: "+annotationInClass.get(className));
                acValues.add((double) annotationInClass.get(className));
            }
        }
        //System.out.println("Total Classes: "+classCount+" No. of Classes with Annotations: "+acValues.size());

        //populate AC values
        int classWithoutAnnotation = classCount - acValues.size();
        for(int i=0; i<classWithoutAnnotation; i++){
            acValues.add(0.0);
        }

        //calculate percentile of each app
        if(!acValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(acValues, "AC");
        }

    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {

        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg) {
            String className = getClassName(ma);
            getAnnotation(className);
            super.visit(ma, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg) {
            String className = getClassName(na);
            getAnnotation(className);
            super.visit(na, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg) {
            String className = getClassName(sma);
            getAnnotation(className);
            super.visit(sma, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
            classCount++;
            super.visit(cid, arg);
        }
    }

    private void getAnnotation(String className){
        if(!className.equals("")){
            if(annotationInClass.containsKey(className)){
                annotationInClass.put(className, annotationInClass.get(className)+1);
            }else{
                annotationInClass.put(className, 1);
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
