package type;

import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.List;

public class AnnotationDec {

    public void getDeclaration(File f, List<File> javaFiles) {
        for(File javaFile: javaFiles){
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(AnnotationDeclaration ad, Object arg) {
                    System.out.println(ad);
                    super.visit(ad, arg);
                }
            }, null);
        }
    }

}
