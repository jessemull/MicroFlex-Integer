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

/* --------------------------- Package declaration -------------------------- */

package com.github.jessemull.microflexinteger.plate;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Stack;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;
import com.github.jessemull.microflexinteger.plate.WellList;
import com.github.jessemull.microflexinteger.util.RandomUtil;

/**
 * This class tests the methods and constructors in the integer stack plate 
 * class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StackTest {

	/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private Integer minValue = new Integer(-1000000000);   // Minimum integer value for wells
	private Integer maxValue = new Integer(1000000000);    // Maximum integer value for wells
	private int minLength = 100;                           // Minimum well set length
	private int maxLength = 1000;                          // Maximum well set length
	private int groupNumber = 0;                           // Group number
	private int minRow = 0;								   // Minimum row number
	private int minColumn = 1;							   // Minimum column number
	private int minPlate = 10;                             // Plate minimum
	private int maxPlate = 25;                             // Plate maximum
	private Random random = new Random();                  // Generates random integers
	
	/* Random objects and numbers for testing */

	private int rows;
	private int columns;
	private int length;
	private int plateNumber;
	private Stack testStack;
	private List<Plate[]> arrays = new ArrayList<Plate[]>();
	private List<TreeSet<Plate>> collections = new ArrayList<TreeSet<Plate>>();
	private List<String> descriptors = new ArrayList<String>();
	
    /* Value of false redirects System.err */
	
	private static boolean error = false;
	private static PrintStream originalOut = System.out;
	
	/**
	 * Toggles system error.
	 */
	@BeforeClass
	public static void redirectorErrorOut() {
		
		if(error == false) {

			System.setErr(new PrintStream(new OutputStream() {
			    public void write(int x) {}
			}));

		}
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
	/**
	 * Generates random objects and numbers for testing.
	 */
	@Before
	public void setUp() {
		
		rows = Plate.ROWS_48WELL + random.nextInt(Plate.ROWS_1536WELL - 
	           Plate.ROWS_48WELL + 1);

		columns =  Plate.COLUMNS_48WELL + random.nextInt(Plate.COLUMNS_1536WELL - 
		           Plate.COLUMNS_48WELL + 1);
		
		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
		
    	length = rows * columns / 5;
    	
    	testStack = RandomUtil.randomStackInteger(rows, columns, minValue, 
    			maxValue, minLength, maxLength, 0, length, "TestStack", plateNumber);
    	
    	for(int i = 0; i < 25; i++) {
    		
    		TreeSet<Plate> list = new TreeSet<Plate>();
    		Plate[] array = new Plate[20];
    		
    		for(int j = 0; j < 20; j++) {
    			Plate plate = RandomUtil.randomPlateInteger(
            			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "PlateInteger" + j);
    			list.add(plate);
    			array[j] = plate;
    		}
    		
    		this.getDescriptor(rows, columns);
    		this.collections.add(list);
    		this.arrays.add(array);
    	}
	}
	
    /* -------------------------- Tests constructors ------------------------ */
    
    /**
     * Tests the constructor using a type constant.
     */
	@Test
    public void testConstructorType() {

    	Stack stack = new Stack(Plate.PLATE_96WELL);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), Plate.ROWS_96WELL);
        assertEquals(stack.columns(), Plate.COLUMNS_96WELL);      
        assertEquals(stack.type(), Plate.PLATE_96WELL);
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), "96-Well");
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
    	
    }
    
    /**
     * Tests the constructor using a row and a column.
     */
	@Test
    public void testConstructorRowColumn() {

    	if(this.checkType(rows, columns)) {
    		rows++;
    		columns++;
    	}
    	
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	Stack stack = new Stack(rows, columns);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a type constant and label.
     */
	@Test
    public void testConstructorTypeLabel() {
    	
    	String label = "TestLabel";
    	
    	Stack stack = new Stack(Stack.PLATE_384WELL, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), Stack.ROWS_384WELL);
        assertEquals(stack.columns(), Stack.COLUMNS_384WELL);      
        assertEquals(stack.type(), Stack.PLATE_384WELL);
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), "384-Well");
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }

    /**
     * Tests the constructor using a row, column and label.
     */
	@Test
    public void testConstructorRowColumnLabel() {

    	String label = "TestLabel";
    
    	if(this.checkType(rows, columns)) {
    		rows++;
    		columns++;
    	}
    	
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	Stack stack = new Stack(rows, columns, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate.
     */
	@Test
    public void testConstructorStackPlate() {
		
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	Plate plate = RandomUtil.randomPlateInteger(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "StackInteger");
    	
    	Stack stack = new Stack(plate);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), 1);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate and label.
     */
	@Test
    public void testConstructorPlateLabel() {

    	String label = "TestLabel";
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	Plate plate = RandomUtil.randomPlateInteger(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, label);
    	
    	Stack stack = new Stack(plate, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), 1);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate collection.
     */
	@Test
    public void testConstructorCollection() {
    	
    	Stack stack = new Stack(collections.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), collections.get(0).size());
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate collection and label.
     */
    @Test
    public void testConstructorCollectionLabel() {

    	String label = "TestLabel";
    	
    	Stack stack = new Stack(collections.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), collections.get(0).size());
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate array.
     */
    @Test
    public void testConstructorArray() {
    
    	Stack stack = new Stack(arrays.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), arrays.get(0).length);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate array and a label.
     */
    @Test
    public void testConstructorArrayLabel() {

    	String label = "TestLabel";
    	
    	Stack stack = new Stack(arrays.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), Well.INTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), arrays.get(0).length);
        assertFalse(stack.getAll().isEmpty());
    }
    
    /**
     * Tests the parse dimensions constructor helper method.
     */ 
    @Test
    public void testParseDimensions() {
    	
    	Stack sixWell = new Stack(Stack.PLATE_6WELL);
    	Stack twelveWell = new Stack(Stack.PLATE_12WELL);
    	Stack twentyFourWell = new Stack(Stack.PLATE_24WELL);
    	Stack fortyEightWell = new Stack(Stack.PLATE_48WELL);
    	Stack ninetySixWell = new Stack(Stack.PLATE_96WELL);
    	Stack threeEightyFourWell = new Stack(Stack.PLATE_384WELL);
    	Stack fifteenThirtySixWell = new Stack(Stack.PLATE_1536WELL);
    	
    	assertEquals(sixWell.rows(), Stack.ROWS_6WELL);
    	assertEquals(sixWell.columns(), Stack.COLUMNS_6WELL);
    	assertEquals(sixWell.type(), Stack.PLATE_6WELL);
    	
    	assertEquals(twelveWell.rows(), Stack.ROWS_12WELL);
    	assertEquals(twelveWell.columns(), Stack.COLUMNS_12WELL);
    	assertEquals(twelveWell.type(), Stack.PLATE_12WELL);
    	
    	assertEquals(twentyFourWell.rows(), Stack.ROWS_24WELL);
    	assertEquals(twentyFourWell.columns(), Stack.COLUMNS_24WELL);
    	assertEquals(twentyFourWell.type(), Stack.PLATE_24WELL);
    	
    	assertEquals(fortyEightWell.rows(), Stack.ROWS_48WELL);
    	assertEquals(fortyEightWell.columns(), Stack.COLUMNS_48WELL);
    	assertEquals(fortyEightWell.type(), Stack.PLATE_48WELL);
    	
    	assertEquals(ninetySixWell.rows(), Stack.ROWS_96WELL);
    	assertEquals(ninetySixWell.columns(), Stack.COLUMNS_96WELL);
    	assertEquals(ninetySixWell.type(), Stack.PLATE_96WELL);
    	
    	assertEquals(threeEightyFourWell.rows(), Stack.ROWS_384WELL);
    	assertEquals(threeEightyFourWell.columns(), Stack.COLUMNS_384WELL);
    	assertEquals(threeEightyFourWell.type(), Stack.PLATE_384WELL);
    	
    	assertEquals(fifteenThirtySixWell.rows(), Stack.ROWS_1536WELL);
    	assertEquals(fifteenThirtySixWell.columns(), Stack.COLUMNS_1536WELL);
    	assertEquals(fifteenThirtySixWell.type(), Stack.PLATE_1536WELL);
    }
    
    /* ------------------- Tests methods for adding plates ------------------ */
    
    /**
     * Tests the add method using a plate.
     */
    @Test
    public void testAddPlate() {

    	for(Plate[] plates : this.arrays) {
    	
	        testStack.clear();
	    	
	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		Plate plate = plates[i];
	    		testStack.add(plate);
	    		
	    		assertTrue(testStack.contains(plate));
	    	}
    	}
    }
    
    /**
     * Tests the add method using a plate collection.
     */
    @Test
    public void testAddCollection() {

    	for(Collection<Plate> collection : this.collections) {
        	
    		testStack.clear();    		
    		testStack.add(collection);
    		
    		assertTrue(testStack.contains(collection));
    		
    	}
    	
    }
    
    /**
     * Tests the add method using a plate array.
     */
    @Test
    public void testAddArray() {
		
        for(Plate[] array : this.arrays) {
        	
    		testStack.clear();    		
    		testStack.add(array);
    		
    		assertTrue(testStack.contains(array));
    		
    	}
    	
    }
    
    /* ------------------ Tests methods for removing plates ----------------- */
    
    /**
     * Tests the remove method using a plate.
     */
    @Test
    public void testRemovePlate() {
    	
    	for(Plate[] plates : this.arrays) {
        	
	        Stack stack = new Stack(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		Plate plate = plates[i];	
	    		stack.remove(plate);

	    		assertFalse(stack.contains(plate));
	    		
	    	}
    	}

    }
    
    /**
     * Tests the remove method using a plate collection.
     */
    @Test
    public void testRemoveCollection() {

    	for(Collection<Plate> collection : collections) {
        	
	        Stack stack = new Stack(testStack);
	        stack.add(collection);
	        stack.remove(collection);
	        
	        assertFalse(stack.contains(collection));
    	}

    }
    
    /**
     * Tests the remove method using a plate array.
     */
    @Test
    public void testRemoveArray() {

        for(Plate[] array : arrays) {
        	
	        Stack stack = new Stack(testStack);
	        stack.add(array);
	        stack.remove(array);
	        
	        assertFalse(stack.contains(array));
    	}

    }
    
    /**
     * Tests the remove method using a label.
     */
    @Test
    public void testRemoveLabel() {
    	
    	for(Plate[] plates : this.arrays) {
        	
	        Stack stack = new Stack(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		Plate plate = plates[i];	
	    		stack.remove(plate.label());

	    		assertFalse(stack.contains(plate));
	    		
	    	}
    	}

    }
    
    /**
     * Tests the remove method using a list of labels.
     */
    @Test
    public void testRemoveLabelList() {

        for(Plate[] array : arrays) {

        	List<String> labels = new ArrayList<String>();
        	
        	for(int i = 0; i < array.length; i++) {
        		String label = "TestPlate" + i;
        		array[i].setLabel(label);
        		labels.add(label);
        	}
 
	        Stack stack = new Stack(testStack);
	        stack.add(array);
	        stack.remove(labels);

	        assertFalse(stack.contains(array));
    	}

    }
    
    /**
     * Tests the clear method.
     */
    @Test
    public void testClear() {
    	
    	Stack clone = new Stack(testStack);
    	clone.clear();
    	
    	assertTrue(clone.isEmpty());
    	assertTrue(clone.getAll().size() == 0);
    	
    	for(Plate plate : testStack) {
    		assertFalse(clone.contains(plate));
    	}
    	
    }
    
    /*------------------ Test methods for replacing plates -------------------*/
    
    /**
     * Tests the replace method using a plate.
     */
    @Test
    public void testReplacePlate() {
    	
    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);   		
    		Plate plate = new Plate(array[0]);
    		
    		plate.clearWells();
    		plate.clearGroups();
    		
    		WellSet set = RandomUtil.randomWellSetInteger(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellSet groupSet = RandomUtil.randomWellSetInteger(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellList groups = groupSet.wellList();
    		
    		plate.addWells(set);
    		plate.addGroups(groups);

    		stack.replace(plate);

    		Plate returned = stack.get(plate);
    		assertEquals(returned, plate);
    		assertEquals(returned.dataSet(), plate.dataSet());
    		assertEquals(returned.allGroups(), plate.allGroups());
    		assertFalse(returned.allGroups().equals(array[0].allGroups()));
    		assertFalse(returned.dataSet().equals(array[0].dataSet()));
    		
    	}
    	
    }
    
    /**
     * Tests the replace method using a plate collection.
     */
    @Test
    public void testReplaceCollection() {

    	for(Collection<Plate> collection : collections) {

    		Stack stack = new Stack(collection);   		
    		TreeSet<Plate> inputSet = new TreeSet<Plate>();
    		
    		for(Plate toReplace : collection) {
	    		
    			Plate plate = new Plate(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSet set = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSet groupSet = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<Plate> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<Plate> iterInput = inputSet.iterator();
    		Iterator<Plate> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			Plate plateInput = iterInput.next();
    			Plate plateReturned = iterReturned.next();

    			assertEquals(plateInput, plateReturned);
    			assertEquals(plateInput.dataSet(), plateReturned.dataSet());
    			assertEquals(plateInput.allGroups(), plateReturned.allGroups());

    		}
    	}
    }
    
    /**
     * Tests the replace method using a plate array.
     */
    @Test
    public void testReplaceArray() {

    	for(Plate[] array : arrays) {

    		Stack stack = new Stack(array);   		
    		TreeSet<Plate> inputSet = new TreeSet<Plate>();
    		
    		for(Plate toReplace : array) {
	    		
    			Plate plate = new Plate(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSet set = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSet groupSet = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<Plate> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<Plate> iterInput = inputSet.iterator();
    		Iterator<Plate> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			Plate plateInput = iterInput.next();
    			Plate plateReturned = iterReturned.next();

    			assertEquals(plateInput, plateReturned);
    			assertEquals(plateInput.dataSet(), plateReturned.dataSet());
    			assertEquals(plateInput.allGroups(), plateReturned.allGroups());

    		}
    	}
    }
    
    /*------------------ Tests methods for retaining plates ------------------*/
    
    /**
     * Tests the retain method using a plate.
     */
    @Test
    public void testRetainPlate() {
    
        for(Plate[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		Stack stack = new Stack(array);   
    		Plate plate = new Plate(array[index]);
    		
    		stack.retain(plate);

    		assertEquals(stack.first(), plate);
    		assertEquals(stack.size(), 1);
    	}

    }
    
    /**
     * Tests the retain method using a plate collection.
     */
    @Test
    public void testRetainCollection() {

    	for(Collection<Plate> collection : collections) {
    		
    		Stack stack = new Stack(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		TreeSet<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		stack.retain(set);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(Plate plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a plate array.
     */
    @Test
    public void testRetainArray() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		TreeSet<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		Plate[] retainArray = set.toArray(new Plate[set.size()]);
    		stack.retain(retainArray);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(Plate plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a label.
     */
    @Test
    public void testRetainLabel() {

    	for(Plate[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		Stack stack = new Stack(array);   
    		Plate plate = new Plate(array[index]);
    		
    		stack.retain(plate.label());

    		assertEquals(stack.first(), plate);
    		assertEquals(stack.size(), 1);
    	}
    	
    }
    
    /**
     * Tests the retain method using a list of labels.
     */
    @Test
    public void testRetainLabelList() {
    	
    	for(Collection<Plate> collection : collections) {
    		
    		Stack stack = new Stack(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		TreeSet<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(Plate plate : set) {
    			labelList.add(plate.label());
    		}
    		
    		stack.retain(labelList);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(Plate plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}

    }
    
    /*----------------- Tests methods for retrieving plates ------------------*/
    
    /**
     * Tests the get method using a label.
     */
    @Test
    public void testGetLabel() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
    		for(Plate plate : array) {
    			assertEquals(stack.get(plate.label()), plate);
    		}
    	}
    }
    
    /**
     * Tests the get method using a list of labels.
     */
    @Test
    public void testGetLabelList() {

    	for(Collection<Plate> collection : collections) {
    		
    		Stack stack = new Stack(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		Set<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(Plate plate : set) {
    			labelList.add(plate.label());
    		}
    		
    		assertTrue(stack.get(labelList).equals(set));
    		
    	}
    }
   
    /**
     * Tests the get method using a plate.
     */
    @Test
    public void testGetPlate() {

        for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
    		for(Plate plate : array) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}

    }
    
    /**
     * Tests the get method using a plate collection.
     */
    public void testGetCollection() {
    	
    	for(Collection<Plate> collection : collections) {
    		
    		Stack stack = new Stack(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		Set<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		assertEquals(stack.get(set), set);
    		
    	}

    }
    
    /**
     * Tests the get method using a plate array.
     */
    @Test
    public void testGetArray() {

        for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		int begin = 1 + random.nextInt(array.length / 2 - 1);
    		int end = begin + random.nextInt(array.length - begin) + 1;
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		Set<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}

    		Plate[] getArray = set.toArray(new Plate[set.size()]);
    		
    		assertEquals(stack.get(getArray), set);
    		
    	}

    }
    
    /**
     * Tests the get all method.
     */
    @Test
    public void testGetAll() {

    	for(Collection<Plate> collection : collections) {

    		Stack stack = new Stack(collection);   
    		assertEquals(stack.getAll(), collection);
    	}
    	
    }
    
    /*------------------- Tests methods for plate lookup ---------------------*/

    /**
     * Tests the contains method using a plate.
     */
    @Test
    public void testContainsPlate() {
    	
    	for(Plate[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		Stack stack = new Stack(array);   
    		Plate plate = new Plate(array[index]);

    		assertTrue(stack.contains(plate));
    	}
    	
    }
    
    /**
     * Tests the contains method using a plate collection.
     */
    @Test
    public void testContainsCollection() {

        for(Collection<Plate> collection : collections) {
    		
    		Stack stack = new Stack(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		Set<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		assertTrue(stack.contains(set));
    		
    	}

    }
    
    /**
     * Tests the contains method using a plate array.
     */
    @Test
    public void testContainsArray() {

        for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		Set<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		Plate[] getArray = set.toArray(new Plate[set.size()]);
    		
    		assertTrue(stack.contains(getArray));
    		
    	}

    }
    
    /**
     * Tests the contains method using a label.
     */
    @Test
    public void testContainsLabel() {

        for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
    		for(Plate plate : array) {
    			assertTrue(stack.contains(plate));
    		}
    	}

    }
    
    /**
     * Tests the contains method using a list of labels.
     */
    @Test
    public void testContainsLabelList() {

        for(Collection<Plate> collection : collections) {
    		
    		Stack stack = new Stack(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin) + 1;
    		int index = 0;

    		Iterator<Plate> iter = stack.iterator();
    		Set<Plate> set = new TreeSet<Plate>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(Plate plate : set) {
    			labelList.add(plate.label());
    		}

    		assertTrue(stack.contains(labelList));
    		
    	}

    }
    
    /* ------------ Tests plate dimensions and data type methods ------------ */
    
    /**
     * Tests the rows getter.
     */
    @Test
    public void testRows() {
    	
    	for(int i = 0; i < 100; i++) {
    		
    		int rows = Plate.ROWS_48WELL + 
	                 random.nextInt(Plate.ROWS_1536WELL - 
		             Plate.ROWS_48WELL + 1);

    		int columns = 1;
    		
    		Stack plate = new Stack(rows, columns);
    		
    		assertEquals(plate.rows(), rows);
    		
    	}
    	
    }
    
    /**
     * Tests the columns getter.
     */
    @Test
    public void testColumns() {
    	
        for(int i = 0; i < 100; i++) {
    		
    		int columns = Plate.COLUMNS_48WELL + 
	                 random.nextInt(Plate.COLUMNS_1536WELL - 
		             Plate.COLUMNS_48WELL + 1);

    		int rows = 0;
    		
    		Stack stack = new Stack(rows, columns);
    		
    		assertEquals(stack.columns(), columns);
    		
    	}
    }

    /**
     * Tests the type getter.
     */
    @Test
    public void testType() {
     
    	for(int i = 0; i < 100; i++) {
    	    int type = random.nextInt(6 + 1);
    	    Stack stack = new Stack(type);
    	    assertEquals(type, stack.type());
    	}
    	
    }
    
    /**
     * Tests the descriptor getter.
     * @Test
     */
    @Test
    public void testDescriptor() {
 	
    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		Stack stack = new Stack(type);
    		
    		switch(type) {
    		
    		    case Stack.PLATE_6WELL:    assertEquals(stack.descriptor(), "6-Well");
    		                                         break;
    		    case Stack.PLATE_12WELL:   assertEquals(stack.descriptor(), "12-Well");
                                                     break;
    		    case Stack.PLATE_24WELL:   assertEquals(stack.descriptor(), "24-Well");
                                                     break;
    		    case Stack.PLATE_48WELL:   assertEquals(stack.descriptor(), "48-Well");
                                                     break;
    		    case Stack.PLATE_96WELL:   assertEquals(stack.descriptor(), "96-Well");
                                                     break;
    		    case Stack.PLATE_384WELL:  assertEquals(stack.descriptor(), "384-Well");
                                                     break;
    		    case Stack.PLATE_1536WELL: assertEquals(stack.descriptor(), "1536-Well");
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		int rows = Stack.ROWS_48WELL + 
	                   random.nextInt(Stack.ROWS_1536WELL - 
   		               Stack.ROWS_48WELL + 1);

            int columns =  Stack.COLUMNS_48WELL + 
	                       random.nextInt(Stack.COLUMNS_1536WELL - 
			               Stack.COLUMNS_48WELL + 1);
            
            Plate randomPlate = new Plate(rows, columns);
            
            if(randomPlate.type() == -1) {
            	String desc = "Custom Plate: " + rows + "x" + columns;
            	assertEquals(desc, randomPlate.descriptor());
            }
    	}
    }
    
    /**
     * Tests the data type getter.
     */
    @Test
    public void testDataType() {
    	for(int i = 0; i < 100; i++) {
    		Stack stack = new Stack(0);
    		assertEquals(stack.dataType(), Plate.PLATE_INTEGER);
    	}
    }
    
    /**
     * Tests the data type string getter.
     */
    @Test
    public void testDataTypeString() {
    	for(int i = 0; i < 100; i++) {
    		Stack stack = new Stack(0);
    		assertEquals(stack.dataTypeString(), "Integer");
    	}
    }
    
    /**
     * Tests the label setter.
     */
    @Test
    public void testSetLabel() {
    	for(int i = 0; i < 100; i++) {
    		String label = "TestLabel";
    		Stack stack = new Stack(0);
    		stack.setLabel(label);
    		assertEquals(stack.label(), label);
    	}
    }
    
    /**
     * Tests the label getter.
     */
    @Test
    public void testLabel() {
    	for(int i = 0; i < 100; i++) {
    		Stack stack = new Stack(0);
    		assertEquals(stack.label(), "StackInteger");
    	}
    }

    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		Stack stack = new Stack(type);
    		
    		String toString = "Type: ";
    		
    		switch(type) {
    		
    		    case Stack.PLATE_6WELL:    toString += "6-Well Label: ";
    		                                         break;
    		    case Stack.PLATE_12WELL:   toString += "12-Well Label: ";
                                                     break;
    		    case Stack.PLATE_24WELL:   toString += "24-Well Label: ";
                                                     break;
    		    case Stack.PLATE_48WELL:   toString += "48-Well Label: ";
                                                     break;
    		    case Stack.PLATE_96WELL:   toString += "96-Well Label: ";
                                                     break;
    		    case Stack.PLATE_384WELL:  toString += "384-Well Label: ";
                                                     break;
    		    case Stack.PLATE_1536WELL: toString += "1536-Well Label: ";
                                                     break;
                default: throw new IllegalArgumentException("Invalid stack type constant.");
    		
    		}
    		
    		toString += stack.label();
    		
    		assertEquals(toString, stack.toString());
    		
    		int rows = Stack.ROWS_48WELL + 
	                   random.nextInt(Stack.ROWS_1536WELL - 
   		               Stack.ROWS_48WELL + 1);

            int columns =  Stack.COLUMNS_48WELL + 
	                       random.nextInt(Stack.COLUMNS_1536WELL - 
			               Stack.COLUMNS_48WELL + 1);
            
            Stack randomStack = new Stack(rows, columns);
            
            if(randomStack.type() == -1) {
            	String toStringRandom = "Type: Custom Stack: " + rows + "x" + columns + " Label: " + randomStack.label();
            	assertEquals(toStringRandom, toStringRandom);
            }
            
    	}
    }

    /*------------------------- Tests TreeSet methods ------------------------*/
    
    /**
     * Tests the size getter.
     */
    @Test
    public void testSize() {
    	for(Plate[] array : arrays) {
    		Stack stack = new Stack(array);
    		assertEquals(stack.size(), array.length);
    	}
    }
    
    /**
     * Tests the to array method.
     */
    @Test
    public void testToArray() {
    	
    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		Plate[] stackArray = stack.toArray();
    		
    		Arrays.sort(array);
    		Arrays.sort(stackArray);
  
    		for(int i = 0; i < array.length; i++) {
    			assertEquals(array[i], stackArray[i]);
    		}
    	}
    }
    
    /**
     * Tests the higher method.
     */
    @Test
    public void testHigher() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	int current = 0;
	    	Set<Plate> testSet = stack.getAll();
	
	    	int index = 1 + random.nextInt(testSet.size() - 2);

	        Iterator<Plate> iter = stack.iterator();
	        
	        while(current < index) {
	        	current++;
	        	iter.next();
	        }
	        
	        Plate toHigher = iter.next();
	        Plate toReturn = iter.next();
	        Plate input = new Plate(toReturn.rows(), toReturn.columns() - 1);
	        Plate outside = new Plate(stack.rows() + 1, stack.columns());
	         
	        toReturn = toHigher.compareTo(input) < 1 ? toHigher : toReturn;

	        assertEquals(stack.higher(toHigher), toReturn);
	        assertNull(stack.higher(outside));
    	}
    }
    
    /**
     * Tests the lower method.
     */
    @Test
    public void testLower() {
    	
    	for(Plate[] array : arrays) {
    	
    		Stack stack = new Stack(array);
    		
	    	int current = 0;
	    	int index = 1 + random.nextInt(stack.size() - 2);
	        
	        Iterator<Plate> iter = stack.iterator();
	        
	        while(current < index && iter.hasNext()) {
	        	current++;
	        	iter.next();
	        }

	        Plate toReturn = iter.next();
	        Plate toFloor = iter.next();
	        Plate input = new Plate(toFloor.rows(), toFloor.columns() - 1);
	        Plate outside = new Plate(stack.rows() - 1, stack.columns());
	        
	        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;
	
	        assertEquals(stack.lower(toFloor), toReturn);
	        assertNull(stack.lower(outside));
    	}
    }
    
    /**
     * Tests the poll first method.
     */
    @Test
    public void testPollFirst() {
    	
    	for(Plate[] array : arrays) {
    
    		Stack stack = new Stack(array);
    	
	    	Plate first = stack.first();
	    	Plate polled = stack.pollFirst();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(first, polled);
    	}
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	for(Plate[] array : arrays) {
    	    
    		Stack stack = new Stack(array);
    	
	    	Plate last = stack.last();
	    	Plate polled = stack.pollLast();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(last, polled);
    	}
    	
    }  
    
    /**
     * Tests the iterator getter.
     */
    @Test
    public void testIterator() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
	    	Iterator<Plate> iter = stack.iterator();
	    	
	    	Arrays.sort(array);
	    	
	    	for(int i = 0; i < array.length; i++) {
	    		assertEquals(array[i], iter.next());
	    	}
    	}
    }
    
    /**
     * Tests the descending iterator getter.
     */
    @Test
    public void testDescendingIterator() {
    	
    	for(Plate[] array : arrays) {

    		Stack stack = new Stack(array);
	    	Iterator<Plate> iter = stack.descendingIterator();
	    	
	    	Arrays.sort(array);
	    	
	    	for(int i = array.length - 1; i >= 0; i--) {
	    		assertEquals(array[i], iter.next());
	    	}
    	}
    }
    
    /**
     * Tests the descending set method.
     */
    @Test
    public void testDescendingSet() {
    	
    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	Set<Plate> reversed = stack.descendingSet();
	
	    	Iterator<Plate> iter = stack.descendingIterator();
	    	Iterator<Plate> iterReversed = reversed.iterator();
	    	
	    	while(iter.hasNext() && iterReversed.hasNext()) {
	    		assertEquals(iter.next(), iterReversed.next());
	    	}
    	}
    }
    
    /**
     * Tests the first method.
     */
    @Test
    public void testFirst() {
 	
    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
    		Iterator<Plate> iter = stack.iterator();
    		assertEquals(iter.next(), stack.first());
    
    	}  	
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {
    	
    	for(Plate[] array : arrays) {
        
    		Stack stack = new Stack(array);
    		
    		Iterator<Plate> iter = stack.iterator();
    		Plate last = null;
    	
    		while(iter.hasNext()) {	
    			last = iter.next();
    		}
    	
    		assertEquals(last, stack.last());
    
    	}
    }
    
    /**
     * Tests the ceiling method.
     */
    @Test
    public void testCeiling() {

    	for(Plate[] array : arrays) {
    	
	    	Stack stack = new Stack(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<Plate> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        Plate toCeiling = iter.next();
	        Plate outside = new Plate(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toCeiling), toCeiling);
	        assertNull(stack.ceiling(outside));
    	}
        
    }
    
    /**
     * Tests the floor method.
     */
    @Test
    public void testFloor() {

    	for(Plate[] array : arrays) {
        	
	    	Stack stack = new Stack(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<Plate> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        Plate toFloor = iter.next();
	        Plate outside = new Plate(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toFloor), toFloor);
	        assertNull(stack.ceiling(outside));
    	}
    	
    }
    
    /**
     * Tests the head set method.
     */
    @Test
    public void testHeadSet() {
    	
    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	Set<Plate> testSet = stack.getAll();
	    	
	        Plate random = (Plate) RandomUtil.randomObject(testSet);
	    	
	    	Set<Plate> headSet = stack.headSet(random);
	    	Set<Plate> subSet = stack.subSet(stack.first(), random);
	    	
	    	assertEquals(headSet, subSet);
    	}
    }
    
    /**
     * Tests the head set inclusive method.
     */
    @Test
    public void testHeadSetInclusive() {
    	
    	for(Plate[] array : arrays) {
	    	
	    	Stack stack = new Stack(array);
	    	
	    	Set<Plate> testSet = stack.getAll();
	    	
	    	Plate random = (Plate) RandomUtil.randomObject(testSet);
	    	
	    	Set<Plate> headSetTrue = stack.headSet(random, true);
	    	Set<Plate> subSetTrue = stack.subSet(stack.first(), true, random, true);
	    	Set<Plate> headSetFalse = stack.headSet(random, false);
	    	Set<Plate> subSetFalse = stack.subSet(stack.first(), true, random, false);
	    	
	    	assertEquals(headSetTrue, subSetTrue);
	    	assertEquals(headSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the tail set method.
     */
    @Test
    public void testTailSet() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	Set<Plate> testSet = stack.getAll();
	    	
	    	Plate random = (Plate) RandomUtil.randomObject(testSet);
	    	
	    	Set<Plate> tailSet = stack.tailSet(random);
	    	Set<Plate> subSet = stack.subSet(random, true, stack.last(), true);
	
	    	assertEquals(tailSet, subSet);
    	}
    }
    
    /**
     * Tests the tail set inclusive method.
     */
    @Test
    public void testTailSetInclusive() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	Set<Plate> testSet = stack.getAll();
	    	
	    	Plate random = (Plate) RandomUtil.randomObject(testSet);
	    	
	    	Set<Plate> tailSetTrue = stack.tailSet(random, true);
	    	Set<Plate> subSetTrue = stack.subSet(random, true, stack.last(), true);
	    	Set<Plate> tailSetFalse = stack.tailSet(random, false);
	    	Set<Plate> subSetFalse = stack.subSet(random, false, stack.last(), true);
	    	
	    	assertEquals(tailSetTrue, subSetTrue);
	    	assertEquals(tailSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the inclusive subset method.
     */
    @Test
    public void testSubSetInclusive() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;
	    	
	    	Set<Plate> subSetTrueTrue = new HashSet<Plate>();
	    	Set<Plate> subSetTrueFalse = new HashSet<Plate>();
	    	Set<Plate> subSetFalseTrue = new HashSet<Plate>();;
	    	Set<Plate> subSetFalseFalse = new HashSet<Plate>();;
	    	
	    	Plate startingPlate = null;
	    	Plate endingPlate = null;
	    	
	    	Iterator<Plate> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		Plate inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSetTrueTrue.add(new Plate(inclusive));
	    		    subSetTrueFalse.add(new Plate(inclusive));
	    		    startingPlate = new Plate(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		Plate inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new Plate(inclusive);
	    		    subSetTrueTrue.add(new Plate(inclusive));
	    		    subSetFalseTrue.add(new Plate(inclusive));
	    		    break;
	    		    
	    		} else {
	    			
	    			subSetTrueTrue.add(new Plate(inclusive));
	    			subSetTrueFalse.add(new Plate(inclusive));
	    			subSetFalseTrue.add(new Plate(inclusive));
	    			subSetFalseFalse.add(new Plate(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}

	    	Set<Plate> trueTrue = stack.subSet(startingPlate, true, endingPlate, true);
	    	Set<Plate> trueFalse = stack.subSet(startingPlate, true, endingPlate, false);
	    	Set<Plate> falseTrue = stack.subSet(startingPlate, false, endingPlate, true);
	    	Set<Plate> falseFalse = stack.subSet(startingPlate, false, endingPlate, false);
	
	    	assertEquals(trueTrue, subSetTrueTrue);
	    	assertEquals(trueFalse, subSetTrueFalse);
	    	assertEquals(falseTrue, subSetFalseTrue);
	    	assertEquals(falseFalse, subSetFalseFalse);
    	}
    	
    }
    
    /**
     * Tests the subset method.
     */
    @Test
    public void testSubSet() {

    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;     	
	    	
	    	Set<Plate> subSet = new TreeSet<Plate>();
	    	
	    	Plate startingPlate = null;
	    	Plate endingPlate = null;
	    	
	    	Iterator<Plate> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		Plate inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSet.add(new Plate(inclusive));
	    		    startingPlate = new Plate(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		Plate inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new Plate(inclusive);
	    		    break;
	    		    
	    		} else {
	    			
	    			subSet.add(new Plate(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}
	    	
	    	Set<Plate> toCompare = stack.subSet(startingPlate, endingPlate);
	
	    	assertEquals(toCompare, subSet);
    	}
    }

    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {
    	
    	for(Plate[] array : arrays) {
    		
    		Stack stack = new Stack(array);
    		
    		assertFalse(stack.isEmpty());
    		stack.clear();
    		assertTrue(stack.isEmpty());
    		
    	}
    	
    }

    /*----------------------- Testing helper methods -------------------------*/
    
    /**
     * Returns true if the row and column match plate type constants.
     * @param    int    plate row number
     * @param    int    plate column number
     * @return          true if row and column match a plate type
     */
    private boolean checkType(int rows, int columns) {
    	
        if((rows == Stack.ROWS_6WELL && columns == Stack.COLUMNS_6WELL) ||
          (rows == Stack.ROWS_12WELL && columns == Stack.COLUMNS_12WELL) ||
          (rows == Stack.ROWS_24WELL && columns == Stack.COLUMNS_24WELL) ||
          (rows == Stack.ROWS_48WELL && columns == Stack.COLUMNS_48WELL) ||
          (rows == Stack.ROWS_96WELL && columns == Stack.COLUMNS_96WELL) ||
          (rows == Stack.ROWS_384WELL && columns == Stack.COLUMNS_384WELL) ||
          (rows == Stack.ROWS_1536WELL && columns == Stack.COLUMNS_1536WELL)) {
        	return true;
        }
        
        return false;
    }
    
    /**
     * Adds the appropriate type and descriptor for the plate dimensions.
     * @param    int    plate row number
     * @param    int    plate column number
     */
    private void getDescriptor(int rows, int columns) {

    	String descriptor;
    	
        if(rows == Stack.ROWS_6WELL && 
           columns == Stack.COLUMNS_6WELL) {

            descriptor = "6-Well";

        } else if(rows == Stack.ROWS_12WELL && 
                  columns == Stack.COLUMNS_12WELL) {

            descriptor = "12-Well";
        
        } else if(rows == Stack.ROWS_24WELL && 
                  columns == Stack.COLUMNS_24WELL) {

            descriptor = "24-Well";
        
        } else if(rows == Stack.ROWS_48WELL && 
                  columns == Stack.COLUMNS_48WELL) {

            descriptor = "48-Well";
        
        } else if(rows == Stack.ROWS_96WELL && 
                  columns == Stack.COLUMNS_96WELL) {

            descriptor = "96-Well";
        
        } else if(rows == Stack.ROWS_384WELL && 
                  columns == Stack.COLUMNS_384WELL) {

            descriptor = "384-Well";
        
        } else if(rows == Stack.ROWS_1536WELL && 
                  columns == Stack.COLUMNS_1536WELL) {

            descriptor = "1536-Well";
        
        } else {

            descriptor = "Custom Stack: " + rows + "x" + columns;
            
        }

        this.descriptors.add(descriptor);
    }
}
