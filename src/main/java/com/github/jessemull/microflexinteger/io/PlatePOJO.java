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
import java.util.List;

import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;

/**
 * This is a wrapper class for importing or exporting a JSON encoded plate object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlatePOJO {
    
	/*---------------------------- Private fields ----------------------------*/

    private String type;                                   // Data set numerical type
    private String label;                                  // Plate label
    private String descriptor;                             // Plate descriptor
    private int rows;                                      // Number of plate rows
    private int columns;                                   // Number of plate columns 
    private int size;                                      // Number of plate wells   
    private List<SimpleWellSetPOJO> wellsets = 
    		new ArrayList<SimpleWellSetPOJO>();    // Plate well sets
    private List<SimpleWellPOJO> wells = 
    		new ArrayList<SimpleWellPOJO>();       // Plate wells
    
    /*------------------------------ Constructors ----------------------------*/
    
    public PlatePOJO(){}
    
    /**
     * Creates a plate POJO from a plate object.
     * @param    Plate    the plate
     */
    public PlatePOJO(Plate plate) {
        
        this.type = "Integer";
        this.label = plate.label();
        this.descriptor = plate.descriptor();
        this.rows = plate.rows();
        this.columns = plate.columns();
        this.size = plate.size();
        
        for(WellSet set : plate.allGroups()) {
            wellsets.add(new SimpleWellSetPOJO(set));
        }
        
        for(Well well : plate) {
            wells.add(new SimpleWellPOJO(well));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the data type.
     * @param    String    the data set numerical type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the stack label.
     * @param    String    the stack label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the row number.
     * @param   int    number of rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * Sets the column number.
     * @param   int    number of columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the size.
     * @param   int    number of plates in the stack
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the plate descriptor.
     * @param   String    the plate descriptor
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    
    /**
     * Sets the list of well sets.
     * @param    List<SimpleWellSetPOJOInteger>    list of plate well sets
     */
    public void setWellsets(List<SimpleWellSetPOJO> wellsets) {
    	this.wellsets = wellsets;
    }
    
    /**
     * Sets the list of wells.
     * @param    List<SimpleWellPOJOInteger>    list of plate wells
     */
    public void setWells(List<SimpleWellPOJO> wells) {
    	this.wells = wells;
    }
    
    /**
     * Returns the data type.
     * @return    the data set numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the stack label.
     * @return    the stack label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the row number.
     * @return    number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Returns the column number.
     * @return    number of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Returns the size.
     * @return    number of plates in the stack
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the descriptor.
     * @return    plate descriptor
     */
    public String getDescriptor() {
        return this.descriptor;
    }
    
    /**
     * Returns the list of well sets.
     * @return    returns the plate well sets
     */
    public List<SimpleWellSetPOJO> getWellsets() {
        return this.wellsets;
    }
    
    /**
     * Returns the list of wells.
     * @return    list of plate wells
     */
    public List<SimpleWellPOJO> getWells() {
        return this.wells;
    }
    
    /**
     * Returns a PlateInteger object.
     * @return    the plate
     */
    public Plate toPlateObject() {

        Plate plate = new Plate(this.rows, this.columns, this.label);
        
        for(SimpleWellSetPOJO set : this.wellsets) {
        	plate.addGroups(set.toWellSetObject().wellList());
        }
        
        for(SimpleWellPOJO well : wells) {
        	plate.addWells(well.toWellObject());
        }
        
        return plate;
    }
}
