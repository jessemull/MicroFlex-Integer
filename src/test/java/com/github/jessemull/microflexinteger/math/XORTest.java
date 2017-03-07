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

/* -------------------------------- Package --------------------------------- */

package com.github.jessemull.microflexinteger.math;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflexinteger.math.XOR;
import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Stack;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;
import com.github.jessemull.microflexinteger.util.RandomUtil;

/**
 * This class tests the methods in the XOR integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Dec 9, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XORTest {
	
/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = -100000;          // Minimum integer value for wells
	private static int maxValue = 1000000;          // Maximum integer value for wells
	private static int minPlate = 10;               // Plate minimum
	private static int maxPlate = 25;               // Plate maximum
	private static Random random = new Random();    // Generates random integers
	
	/* The XOR operation */
	
	private static XOR xor = new XOR();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<Plate[]> arrays1 = new ArrayList<Plate[]>();
	private static List<Plate[]> arrays2 = new ArrayList<Plate[]>();
	private static List<Plate[]> uneven = new ArrayList<Plate[]>();
	private static List<Stack> stacks1 = new ArrayList<Stack>();
	private static List<Stack> stacks2 = new ArrayList<Stack>();
	private static List<Stack> stacksUneven = new ArrayList<Stack>();
	
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
		
		rows = Plate.ROWS_48WELL + random.nextInt(Plate.ROWS_1536WELL - 
	           Plate.ROWS_48WELL + 1);

		columns =  Plate.COLUMNS_48WELL + random.nextInt(Plate.COLUMNS_1536WELL - 
		           Plate.COLUMNS_48WELL + 1);

    	length = rows * columns / 5;
    	
    	for(int i = 0; i < stackNumber; i++) {
    		
    		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    		
    		Stack stack1 = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length, "Plate1-" + i, plateNumber);
    		Stack stack2 = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length, "Plate2-" + i, plateNumber);
    		Stack stackUneven = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + i, plateNumber);
    		
    		stacks1.add(stack1);
    		stacks2.add(stack2);
    		stacksUneven.add(stackUneven);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		Plate[] array1 = new Plate[plateNumber];
    		Plate[] array2 = new Plate[plateNumber];
    		Plate[] unevenArray = new Plate[plateNumber];
    		
    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = Plate.ROWS_48WELL + random.nextInt(Plate.ROWS_1536WELL - 
    			       Plate.ROWS_48WELL + 1);

    			columns =  Plate.COLUMNS_48WELL + random.nextInt(Plate.COLUMNS_1536WELL - 
    			           Plate.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			Plate plate1 = RandomUtil.randomPlateInteger(
    					rows, columns, minValue, maxValue, length, "Plate1-" + j);
    			Plate plate2 = RandomUtil.randomPlateInteger(
    					rows, columns, minValue, maxValue, length, "Plate2-" + j);
    			Plate unevenPlate = RandomUtil.randomPlateInteger(
    					rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + j);
    			
    			array1[j] = plate1;
    			array2[j] = plate2;
    			unevenArray[j] = unevenPlate;
    		}
    		
    		arrays1.add(array1);
    		arrays2.add(array2);
    		uneven.add(unevenArray);
    	}
    	
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
    /* ---------------------------- Constructors ---------------------------- */
	
	/**
	 * Tests the constructor.
     */
	@Test
	public void testConstructor() {		
		XOR test = new XOR();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the XOR operation using two wells.
     */
	@Test
    public void testWells() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], well2[j]);
    			List<Integer> returnedUneven = xor.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			for(int l = list1.size(); l < unevenList.size(); l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
	
    /**
     * Tests the XOR operation using two wells and indices.
     */
	@Test
    public void testWellsIndices() {
		
		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = xor.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict XOR operation using two wells.
     */
	@Test
    public void testWellsStrict() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wellsStrict(well1[j], well2[j]);
    			List<Integer> returnedUneven = xor.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict XOR operation using two wells and indices.
     */
	@Test
    public void testWellsStrictIndices() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = xor.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the XOR operation using a constant and a well.
     */
	@Test
    public void testWellConstant() {
    	
    	Plate[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();

    		int constantInt = random.nextInt();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], constantInt);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ constantInt);
    			}

    			assertEquals(result, returned);
    		}
    		
    	}
    }
    
    /**
     * Tests the XOR operation using an array and a well.
     */ 
	@Test
    public void testWellArray() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
                int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], array);
    			List<Integer> returnedUneven = xor.wells(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ array[k]);
    				resultUneven.add(list1.get(k) ^ arrayUneven[k]);
    			}

    			for(int l = list1.size(); l < unevenList.size(); l++) {
    				resultUneven.add(arrayUneven[l]);
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the XOR operation using an array, a well and indices.
     */
	@Test
    public void testWellArrayIndices() {

    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
                int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], array, begin, end - begin);
    			List<Integer> returnedUneven = xor.wells(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) ^ array[k]);
    				resultUneven.add(list1.get(k) ^ arrayUneven[k]);
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(arrayUneven[l]);
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the XOR operation using a collection and a well.
     */
	@Test
    public void testWellCollection() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], well2[j]);
    			List<Integer> returnedUneven = xor.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			for(int l = list1.size(); l < unevenList.size(); l++) {
    				resultUneven.add(unevenList.get(l));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the XOR operation using a collection, a well and indices.
     */
    @Test
    public void testWellCollectionIndices() {


		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wells(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = xor.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict XOR operation using an array and a well.
     */
    @Test
    public void testWellStrictArray() {


		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
                int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wellsStrict(well1[j], array);
    			List<Integer> returnedUneven = xor.wellsStrict(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ array[k]);
    				resultUneven.add(list1.get(k) ^ arrayUneven[k]);
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict XOR operation using an array, a well and indices.
     */
    @Test
    public void testWellStrictArrayIndices() {

    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
                int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wellsStrict(well1[j], array, begin, end - begin);
    			List<Integer> returnedUneven = xor.wellsStrict(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) ^ array[k]);
    				resultUneven.add(list1.get(k) ^ arrayUneven[k]);
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}	
    	}
    }
    
    /**
     * Tests the strict XOR operation using a collection and a well.
     */
    @Test
    public void testWellStrictCollection() {

    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wellsStrict(well1[j], well2[j]);
    			List<Integer> returnedUneven = xor.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict XOR operation using a collection and a well.
     */
    public void testWellStrictCollectionIndices() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Well[] well1 = plates1[i].dataSet().toWellArray();
    		Well[] well2 = plates2[i].dataSet().toWellArray();
    		Well[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = xor.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = xor.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) ^ list2.get(k));
    				resultUneven.add(list1.get(k) ^ unevenList.get(k));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Tests the XOR operation using two plates.
     */
    @Test
    public void testPlates() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		Plate plate2 = plates2[i];
    		Plate plateUneven = unevenPlates[i];
    		
    		WellSet set1 = plate1.dataSet();
    		WellSet set2 = plate2.dataSet();
    		WellSet setUneven = plateUneven.dataSet();
    		
    		WellSet[] result = this.set(set1, set2, setUneven, false);
    		Plate returnedPlate = xor.plates(plate1, plate2);
    		Plate returnedPlateUneven = xor.plates(plate1, plateUneven);
    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returnedPlate.iterator();
    		Iterator<Well> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<Well> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using two plates and indices.
     */
    @Test
    public void testPlatesIndices() {
    	
		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		Plate plate2 = plates2[i];
    		Plate plateUneven = unevenPlates[i];
    		
    		WellSet set1 = plate1.dataSet();
    		WellSet set2 = plate2.dataSet();
    		WellSet setUneven = plateUneven.dataSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSet[] result = this.set(set1, set2, setUneven, begin, end, false);
    		Plate returnedPlate = xor.plates(plate1, plate2, begin, end - begin);
    		Plate returnedPlateUneven = xor.plates(plate1, plateUneven, begin, end - begin);
    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returnedPlate.iterator();
    		Iterator<Well> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<Well> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using two plates.
     */
    public void testPlatesStrict() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		Plate plate2 = plates2[i];
    		Plate plateUneven = unevenPlates[i];
    		
    		WellSet set1 = plate1.dataSet();
    		WellSet set2 = plate2.dataSet();
    		WellSet setUneven = plateUneven.dataSet();
    		
    		WellSet[] result = this.set(set1, set2, setUneven, true);
    		Plate returnedPlate = xor.platesStrict(plate1, plate2);
    		Plate returnedPlateUneven = xor.platesStrict(plate1, plateUneven);
    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returnedPlate.iterator();
    		Iterator<Well> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<Well> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the strict XOR operation using two plates and indices.
     */
    @Test
    public void testPlatesStrictIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		Plate plate2 = plates2[i];
    		Plate plateUneven = unevenPlates[i];
    		
    		WellSet set1 = plate1.dataSet();
    		WellSet set2 = plate2.dataSet();
    		WellSet setUneven = plateUneven.dataSet();

    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSet[] result = this.set(set1, set2, setUneven, begin, end, true);
    		Plate returnedPlate = xor.platesStrict(plate1, plate2, begin, end - begin);
    		Plate returnedPlateUneven = xor.platesStrict(plate1, plateUneven, begin, end - begin);
    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returnedPlate.iterator();
    		Iterator<Well> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<Well> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using a constant and a plate.
     */
    @Test
    public void testPlateConstant() {
    	
    	Plate[] plates1 = arrays1.get(0);

    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];

    		WellSet set1 = plate1.dataSet();
    		
    		int min = 1000;
    		int max = 10000;
    		
    		int randomInt =  min + (max - min) * random.nextInt();

    	    WellSet result = new WellSet();
    	    
    		for(Well well : set1) {
    			
    			List<Integer> list = new ArrayList<Integer>();
    			
    			for(int in : well.data()) {
    				list.add(in ^ randomInt);
    			}
    			
    			result.add(new Well(well.row(), well.column(), list));
    		}

    		Plate returnedPlate = xor.plates(plate1, randomInt);
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		
    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returnedPlate.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);
    	}
    }
    
    /**
     * Tests the XOR operation using an array and a plate.
     */
    @Test
    public void testPlateArray() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    		Plate returned = xor.plates(plate1, array);
    		Plate returnedUneven = xor.plates(plate1, arrayUneven);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using an array, a plate and indices.
     */
    @Test
    public void testPlateArrayIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
           for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    		Plate returned = xor.plates(plate1, array, begin, end - begin);
    		Plate returnedUneven = xor.plates(plate1, arrayUneven, begin, end - begin);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using a collection and a plate.
     */
    @Test
    public void testPlateCollection() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    		Plate returned = xor.plates(plate1, inputList);
    		Plate returnedUneven = xor.plates(plate1, inputListUneven);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using a collection, a plate and indices.
     */
    @Test
    public void testPlateCollectionIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    	
    		Plate returned = xor.plates(plate1, inputList, begin, end - begin);
    		Plate returnedUneven = xor.plates(plate1, inputListUneven, begin, end - begin);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict XOR operation using an array and a plate.
     */
    @Test
    public void testPlateStrictArray() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    		Plate returned = xor.platesStrict(plate1, array);
    		Plate returnedUneven = xor.platesStrict(plate1, arrayUneven);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using an array, a plate and indices.
     */
    @Test
    public void testPlateStrictArrayIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    	    Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
            for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    		Plate returned = xor.platesStrict(plate1, array, begin, end - begin);
    		Plate returnedUneven = xor.platesStrict(plate1, arrayUneven, begin, end - begin);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}    	
    }
    
    /**
     * Tests the strict XOR operation using a collection and a plate.
     */
    @Test
    public void testPlateStrictCollection() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    		Plate returned = xor.platesStrict(plate1, inputList);
    		Plate returnedUneven = xor.platesStrict(plate1, inputListUneven);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using a collection, a plate and indices.
     */
    @Test
    public void testPlateStrictCollectionIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		Plate plate1 = plates1[i];
    		WellSet set1 = plate1.dataSet();
    		
    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		Plate resultPlate = new Plate(plate1.rows(), plate1.columns(), result);
    		Plate resultPlateUneven = new Plate(plate1.rows(), plate1.columns(), resultUneven);
    	
    		Plate returned = xor.platesStrict(plate1, inputList, begin, end - begin);
    		Plate returnedUneven = xor.platesStrict(plate1, inputListUneven, begin, end - begin);

    		Iterator<Well> iter1 = resultPlate.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the XOR operation using two sets.
     */
    @Test
    public void testSets() {
    	
		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();
    		WellSet set2 = plates2[i].dataSet();
    		WellSet setUneven = unevenPlates[i].dataSet();
    		
    		WellSet[] result = this.set(set1, set2, setUneven, false);
    		WellSet returned = xor.sets(set1, set2);
    		WellSet returnedUneven = xor.sets(set1, setUneven);

    		Iterator<Well> iter1 = result[0].iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = result[1].iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using two sets and indices.
     */
    @Test
    public void testSetsIndices() {

    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();
    		WellSet set2 = plates2[i].dataSet();
    		WellSet setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSet[] result = this.set(set1, set2, setUneven, begin, end, false);
    		WellSet returned = xor.sets(set1, set2, begin, end - begin);
    		WellSet returnedUneven = xor.sets(set1, setUneven, begin, end - begin);

    		Iterator<Well> iter1 = result[0].iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = result[1].iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict XOR operation using two sets.
     */
    @Test
    public void testSetsStrict() {

		Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();
    		WellSet set2 = plates2[i].dataSet();
    		WellSet setUneven = unevenPlates[i].dataSet();
    		
    		WellSet[] result = this.set(set1, set2, setUneven, true);
    		WellSet returned = xor.setsStrict(set1, set2);
    		WellSet returnedUneven = xor.setsStrict(set1, setUneven);

    		Iterator<Well> iter1 = result[0].iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = result[1].iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict XOR operation using two sets and indices.
     */
    @Test
    public void testSetsStrictIndices() {

    	Plate[] plates1 = arrays1.get(0);
		Plate[] plates2 = arrays2.get(0);
		Plate[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();
    		WellSet set2 = plates2[i].dataSet();
    		WellSet setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSet[] result = this.set(set1, set2, setUneven, begin, end, true);
    		WellSet returned = xor.setsStrict(set1, set2, begin, end - begin);
    		WellSet returnedUneven = xor.setsStrict(set1, setUneven, begin, end - begin);

    		Iterator<Well> iter1 = result[0].iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = result[1].iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using a constant and a set.
     */
    @Test
    public void testSetConstant() {
    	
    	Plate[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set = plates1[i].dataSet();
    		WellSet result = new WellSet();

    		int min = 1000;
    		int max = 10000;
    		
    		int randomInt =  min + (max - min) * random.nextInt();

    		for(Well well : set) {
    			
    			List<Integer> list = new ArrayList<Integer>();
    			
    			for(int in : well.data()) {
    				list.add(in ^ randomInt);
    			}
    			
    			result.add(new Well(well.row(), well.column(), list));
    		}

    		WellSet returned = xor.sets(set, randomInt);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();

    		while(iter1.hasNext()) {
    			
    			List<Integer> well1 = iter1.next().data();
    			List<Integer> well2 = iter2.next().data();
    			
    			assertEquals(well1, well2);
    			
    		}
    		
    		assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the XOR operation using an array and a set.
     */
    @Test
    public void testSetArray() {   	

		Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		WellSet returned = xor.sets(set1, array);
    		WellSet returnedUneven = xor.sets(set1, arrayUneven);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the XOR operation using an array, a set and indices.
     */
    @Test
    public void testSetArrayIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		WellSet returned = xor.sets(set1, array, begin, end - begin);
    		WellSet returnedUneven = xor.sets(set1, arrayUneven, begin, end - begin);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the XOR operation using a collection and a set.
     */
    @Test
    public void testSetCollection() {   

		Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		WellSet returned = xor.sets(set1, inputList);
    		WellSet returnedUneven = xor.sets(set1, inputListUneven);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the XOR operation using a collection, a set and indices.
     */
    @Test
    public void testSetCollectionIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(Well well : set1) {
    		
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
 		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
			
    		WellSet returned = xor.sets(set1, inputList, begin, end - begin);
    		WellSet returnedUneven = xor.sets(set1, inputListUneven, begin, end - begin);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict XOR operation using an array and a set.
     */
    @Test
    public void testSetStrictArray() {

		Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}

    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		WellSet returned = xor.setsStrict(set1, array);
    		WellSet returnedUneven = xor.setsStrict(set1, arrayUneven);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict XOR operation using an array, a set and indices.
     */
    public void testSetStrictArrayIndices() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ array[index]);
    			}
    			
    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ arrayUneven[index]);
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		WellSet returned = xor.setsStrict(set1, array, begin, end - begin);
    		WellSet returnedUneven = xor.setsStrict(set1, arrayUneven, begin, end - begin);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using a collection and a set.
     */
    @Test
    public void testSetStrictCollection() {
    	
    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}

    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
    		

    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
    		
    		WellSet returned = xor.setsStrict(set1, inputList);
    		WellSet returnedUneven = xor.setsStrict(set1, inputListUneven);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using a collection, a set and indices.
     */
    @Test
    public void testSetStrictCollectionIndices() {

    	Plate[] plates1 = arrays1.get(0);
		Well wellEven = arrays2.get(0)[0].dataSet().first();
		Well wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSet set1 = plates1[i].dataSet();

    		WellSet result = new WellSet();
    		WellSet resultUneven = new WellSet();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(Well well : set1) {
    		
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputList.get(index));
    			}

    			result.add(new Well(well.row(), well.column(), wellResult));
    		}
 		
    		for(Well well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
    		}
			
    		WellSet returned = xor.setsStrict(set1, inputList, begin, end - begin);
    		WellSet returnedUneven = xor.setsStrict(set1, inputListUneven, begin, end - begin);

    		Iterator<Well> iter1 = result.iterator();
    		Iterator<Well> iter2 = returned.iterator();
    		Iterator<Well> iterUneven1 = resultUneven.iterator();
    		Iterator<Well> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			Well well1 = iter1.next();
    			Well well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			Well well1 = iterUneven1.next();
    			Well well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the XOR operation using two stacks.
     */
    @Test
    public void testStacks() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			Stack stack2 = stacks2.get(k);
			Stack stackUneven = stacksUneven.get(k);

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			Iterator<Plate> stackIter2 = stack2.iterator();
			Iterator<Plate> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		Plate plate1 = stackIter1.next();
	    		Plate plate2 = stackIter2.next();
	    		Plate plateUneven = stackUnevenIter.next();
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		WellSet setUneven = plateUneven.dataSet();
	    		
	    		WellSet[] result = this.set(set1, set2, setUneven, false);
	    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
	    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		resultStack.add(stackIter1.next());
	    	}
	    	
	    	while(stackIter2.hasNext()) {
	    		resultStack.add(stackIter2.next());
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		resultStackUneven.add(stackUnevenIter.next());
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, stack2);
	    	Stack returnedUneven = xor.stacks(stack1, stackUneven);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the XOR operation using two stack and indices.
     */
    @Test
    public void testStacksIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			Stack stack2 = stacks2.get(k);
			Stack stackUneven = stacksUneven.get(k);

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			Iterator<Plate> stackIter2 = stack2.iterator();
			Iterator<Plate> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		Plate plate1 = stackIter1.next();
	    		Plate plate2 = stackIter2.next();
	    		Plate plateUneven = stackUnevenIter.next();
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		WellSet setUneven = plateUneven.dataSet();

	    		WellSet[] result = this.set(set1, set2, setUneven, begin, end, false);
	    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
	    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		for(Well well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackIter2.hasNext()) {

	    		Plate plate = stackIter2.next();
	    		
	    		for(Well well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		
	    		Plate plate = stackUnevenIter.next();
	    		
	    		for(Well well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStackUneven.add(plate);
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, stack2, begin, end - begin);
	    	Stack returnedUneven = xor.stacks(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using two stacks.
     */
    @Test
    public void testStacksStrict() {

    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			Stack stack2 = stacks2.get(k);
			Stack stackUneven = stacksUneven.get(k);

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			Iterator<Plate> stackIter2 = stack2.iterator();
			Iterator<Plate> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		Plate plate1 = stackIter1.next();
	    		Plate plate2 = stackIter2.next();
	    		Plate plateUneven = stackUnevenIter.next();
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		WellSet setUneven = plateUneven.dataSet();
	    		
	    		WellSet[] result = this.set(set1, set2, setUneven, true);
	    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
	    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	
	    	Stack returned = xor.stacksStrict(stack1, stack2);
	    	Stack returnedUneven = xor.stacksStrict(stack1, stackUneven);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict XOR operation using two stacks and indices.
     */
    @Test
    public void testStacksStrictIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			Stack stack2 = stacks2.get(k);
			Stack stackUneven = stacksUneven.get(k);

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			Iterator<Plate> stackIter2 = stack2.iterator();
			Iterator<Plate> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		Plate plate1 = stackIter1.next();
	    		Plate plate2 = stackIter2.next();
	    		Plate plateUneven = stackUnevenIter.next();
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		WellSet setUneven = plateUneven.dataSet();

	    		WellSet[] result = this.set(set1, set2, setUneven, begin, end, true);
	    		Plate resultPlate = new Plate(plate1.rows(), plate2.columns(), result[0]);
	    		Plate resultUnevenPlate = new Plate(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacksStrict(stack1, stack2, begin, end - begin);
	    	Stack returnedUneven = xor.stacksStrict(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the XOR operation using a constant and a stack.
     */
    @Test
    public void testStackConstant() {

    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			Stack resultStack = new Stack(stack1.rows(), stack1.columns());

			int min = 1000;
    		int max = 10000;
    		
    		int randomInt =  min + (max - min) * random.nextInt();
    	    
			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();  
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns());

	    		for(Well well : plate) {
	    			
	    			List<Integer> resultList = new ArrayList<Integer>();
	    			
	    			for(int in : well) {
	    				resultList.add(in ^ randomInt);
	    			}
	    			
	    			resultPlate.addWells(new Well(well.row(), well.column(), resultList));
	    		}
	
	    		resultStack.add(resultPlate);
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, randomInt);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
    	}  	
    }
    
    /**
     * Tests the XOR operation using an array and a stack.
     */
    @Test
    public void testStackArray() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index) ^ array[index]);
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index) ^ arrayUneven[index]);
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, array);
	    	Stack returnedUneven = xor.stacks(stack1, arrayUneven);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the XOR operation using an array, a stack and indices.
     */
    @Test
    public void testStackArrayIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ array[index]);
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length && j < end; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ arrayUneven[index]);
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length && j < end; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, array, begin, end - begin);
	    	Stack returnedUneven = xor.stacks(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the XOR operation using a collection and a stack.
     */
    @Test
    public void testStackCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index) ^ inputList.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size(); j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size(); j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, inputList);
	    	Stack returnedUneven = xor.stacks(stack1, inputListUneven);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the XOR operation using a collection, a stack and indices.
     */
    @Test
    public void testStackCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ inputList.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size() && j < end; j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacks(stack1, inputList, begin, end - begin);
	    	Stack returnedUneven = xor.stacks(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict XOR operation using an array and a stack.
     */
    @Test
    public void testStackStrictArray() {

    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index) ^ array[index]);
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index) ^ arrayUneven[index]);
	    			}

	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacksStrict(stack1, array);
	    	Stack returnedUneven = xor.stacksStrict(stack1, arrayUneven);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict XOR operation using an array, a stack and indices.
     */
    @Test
    public void testStackStrictArrayIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ array[index]);
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ arrayUneven[index]);
	    			}
	    			
	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacksStrict(stack1, array, begin, end - begin);
	    	Stack returnedUneven = xor.stacksStrict(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict XOR operation using a collection and a stack.
     */
    public void testStackStrictCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index) ^ inputList.get(index));
	    			}
	    			
	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
	    			}
	    			
	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacksStrict(stack1, inputList);
	    	Stack returnedUneven = xor.stacksStrict(stack1, inputListUneven);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict XOR operation using a collection, a stack and indices.
     */
    public void testStackStrictCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			Stack stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			Stack resultStack = new Stack(stack1.rows(), stack1.columns());
			Stack resultStackUneven = new Stack(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<Plate> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		Plate plate = stackIter1.next();
	    		
	    		WellSet set = plate.dataSet();
	    		WellSet result = new WellSet();
	    		WellSet resultUneven = new WellSet();
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ inputList.get(index));
	    			}

	    			result.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(Well well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) ^ inputListUneven.get(index));
	    			}

	    			resultUneven.add(new Well(well.row(), well.column(), wellResult));
	    		}
	    		
	    		Plate resultPlate = new Plate(plate.rows(), plate.columns(), result);
	    		Plate resultUnevenPlate = new Plate(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	Stack returned = xor.stacksStrict(stack1, inputList, begin, end - begin);
	    	Stack returnedUneven = xor.stacksStrict(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<Plate> iter1 = resultStack.iterator();
	    	Iterator<Plate> iter2 = returned.iterator();
	    	Iterator<Plate> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<Plate> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		Plate plate1 = iter1.next();
	    		Plate plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSet set1 = plate1.dataSet();
	    		WellSet set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<Well> set1Iter = set1.iterator();
	    		Iterator<Well> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			Well well1 = set1Iter.next();
	    			Well well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		Plate plate1 = iterUneven1.next();
	    		Plate plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }

    /*---------------------------- Helper methods ----------------------------*/
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length and returns the result.
     * @param    WellSet    the first set
     * @param    WellSet    set of equal length
     * @param    WellSet    set of unequal length
     * @param    boolean           strict operation when true
     * @return                     result of two equal sets at index 0
     *                             result of two unequal sets at index 1
     */
    private WellSet[] set(WellSet set1, WellSet set2, WellSet uneven, boolean strict) {

    	WellSet finalResult = new WellSet();
    	WellSet finalResultUneven = new WellSet();
    	WellSet[] finalResultReturn = new WellSet[2];
    			
    	WellSet clone1 = new WellSet(set1);
    	WellSet clone2 = new WellSet(set2);
    	WellSet cloneUneven1 = new WellSet(set1);
    	WellSet cloneUneven2 = new WellSet(uneven);
    	
    	WellSet excluded1 = new WellSet(set1);
    	WellSet excluded2 = new WellSet(set2);
    	WellSet excludedUneven1 = new WellSet(set1);
    	WellSet excludedUneven2 = new WellSet(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<Well> iter1 = clone1.iterator();
    	Iterator<Well> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			Well well1 = iter1.next();
			Well well2 = iter2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();

			List<Integer> result = new ArrayList<Integer>();
				
			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k) ^ list2.get(k));
			}
	
			finalResult.add(new Well(well1.row(), well1.column(), result));
		}
		
		Iterator<Well> iterUneven1 = cloneUneven1.iterator();
    	Iterator<Well> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			Well well1 = iterUneven1.next();
			Well well2 = iterUneven2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();
	
			List<Integer> result = new ArrayList<Integer>();

			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k) ^ list2.get(k));
			}

			if(!strict) {
			    for(int j = list1.size(); j < list2.size(); j++) {
		            result.add(list2.get(j));
			    }
			}
			
			finalResultUneven.add(new Well(well1.row(), well1.column(), result));
		}
		
        if(!strict) {
			
			finalResult.add(excluded1);
			finalResult.add(excluded2);
			
		    finalResultUneven.add(excludedUneven1);
		    finalResultUneven.add(excludedUneven2);
		}
		
		finalResultReturn[0] = finalResult;
		finalResultReturn[1] = finalResultUneven;

		return finalResultReturn;
    }
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length using the values between the indices and returns the result.
     * @param    WellSet    the first set
     * @param    WellSet    set of equal length
     * @param    WellSet    set of unequal length
     * @param    int               beginning index
     * @param    int               ending index
     * @param    boolean           strict operation when true
     * @return                     result of two equal sets at index 0
     *                             result of two unequal sets at index 1
     */
    private WellSet[] set(WellSet set1, WellSet set2, WellSet uneven, int begin, int end, boolean strict) {

    	WellSet finalResult = new WellSet();
    	WellSet finalResultUneven = new WellSet();
    	WellSet[] finalResultReturn = new WellSet[2];
    			
    	WellSet clone1 = new WellSet(set1);
    	WellSet clone2 = new WellSet(set2);
    	WellSet cloneUneven1 = new WellSet(set1);
    	WellSet cloneUneven2 = new WellSet(uneven);
    	
    	WellSet excluded1 = new WellSet(set1);
    	WellSet excluded2 = new WellSet(set2);
    	WellSet excludedUneven1 = new WellSet(set1);
    	WellSet excludedUneven2 = new WellSet(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<Well> iter1 = clone1.iterator();
    	Iterator<Well> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			Well well1 = iter1.next();
			Well well2 = iter2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();

			List<Integer> result = new ArrayList<Integer>();
				
			for(int k = begin; k < end; k++) {
				result.add(list1.get(k) ^ list2.get(k));
			}
	
			finalResult.add(new Well(well1.row(), well1.column(), result));
		}
		
		Iterator<Well> iterUneven1 = cloneUneven1.iterator();
    	Iterator<Well> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			Well well1 = iterUneven1.next();
			Well well2 = iterUneven2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();
	
			List<Integer> result = new ArrayList<Integer>();

			for(int k = begin; k < end; k++) {
				result.add(list1.get(k) ^ list2.get(k));
			}
			
			finalResultUneven.add(new Well(well1.row(), well1.column(), result));
		}
		
		if(!strict) {
			
			for(Well well : excludedUneven1) {  
	    		well = well.subList(begin, end - begin);
	    	}
	    	
	    	for(Well well : excludedUneven2) {
	    		well = well.subList(begin, end - begin);
	    	}
	    	
			finalResult.add(excluded1);
			finalResult.add(excluded2);
			
		    finalResultUneven.add(excludedUneven1);
		    finalResultUneven.add(excludedUneven2);
		}
		
		finalResultReturn[0] = finalResult;
		finalResultReturn[1] = finalResultUneven;

		return finalResultReturn;
    }
}
