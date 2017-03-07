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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflexinteger.plate.Stack;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded stack objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="stacks")
public class StackListXML implements Iterable<StackXML> {
    
	/*---------------------------- Private fields ----------------------------*/
    
    private List<StackXML> stacks = new ArrayList<StackXML>();    // The stacks in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public StackListXML(){}
    
    /**
     * Creates an XML stack list from a stack object.
     * @param    Stack    the stack object
     */
    public StackListXML(Stack stack) {
        this.stacks.add(new StackXML(stack));
    }
    
    /**
     * Creates an XML stack list from a collection of stack objects.
     * @param    Collection<StackInteger>    collection of stack objects
     */
    public StackListXML(Collection<Stack> collection) {
        for(Stack stack : collection) {
            this.stacks.add(new StackXML(stack));
        }
    }
    
    /**
     * Creates an XML stack list from an array of stack objects.
     * @param    StackInteger[]    array of stack objects
     */
    public StackListXML(Stack[] array) {
        for(Stack stack : array) {
            this.stacks.add(new StackXML(stack));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of XML stacks.
     * @param    List<StackXMLInteger>    the new list of stacks
     */
    @XmlElement(name="stack")
    public void setStacks(List<StackXML> stacks) {
        this.stacks = stacks;
    }
    
    /**
     * Gets the list of XML stacks.
     * @return    the list of stacks
     */
    public List<StackXML> getStacks() {
        return this.stacks;
    }
    
    /**
     * Returns the stack at the indicated index.
     * @param    int    the index into the stack list
     * @return          the XML stack
     */
    public StackXML get(int index) {
        return this.stacks.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of stacks.
     * @return    the iterator
     */
    public Iterator<StackXML> iterator() {
        return this.stacks.iterator();
    }
    
    /**
     * Returns the size of the stack list.
     * @return    the size of the list
     */
    public int size() {
        return this.stacks.size();
    }
}
