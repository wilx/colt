/*
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*/
package hep.aida.ref;

import hep.aida.*;

import java.util.Random;
import java.io.*;

/**
 * A very(!) basic test of the reference implementations of AIDA histograms.
 */
public class Test
{
	public static void main(String[] argv)
	{
		Random r = new Random();
		IHistogram1D h1 = new Histogram1D("AIDA 1D Histogram",40,-3,3);
		for (int i=0; i<10000; i++) h1.fill(r.nextGaussian());
		
		IHistogram2D h2 = new Histogram2D("AIDA 2D Histogram",40,-3,3,40,-3,3);
		for (int i=0; i<10000; i++) h2.fill(r.nextGaussian(),r.nextGaussian());
		
		// Write the results as a PlotML files!
		writeAsXML(h1,"aida1.xml");
		writeAsXML(h2,"aida2.xml");
		
		// Try some projections
		
		writeAsXML(h2.projectionX(),"projectionX.xml");
		writeAsXML(h2.projectionY(),"projectionY.xml");
	}
	private static void writeAsXML(IHistogram1D h,String filename)
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			out.println("<!DOCTYPE plotML SYSTEM \"plotML.dtd\">");
			out.println("<plotML>");
			out.println("<plot>");
			out.println("<dataArea>");
			out.println("<data1d>");
			out.println("<bins1d title=\""+h.title()+"\">");
			for (int i=0; i<h.xAxis().bins(); i++)
			{
				out.println(h.binEntries(i)+","+h.binError(i));
			}
			out.println("</bins1d>");
			out.print("<binnedDataAxisAttributes type=\"double\" axis=\"x0\"");
			  out.print(" min=\""+h.xAxis().lowerEdge()+"\"");
			  out.print(" max=\""+h.xAxis().upperEdge()+"\"");
			  out.print(" numberOfBins=\""+h.xAxis().bins()+"\"");
			  out.println("/>");
			out.println("<statistics>");
			out.println("<statistic name=\"Entries\" value=\""+h.entries()+"\"/>");
			out.println("<statistic name=\"Underflow\" value=\""+h.binEntries(h.UNDERFLOW)+"\"/>");
			out.println("<statistic name=\"Overflow\" value=\""+h.binEntries(h.OVERFLOW)+"\"/>");
			if (!Double.isNaN(h.mean())) out.println("<statistic name=\"Mean\" value=\""+h.mean()+"\"/>");
			if (!Double.isNaN(h.rms())) out.println("<statistic name=\"RMS\" value=\""+h.rms()+"\"/>");
			out.println("</statistics>");
			out.println("</data1d>");
			out.println("</dataArea>");
			out.println("</plot>");
			out.println("</plotML>");
			out.close();
		}
		catch (IOException x) { x.printStackTrace(); }
	}
	private static void writeAsXML(IHistogram2D h,String filename)
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			out.println("<!DOCTYPE plotML SYSTEM \"plotML.dtd\">");
			out.println("<plotML>");
			out.println("<plot>");
			out.println("<dataArea>");
			out.println("<data2d type=\"xxx\">");
			out.println("<bins2d title=\""+h.title()+"\" xSize=\""+h.xAxis().bins()+"\" ySize=\""+h.yAxis().bins()+"\">");
			for (int i=0; i<h.xAxis().bins(); i++)
				for (int j=0; j<h.yAxis().bins(); j++)
			{
				out.println(h.binEntries(i,j)+","+h.binError(i,j));
			}
			out.println("</bins2d>");
			out.print("<binnedDataAxisAttributes type=\"double\" axis=\"x0\"");
			  out.print(" min=\""+h.xAxis().lowerEdge()+"\"");
			  out.print(" max=\""+h.xAxis().upperEdge()+"\"");
			  out.print(" numberOfBins=\""+h.xAxis().bins()+"\"");
			  out.println("/>");
			out.print("<binnedDataAxisAttributes type=\"double\" axis=\"y0\"");
			  out.print(" min=\""+h.yAxis().lowerEdge()+"\"");
			  out.print(" max=\""+h.yAxis().upperEdge()+"\"");
			  out.print(" numberOfBins=\""+h.yAxis().bins()+"\"");
			  out.println("/>");
			//out.println("<statistics>");
			//out.println("<statistic name=\"Entries\" value=\""+h.entries()+"\"/>");
			//out.println("<statistic name=\"MeanX\" value=\""+h.meanX()+"\"/>");
			//out.println("<statistic name=\"RmsX\" value=\""+h.rmsX()+"\"/>");
			//out.println("<statistic name=\"MeanY\" value=\""+h.meanY()+"\"/>");
			//out.println("<statistic name=\"RmsY\" value=\""+h.rmsY()+"\"/>");
			//out.println("</statistics>");
			out.println("</data2d>");
			out.println("</dataArea>");
			out.println("</plot>");
			out.println("</plotML>");
			out.close();
		}
		catch (IOException x) { x.printStackTrace(); }
	}
}
