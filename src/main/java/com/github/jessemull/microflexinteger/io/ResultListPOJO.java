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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.jessemull.microflexinteger.plate.Well;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded results.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ResultListPOJO implements Iterable<ResultPOJO> {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private List<ResultPOJO> results = new ArrayList<ResultPOJO>();    // The results in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultListPOJO(){}
    
    /**
     * Creates a result list POJO from a well object.
     * @param    Map<WellInteger, Integer>    the input results
     */
    public ResultListPOJO(Map<Well, Integer> results) {
        this.results.add(new ResultPOJO(results, "Result"));
    }
    
    /**
     * Creates a result list POJO from a well object.
     * @param    Map<WellInteger, Integer>    the input results
     */
    public ResultListPOJO(Map<Well, Integer> results, String label) {
        this.results.add(new ResultPOJO(results, label));
    }
    
    /**
     * Creates a result list POJO from a collection of well objects.
     * @param    Collection<Map<WellInteger, Integer>>    collection of results
     */
    public ResultListPOJO(Collection<Map<Well, Integer>> collection) {
        for(Map<Well, Integer> result : collection) {
        	this.results.add(new ResultPOJO(result, "Result"));
        }
    }
    
    /**
     * Creates a result list POJO from a collection of well objects.
     * @param    Collection<Map<WellInteger, Integer>>    collection of results
     * @param    List<String>                             list of result labels
     */
    public ResultListPOJO(Collection<Map<Well, Integer>> collection, List<String> labels) {
    	int index = 0;
        for(Map<Well, Integer> result : collection) {
        	this.results.add(new ResultPOJO(result, labels.get(index++)));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of result POJOs.
     * @param    List<ResultPOJOInteger>    the new list of results
     */
    public void setResults(List<ResultPOJO> results) {
        this.results = results;
    }
    
    /**
     * Gets the list of result POJOs.
     * @return    the list of results
     */
    public List<ResultPOJO> getResults() {
        return this.results;
    }

    /**
     * Returns the result at the indicated index.
     * @param    int    the index into the well list
     * @return          the result at the index
     */
    public ResultPOJO get(int index) {
        return this.results.get(index);
    }
    
    /* Methods for iterating over the list */
    
    /**
     * Returns an iterator for the list of results.
     * @return    the iterator
     */
    public Iterator<ResultPOJO> iterator() {
        return this.results.iterator();
    }
    
    /**
     * Returns the size of the result list.
     * @return    the size of the list
     */
    public int size() {
        return this.results.size();
    }

}
