package metrics;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASCMetric {

    ArrayList<Double> ascValues = new ArrayList<>();
    Map<String, String> importName;
    Multimap<String, String> annotationInClass;
    Multimap<String, String> importInClass;

    public void getASCMetric(List<File> javaFiles) {

        for(File javaFile: javaFiles){
            System.out.println(javaFile.getName());

            importName = new HashMap<>();
            annotationInClass = ArrayListMultimap.create();
            importInClass = ArrayListMultimap.create();

            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);

            for(String className : annotationInClass.keySet()){
                for(String value : annotationInClass.get(className)){
                    if(importName.containsKey(value)){
                        if(!importInClass.containsEntry(className, importName.get(value))){
                            importInClass.put(className, importName.get(value));
                        }
                    }else{
                        if(!importInClass.containsEntry(className, "java.lang")){
                            importInClass.put(className, "java.lang");
                        }
                    }
                }
            }

            for(String className : importInClass.keySet()){
                int num = importInClass.get(className).size();
                ascValues.add((double) num);
            }

        }
        //calculate percentile of each app
        if(!ascValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(ascValues, "ASC");
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {
        @Override
        public void visit(ImportDeclaration id, Object arg) {
            getImport(id);
            super.visit(id, arg);
        }

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

    private void getImport(ImportDeclaration id){
        String name = id.getName().asString();
        String lastName = name.substring(name.lastIndexOf(".")+1);
        String schema;

        if(name.contains(".")){
            schema = name.substring(0, name.lastIndexOf("."));
        }else{
            schema = name;
        }
        importName.put(lastName, schema);
    }

    private void getAnnotation(String className, AnnotationExpr expr){
        if(!className.equals("")){
            String annotationName = expr.getNameAsString();
            if(!annotationInClass.containsEntry(className, annotationName)){
                annotationInClass.put(className, annotationName);
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
