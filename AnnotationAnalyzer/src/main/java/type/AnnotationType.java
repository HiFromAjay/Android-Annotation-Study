package type;

import analyzer.Analyzer;
import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationType {

    Map<String, String> importName;

    public void getType(List<File> javaFiles) {
        for(File javaFile: javaFiles){
            importName = new HashMap<>();
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);
        }

    }

    private class AnnotationVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ImportDeclaration id, Object arg) {
            getImport(id);
            super.visit(id, arg);
        }

        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg) {
            String name = ma.getName().toString();
            if(!importName.containsKey(name)){
                if(!Analyzer.temp.contains(name))
                    Analyzer.temp.add(name);
            }
            addAnnotation(name);

            super.visit(ma, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg) {
            String name = na.getName().toString();
            if(!importName.containsKey(name)){
                if(!Analyzer.temp.contains(name))
                    Analyzer.temp.add(name);
            }
            addAnnotation(name);

            super.visit(na, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg) {
            String name = sma.getName().toString();
            if(!importName.containsKey(name)){
                if(!Analyzer.temp.contains(name))
                    Analyzer.temp.add(name);
            }
            addAnnotation(name);

            super.visit(sma, arg);
        }
    }

    private void addAnnotation(String name){
        if(Analyzer.annotationType.containsKey(name)){
            Analyzer.annotationType.put(name, Analyzer.annotationType.get(name)+1);
        }else{
            Analyzer.annotationType.put(name, 1);
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

}
