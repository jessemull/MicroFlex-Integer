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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflexinteger.plate.WellSet;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded well set objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="wellsets")
public class WellSetListXML implements Iterable<WellSetXML> {
	
	/*---------------------------- Private fields ----------------------------*/
	
	private List<WellSetXML> wellsets = new ArrayList<WellSetXML>();
	
	/*------------------------------ Constructors ----------------------------*/
	
	public WellSetListXML(){}
	
    /**
     * Creates an xml well set list from a well set object.
     * @param    WellSet    the well set object
     */
    public WellSetListXML(WellSet single) {
      this.wellsets.add(new WellSetXML(single));
    }
    
    /**
     * Creates an xml well set list from a collection of well set objects.
     * @param    Collection<WellSetInteger>    the collection of sets
     */
    public WellSetListXML(Collection<WellSet> collection) {
        for(WellSet set : collection) {
            this.wellsets.add(new WellSetXML(set));
        }
    }
    
    /**
     * Creates an XML well set list from an array of well set objects.
     * @param    WellSet    the well set object
     */
    public WellSetListXML(WellSet[] array) {
        for(WellSet set : array) {
            this.wellsets.add(new WellSetXML(set));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of XML well sets.
     * @param   List<WellSetXMLInteger>    the new list of XML well sets
     */
    @XmlElement(name="wellset")
    public void setWellsets(List<WellSetXML> wellsets) {
        this.wellsets = wellsets;
    }
      
    /**
     * Returns the list of XML sets.
     * @return    the list of well sets
     */
    public List<WellSetXML> getWellsets() {
    	return this.wellsets;
    }
    
    /**
     * Returns the XML well set at the indicated index.
     * @param    int    the index into the well list
     * @return          the well set POJO
     */
    public WellSetXML get(int index) {
        return this.wellsets.get(index);
    }
    
    /*------------------ Methods for iterating over the list -----------------*/
    
    /**
     * Returns an iterator for the list of well sets.
     * @return    the iterator
     */
    public Iterator<WellSetXML> iterator() {
        return this.wellsets.iterator();
    }
    
    /**
     * Returns the size of the well set list.
     * @return    the size of the list
     */
    public int size() {
        return this.wellsets.size();
    }
}
