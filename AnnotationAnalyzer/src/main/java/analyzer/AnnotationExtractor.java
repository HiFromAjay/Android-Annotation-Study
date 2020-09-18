package analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnnotationExtractor {

    private final Map<String, Integer> elements = new HashMap<>();

    public void listAnnotations(File f, List<File> javaFiles) {
        String projectName = f.getName();
        System.out.println("App Name: "+ projectName);

        for (File javaFile : javaFiles) {
            elements.clear();
            CompilationUnit cu = getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);

            if (!elements.isEmpty()) {
                if (!Analyzer.fileNum.containsKey(projectName)) Analyzer.fileNum.put(projectName, 1);
                else Analyzer.fileNum.put(projectName, Analyzer.fileNum.get(projectName) + 1);
            }
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {
        @Override
        public void visit(MarkerAnnotationExpr ma, Object arg){
            if(ma.getParentNode().get().getMetaModel().toString().equals("MethodDeclaration")){
                int line = ((MethodDeclaration)ma.getParentNode().get()).getName().getBegin().get().line;
                String md = ((MethodDeclaration)ma.getParentNode().get()).getSignature().toString()+" -> Line no: "+line;
                addElements(md);
            }else if(ma.getParentNode().get().getMetaModel().toString().equals("ClassOrInterfaceDeclaration")){
                int line = ((ClassOrInterfaceDeclaration)ma.getParentNode().get()).getName().getBegin().get().line;
                String ci = ((ClassOrInterfaceDeclaration)ma.getParentNode().get()).getName().toString()+" -> Line no: "+line;
                addElements(ci);
            }else if(ma.getParentNode().get().getMetaModel().toString().equals("Parameter")){
                int line = ma.getParentNode().get().getBegin().get().line;
                String par = ma.getParentNode().get().toString()+" -> Line no: "+line;
                addElements(par);
            }else{
                int line = ma.getParentNode().get().getBegin().get().line;
                String par = ma.getParentNode().get().toString()+" -> Line no: "+line;
                addElements(par);
            }
        }

        @Override
        public void visit(NormalAnnotationExpr na, Object arg){
            if(na.getParentNode().get().getMetaModel().toString().equals("MethodDeclaration")){
                int line = ((MethodDeclaration)na.getParentNode().get()).getName().getBegin().get().line;
                String md = ((MethodDeclaration)na.getParentNode().get()).getSignature().toString()+" -> Line no: "+line;
                addElements(md);
            }else if(na.getParentNode().get().getMetaModel().toString().equals("ClassOrInterfaceDeclaration")){
                int line = ((ClassOrInterfaceDeclaration)na.getParentNode().get()).getName().getBegin().get().line;
                String ci = ((ClassOrInterfaceDeclaration)na.getParentNode().get()).getName().toString()+" -> Line no: "+line;
                addElements(ci);
            }else{
                int line = na.getParentNode().get().getBegin().get().line;
                String par = na.getParentNode().get().toString()+" -> Line no: "+line;
                addElements(par);
            }
        }

        @Override
        public void visit(SingleMemberAnnotationExpr sma, Object arg){
            if(sma.getParentNode().get().getMetaModel().toString().equals("MethodDeclaration")){
                int line = ((MethodDeclaration)sma.getParentNode().get()).getName().getBegin().get().line;
                String md = ((MethodDeclaration)sma.getParentNode().get()).getSignature().toString()+" -> Line no: "+line;
                addElements(md);
            }else if(sma.getParentNode().get().getMetaModel().toString().equals("ClassOrInterfaceDeclaration")){
                int line = ((ClassOrInterfaceDeclaration)sma.getParentNode().get()).getName().getBegin().get().line;
                String ci = ((ClassOrInterfaceDeclaration)sma.getParentNode().get()).getName().toString()+" -> Line no: "+line;
                addElements(ci);
            }else if(sma.getParentNode().get().getMetaModel().toString().equals("ConstructorDeclaration")){
                int line = ((ConstructorDeclaration)sma.getParentNode().get()).getName().getBegin().get().line;
                String cd = ((ConstructorDeclaration)sma.getParentNode().get()).getName().toString()+" -> Line no: "+line;
                addElements(cd);
            }else{
                int line = sma.getParentNode().get().getBegin().get().line;
                String par = sma.getParentNode().get().toString()+" -> Line no: "+line;
                addElements(par);
            }
        }

    }

    private void addElements(String name){
        if(elements.containsKey(name)){
            elements.put(name, elements.get(name)+1);
        }else{
            elements.put(name, 1);
        }
    }

    public static CompilationUnit getCompilationUnit(File file) {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(symbolSolver);
        JavaParser parser = new JavaParser(parserConfiguration);

        CompilationUnit cu = null;
        try {
            cu = parser.parse(file).getResult().get();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return cu;
    }
}