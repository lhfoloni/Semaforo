import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 *
 * @author Gabriel Batista e Luiz Henrique
 */

public class Semaforo {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		DefaultCategoryDataset abriu = new DefaultCategoryDataset();
		
		
		float qntCarro = 0.0f;
		float qntPessoa = 0.0f;
		float tempo = 0.0f;
		float qual = 0.0f;
		float tmpMin = 10.0f;
		float tmpMax = 60.0f;
		
		float[] vTempo, vQual;
		vTempo = new float [20];
		vQual = new float [20];

		// Load from 'FCL' file
		String fileName = "duplo.fcl";


		FIS fis = FIS.load(fileName,true);
		// Error while loading?
		if( fis == null ) {
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}
		// Show
		//JFuzzyChart.get().chart(fis);
		// Set inputs

		for (int i=0; i<20; i++) {
			double x = Math.random();
			double y = Math.random();
			qntCarro += (float) Math.abs(x-y);

			x = Math.random();
			y = Math.random();
			qntPessoa += (float) Math.abs(x-y);
			
			if(qntPessoa > 1) {
		        qntPessoa = 1;
			}
		    if(qntCarro > 1) {
		        qntCarro = 1;
		    }
		    if(qntPessoa < 0) {
		        qntPessoa = 0.01f;
		    }
		    if(qntCarro < 0) {
		        qntCarro = 0.01f;
		    }
		    ds.addValue(qntPessoa, "pessoa", ""+i);
			ds.addValue(qntCarro, "carro", ""+i);
			
		    
		    fis.setVariable("carros", qntCarro);
		    fis.setVariable("pessoas", qntPessoa);
		    
		    fis.evaluate();
		    
		    
		    tempo = (float) fis.getVariable("tempo").getValue();
		    qual = (float) fis.getVariable("qual").getValue();
		    
		    vTempo[i] = tempo;
		    vQual[i] = qual;
		    
		    if(qual < 0.5) {
		        qntCarro -= (float)Math.random();
		        System.out.println(i+" abriu Carro");
		    }else if(qual > 0.5) {
		        qntPessoa -= (float)Math.random();
		        System.out.println(i+" abriu Pessoa");
		    }
		    
		    abriu.addValue(tempo, "tempo", ""+i);
		}
		JFreeChart grafico = ChartFactory.createLineChart("Quantidade", "iteração", "quantidade 0 a 1", ds, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart grafico2 = ChartFactory.createLineChart("Tempo aberto", "iteração", "segundos", abriu, PlotOrientation.VERTICAL, true, true, false);
		try{
			OutputStream arquivo = new FileOutputStream("grafico.png");
			ChartUtilities.writeChartAsPNG(arquivo, grafico, 550, 400);
			arquivo.close();
			
			OutputStream arquivo2 = new FileOutputStream("grafico2.png");
			ChartUtilities.writeChartAsPNG(arquivo2, grafico2, 550, 400);
			arquivo.close();
		}catch(Exception e){

		}
	}
	

}
