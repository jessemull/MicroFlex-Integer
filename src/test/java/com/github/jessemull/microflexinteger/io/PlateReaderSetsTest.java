/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*--------------------------- Package Declaration ----------------------------*/

package com.github.jessemull.microflexinteger.io;

/*------------------------------- Dependencies -------------------------------*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflexinteger.io.PlateReader;
import com.github.jessemull.microflexinteger.io.PlateWriter;
import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Stack;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;
import com.github.jessemull.microflexinteger.stat.Mean;
import com.github.jessemull.microflexinteger.util.RandomUtil;

/**
 * Tests the plate reader int test.
 * 
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateReaderSetsTest {
	
    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = 0;      // Minimum int value for wells
	private static int maxValue = 100;    // Maximum int value for wells
	
	/* The addition operation */
	
	private static Mean mean = new Mean();

	/* Random objects and numbers for testing */

	private static int rows = Plate.ROWS_96WELL;
	private static int columns = Plate.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int stackNumber = 5;
	private static Random random = new Random();
	
	private static Plate[] array = new Plate[plateNumber];
	private static List<Map<Well, Integer>> maps = new ArrayList<Map<Well, Integer>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<Stack> stacks = new ArrayList<Stack>();
	
	private static String path = "test.txt";
	private static File file;
	
	
    /* Value of false redirects System.err */
	
	private static boolean error = true;
	private static PrintStream originalOut = System.out;

	/**
	 * Generates random objects and numbers for testing.
	 */
	@BeforeClass
	public static void setUp() {
		
		if(error) {

			System.setErr(new PrintStream(new OutputStream() {
			    public void write(int x) {}
			}));

		}
		
		for(int j = 0; j < array.length; j++) {

			String label = "Plate1-" + j;
			
			Plate plate = RandomUtil.randomPlateInteger(
					rows, columns, minValue, maxValue, length, label);
			
			labelList.add(label);
			array[j] = plate;

			Map<Well, Double> toAddDouble = mean.plate(plate);
			TreeMap<Well, Integer> toAddBigInteger = new TreeMap<Well, Integer>();
			
			for(Map.Entry<Well, Double> entry : toAddDouble.entrySet()) {
				toAddBigInteger.put(entry.getKey(), entry.getValue().intValue());
			}
			
			maps.add(toAddBigInteger);
		}
		
		for(int k = 0; k < stackNumber; k++) {	
			Stack stack = RandomUtil.randomStackInteger(
					rows, columns, minValue, maxValue, length, "Stack" + k, plateNumber);
			stacks.add(stack);
		}
	}
	
	/**
	 * Creates a file and plate writer.
	 * @throws FileNotFoundException 
	 */
	@Before
	public void beforeTests() throws FileNotFoundException {
		file = new File(path);
	}
	
	/**
	 * Deletes the file used in the test.
	 */
	@After
	public void afterTests() {
		file.delete();
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass 
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
    /*-------------------- Methods for JSON Well Set Input -------------------*/
    
    /**
     * Tests the has next JSON set method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasNextJSONSet() throws IOException, JAXBException {    

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);
	
			int index = 0;
			while(reader.hasNextJSONSet()) {
				reader.nextJSONSet();
				index++;
			}
	
			assertTrue(sets.size() == index);
			assertFalse(reader.hasNextJSONSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the has previous JSON set method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testHasPreviousJSONSet() throws IOException, JAXBException {    

    	for(Stack stack : stacks) {
    		
    		File writerFile = new File("setToJSON.txt");
    		PlateWriter writer = new PlateWriter(writerFile);
    		
    		List<WellSet> sets = new ArrayList<WellSet>();
    		
    		for(Plate plate : stack) {
    			sets.add(plate.dataSet());
    		}
    		
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);

			assertFalse(reader.hasPreviousJSONSet());
			
			while(reader.hasNextJSONSet()) {
				reader.nextJSONSet();
			}
	
			int index = 0;
			while(reader.hasPreviousJSONSet()) {
				reader.previousJSONSet();
				index++;
			}
			
			assertTrue(sets.size() == index);
			assertFalse(reader.hasPreviousJSONSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next JSON set method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testNextJSONSet() throws IOException, JAXBException {
    	
    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.iterator();
			
			while(reader.hasNextJSONSet()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = reader.nextJSONSet();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}
    }
    
    /**
     * Tests the previous JSON set method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testPreviousJSONSet() throws IOException, JAXBException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.descendingIterator();
			
			while(reader.hasPreviousJSONSet()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = reader.previousJSONSet();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}
			
    }
    
    /**
     * Tests the remaingin JSON sets method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testRemainingJSONSets() throws IOException, JAXBException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.iterator();
			
			int index = random.nextInt(sets.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextJSONSet();
			}
			
			List<WellSet> remaining = reader.remainingJSONSets();
			Iterator<WellSet> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = remainingIter.next();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the spent JSON sets method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSpentJSONSets() throws IOException, JAXBException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.iterator();
			
			int index = random.nextInt(sets.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextJSONSet();
			}
				
			List<WellSet> spent = reader.spentJSONSets();
			Collections.reverse(spent);
			Iterator<WellSet> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = spentIter.next();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the all JSON sets method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testAllJSONSets() throws IOException, JAXBException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			List<WellSet> all = reader.allJSONSets();
			
			Iterator<WellSet> allIter = all.iterator();
			Iterator<Plate> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = allIter.next();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /*-------------------- Methods for XML Well Set Input --------------------*/
    
    /**
     * Tests the has next XML set method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testHasNextXMLSet() throws IOException, JAXBException, ParserConfigurationException, TransformerException {    
    	
        for(Stack stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);
	
			int index = 0;
			while(reader.hasNextXMLSet()) {
				reader.nextXMLSet();
				index++;
			}
	
			assertTrue(sets.size() == index);
			assertFalse(reader.hasNextJSONSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}
		
    }
    
    /**
     * Tests the has previous XML set method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasPreviousXMLSet() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

    	for(Stack stack : stacks) {
    		
    		File writerFile = new File("setToXML.txt");
    		PlateWriter writer = new PlateWriter(writerFile);
    		
    		List<WellSet> sets = new ArrayList<WellSet>();
    		
    		for(Plate plate : stack) {
    			sets.add(plate.dataSet());
    		}
    		
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);

			assertFalse(reader.hasPreviousXMLSet());
			
			while(reader.hasNextXMLSet()) {
				reader.nextXMLSet();
			}
	
			int index = 0;
			while(reader.hasPreviousXMLSet()) {
				reader.previousXMLSet();
				index++;
			}
			
			assertTrue(sets.size() == index);
			assertFalse(reader.hasPreviousXMLSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next XML set method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testNextXMLSet() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.iterator();
			
			while(reader.hasNextXMLSet()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = reader.nextXMLSet();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the previous XML set method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testPreviousXMLSet() throws IOException, TransformerException, ParserConfigurationException, JAXBException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.descendingIterator();
			
			while(reader.hasPreviousXMLSet()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = reader.previousXMLSet();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the remaingin XML sets method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testRemainingXMLSets() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.iterator();
			
			int index = random.nextInt(sets.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextXMLSet();
			}
			
			List<WellSet> remaining = reader.remainingXMLSets();
			Iterator<WellSet> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = remainingIter.next();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the spent XML sets method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testSpentXMLSets() throws IOException, TransformerException, ParserConfigurationException, JAXBException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			Iterator<Plate> iter = stack.iterator();
			
			int index = random.nextInt(sets.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextXMLSet();
			}
				
			List<WellSet> spent = reader.spentXMLSets();
			Collections.reverse(spent);
			Iterator<WellSet> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = spentIter.next();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the all XML sets method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testAllXMLSets() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(Stack stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriter writer = new PlateWriter(writerFile);
	
			List<WellSet> sets = new ArrayList<WellSet>();
			
			for(Plate plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReader reader = new PlateReader(writerFile);
			List<WellSet> all = reader.allXMLSets();
			
			Iterator<WellSet> allIter = all.iterator();
			Iterator<Plate> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				WellSet inputSet = iter.next().dataSet();
				WellSet outputSet = allIter.next();
				
				Iterator<Well> inputSetIter = inputSet.iterator();
				Iterator<Well> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					Well inputWell = inputSetIter.next();
					Well outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }

}
