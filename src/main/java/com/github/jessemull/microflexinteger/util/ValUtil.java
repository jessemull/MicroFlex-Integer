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

/* -------------------------- Package Declaration --------------------------- */

package com.github.jessemull.microflexinteger.util;

/* ------------------------------ Dependencies ------------------------------ */

import java.util.Collection;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;
import com.github.jessemull.microflexinteger.plate.WellIndex;
import com.github.jessemull.microflexinteger.plate.WellList;

/**
 * This class validates well, well sets, plates and stacks by checking for null
 * values and wells outside a range of indices.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ValUtil {
    
    /*------ Methods for validating integer wells, well sets and plates ------*/
    
    /**
     * Validates the set by checking for wells outside the valid range.
     * @param    int               the number of plate rows
     * @param    int               the number of plate columns
     * @param    WellSet    the well set to validate
     */
    public static void validateSet(int rows, int columns, WellSet set) {

        if(set == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(Well well : set) {

            if(well.row() > rows || well.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + well.toString() 
                                                   + " in well set: " 
                                                   + set.toString());
            }
        }
    }
    
    /**
     * Validates the well by checking for invalid indices.
     * @param    int     the number of plate rows
     * @Param    int columns    the number of plate columns
     * @param    Well    the well to validate
     */
    public static void validateWell(int rows, int columns, Well well) {
        
        if(well == null) {
            throw new NullPointerException("The well cannot be null.");
        }
        
        if(well.row() > rows || well.column() > columns) {
            throw new IllegalArgumentException("Invalid well indices for well: " 
                                               + well.toString()); 
        }
    }

    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    Plate    the first plate
     * @param    Plate    the second plate
     */
    public static void validatePlateInteger(Plate plate1, Plate plate2) {
        Preconditions.checkNotNull(plate1, "Plates cannot be null.");
        Preconditions.checkNotNull(plate2, "Plates cannot be null.");
        Preconditions.checkArgument(plate1.rows() == plate2.rows() || 
                                    plate1.columns() == plate2.columns(), 
                                    "Unequal plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    Plate                the plate
     * @param    Collection<PlateInteger>    the plate collection
     */
    public static void validatePlateInteger(Plate plate1, Collection<Plate> collection) {
        for(Plate plate2 : collection) {
            validatePlateInteger(plate1, plate2);
        }
    }
    
    /**
     * Validate each plate by checking for null values and invalid dimensions.
     * @param    PlateInteger[]    the plate array
     */
    public static void validatePlateInteger(Plate plate1, Plate[] array) {
        for(Plate plate2 : array) {
            validatePlateInteger(plate1, plate2);
        }
    }
    
    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    int             the number of rows
     * @param    int             the number of columns
     * @param    Plate    the second plate
     */
    public static void validatePlateInteger(int rows, int columns, Plate plate) {
        Preconditions.checkNotNull(plate, "Plate cannot be null.");
        Preconditions.checkArgument(plate.rows() == rows && 
                                    plate.columns() == columns, 
                                    "Invalid plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    int                         the number of rows
     * @param    int                         the number of columns
     * @param    Collection<PlateInteger>    the plate collection
     */
    public static void validatePlateInteger(int row, int column, Collection<Plate> collection) {
        for(Plate plate : collection) {
            validatePlateInteger(row, column, plate);
        }
    }
    
    /**
     * Validates each plate by checking for null values and invalid dimensions.
     * @param    int               the number of rows
     * @param    int               the number of columns
     * @param    PlateInteger[]    the plate array
     */
    public static void validatePlateInteger(int row, int column, Plate[] array) {
        for(Plate plate : array) {
            validatePlateInteger(row, column, plate);
        }
    }
    
    /**
     * Validates the group by checking for wells outside the valid range.
     * @param    int         the number of plate rows
     * @param    int         the number of plate columns
     * @param    WellList    the group
     */
    public static void validateGroup(int rows, int columns, WellList list) {
        
        if(list == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(WellIndex index : list) {
            if(index.row() > rows || index.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + index.toString() 
                                                   + " in well group: " 
                                                   + list.toString());
            }
        }
    }
}
