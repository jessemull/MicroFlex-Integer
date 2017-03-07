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

package com.github.jessemull.microflexinteger.stat;

/* ----------------------------- Dependencies ------------------------------- */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;

/**
 * This class performs weighted statistical operations for integer plate stacks, 
 * plates, wells and well sets. To create a custom weighted statistical operation 
 * extend this class and override the calculate methods using the appropriate 
 * weighted statistical operation. 
 * 
 * <br><br>
 * 
 * From wikipedia: a weight function is a mathematical device used when performing 
 * a sum, integral, or average to give some elements more "weight" or influence on 
 * the result than other elements in the same set. In statistics a weighted function 
 * is often used to correct bias. The weighted statistic class implements a weighted 
 * function by accepting an array of values as weights. The values in each well of 
 * the stack, plate, set or well are multiplied by the values within the double 
 * array prior to the statistical calculation.
 * 
 * <br><br>
 * 
 * Weighted statistical operations can be performed on stacks, plates, sets and 
 * wells using standard or aggregated functions. Standard functions calculate the 
 * desired statistic for each well in the stack, plate or set. Aggregated functions 
 * aggregate the values from all the wells in the stack, plate or set and perform 
 * the weighted statistical operation on the aggregated values. Both standard and 
 * aggregated functions can be performed on a subset of data within the stack, 
 * plate, set or well.
 * 
 * <br><br>
 * 
 * The methods within the MicroFlex library are meant to be flexible and the
 * descriptive statistic object supports operations using a single stack, plate,
 * set or well as well as collections and arrays of stacks, plates, sets or wells. 
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Operation<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Beginning<br>Index<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Length of<br>Subset<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Input/Output</div></th>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Standard</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td style="padding-bottom: 7px;">Accepts a single well, set, plate or stack and an array of weights as input</td>
 *             </tr>
 *             <tr>
 *                <td>Multiplies the values in each well of a well, set, plate or stack by the values
 *                <br> in the weights array then calculates the statistic using the weighted values</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Aggregated</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td style="padding-bottom: 7px;">Accepts a single well/set/plate/stack or a collection/array of wells/sets/plates/stacks 
 *                <br>and an array of weights as input</td>
 *             </tr>
 *              <tr>
 *                <td>Multiplies the values in each well of a well, set, plate or stack by the values in the 
 *                <br>weights array, aggregates the data from all the wells in the well/set/plate/stack then 
 *                <br>calculates the statistic using the aggregated weighted values</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class DescriptiveStatisticWeights extends DescriptiveStatistic {

    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Returns the weighted statistic for each plate well.
     * @param    Plate    the plate
     * @param    double[]        weights for the data set
     * @return                   map of wells and results
     */
    public Map<Well, Double> plate(Plate plate, double[] weights) {

        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
    	
    	Map<Well, Double> result = new TreeMap<Well, Double>();
        
        for (Well well : plate) {
            Well clone = new Well(well);
            result.put(clone, well(well, weights));
        }
        
        return result;
        
    }
    
    /**
     * Returns the weighted statistic for each plate well using the values between the 
     * beginning and ending indices.
     * @param    Plate    the plate
     * @param    double[]        weights for the data set
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @return                   map of wells and results
     */
    public Map<Well, Double> plate(Plate plate, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
    	
    	Map<Well, Double> result = new TreeMap<Well, Double>();
        
        for (Well well : plate) {
            Well clone = new Well(well);
            result.put(clone, well(well, weights, begin, length));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated plate statistics -------------------  */ 
        
    /**
     * Returns the aggregated weighted statistic for the plate.
     * @param    Plate    the plate
     * @param    double[]        weights for the data set
     * @return                   the aggregated result
     */
    public double platesAggregated(Plate plate, double[] weights) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");
        Preconditions.checkNotNull(weights, "Weights array cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : plate) {
        	
        	List<Double> input = well.toDouble();
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i) * weights[i]);
        	}
        	
        }
      
        return calculate(aggregated);
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each plate.
     * @param    Collection<PlateInteger>    collection of plates
     * @param    double[]                    weights for the data set
     * @return                               map of plates and aggregated results
     */
    public Map<Plate, Double> platesAggregated(
            Collection<Plate> collection, double[] weights) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<Plate, Double> results = new TreeMap<Plate, Double>();
        
        for(Plate plate : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {

            	List<Double> input = well.toDouble();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each plate.
     * @param    PlateInteger[]    array of plates
     * @param    double[]          weights for the data set
     * @return                     map of plates and aggregated results
     */
    public Map<Plate, Double> platesAggregated(
            Plate[] array, double[] weights) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<Plate, Double> results = new TreeMap<Plate, Double>();
        
        for(Plate plate : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {

            	List<Double> input = well.toDouble();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for the plate using the values 
     * between the indices.
     * @param    Plate    the plate
     * @param    double[]        weights for the data set
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @return                   the aggregated result
     */ 
    public double platesAggregated(Plate plate, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");
        
        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : plate) {

        	List<Double> input = well.toDouble().subList(begin, begin + length);
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i) * weights[i]);
        	}
        	
        }
      
        return calculate(aggregated);
        
    }

    /**
     * Returns the aggregated weighted statistic for each plate using the values
     * between the indices.
     * @param    Collection<PlateInteger>    collection of plates
     * @param    double[]                    weights for the data set
     * @param    int                         beginning index of subset
     * @param    int                         length of subset
     * @return                               map of plates and aggregated results
     */
    public Map<Plate, Double> platesAggregated(
            Collection<Plate> collection, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<Plate, Double> results = new TreeMap<Plate, Double>();
        
        for(Plate plate : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {

            	List<Double> input = well.toDouble().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each plate using the values
     * between the indices.
     * @param    PlateInteger[]    array of plates
     * @param    double[]          weights for the data set
     * @param    int               beginning index of subset
     * @param    int               length of subset
     * @return                     map of plates and aggregated results
     */
    public Map<Plate, Double> platesAggregated(
            Plate[] array, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<Plate, Double> results = new TreeMap<Plate, Double>();
        
        for(Plate plate : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {

            	List<Double> input = well.toDouble().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Returns the weighted statistic of each well in the well set.
     * @param    WellSet    the well set
     * @param    double[]          weights for the data set
     * @return                     map of wells and results
     */
    public Map<Well, Double> set(WellSet set, double[] weights) {

    	Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<Well, Double> result = new TreeMap<Well, Double>();
        
        for (Well well : set) {
        	Well clone = new Well(well);
            result.put(clone,  well(well, weights));
        }
        
        return result;
        
    }
    
    /**
     * Returns the weighted statistic of each well in the well set using the values 
     * between the beginning and ending indices.
     * @param    WellSet    the well set
     * @param    double[]          weights for the data set
     * @param    int               beginning index of subset
     * @param    int               length of subset
     * @return                     map of wells and results
     */
    public Map<Well, Double> set(WellSet set, double[] weights, int begin, int length) {

        Preconditions.checkNotNull(set, "The well set cannot be null.");
    	
    	Map<Well, Double> result = new TreeMap<Well, Double>();
        
        for (Well well : set) {
        	Well clone = new Well(well);
            result.put(clone,  well(well, weights, begin, length));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated set statistics ---------------------  */  
        
    /**
     * Returns the aggregated weighted statistic for the well set.
     * @param    WellSet    the well set
     * @param    double[]          weights for the data set
     * @return                     the aggregated results
     */
    public double setsAggregated(WellSet set, double[] weights) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");
    	Preconditions.checkNotNull(weights, "Weights array cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : set) {

        	List<Double> input = well.toDouble();
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i) * weights[i]);
        	}
        	
        }
      
        return calculate(aggregated);
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each well set.
     * @param    Collection<WellSetInteger>    collection of well sets
     * @param    double[]                      weights for the data set
     * @return                                 map of well sets and aggregated results
     */
    public Map<WellSet, Double> setsAggregated(
            Collection<WellSet> collection, double[] weights) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSet, Double> results = new TreeMap<WellSet, Double>();
        
        for(WellSet set : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {

            	List<Double> input = well.toDouble();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each well set.
     * @param    WellSetInteger[]    array of well sets
     * @param    double[]            weights for the data set
     * @return                       map of well sets and aggregated results
     */
    public Map<WellSet, Double> setsAggregated(
            WellSet[] array, double[] weights) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSet, Double> results = new TreeMap<WellSet, Double>();
        
        for(WellSet set : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {

            	List<Double> input = well.toDouble();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for the well set using the values 
     * between the indices.
     * @param    WellSet    the well set
     * @param    double[]          weights for the data set
     * @param    int               beginning index of subset
     * @param    int               length of subset
     * @return                     the aggregated result
     */
    public double setsAggregated(WellSet set, double[] weights, int begin, int length) {
    	
        Preconditions.checkNotNull(set, "The well set cannot be null.");
        
        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : set) {

        	List<Double> input = well.toDouble().subList(begin, begin + length);
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i) * weights[i]);
        	}
        	
        }
      
        return calculate(aggregated);
        
    }

    /**
     * Returns the aggregated weighted statistic for each well set using the values
     * between the indices.
     * @param    Collection<WellSetInteger>    collection of well sets
     * @param    double[]                      weights for the data set
     * @param    int                           beginning index of subset
     * @param    int                           length of subset
     * @return                                 map of well sets and aggregated results
     */
    public Map<WellSet, Double> setsAggregated(
            Collection<WellSet> collection, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSet, Double> results = new TreeMap<WellSet, Double>();
        
        for(WellSet set : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {

            	List<Double> input = well.toDouble().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each well set using the values
     * between the indices.
     * @param    WellSetInteger[]    array of well sets
     * @param    double[]            weights for the data set
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @return                       map of well sets and aggregated results
     */
    public Map<WellSet, Double> setsAggregated(
            WellSet[] array, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSet, Double> results = new TreeMap<WellSet, Double>();
        
        for(WellSet set : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {

            	List<Double> input = well.toDouble().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i) * weights[i]);
            	}
            	
            }
      
            results.put(clone, calculate(aggregated));
        
        }
        
        return results;
        
    }
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Returns the weighted well statistic.
     * @param    Well    the well
     * @param    double[]       weights for the data set
     * @return                  the result
     */
    public double well(Well well, double[] weights) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkNotNull(weights, "The weights array cannot be null.");
        return calculate(well.toDouble(), weights);  
    }
    
    /**
     * Returns the well weighted statistic for the values between the beginning and 
     * ending indices.
     * @param    Well    the well
     * @param    double[]       weights for the data set
     * @param    int            beginning index of subset
     * @param    int            length of the subset
     * @return                  the result
     */
    public double well(Well well, double[] weights, int begin, int length) {
        
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkNotNull(weights, "The weights array cannot be null.");
        Preconditions.checkArgument(begin <= well.toDouble().size() &&
                                    begin >= 0 &&
                                    begin + length <= well.toDouble().size());
        
        return calculate(well.toDouble(), weights, begin, length);
    }

    /* -------- Methods for calculating the statistic of a data set --------  */

    /**
     * Calculates the weighted statistic.
     * @param    List<Double>        the list
     * @param    double[]        weights for the data set
     * @return                   the result
     */
    public abstract double calculate(List<Double> list, double[] weights);

    /**
     * Calculates the weighted statistic of the values between the beginning and 
     * ending indices.
     * @param    List<Double>        the list
     * @param    double[]        weights of the data set
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @return                   the result
     */
    public abstract double calculate(List<Double> list, double[] weights, int begin, int length);
}
