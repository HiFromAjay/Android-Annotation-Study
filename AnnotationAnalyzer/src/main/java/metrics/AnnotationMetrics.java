package metrics;

import analyzer.Analyzer;
import com.google.common.math.Quantiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnotationMetrics {

    public void getMetrics(File f, List<File> javaFiles) {
		AAMetric aa = new AAMetric();
		aa.getAAMetric(javaFiles);

		LOCADMetric locad = new LOCADMetric();
		locad.getLOCADMetric(javaFiles);

		ANLMetric anl = new ANLMetric();
		anl.getANLMetric(javaFiles);

		AEDMetric aed = new AEDMetric();
		aed.getAEDMetric(javaFiles);

		ACMetric ac = new ACMetric();
		ac.getACMetric(javaFiles);

		UACMetric uac = new UACMetric();
		uac.getUACMetric(javaFiles);

        ASCMetric asc = new ASCMetric();
        asc.getASCMetric(javaFiles);
    }

	void percentileOfEachApp(ArrayList<Double> values, String metricName){
		Collections.sort(values);
		/*
		//Apache
		//Not working correctly for small number of values
		Percentile p = new Percentile();
		System.out.println(p.evaluate(values, 90)+" "+p.evaluate(values, 95)+" "+p.evaluate(values, 99));;
		*/

		//Guava
		//Produces same result as r
		Quantiles q = new Quantiles();
		double percentile90 = q.percentiles().index(90).compute(values);
		double percentile95 = q.percentiles().index(95).compute(values);
		double percentile99 = q.percentiles().index(99).compute(values);

		percentile90 = getRounded(percentile90);
		percentile95 = getRounded(percentile95);
		percentile99 = getRounded(percentile99);

		System.out.println("Percentile rank - "+metricName+" metric: "+percentile90+" "+percentile95+" "+percentile99);

		if(Analyzer.percentile.containsKey(metricName)){
			ArrayList<ArrayList<Double>> temp = Analyzer.percentile.get(metricName);
			temp.get(0).add(percentile90);
			temp.get(1).add(percentile95);
			temp.get(2).add(percentile99);
			Analyzer.percentile.replace(metricName, temp);
		}else{
			Analyzer.percentile.put(metricName,
					new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(percentile90)),
							new ArrayList<>(Arrays.asList(percentile95)), new ArrayList<>(Arrays.asList(percentile99)))));
		}

	}

	private double getRounded(double x){
		return Math.round(x*100.0)/100.0;
	}

}
