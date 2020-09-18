package metrics;

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
import java.util.ArrayList;
import java.util.List;

public class AEDMetric {

    ArrayList<Double> aedValues = new ArrayList<>();

    public void getAEDMetric(List<File> javaFiles) {
        for(File javaFile: javaFiles){
            CompilationUnit cu = AnnotationExtractor.getCompilationUnit(javaFile);
            cu.accept(new AnnotationVisitor(), null);
        }

        //calculate percentile of each app
        if(!aedValues.isEmpty()){
            new AnnotationMetrics().percentileOfEachApp(aedValues, "AED");
        }
    }

    private class AnnotationVisitor extends VoidVisitorAdapter<Object> {
        @Override
        public void visit(EnumDeclaration ed, Object arg) {
            aedValues.add((double) getAnnotationCount(ed));
            //System.out.println(ed.getName()+": "+getAnnotationCount(ed));
            super.visit(ed, arg);
        }

        @Override
        public void visit(EnumConstantDeclaration ecd, Object arg) {
            aedValues.add((double) getAnnotationCount(ecd));
            //System.out.println(ecd.getName()+": "+getAnnotationCount(ecd));
            super.visit(ecd, arg);
        }

        @Override
        public void visit(PackageDeclaration pd, Object arg) {
            aedValues.add((double) getAnnotationCount(pd));
            //System.out.println(pd.getName()+": "+getAnnotationCount(pd));
            super.visit(pd, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Object arg) {
            aedValues.add((double) getAnnotationCount(cid));
            //System.out.println(cid.getName()+": "+getAnnotationCount(cid));
            super.visit(cid, arg);
        }

        @Override
        public void visit(ConstructorDeclaration cd, Object arg) {
            aedValues.add((double) getAnnotationCount(cd));
            //System.out.println(cd.getName()+": "+getAnnotationCount(cd));
            super.visit(cd, arg);
        }

        @Override
        public void visit(AnnotationDeclaration ad, Object arg) {
            aedValues.add((double) getAnnotationCount(ad));
            //System.out.println(ad.getName()+": "+getAnnotationCount(ad));
            super.visit(ad, arg);
        }

        @Override
        public void visit(MethodDeclaration md, Object arg) {
            aedValues.add((double) getAnnotationCount(md));
            //System.out.println(md.getName()+": "+getAnnotationCount(md));
            super.visit(md, arg);
        }

        @Override
        public void visit(FieldDeclaration fd, Object arg) {
            aedValues.add((double) getAnnotationCount(fd));
            //System.out.println(fd.getMetaModel().getMetaModelFieldName() +": "+getAnnotationCount(fd));
            super.visit(fd, arg);
        }

        @Override
        public void visit(Parameter par, Object arg) {
            aedValues.add((double) getAnnotationCount(par));
            //System.out.println(par.getName()+": "+getAnnotationCount(par));
            super.visit(par, arg);
        }

        @Override
        public void visit(VariableDeclarationExpr vde, Object arg) {
            aedValues.add((double) getAnnotationCount(vde));
            //System.out.println(vde.getMetaModel().getMetaModelFieldName() +": "+getAnnotationCount(vde));
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
