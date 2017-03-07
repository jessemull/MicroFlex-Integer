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

/* -------------------------------- Package -------------------------------- */

package com.github.jessemull.microflexinteger.stat;

/* ----------------------------- Dependencies ------------------------------ */

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
 * This class calculates descriptive statistics with calculate functions that take
 * list and integer inputs and return a list of lists.
 * 
 * Statistical operations can be performed on stacks, plates, sets and wells using
 * standard or aggregated functions. Standard functions calculate the desired
 * statistic for each well in the stack, plate or set. Aggregated functions aggregate
 * the values from all the wells in the stack, plate or set and perform the statistical
 * operation on the aggregated values. Both standard and aggregated functions can
 * be performed on a subset of data within the stack, plate, set or well.
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
 *                <td>Accepts a single well, set, plate or stack as input</td>
 *             </tr>
 *             <tr>
 *                <td>Calculates the statistic for each well in a well, set, plate or stack</td>
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
 *                <td>Accepts a single well/set/plate/stack or a collection/array of wells/sets/plates/stacks as input</td>
 *             </tr>
 *              <tr>
 *                <td>Aggregates the data from all the wells in a well/set/plate/stack and calculates the statistic using the aggregated data</td>
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
public abstract class QuantileStatisticList {
    
    /* --------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Returns the statistic for each plate well.
     * @param    Plate    the plate
     * @param    int             the integer value
     * @return                   map of wells and results
     */
    public Map<Well, List<List<Double>>> plate(Plate plate, int p) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<Well, List<List<Double>>> result = new TreeMap<Well, List<List<Double>>>();
        
        for (Well well : plate) {
            Well clone = new Well(well);
            result.put(clone, well(well, p));
        }
      
        return result;
        
    }
    
    /**
     * Returns the statistic for each plate well using the values between the 
     * beginning and ending indices.
     * @param    Plate    the plate
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @param    int             the integer value
     * @return                   map of wells and results   
     */
    public Map<Well, List<List<Double>>> plate(Plate plate, int begin, int length, int p) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<Well, List<List<Double>>> result = new TreeMap<Well, List<List<Double>>>();
        
        for (Well well : plate) {
            Well clone = new Well(well);
            result.put(clone, well(well, begin, length, p));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated plate statistics -------------------  */
    
    /**
     * Returns the aggregated statistic for the plate.
     * @param    Plate    the plate
     * @param    int             the integer value
     * @return                   the aggregated result
     */
    public List<List<Double>> platesAggregated(Plate plate, int p) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : plate) {
            aggregated.addAll(well.toDouble());
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each plate.
     * @param    Collection<PlateInteger>    collection of plates
     * @param    int                         the integer value
     * @return                               map of plates and aggregated results
     */
    public Map<Plate, List<List<Double>>> platesAggregated(Collection<Plate> collection, int p) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");

        Map<Plate, List<List<Double>>> results = new TreeMap<Plate, List<List<Double>>>();
        
        for(Plate plate : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {
                aggregated.addAll(well.toDouble());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated statistic for each plate.
     * @param    PlateInteger[]    array of plates
     * @param    int               the integer value
     * @return                     map of plates and aggregated result
     */
    public Map<Plate, List<List<Double>>> platesAggregated(Plate[] array, int p) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");

        Map<Plate, List<List<Double>>> results = new TreeMap<Plate, List<List<Double>>>();
        
        for(Plate plate : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {
                aggregated.addAll(well.toDouble());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    Plate    the plate
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @param    int             the integer value
     * @return                   the aggregated result
     */
    public List<List<Double>> platesAggregated(
            Plate plate, int begin, int length, int p) {

        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : plate) {
            aggregated.addAll(well.toDouble().subList(begin, begin + length));
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    Collection<PlateInteger>    collection of plates
     * @param    int                         beginning index of subset
     * @param    int                         length of subset
     * @param    int                         the integer value
     * @return                               map of plates and aggregated results
     */
    public Map<Plate, List<List<Double>>> platesAggregated(
            Collection<Plate> collection, int begin, int length, int p) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<Plate, List<List<Double>>> results = new TreeMap<Plate, List<List<Double>>>();
        
        for(Plate plate : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {
                aggregated.addAll(well.toDouble().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    PlateInteger[]    array of plates
     * @param    int               beginning index of subset
     * @param    int               length of subset
     * @param    int               the integer value
     * @return                     map of plates and aggregated results
     */
    public Map<Plate, List<List<Double>>> platesAggregated(
            Plate[] array, int begin, int length, int p) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<Plate, List<List<Double>>> results = new TreeMap<Plate, List<List<Double>>>();
        
        for(Plate plate : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            Plate clone = new Plate(plate);
            
            for (Well well : plate) {
                aggregated.addAll(well.toDouble().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Returns the statistic of each well in the well set.
     * @param    WellSet    the well set
     * @param    int               the integer value
     * @return                     map of wells and results
     */
    public Map<Well, List<List<Double>>> set(WellSet set, int p) {
    	
        Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<Well, List<List<Double>>> result = new TreeMap<Well, List<List<Double>>>();
        
        for (Well well : set) {
            Well clone = new Well(well);
            result.put(clone, well(well, p));
        }
      
        return result;
        
    }
    
    /**
     * Returns the statistic of each well in the well set using the values between 
     * the beginning and ending indices.
     * @param    WellSet    the well set
     * @param    int               beginning index of subset
     * @param    int               length of subset
     * @param    int               the integer value
     * @return                     map of wells and results
     */
    public Map<Well, List<List<Double>>> set(WellSet set, int begin, int length, int p) {
        
    	Preconditions.checkNotNull(set, "The well set cannot be null.");
    	
    	Map<Well, List<List<Double>>> result = new TreeMap<Well, List<List<Double>>>();
        
        for (Well well : set) {
        	Well clone = new Well(well);
            result.put(clone,  well(well, begin, length, p));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated set statistics ---------------------  */
    
    /**
     * Returns the aggregated statistic for the well set.
     * @param    WellSet    the well set
     * @param    int               the integer value
     * @return                     the aggregated result
     */
    public List<List<Double>> setsAggregated(WellSet set, int p) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
        	
        for (Well well : set) {
            aggregated.addAll(well.toDouble());
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each well set.
     * @param    Collection<WellSetInteger>    collection of well sets
     * @param    int                           the integer value
     * @return                                 map of well sets and aggregated results
     */
    public Map<WellSet, List<List<Double>>> setsAggregated(Collection<WellSet> collection, int p) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");

        Map<WellSet, List<List<Double>>> results = new TreeMap<WellSet, List<List<Double>>>();
        
        for(WellSet set : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {
                aggregated.addAll(well.toDouble());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated statistic for each well set.
     * @param    WellSetInteger[]    array of well sets
     * @param    int                 the integer value
     * @return                       map of well sets and aggregated results
     */
    public Map<WellSet, List<List<Double>>> setsAggregated(WellSet[] array, int p) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");

        Map<WellSet, List<List<Double>>> results = new TreeMap<WellSet, List<List<Double>>>();
        
        for(WellSet set : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {
                aggregated.addAll(well.toDouble());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated statistic for each well set using the values between the 
     * indices.
     * @param    WellSet    the well set
     * @param    int               beginning index of subset
     * @param    int               length of subset
     * @param    int               the integer value
     * @return                     the aggregated result
     */
    public List<List<Double>> setsAggregated(
            WellSet set, int begin, int length, int p) {

        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (Well well : set) {
            aggregated.addAll(well.toDouble().subList(begin, begin + length));
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each well set using the values between 
     * the indices.
     * @param    Collection<WellSetInteger>    collection of well sets
     * @param    int                           beginning index of subset
     * @param    int                           length of subset
     * @param    int                           the integer value
     * @return                                 map of well sets and aggregated results
     */
    public Map<WellSet, List<List<Double>>> setsAggregated(
            Collection<WellSet> collection, int begin, int length, int p) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSet, List<List<Double>>> results = new TreeMap<WellSet, List<List<Double>>>();
        
        for(WellSet set : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {
                aggregated.addAll(well.toDouble().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated statistic for each well set using the values between 
     * the indices.
     * @param    WellSetInteger[]    array of well sets
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @param    int                 the integer value
     * @return                       map of well sets and aggregated results
     */
    public Map<WellSet, List<List<Double>>> setsAggregated(
            WellSet[] array, int begin, int length, int p) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSet, List<List<Double>>> results = new TreeMap<WellSet, List<List<Double>>>();
        
        for(WellSet set : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSet clone = new WellSet(set);
            
            for (Well well : set) {
                aggregated.addAll(well.toDouble().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Returns the well statistic.
     * @param    Well    the well
     * @param    int            the integer value
     * @return                  the result
     */
    public List<List<Double>> well(Well well, int p) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        return calculate(well.toDouble(), p);     
    }
    
    /**
     * Returns the well statistic for the values between the beginning and ending 
     * indices.
     * @param    Well    the well
     * @param    int            beginning index of subset
     * @param    int            length of the subset
     * @param    int            the integer value
     * @return                  the result
     */
    public List<List<Double>> well(Well well, int begin, int length, int p) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkArgument(begin <= well.data().size() &&
                                    begin >= 0 &&
                                    begin + length <= well.data().size());   
        return calculate(well.toDouble(), begin, length, p);
        
    }

    /* ----------- Methods for calculating the data set statistic ----------  */
    
    /**
     * Calculates the statistic.
     * @param    List<Double>    the list
     * @param    int             the integer value
     * @return                   the result
     */
    public abstract List<List<Double>> calculate(List<Double> list, int p);
    
    /**
     * Calculates the statistic of the values between the beginning and ending 
     * indices.
     * @param    List<Double>    the list
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @param    int             the integer value
     * @return                   the result
     */
    public abstract List<List<Double>> calculate(List<Double> list, int begin, int length, int p);
}
