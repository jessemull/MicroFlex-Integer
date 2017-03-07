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

/* ---------------------------------- Package ------------------------------- */

package com.github.jessemull.microflexinteger.plate;

/* ------------------------------- Dependencies ----------------------------- */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflexinteger.util.ValUtil;

/**
 * Scientists commonly refer to a collection of microplates as a stack. This 
 * class represents a stack of plates. Plates are stored internally in a 
 * list. All plates within a single stack must share the same plate dimensions.
 * Please refer to the PlateBigDecimal class for more information.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class Stack implements Iterable<Plate> {

    /* ---------------------------- Private fields -------------------------- */
    
    /* Plate storage */
    
    private TreeSet<Plate> stack;
    
    /* Plate stack label */
    
    private String label = "StackInteger";
    
    /* Plate stack dimensions */
    
    private int rows;
    private int columns;
    
    /* Descriptors */
    
    private String descriptor;
    private int type;
    private int dataType = PLATE_INTEGER;
    
    /* Flags for predefined plate dimension types */
    
    public static final int PLATE_6WELL = 0;
    public static final int PLATE_12WELL = 1;
    public static final int PLATE_24WELL = 2;
    public static final int PLATE_48WELL = 3;
    public static final int PLATE_96WELL = 4;
    public static final int PLATE_384WELL = 5;
    public static final int PLATE_1536WELL = 6;
    
    /* Flags for available data types */
    
    public static final int PLATE_DOUBLE = 0;
    public static final int PLATE_INTEGER = 1;
    public static final int PLATE_BIGDECIMAL = 2;
    public static final int PLATE_BIGINTEGER = 3;
    
    /* Row number for each predefined plate dimension type */
    
    public static final int ROWS_6WELL = 2;
    public static final int ROWS_12WELL = 3;
    public static final int ROWS_24WELL = 4;
    public static final int ROWS_48WELL = 6;
    public static final int ROWS_96WELL = 8;
    public static final int ROWS_384WELL = 16;
    public static final int ROWS_1536WELL = 32;
    
    public static final int COLUMNS_6WELL = 3;
    public static final int COLUMNS_12WELL = 4;
    public static final int COLUMNS_24WELL = 6;
    public static final int COLUMNS_48WELL = 8;
    public static final int COLUMNS_96WELL = 12;
    public static final int COLUMNS_384WELL = 24;
    public static final int COLUMNS_1536WELL = 48;
    
    /* ----------------------------- Constructors --------------------------- */
    
    /**
     * Creates a new plate stack.
     * @param    int    the plate type
     */
    public Stack(int type) {
        this.initializePlateType(type);
        this.stack = new TreeSet<Plate>();
    }
    
    /**
     * Creates a new empty plate stack.
     * @param    int    number of rows
     * @param    int    number of columns
     */
    public Stack(int rows, int columns) {
        this.initializePlateType(rows, columns);
        this.stack = new TreeSet<Plate>();
    }
    
    /**
     * Creates a new plate stack using the label.
     * @param    int       the plate type
     * @param    String    the plate stack label
     */
    public Stack(int type, String labelIn) {
        this.initializePlateType(type);
        this.stack = new TreeSet<Plate>();
        this.label = labelIn;
    }
    
    /**
     * Creates a new plate stack using the label.
     * @param    int       number of rows
     * @param    int       number of columns
     * @param    String    the plate stack label
     */
    public Stack(int rows, int columns, String labelIn) {
    	this.initializePlateType(rows, columns);
        this.stack = new TreeSet<Plate>();
        this.label = labelIn;
    }
    
    /**
     * Creates a new plate stack using the input plate.
     * @param    Plate    the input plate
     */
    public Stack(Plate plateIn) {
    	this.initializePlateType(plateIn.rows(), plateIn.columns());
    	ValUtil.validatePlateInteger(this.rows(), this.columns(), plateIn);
        this.stack = new TreeSet<Plate>();
        this.stack.add(plateIn);    
    }
    
    /**
     * Creates a new plate stack using the input plate and label.
     * @param    Plate     the input plate
     * @param    String    the plate stack label
     */
    public Stack(Plate plateIn, String labelIn) {
    	this.initializePlateType(plateIn.rows(), plateIn.columns());
    	ValUtil.validatePlateInteger(this.rows(), this.columns(), plateIn);
        this.stack = new TreeSet<Plate>();
        this.stack.add(plateIn);
        this.label = labelIn;
    }
    
    /**
     * Creates a new plate stack using the input collection of plates.
     * @param    Collection<PlateInteger>    list of input plates
     */
    public Stack(Collection<Plate> platesIn) {
        Plate[] array = platesIn.toArray(new Plate[platesIn.size()]);
        this.initializePlateType(array[0].rows(), array[0].columns());   
        ValUtil.validatePlateInteger(this.rows(), this.columns(), platesIn);
        this.stack = new TreeSet<Plate>();
        this.stack.addAll(platesIn);
    }
    
    /**
     * Creates a new plate stack using the input collection of plates and label.
     * @param    Collection<PlateInteger>    list of input plates
     */
    public Stack(Collection<Plate> platesIn, String labelIn) {
        Plate[] array = platesIn.toArray(new Plate[platesIn.size()]);
        this.initializePlateType(array[0].rows(), array[0].columns());  
        ValUtil.validatePlateInteger(this.rows(), this.columns(), platesIn);   
        this.stack = new TreeSet<Plate>();
        this.stack.addAll(platesIn);
        this.label = labelIn;
    }
    
    /**
     * Creates a new plate stack using the input plate array.
     * @param    PlateInteger[]    list of input plates
     */
    public Stack(Plate[] platesIn) {     
        this.initializePlateType(platesIn[0].rows(), platesIn[0].columns());      
        ValUtil.validatePlateInteger(this.rows(), this.columns(), platesIn);
        this.stack = new TreeSet<Plate>();;
        this.stack.addAll(Arrays.asList(platesIn));
    }
    
    /**
     * Creates a new plate stack using the input plate array and label.
     * @param    PlateInteger[]    list of input plates
     */
    public Stack(Plate[] platesIn, String labelIn) {       
        this.initializePlateType(platesIn[0].rows(), platesIn[0].columns()); 
        ValUtil.validatePlateInteger(this.rows(), this.columns(), platesIn);
        this.stack = new TreeSet<Plate>();
        this.stack.addAll(Arrays.asList(platesIn));
        this.label = labelIn;
    }
    
    /**
     * Creates a new plate stack from an existing plate stack.
     * @param    Stack    the input stack
     */
    public Stack(Stack stack) {              
    	this.initializePlateType(stack.rows(), stack.columns());
    	this.stack = new TreeSet<Plate>();
    	this.stack.addAll(stack.getAll());
    	this.label = stack.label();  	
    }

    /* ---------------------- Constructor Helper Methods ---------------------*/

    /**
     * Sets the number of rows, the number of columns, the size, the plate type 
     * and the descriptor.
     * @param    int    plate row number
     * @param    int    plate column number
     */
    private void initializePlateType(int rows, int columns) {
        
        this.rows = rows;
        this.columns = columns;
        
        if(rows == ROWS_6WELL && 
           columns == COLUMNS_6WELL) {
            
            this.type = PLATE_6WELL;
            this.descriptor = "6-Well";

        } else if(rows == ROWS_12WELL && 
                  columns == COLUMNS_12WELL) {
            
            this.type = PLATE_12WELL;
            this.descriptor = "12-Well";
        
        } else if(rows == ROWS_24WELL && 
                  columns == COLUMNS_24WELL) {
        
            this.type = PLATE_24WELL;
            this.descriptor = "24-Well";
        
        } else if(rows == ROWS_48WELL && 
                  columns == COLUMNS_48WELL) {
            
            this.type = PLATE_48WELL;
            this.descriptor = "48-Well";
        
        } else if(rows == ROWS_96WELL && 
                  columns == COLUMNS_96WELL) {
            
            this.type = PLATE_96WELL;
            this.descriptor = "96-Well";
        
        } else if(rows == ROWS_384WELL && 
                  columns == COLUMNS_384WELL) {
            
            this.type = PLATE_384WELL;
            this.descriptor = "384-Well";
        
        } else if(rows == ROWS_1536WELL && 
                  columns == COLUMNS_1536WELL) {
            
            this.type = PLATE_1536WELL;
            this.descriptor = "1536-Well";
        
        } else {
            
            this.type = -1;
            this.descriptor = "Custom Stack: " + rows + "x" + columns;
            
        }
    }
    
    /**
     * Sets the number of rows, the number of columns, the size, the plate type 
     * and the descriptor.
     * @param    int    the plate type
     */
    private void initializePlateType(int type) {
        
        switch(type) {
            case PLATE_6WELL:    this.initializePlateType(ROWS_6WELL, COLUMNS_6WELL);
                                 break;
            case PLATE_12WELL:   this.initializePlateType(ROWS_12WELL, COLUMNS_12WELL);
                                 break;
            case PLATE_24WELL:   this.initializePlateType(ROWS_24WELL, COLUMNS_24WELL);
                                 break;
            case PLATE_48WELL:   this.initializePlateType(ROWS_48WELL, COLUMNS_48WELL);
                                 break;
            case PLATE_96WELL:   this.initializePlateType(ROWS_96WELL, COLUMNS_96WELL);
                                 break;
            case PLATE_384WELL:  this.initializePlateType(ROWS_384WELL, COLUMNS_384WELL);
                                 break;
            case PLATE_1536WELL: this.initializePlateType(ROWS_1536WELL, COLUMNS_1536WELL);
                                 break;
            default:             throw new IllegalArgumentException("Invalid plate type: " + type + ".");
        }
        
    }
    
    /* ---------------------- Methods for adding plates --------------------- */
    
    /**
     * Adds a plate to the plate stack.
     * @param    Plate    the input plate
     * @return            true on successful addition
     */
    public boolean add(Plate plateIn) {
        try { 
            ValUtil.validatePlateInteger(this.rows(), this.columns(), plateIn);
            this.stack.add(plateIn);
            return true;
        } catch(Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    /**
     * Adds a collection of plates to the plate stack.
     * @param    Collection<PlateInteger>    the plate collection
     * @return                               true on successful addition
     */
    public boolean add(Collection<Plate> platesIn) {
        try {
            ValUtil.validatePlateInteger(this.rows(), this.columns(), platesIn);
            this.stack.addAll(platesIn);
            return true;
        } catch(Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    /**
     * Adds an array of plates to the plate stack.
     * @param    PlateInteger[]    the plate array
     * @return                     true on successful addition
     */
    public boolean add(Plate[] platesIn) {
        try { 
            ValUtil.validatePlateInteger(this.rows(), this.columns(), platesIn);
            this.stack.addAll(Arrays.asList(platesIn));
            return true;
        } catch(Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    /* --------------------- Methods for removing plates -------------------- */
    
    /**
     * Removes a plate from the plate stack.
     * @param    Plate    the input plate
     * @return            true on successful removal
     */
    public boolean remove(Plate plateIn) {

    	try {
            
            ValUtil.validatePlateInteger(this.rows, this.columns, plateIn);
            
            boolean remove = this.stack.remove(plateIn);
            if(!remove) {
            	throw new IllegalArgumentException("Failed to remove plate " + 
                        plateIn.toString() + ". This plate does not exist in the data set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Removes a collection of plates from the plate stack.
     * @param    Collection<PlateInteger>    the plate collection
     * @return                               true on successful removal
     */
    public  boolean remove(Collection<Plate> platesIn) {

    	boolean success = true;

        try {
            Preconditions.checkNotNull(platesIn, "The plate collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(Plate plate : platesIn) {
            try {
                ValUtil.validatePlateInteger(this.rows,  this.columns, plate);
                success = this.remove(plate) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
       
        return success;
    }
    
    /**
     * Removes an array of plates from the plate stack.
     * @param    PlateInteger[]    the plate array
     * @return                     true on successful removal
     */
    public  boolean remove(Plate[] platesIn) {

    	boolean success = true;
        
        try {
            Preconditions.checkNotNull(platesIn, "The plate array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(Plate plate : platesIn) {
            try {
                ValUtil.validatePlateInteger(this.rows,  this.columns, plate);
                success = this.remove(plate) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes the plate with the input label.
     * @param    String    the label
     * @return             true on successful removal
     */
    public boolean remove(String label) {
    	
    	boolean success = true;

        try {
            
        	for(Plate plate : this.stack) {
        		if(plate.label().equals(label)) {
        			success = this.stack.remove(plate);
        			break;
        		}
        	}

        } catch(Exception e) {
        	System.err.println(e.getMessage());
            return false;
        }
        
        return success;
    }
    
    /**
     * Removes all plates with a label found in the input list.
     * @param    List<String>    the labels
     * @return                   true on successful removal
     */
    public boolean remove(List<String> labels) {
    	
    	boolean success = true;
        
        Set<Plate> list = new TreeSet<Plate>();
        
        try {
            
        	for(Plate plate : this.stack) {
        		if(labels.contains(plate.label())) {
        			list.add(plate);
        		}
        	}

            success = this.remove(list);

        } catch(Exception e) {
        	System.err.println(e.getMessage());
            return false;
        }
        
        return success;
    }
    
    /**
     * Clears the stack.
     */
    public void clear() {
        this.stack.clear();
    }
    
    /*--------------------- Methods for replacing plates ---------------------*/
    
    /**
     * Replaces a plate from the plate stack.
     * @param    Plate    the input plate
     * @return            true on successful replacement
     */
    public boolean replace(Plate plateIn) {
    	
    	try {
            this.stack.remove(plateIn);
            this.add(plateIn);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Replaces a collection of plates from the plate stack.
     * @param    Collection<PlateInteger>    the plate collection
     * @return                               true on successful replacement
     */
    public  boolean replace(Collection<Plate> platesIn) {

    	boolean success = true;
        
        try {
            Preconditions.checkNotNull(platesIn, "The plate collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(Plate plate : platesIn) {
            try {
                this.remove(plate);
                this.add(plate);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }
    
    /**
     * Replaces an array of plates from the plate stack.
     * @param    PlateInteger[]    the plate array
     * @return                     true on successful replacement
     */
    public  boolean replace(Plate[] platesIn) {

    	boolean success = true;
        
        try {
            Preconditions.checkNotNull(platesIn, "The plate array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(Plate plate : platesIn) {
            try {
                this.remove(plate);
                this.add(plate);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }
    
    /*--------------------- Methods for retaining plates ---------------------*/
    
    /**
     * Retains a plate from the plate stack.
     * @param    Plate    the input plate
     * @return            true on successful retention
     */
    public boolean retain(Plate plateIn) {
    	
    	boolean success = true;
        
        List<Plate> list = new ArrayList<Plate>();
        list.add(plateIn);
        
        try {
            ValUtil.validatePlateInteger(this.rows, this.columns, plateIn);
            success = this.stack.retainAll(list);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return success;
    }
    
    /**
     * Retains a collection of plates from the plate stack.
     * @param    Collection<PlateInteger>    the plate collection
     * @return                               true on successful retention
     */
    public  boolean retain(Collection<Plate> platesIn) {

    	boolean success = true;
        
        try {
            Preconditions.checkNotNull(platesIn, "The plate collection cannot be null.");
            for(Plate plate : platesIn) {
                ValUtil.validatePlateInteger(this.rows,  this. columns, plate);
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.stack.retainAll(platesIn) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
               
        return success;
    }
    
    /**
     * Retains an array of plates from the plate stack.
     * @param    PlateInteger[]    the plate array
     * @return                     true on successful retention
     */
    public  boolean retain(Plate[] platesIn) {

        boolean success = true;
        
        try {
            Preconditions.checkNotNull(platesIn, "The plate array cannot be null.");
            for(Plate plate : platesIn) {
                ValUtil.validatePlateInteger(this.rows,  this. columns, plate);
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.stack.retainAll(Arrays.asList(platesIn)) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
               
        return success;
    }
    
    /**
     * Retains the plate with the input label.
     * @param    String    the label
     * @return             true on successful retention
     */
    public boolean retain(String label) {
    	
    	boolean success = true;

        try {
            
        	for(Plate plate : this.stack) {
        		if(plate.label().equals(label)) {
        			List<Plate> plateList = new ArrayList<Plate>();
        			plateList.add(plate);
        			success = this.stack.retainAll(plateList);
        			break;
        		}
        	}

        } catch(Exception e) {
        	System.err.println(e.getMessage());
            return false;
        }
        
        return success;
    }
    
    /**
     * Retains all plates with a label found in the input list.
     * @param    List<String>    the labels
     * @return                   true on successful retention
     */
    public boolean retain(List<String> labels) {
    	
    	boolean success = true;
        
        List<Plate> list = new ArrayList<Plate>();
        
        try {
            
        	for(Plate plate : this.stack) {
        		if(labels.contains(plate.label())) {
        			list.add(plate);
        		}
        	}
        	
            success = this.stack.retainAll(list);
            
        } catch(Exception e) {
        	System.err.println(e.getMessage());
            return false;
        }
        
        return success;
    }
    
    /*-------------------- Methods for retrieving plates ---------------------*/
    
    /**
     * Returns the plate with the input label or null if no such plate exists.
     * @param    String    the label
     * @return             the plate 
     */
    public Plate get(String label) {

        try {
            
        	for(Plate plate : this.stack) {
        		if(plate.label().equals(label)) {
        			return plate;
        		}
        	}

        } catch(Exception e) {
        	System.err.println(e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Returns all the plates with a label found in the input list or null if none
     * of the plates exist.
     * @param    List<String>         the labels
     * @return                        the plates
     */
    public Set<Plate> get(List<String> labels) {

        Set<Plate> plates = new TreeSet<Plate>();
        
        try {
            
        	for(Plate plate : this.stack) {
        		if(labels.contains(plate.label())) {
        			plates.add(plate);
        		}
        	}
        	
            return plates;
            
        } catch(Exception e) {
        	System.err.println(e.getMessage());
        }
        
        return null;
    }
   
    /**
     * Returns the plate or null if not such plate exists.
     * @param    Plate    the input plate
     * @result   PlateInteger    	  the output plate
     */
    public Plate get(Plate plateIn) {
    	
    	Preconditions.checkNotNull(plateIn, "The plate cannot be null.");
    	
    	for(Plate plate : this.stack) {
            if(plateIn.equals(plate)) {
            	return plate;
            }
        }
    	
    	return null;
    }
    
    /**
     * Returns the plates in the collection or null if none of the plates exist.
     * @param    Collection<PlateInteger>    the input plates
     * @result   Collection<PlateInteger>               the output plates
     */
    public Set<Plate> get(Collection<Plate> platesIn) {
    	
    	Preconditions.checkNotNull(platesIn, "The plate collection cannot be null.");
        
    	Set<Plate> result = new TreeSet<Plate>();
        
        for(Plate plate : platesIn) {
        
        	Plate toAdd = this.get(plate);
        	
        	if(toAdd != null) {
        		result.add(toAdd);
        	}	
        }
        
        return result.size() == 0 ? null : result;
    }
    
    /**
     * Returns plates in the array or null if none of the plates exist.
     * @param    PlateInteger[]    the input plate
     * @result   PlateInteger            the output plate
     */
    public Set<Plate> get(Plate[] platesIn) {
    	
    	Preconditions.checkNotNull(platesIn, "The plate collection cannot be null.");
        
    	Set<Plate> result = new TreeSet<Plate>();
        
        for(Plate plate : platesIn) {
        
        	Plate toAdd = this.get(plate);
        	
        	if(toAdd != null) {
        		result.add(toAdd);
        	}	
        }
        
        return result.size() != 0 ? result : null;
    }
    
    /**
     * Returns all the plates in the stack as a set.
     * @return    all plates in the stack
     */
    public Set<Plate> getAll() {
    	return this.stack;
    }
    
    /*-------------------- Methods for plate lookup -----------------------*/

    /**
     * Returns true if the plate exists in the stack.
     * @param    Plate    the input plate
     * @return                   true if the plate exists
     */
    public boolean contains(Plate plateIn) {
        return this.stack.contains(plateIn);
    }
    
    /**
     * Returns true if all the input plates are found within the stack.
     * @param    Collection<PlateInteger>    the input plates
     * @return                               true if all input plates 
     *                                       are found within the set
     */
    public boolean contains(Collection<Plate> platesIn) {

        if(platesIn == null) {
            return false;
        }

        for(Plate plate : platesIn) {
            if(!this.contains(plate)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if all the input plates are found within the stack.
     * @param    PlateInteger[]    the input plates
     * @return                     true if all input plates are found within the set
     */
    public boolean contains(Plate[] platesIn) {

        if(platesIn == null) {
            return false;
        }

        for(Plate plate : platesIn) {
            if(!this.contains(plate)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if a plate with the label exists in the stack.
     * @param    String    the label
     * @return             true if the plate exists in the stack 
     */
    public boolean contains(String label) {

        try {
            
        	for(Plate plate : this.stack) {
        		if(plate.label().equals(label)) {
        			return true;
        		}
        	}

        	return true;
        	
        } catch(Exception e) {
        	System.err.println(e.getMessage());
        	return false;
        }

    }
    
    /**
     * Returns true if all the plates in the list exist in the stack.
     * @param    List<String>    the labels
     * @return                   true if all the plates in the list exist
     */
    public boolean contains(List<String> labels) {

        try {
            
        	for(String label : labels) {
        		if(!this.contains(label)) {
        			return false;
        		}
        	}
        	
            return true;
            
        } catch(Exception e) {
        	System.err.println(e.getMessage());
        	return false;
        }

    }
    
    /* ---------------- Plate dimensions and data type methods -------------- */
    
    /**
     * Returns the row number.
     * @return    number of rows
     */
    public int rows() {
    	return this.rows;
    }
    
    /**
     * Returns the column number.
     * @return    number of columns
     */
    public int columns() {
    	return this.columns;
    }

    /**
     * Returns the plate type.
     * @return    the plate type
     */
    public int type() {
        return this.type;
    }
    
    /**
     * Returns the descriptor.
     * @return    descriptor
     */
    public String descriptor() {
        return this.descriptor;
    }
    
    /**
     * Returns the data type as an integer value.
     * @return    the data type
     */
    public int dataType() {
        return this.dataType;
    }
    
    /**
     * Returns the data type as a string.
     * @return    the data type
     */
    public String dataTypeString() {
        return "Integer";
    }
    
    /**
     * Sets the label for the plate stack.
     * @param    String    the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Returns the label for the plate stack.
     * @return    the label
     */
    public String label() {
        return this.label;
    }

    /**
     * Returns the stack descriptor and label.
     * @return    stack descriptor and label
     */
    public String toString() {
        return "Type: " + this.descriptor + " Label: " + this.label;
    }
    
    /*---------------------------- TreeSet methods ---------------------------*/
    
    /**
     * Returns the number of plates in the stack.
     * @return    number of plates in the stack
     */
    public int size() {
        return this.stack.size();
    }
    
    /**
     * Returns an array of the plates in the stack.
     * @return    array containing all the plates in the stack
     */
    public Plate[] toArray() {
        return this.stack.toArray(new Plate[stack.size()]);
    }
    
    /**
     * Returns the least plate in this set strictly greater than the given 
     * plate, or null if there is no such plate.
     * @param    Plate    input plate
     * @return                   greatest plate less than or equal to the input plate
     */
    public Plate higher(Plate plate) {
        return this.stack.higher(plate);
    }
    
    /**
     * Returns the greatest plate in this set strictly less than the given plate, 
     * or null if there is no such plate.
     * @param     Plate    input plate
     * @return                    least plate greater than or equal to the input plate
     */
    public Plate lower(Plate plate) {
        return this.stack.lower(plate);
    }
    
    /**
     * Retrieves and removes the first (lowest) plate, or returns null if this 
     * set is empty.
     * @return    first/lowest plate
     */
    public Plate pollFirst() {
        return this.stack.pollFirst();
    }
    
    /**
     * Retrieves and removes the last (highest) plate, or returns null if this 
     * set is empty.
     * @return    last/highest plate
     */
    public Plate pollLast() {
        return this.stack.pollLast();
    }  
    
    /**
     * Returns an iterator that iterates over the plates in the stack.
     * @return    iterator for plates
     */
    public Iterator<Plate> iterator() {
        return this.stack.iterator();
    }
    
    /**
     * Returns an iterator over the plates in this set in descending order.
     * @return    iterator for traversing set in descending order
     */
    public Iterator<Plate> descendingIterator() {
        return this.stack.descendingIterator();
    }
    
    /**
     * Returns a reverse order view of the plates contained in this set.
     * @return    the set in descending order
     */
    public Set<Plate> descendingSet() {
        return this.stack.descendingSet();
    }
    
    /**
     * Returns the first plate in the set.
     * @return    the first plate in the set
     */
    public Plate first() {
        return this.stack.first();
    }
    
    /**
     * Returns the last plate in the set.
     * @return    the first plate in the set
     */
    public Plate last() {
        return this.stack.last();
    }
    
    /**
     * Returns the least plate in this set greater than or equal to the given 
     * plate, or null if there is no such plate.
     * @param    Plate    the plate
     * @return                   least plate greater than or equal to the input plate
     */
    public Plate ceiling(Plate plate) {
        return this.stack.ceiling(plate);
    }
    
    /**
     * Returns the greatest plate in this set less than or equal to the given 
     * plate, or null if there is no such plate.
     * @return    greatest plate less than or equal to the input plate
     */
    public Plate floor(Plate plate) {
        return this.stack.floor(plate);
    }
    
    /**
     * Returns a view of the portion of this set whose plates are strictly 
     * less than the input plate.
     * @param    Plate    input plate
     * @return                   plate set with plates less than the input plate
     */
    public Set<Plate> headSet(Plate plate) {
        return this.stack.headSet(plate);
    }
    
    /**
     * Returns a view of the portion of this set whose plates are less than 
     * (or equal to, if inclusive is true) to the input plate.
     * @param    Plate plate    input   plate 
     * @param    boolean    a true value retains the input plate in the sub set
     * @return              plate set with plates less than the input plate
     */
    public Set<Plate> headSet(Plate plate, boolean inclusive) {
        return this.stack.headSet(plate, inclusive);
    }
    
    /**
     * Returns a view of the portion of this stack whose plates are greater than 
     * or equal to the input plate.
     * @param     Plate    the input plate
     * @return                    set with plates greater than or equal to the input plate
     */
    public Set<Plate> tailSet(Plate plate) {
        return this.stack.tailSet(plate);
    }
    
    /**
     * Returns a view of the portion of this stack whose plates are greater than 
     * (or equal to, if inclusive is true) the input plate.
     * @param     Plate    the input plate
     * @return                    set with plates greater than or equal to the input plate
     */
    public Set<Plate> tailSet(Plate plate, boolean inclusive) {
        return this.stack.tailSet(plate, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose plates range from 
     * the first input plate to the second input plate.
     * @param    Plate    the first input plate
     * @param    Plate    the second input plate
     * @param    boolean         includes the first input plate in the returned set
     * @param    boolean         includes the second input plate in the returned set
     * @return                   plates in range
     */
    public Set<Plate> subSet(Plate plate1, boolean inclusive1, Plate plate2, boolean inclusive2) {
        return this.stack.subSet(plate1, inclusive1, plate2, inclusive2);
    }
    
    /**
     * Returns a view of the portion of this set whose plates range from 
     * the first input plate inclusive to the second input plate inclusive.
     * @param    Plate    the first input plate
     * @param    Plate    the second input plate
     * @return                   plates in range
     */
    public Set<Plate> subSet(Plate plate1, Plate plate2) {
        return this.stack.subSet(plate1, plate2);
    }

    /**
     * Returns true if the plate is empty.
     * @return    true if plate has no plates
     */
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }
    
    /** 
     * Plate stacks are equivalent if they contain the same plates. 
     * @param    Object    the object
     * @return             true if equal, false otherwise
     */
    public boolean equals(Object object) {
        
        if (object instanceof Stack == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }

		Stack stackIn = (Stack) object;
        
        if(this.stack.size() != stackIn.size()
           || this.rows != stackIn.rows
           || this.columns != stackIn.columns
           || !this.label.equals(stackIn.label())) {
        	return false;
        }
        
        Iterator<Plate> input = stackIn.iterator();
        Iterator<Plate> compareTo = this.stack.iterator();
        
        while(input.hasNext() && compareTo.hasNext()) {
        	if(!input.next().equals(compareTo.next())) {
        		return false;
        	}
        }
        
        return true;
    }
    
    /**
     * Hash code using the rows, columns, data type, label and stack fields.
     * @return    the hash code
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                   append(this.rows).
                   append(this.columns).
                   append(this.label).
                   append(this.stack).
                   toHashCode();    
    }
}
