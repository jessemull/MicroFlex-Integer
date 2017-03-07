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

import com.github.jessemull.microflexinteger.plate.Well;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded well objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name = "wells")
public class WellListXML implements Iterable<WellXML> {
    
	/*---------------------------- Private fields ----------------------------*/
    
    private List<WellXML> wells = new ArrayList<WellXML>();    // The wells in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public WellListXML(){}
    
    /**
     * Creates an XML well list from a well object.
     * @param    Well    the well object
     */
    public WellListXML(Well well) {
        this.wells.add(new WellXML(well));
    }
    
    /**
     * Creates an XML well list from a collection of well objects.
     * @param    Collection<WellInteger>    collection of well objects
     */
    public WellListXML(Collection<Well> collection) {
        for(Well well : collection) {
            this.wells.add(new WellXML(well));
        }
    }
    
    /**
     * Creates an XML well list from an array of well objects.
     * @param    WellInteger[]    array of well objects
     */
    public WellListXML(Well[] array) {
        for(Well well : array) {
            this.wells.add(new WellXML(well));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of XML wells.
     * @param    List<WellXMLInteger>    the new list of wells
     */
    @XmlElement(name = "well")
    public void setWells(List<WellXML> wells) {
        this.wells = wells;
    }
    
    /**
     * Gets the list of XML wells.
     * @return    the list of wells
     */
    public List<WellXML> getWells() {
        return this.wells;
    }

    /**
     * Returns the XML well at the indicated index.
     * @param    int    the index into the well list
     * @return          the XML well
     */
    public WellXML get(int index) {
        return this.wells.get(index);
    }
    
    /* Methods for iterating over the list */
    
    /**
     * Returns an iterator for the list of XML wells.
     * @return    the iterator
     */
    public Iterator<WellXML> iterator() {
        return this.wells.iterator();
    }
    
    /**
     * Returns the size of the XML well list.
     * @return    the size of the list
     */
    public int size() {
        return this.wells.size();
    }

}
