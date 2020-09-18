package density;

import analyzer.Analyzer;
import analyzer.AnnotationExtractor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.CharMatcher;

import java.io.File;
import java.util.List;

public class AnnotationPerElement {

    String fileName;

    public void getAnnotationPerElement(List<File> javaFiles) {
        for(File javaFile: javaFiles){
            fileName = javaFile.getName();
            System.out.println("Num of annotations in a program element:");
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(EnumDeclaration ed, Object arg) {
            int annotationCount = getAnnotationCount(ed);
            if(annotationCount > 0){
                System.out.println(ed.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(ed, arg);
        }

        @Override
        public void visit(EnumConstantDeclaration ecd, Object arg) {
            int annotationCount = getAnnotationCount(ecd);
            if(annotationCount > 0){
                System.out.println(ecd.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(ecd, arg);
        }

        @Override
        public void visit(PackageDeclaration pd, Object arg) {
            int annotationCount = getAnnotationCount(pd);
            if(annotationCount > 0){
                System.out.println(pd.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(pd, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
            int annotationCount = getAnnotationCount(cid);
            if(annotationCount > 0){
                System.out.println(cid.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(cid, arg);
        }

        @Override
        public void visit(ConstructorDeclaration cd, Object arg) {
            int annotationCount = getAnnotationCount(cd);
            if(annotationCount > 0){
                System.out.println(cd.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(cd, arg);
        }

        @Override
        public void visit(AnnotationDeclaration ad, Object arg) {
            int annotationCount = getAnnotationCount(ad);
            if(annotationCount > 0){
                System.out.println(ad.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(ad, arg);
        }

        @Override
        public void visit(MethodDeclaration md, Object arg) {
            int annotationCount = getAnnotationCount(md);
            if(annotationCount > 0){
                System.out.println(md.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(md, arg);
        }

        @Override
        public void visit(FieldDeclaration fd, Object arg) {
            int annotationCount = getAnnotationCount(fd);
            if(annotationCount > 0){
                System.out.println(fd.getMetaModel().getMetaModelFieldName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(fd, arg);
        }

        @Override
        public void visit(Parameter par, Object arg) {
            int annotationCount = getAnnotationCount(par);
            if(annotationCount > 0){
                System.out.println(par.getName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(par, arg);
        }

        @Override
        public void visit(VariableDeclarationExpr vde, Object arg) {
            int annotationCount = getAnnotationCount(vde);
            if(annotationCount > 0){
                System.out.println(vde.getMetaModel().getMetaModelFieldName()+" of file "+fileName+": "+annotationCount);
                /*
                Analyzer.count += annotationCount;
                if(Analyzer.nElem.containsKey(annotationCount)){
                    Analyzer.nElem.put(annotationCount, Analyzer.nElem.get(annotationCount)+1);
                }else{
                    Analyzer.nElem.put(annotationCount, 1);
                }
                 */
            }
            super.visit(vde, arg);
        }
    }

    private int getAnnotationCount(NodeWithAnnotations node){
        int count = 0;
        NodeList<AnnotationExpr> list = node.getAnnotations();
        count = list.size();
        for(AnnotationExpr expr : list){
            if(expr.isNormalAnnotationExpr() || expr.isSingleMemberAnnotationExpr()){
                count += getNestedAnnotationCount(expr);
            }
        }
        return count;
    }

    private int getNestedAnnotationCount(AnnotationExpr expr){
        int nCount = 0;
        if(expr.isNormalAnnotationExpr()){
            NodeList<MemberValuePair> kvPair = expr.asNormalAnnotationExpr().getPairs();
            for(MemberValuePair pair : kvPair){
                String temp = pair.getValue().toString();
                if(!temp.contains(".com") && !temp.contains(".org") && !temp.contains(".de")){
                    nCount += CharMatcher.is('@').countIn(temp);
                }
            }
        }else{
            String temp = expr.asSingleMemberAnnotationExpr().getMemberValue().toString();
            if(!temp.contains(".com") && !temp.contains(".org") && !temp.contains(".de")){
                nCount += CharMatcher.is('@').countIn(temp);
            }
        }
        return nCount;
    }

}
