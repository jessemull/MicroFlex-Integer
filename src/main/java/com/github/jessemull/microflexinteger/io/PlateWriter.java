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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.jessemull.microflexinteger.plate.Plate;
import com.github.jessemull.microflexinteger.plate.Stack;
import com.github.jessemull.microflexinteger.plate.Well;
import com.github.jessemull.microflexinteger.plate.WellSet;

/**
 * Formats and writes stacks, plates, well sets and wells to an output stream or 
 * string. Supports the following formats:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Formats<div></th>
 *    <tr>
 *       <td>Plate Map</td>
 *    </tr>
 *    <tr>
 *       <td>Result Table</td>
 *    </tr>
 *    <tr>
 *       <td>JSON</td>
 *    </tr>
 *    <tr>
 *       <td>XML</td>
 *    </tr>
 * </table>
 *   
 * Result tables and maps are separated using a user defined delimiter or the
 * default tab delimiter.
 * 
 * <br><br>
 * 
 *  <div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result Plate Map:</p>
 *          <table style="text-align: left;" cellpadding="5px">
 *              <tr>
 *                  <td>Result</td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *              </tr>
 *              <tr>
 *                  <td></td>
 *                  <td>1</td>
 *                  <td>2</td>
 *                  <td>3</td>
 *                  <td>4</td>
 *                  <td>5</td>
 *                  <td>6</td>
 *              </tr>
 *              <tr>
 *                  <td>A</td>
 *                  <td>9114</td>
 *                  <td>2667</td>
 *                  <td>4214</td>
 *                  <td>5154</td>
 *                  <td>1293</td>
 *                  <td>7882</td>
 *              </tr>
 *              <tr>
 *                  <td>B</td>
 *                  <td>8953</td>
 *                  <td>4827</td>
 *                  <td>5149</td>
 *                  <td>9792</td>
 *                  <td>2514</td>
 *                  <td>6448</td>
 *              </tr>
 *              <tr>
 *                  <td>C</td>
 *                  <td>6328</td>
 *                  <td>4896</td>
 *                  <td>5130</td>
 *                  <td>9655</td>
 *                  <td>5120</td>
 *                  <td>1485</td>
 *              </tr>
 *              <tr>
 *                  <td>D</td>
 *                  <td>2661</td>
 *                  <td>5290</td>
 *                  <td>4057</td>
 *                  <td>2374</td>
 *                  <td>1200</td>
 *                  <td>2724</td>
 *              </tr>
 *          </table>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result Table:</p>
 *          <table style="text-align: left;" cellpadding="5px">
 *              <tr>
 *                  <td>Result</td>
 *                  <td></td>
 *              </tr>
 *              <tr>
 *                  <td>Index</td>
 *                  <td>Value</td>
 *              </tr>
 *              <tr>
 *                  <td>A1</td>
 *                  <td>4877</td>
 *              </tr>
 *              <tr>
 *                  <td>A2</td>
 *                  <td>5032</td>
 *              </tr>
 *              <tr>
 *                  <td>A3</td>
 *                  <td>6723</td>
 *              </tr>
 *              <tr>
 *                  <td>B1</td>
 *                  <td>4981</td>
 *              </tr>
 *          </table>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result JSON:</p>
 *          <pre>
 *  {
 *   "results" : [ {
 *     "type" : "Integer",
 *     "label" : "Example Result",
 *     "size" : 10,
 *     "wells" : {
 *       "A11" : 5645,
 *       "A2" : 6149,
 *       "A5" : 5846,
 *       "D10" : 5895,
 *       "D2" : 5452,
 *       "E4" : 5682,
 *       "E8" : 6116,
 *       "E9" : 4945,
 *       "G6" : 5874,
 *       "H1" : 4911
 *     }
 *   } ]
 *  }
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result XML:</p>
 *          <pre>
 *  &lt?xml version=&quot1.0&quot encoding=&quotUTF-8&quot standalone=&quotyes&quot?&gt
 *  &ltresults&gt
 *     &ltresult&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Result&lt/label&gt
 *         &ltsize&gt3&lt/size&gt
 *         &ltwells&gt
 *             &ltwell&gt
 *                 &ltindex&gtA2&lt/index&gt
 *                 &ltvalue&gt55873951592640616&lt/value&gt
 *             &lt/well&gt
 *             &ltwell&gt
 *                 &ltindex&gtA3&lt/index&gt
 *                 &ltvalue&gt5303191840271489&lt/value&gt
 *             &lt/well&gt
 *             &ltwell&gt
 *                 &ltindex&gtA4&lt/index&gt
 *                 &ltvalue&gt4902962476222037&lt/value&gt
 *             &lt/well&gt
 *         &lt/wells&gt
 *     &lt/result&gt
 *  &lt/results&gt
 *       </pre>
 *      </div>
 *  </div>
 *  <div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Well JSON:</p>
 *          <pre>
 *  {
 *    "wells" : [ {
 *      "type" : "Integer",
 *      "index" : "F8",
 *      "size" : 24,
 *      "values" : [ 1511, 2882, 5254 ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Well Set JSON:</p>
 *          <pre>
 *  {
 *    "wellsets" : [ {
 *      "type" : "Integer",
 *      "label" : "Example Well Set",
 *      "size" : 1,
 *      "wells" : [ {
 *        "index" : "A3",
 *        "values" : [ 4470, 9116, 5215 ]
 *      } ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Plate JSON:</p>
 *          <pre>
 *  {
 *    "plates" : [ {
 *      "type" : "Integer",
 *      "label" : "Example Plate",
 *      "descriptor" : "96-Well",
 *      "rows" : 8,
 *      "columns" : 12,
 *      "size" : 1,
 *      "wellsets" : [ {
 *        "label" : "Example Well Set",
 *        "size" : 3,
 *        "wells" : [ "C5", "D4", "F7" ]
 *      } ],
 *      "wells" : [ {
 *        "index" : "A7",
 *        "values" : [ 4451, 3453, 5592 ]
 *      } ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Stack JSON:</p>
 *          <pre>
 *  {
 *    "stacks" : [ {
 *      "type" : "Integer",
 *      "label" : "Example Stack",
 *      "rows" : 8,
 *      "columns" : 12,
 *      "size" : 1,
 *      "plates" : [ {
 *        "type" : "Integer",
 *        "label" : "Example Plate",
 *        "descriptor" : "96-Well",
 *        "rows" : 8,
 *        "columns" : 12,
 *        "size" : 1,
 *        "wellsets" : [ {
 *          "label" : "Example Well Set",
 *          "size" : 3,
 *          "wells" : [ "B6", "G1", "G12" ]
 *        } ],
 *        "wells" : [ {
 *          "index" : "A2",
 *          "values" : [ 3594, 2817, 2100 ]
 *        } ]
 *      } ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *  </div>
 *  <div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Well XML:</p>
 *          <pre>
 *  &ltwells&gt
 *     &ltwell&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltindex&gtH8&lt/index&gt
 *         &ltsize&gt24&lt/size&gt
 *         &ltvalues&gt
 *             &ltvalue&gt7870&lt/value&gt
 *             &ltvalue&gt7296&lt/value&gt
 *             &ltvalue&gt3898&lt/value&gt
 *         &lt/values&gt
 *     &lt/well&gt
 *  &lt/wells&gt
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Well Set XML:</p>
 *          <pre>
 *  &ltwellsets&gt
 *     &ltwellset&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Well Set&lt/label&gt
 *         &ltsize&gt1&lt/size&gt
 *         &ltwells&gt
 *             &ltwell&gt
 *                 &ltindex&gtA11&lt/index&gt
 *                 &ltvalues&gt
 *                     &ltvalue&gt1739&lt/value&gt
 *                     &ltvalue&gt5465&lt/value&gt
 *                     &ltvalue&gt5412&lt/value&gt
 *                 &lt/values&gt
 *             &lt/well&gt
 *         &lt/wells&gt
 *     &lt/wellset&gt
 *  &lt/wellsets&gt
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Plate XML:</p>
 *          <pre>
 *  &ltplates&gt
 *     &ltplate&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Plate&lt/label&gt
 *         &ltdescriptor&gt96-Well&lt/descriptor&gt
 *         &ltrows&gt8&lt/rows&gt
 *         &ltcolumns&gt12&lt/columns&gt
 *         &ltsize&gt1&lt/size&gt
 *         &ltwellsets&gt
 *             &ltwellset&gt
 *                 &ltlabel&gtExample Well Set&lt/label&gt
 *                 &ltsize&gt3&lt/size&gt
 *                 &ltwells&gt
 *                     &ltwell&gtB12&lt/well&gt
 *                     &ltwell&gtE2&lt/well&gt
 *                     &ltwell&gtH8&lt/well&gt
 *                 &lt/wells&gt
 *             &lt/wellset&gt
 *         &lt/wellsets&gt
 *         &ltwells&gt
 *             &ltwell&gt
 *                 &ltindex&gtA5&lt/index&gt
 *                 &ltvalues&gt
 *                     &ltvalue&gt1389&lt/value&gt
 *                     &ltvalue&gt9246&lt/value&gt
 *                     &ltvalue&gt1948&lt/value&gt
 *                 &lt/values&gt
 *             &lt/well&gt
 *         &lt/wells&gt
 *     &lt/plate&gt
 *  &lt/plates&gt
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Stack XML:</p>
 *          <pre>
 *  &ltstacks&gt
 *     &ltstack&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Stack&lt/label&gt
 *         &ltrows&gt8&lt/rows&gt
 *         &ltcolumns&gt12&lt/columns&gt
 *         &ltsize&gt1&lt/size&gt
 *         &ltplates&gt
 *             &ltplate&gt
 *                 &lttype&gtDouble&lt/type&gt
 *                 &ltlabel&gtExample Plate&lt/label&gt
 *                 &ltdescriptor&gt96-Well&lt/descriptor&gt
 *                 &ltrows&gt8&lt/rows&gt
 *                 &ltcolumns&gt12&lt/columns&gt
 *                 &ltsize&gt1&lt/size&gt
 *                 &ltwellsets&gt
 *                     &ltwellset&gt
 *                         &ltlabel&gtExample Well Set&lt/label&gt
 *                         &ltsize&gt3&lt/size&gt
 *                         &ltwells&gt
 *                             &ltwell&gtB7&lt/well&gt
 *                             &ltwell&gtH2&lt/well&gt
 *                             &ltwell&gtH3&lt/well&gt
 *                         &lt/wells&gt
 *                     &lt/wellset&gt
 *                 &lt/wellsets&gt
 *                 &ltwells&gt
 *                     &ltwell&gt
 *                         &ltindex&gtA2&lt/index&gt
 *                         &ltvalues&gt
 *                             &ltvalue&gt1303&lt/value&gt
 *                             &ltvalue&gt5321&lt/value&gt
 *                             &ltvalue&gt2808&lt/value&gt
 *                         &lt/values&gt
 *                     &lt/well&gt
 *                 &lt/wells&gt
 *             &lt/plate&gt
 *         &lt/plates&gt
 *     &lt/stack&gt
 *  &lt/stacks&gt
 *       </pre>
 *      </div>
 *  </div>
 *  
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlateWriter extends PrintWriter {
	
	/*---------------------------- Private Fields ----------------------------*/
	
	/* Number of character types available for the row ID */

	private int ALPHA_BASE = 26;
    
    /* The delimiter for delimiter separated values */
    
    private String delimiter = "\t";
    
    /*------------------------------ Constructors ----------------------------*/
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using 
     * the specified file.
     * @param    File    the output file
     * @throws   FileNotFoundException 
     */
    public PlateWriter(File file) throws FileNotFoundException {
    	super(file);
    }
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using 
     * the specified file and character set.
     * @param    File      the output file
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriter(File file, String csn) throws FileNotFoundException, 
                                                            UnsupportedEncodingException {
    	super(file, csn);
    }

    /**
     * Creates a new PlateWriterInteger, without automatic line flushing using
     * the OutputStream.
     * @param    OutputStream    the output stream
     */
    public PlateWriter(OutputStream out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterInteger with automatic line flushing, using the 
     * OutputStream.
     * @param    OutputStream    the output stream
     * @param    boolean         sets automatic flush when true
     */
    public PlateWriter(OutputStream out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using the
     * specified file name.
     * @param    String    the file name
     * @throws   FileNotFoundException 
     */
    public PlateWriter(String fileName) throws FileNotFoundException {
    	super(fileName);
    }
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using the
     * specified file name and character set.
     * @param    String    the file name
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriter(String fileName, String csn) throws FileNotFoundException, 
                                                                  UnsupportedEncodingException {
    	super(fileName, csn);
    }

    /**
     * Creates a new PlateWriterInteger without automatic line flushing using the
     * writer.
     * @param    Writer    the writer
     */
    public PlateWriter(Writer out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterInteger with automatic line flushing using the
     * writer.
     * @param    Writer     the writer
     * @param    boolean    sets auto flush when true
     */
    public PlateWriter(Writer out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<Well, Integer> data, int type) 
			throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
		
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
     */
	public String resultToPlateMapAsString(Map<Well, Integer> data, int type) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<Well, Integer>> data, 
    		int type) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<Well, Integer> map : data) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
    
	/**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<Well, Integer>> data, int type) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<Well, Integer> map : data) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
	
    
    
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<Well, Integer> data, int rows, int columns) 
			throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
	    
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
	 * @return                                   the plate map
     */
	public String resultToPlateMapAsString(Map<Well, Integer> data, 
			int rows, int columns) {
	    
	    TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
	    
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<Well, Integer>> data, int rows, int columns) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<Well, Integer> map : data) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
	
    /**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<Well, Integer>> data, 
    		int rows, int columns) {
        
    	String result = "";
    	
        for(Map<Well, Integer> map : data) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
     * @param    String                       the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<Well, Integer> data, int type, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
		
		this.printMapResult(sorted, rows, columns, label);
		
		this.flush();
	}
	
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
     * @param    String                       the data set label
	 * @return                                   the plate map
     */
	public String resultToPlateMapAsString(Map<Well, Integer> data, 
			int type, String label) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, label);
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
     * @param    List<String>                       list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<Well, Integer>> data, int type, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
     * @param    List<String>                       list of data set labels
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<Well, Integer>> data, 
    		int type, List<String> labels) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }

        return result;
    }
	
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<Well, Integer> data, int rows, int columns, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
	    
		this.printMapResult(sorted, rows, columns, label);	
		
		this.flush();
	}

	/**
     * Returns a string containing the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
	 * @return                                   the plate map
     */
	public String resultToPlateMapAsString(Map<Well, Integer> data, int rows, int columns, 
			String label) {
	    
	    TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
	    
	    return this.printMapResultAsString(sorted, rows, columns, label);	
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    List<String>                       list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<Well, Integer>> data, int rows, int columns, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
	
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    List<String>                       list of data set labels
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<Well, Integer>> data, 
    		int rows, int columns, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
     * @throws   FileNotFoundException 
     * @throws   UnsupportedEncodingException 
     */
	public void printMapResult(Map<Well, Integer> data, int rows, 
			int columns, String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		this.println(label);
		this.print(delimiter);
		
		for(int k = 0; k < columns; k++) {
			this.print((k + 1) + delimiter);
		}
		
		this.println();
		
		for(int i = 0; i < rows; i++) {
			
			this.print(this.rowString(i) + delimiter);
			
			for(int j = 1; j < columns + 1; j++) {

				Well well = new Well(i, j);
				
				if(data.containsKey(well)) {
					this.print(data.get(well) + delimiter);
				} else {
					this.print("Null" + delimiter);
				}
			}
			
			this.println();
		}
		
		this.println();
	}
    
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
     * @return                                the plate map
     */
	public String printMapResultAsString(Map<Well, Integer> data, 
			int rows, int columns, String label) {
		
		String result = label + "\n";
		result += this.delimiter;

		for(int k = 0; k < columns; k++) {
			result += (k + 1) + delimiter;
		}
		
		result += "\n";
		
		for(int i = 0; i < rows; i++) {
			
			result += this.rowString(i) + delimiter;
			
			for(int j = 1; j < columns + 1; j++) {

				Well well = new Well(i, j);
				
				if(data.containsKey(well)) {
					result += data.get(well) + delimiter;
				} else {
					result += "Null" + delimiter;
				}
			}
			
			result += "\n";;
		}

		return result;
	}
	
	/*----------------------- Methods for Table Output -----------------------*/
	
	/**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<Well, Integer> data) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
    	this.printTableResult(sorted, "Result");
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellInteger, Integer>    the data set
     * @return                                the table
     */
    public String resultToTableAsString(Map<Well, Integer> data) {
        TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
    	return this.printTableResultAsString(sorted, "Result");
    }
    
    /**
     * Prints each set of well value pairs as a delimiter separated table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<Well, Integer>> data) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<Well, Integer> map : data) {
            
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(map);
            this.printTableResult(sorted, "Result");
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing each set of well value pairs as a delimiter 
     * separated table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @return                                      the tables
     */
    public String resultToTableAsString(List<Map<Well, Integer>> data) {
        
    	String result = "";
    	
        for(Map<Well, Integer> map : data) {
            
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(map);
            result += this.printTableResultAsString(sorted, "Result");
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<Well, Integer> data, String label) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
        
    	this.printTableResult(sorted, label);
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @return                                the table 
     */
    public String resultToTableAsString(Map<Well, Integer> data, String label) {
        TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data);
    	return this.printTableResultAsString(sorted, label);
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    List<String>                       list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<Well, Integer>> data, List<String> labels) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            this.printTableResult(sorted, label);
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    List<String>                       list of data set labels
     * @return                                      the tables
     */
    public String resultToTableAsString(List<Map<Well, Integer>> data, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<Well, Integer> sorted = new TreeMap<Well, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            
            result += this.printTableResultAsString(sorted, label);
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void printTableResult(Map<Well, Integer> data, String label) throws FileNotFoundException, UnsupportedEncodingException {
    	    	
    	this.println(label);
    	
    	this.println("Index" + this.delimiter + "Value");
    	
    	for (Map.Entry<Well, Integer> entry : data.entrySet()) {
    	    this.println(entry.getKey().index() + this.delimiter + entry.getValue());
    	}
    	
    	this.println();
    }
    
    /**
     * Resturns a string holding the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @return                                the table string
     */
    public String printTableResultAsString(Map<Well, Integer> data, String label) {
    	    	
    	String result = label + "\n";
    	
    	result += "Index" + this.delimiter + "Value\n";
    	
    	for (Map.Entry<Well, Integer> entry : data.entrySet()) {
    	    result += entry.getKey().index() + this.delimiter + entry.getValue();
    	    result += "\n";
    	}
    	
    	result += "\n";
    	
    	return result;
    }
    
    /*------------------- Methods for Data Set JSON Output -------------------*/
                                                            
    /**
     * Prints the well value pairs in a JSON format.
     * @param    Map<WellInteger, Integer>    the data set
     * @throws   IOException
     */
    public void resultToJSON(Map<Well, Integer> data) throws IOException {  
          ObjectMapper mapper = new ObjectMapper();
          mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJO(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format.
     * @param    Map<WellInteger, Integer>    the data set
     * @return                                the JSON formatted result
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(Map<Well, Integer> data) throws JsonProcessingException {  
          ObjectMapper mapper = new ObjectMapper();
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJO(data));
    }
     
    /**
     * Prints the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @throws   IOException
     */
    public void resultToJSON(List<Map<Well, Integer>> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJO(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @return                                      the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<Well, Integer>> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJO(data));
    }
    
    /**
     * Prints the well value pairs in a JSON format using the label. 
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @throws   IOException
     */
    public void resultToJSON(Map<Well, Integer> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJO(data, label));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format using the label. 
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @return                                the JSON formatted result
     */
    public String resultToJSONAsString(Map<Well, Integer> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJO(data, label));
    }
    
    /**
     * Prints the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @param    List<String>                       the list of data set labels
     * @throws   IOException
     */
    public void resultToJSON(List<Map<Well, Integer>> data, List<String> labels) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJO(data, labels));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @param    List<String>                       the list of data set labels
     * @return                                      the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<Well, Integer>> data, List<String> labels) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJO(data, labels));
    }
    
    /*--------------------- Methods for Well JSON Output ---------------------*/
    
    /**
     * Prints the well values in a JSON format.
     * @param    Well    the well
     * @throws   IOException 
     */
    public void wellToJSON(Well well) throws IOException {   	
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJO(well));
    }
    
    /**
     * Returns a string containing the well values in a JSON format.
     * @param    Well    the well
     * @return                  the JSON formatted well
     * @throws  JsonProcessingException 
     */
    public String wellToJSONAsString(Well well) throws JsonProcessingException {   	
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJO(well));
    }
    
    /**
     * Prints the collection of wells in a JSON format.
     * @param    Collection<WellInteger>    the collection of wells
     * @throws   IOException 
     */
    public void wellToJSON(Collection<Well> collection) throws IOException {     
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJO(collection));
    }
    
    /**
     * Returns a string containing the collection of wells in a JSON format.
     * @param    Collection<WellInteger>    the collection of wells
     * @return                              the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(Collection<Well> collection) throws JsonProcessingException {     
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJO(collection));
    }
    
    /**
     * Prints the wells values in the array in a JSON format.
     * @param    WellInteger[]    the array of wells
     * @throws   IOException 
     */
    public void wellToJSON(Well[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJO(array));
    }
    
    /**
     * Returns a string containing the well values in the array in a JSON format.
     * @param    WellInteger[]    the array of wells
     * @return                    the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(Well[] array) throws   JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJO(array));
    }
    
    /*------------------- Methods for Well Set JSON Output -------------------*/
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSet    the well set
     * @throws   IOException 
     */
    public void setToJSON(WellSet set) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJO(set));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSet    the well set
     * @return                     the JSON formatted set
     * @throws   JsonProcessingException 
     */
    public String setToJSONAsString(WellSet set) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJO(set));
    }
    
    /**
     * Prints the collection of well sets in a JSON format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @throws   IOException 
     */
    public void setToJSON(Collection<WellSet> collection) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJO(collection));
    }
    
    /**
     * Returns a string containing the collection of well sets in a JSON format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @return                                 the JSON formatted set
     * @throws   JsonProcessingException 
     */ 
    public String setToJSONAsString(Collection<WellSet> collection) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJO(collection));
    }
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetInteger[]    the array of well sets
     * @throws   IOException 
     */
    public void setToJSON(WellSet[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJO(array));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetInteger[]    the array of well sets
     * @return                       the JSON formatted set
     * @throws   JsonProcessingException  
     */
    public String setToJSONAsString(WellSet[] array) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJO(array));
    }
    
    /*-------------------- Methods for Plate JSON Output ---------------------*/
    
    /**
     * Prints the plate values in a JSON format.
     * @param    Plate    the plate
     * @throws   IOException 
     */
    public void plateToJSON(Plate plate) throws IOException {    
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJO(plate));
    }
    
    /**
     * Returns a string containing the plate values in a JSON format.
     * @param    Plate    the plate
     * @return                   the JSON formatted plate
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Plate plate) throws JsonProcessingException {    
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJO(plate));
    } 
    
    /**
     * Prints the collection of plates in a JSON format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @throws   IOException 
     */
    public void plateToJSON(Collection<Plate> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJO(collection));
    }
    
    /**
     * Returns a string containing the collection of plates in a JSON format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @return                               the JSON formatted plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Collection<Plate> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJO(collection));
    }
     
    /**
     * Prints the array of plates in a JSON format.
     * @param    PlateInteger[]    the array of plates
     * @throws IOException 
     */
    public void plateToJSON(Plate[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJO(array));
    }
    
    /**
     * Returns a string containing the array of plates in a JSON format.
     * @param    PlateInteger[]    the array of plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Plate[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJO(array));
    }
    
    /*-------------------- Methods for Stack JSON Output ---------------------*/
    
    /**
     * Prints the plate stack in a JSON format.
     * @param    Stack    the plate stack
     * @throws   IOException 
     */
    public void stackToJSON(Stack stack) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJO(stack));
    }
    
    /**
     * Returns a string containing the plate stack in a JSON format.
     * @param    Stack    the plate stack
     * @return                   the JSON formatted stack
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Stack stack) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJO(stack));
    }
    
    /**
     * Prints the collection of plate stacks in a JSON format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @throws   IOException 
     */
    public void stackToJSON(Collection<Stack> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJO(collection));
    }
    
    /**
     * Returns a string containing the collection of plate stacks in a JSON format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @return                               the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Collection<Stack> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJO(collection));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackInteger[]    the array of plate stacks
     * @throws IOException 
     */
    public void stackToJSON(Stack[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJO(array));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackInteger[]    the array of plate stacks
     * @return                     the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Stack[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJO(array));
    } 
    
    /*-------------------- Methods for Result XML Output --------------------*/
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<Well, Integer> map) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	ResultListXML resultList = new ResultListXML(map);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @return                                the XML formatted result
     */
    public String resultToXMLAsString(Map<Well, Integer> map) {
    	ResultListXML resultList = new ResultListXML(map);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @param    String                       the label
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<Well, Integer> map, String label) 
    		throws IOException, ParserConfigurationException, TransformerException {
    	ResultListXML resultList = new ResultListXML(map, label);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @param    String                       the label
     * @return                                the XML formatted result values
     */
    public String resultToXMLAsString(Map<Well, Integer> map, String label) {
    	ResultListXML resultList = new ResultListXML(map, label);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<Well, Integer>> collection) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXML resultList = new ResultListXML(collection);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @return                                 the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<Well, Integer>> collection) {
    	ResultListXML resultList = new ResultListXML(collection);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @param    List<String>                  result labels
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<Well, Integer>> collection, List<String> labels) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXML resultList = new ResultListXML(collection, labels);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @param    List<String>                  result labels
     * @return                                 the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<Well, Integer>> collection, List<String> labels) {
    	ResultListXML resultList = new ResultListXML(collection, labels);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    ResultListXML    the XML result list
     * @throws   IOException 
     */
    private void printXMLResult(ResultListXML resultList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(ResultListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Returns a string containing the result as an XML object.
     * @param    ResultListXML    the XML result list
     * @return                           the XML formatted result list
     */
    private String printXMLResultAsString(ResultListXML resultList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(ResultListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}

    }
    
    /*--------------------- Methods for Well XML Output ----------------------*/
    
    /**
     * Prints the well values in an XML format.
     * @param    Well    the set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(Well well) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXML wellList = new WellListXML(well);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the well values in an XML format.
     * @param    Well    the set
     * @return                  the XML formatted well values
     */
    public String wellToXMLAsString(Well well) {
    	WellListXML wellList = new WellListXML(well);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the collection of well values in an XML format.
     * @param    Collection<WellInteger>    the collection of wells
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void wellToXML(Collection<Well> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXML wellList = new WellListXML(collection);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the collection of well values in an XML format.
     * @param    Collection<WellInteger>    the collection of wells
     * @return                              the XML formatted wells
     */
    public String wellToXMLAsString(Collection<Well> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXML wellList = new WellListXML(collection);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellInteger[]    the array of wells
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(Well[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXML wellList = new WellListXML(array);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellInteger[]    the array of wells
     * @return                    the XML formatted wells
     */
    public String wellToXMLAsString(Well[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXML wellList = new WellListXML(array);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well list as an XML object.
     * @param    WellListXML    the well list
     * @throws   IOException 
     */
    private void printXMLWell(WellListXML wellList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well list as an XML object.
     * @param    WellListXML    the well list
     * @return                         the XML formatted well list
     */
    private String printXMLWellAsString(WellListXML wellList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, writer);
			
			return writer.toString();
		
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    /*-------------------- Methods for Well Set XML Output --------------------*/
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSet    the well set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSet set) throws IOException, 
                                                    ParserConfigurationException, 
                                                    TransformerException {
    	WellSetListXML setList = new WellSetListXML(set);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSet    the well set
     * @return                     the XML formatted well sets
     */
    public String setToXMLAsString(WellSet set) {
    	WellSetListXML setList = new WellSetListXML(set);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the collection of well sets in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void setToXML(Collection<WellSet> collection) throws IOException, 
                                                                       TransformerException, 
                                                                       ParserConfigurationException {
    	WellSetListXML setList = new WellSetListXML(collection);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the collection of well sets in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @return                                 the XML formatted well sets
     */
    public String setToXMLAsString(Collection<WellSet> collection) {
    	WellSetListXML setList = new WellSetListXML(collection);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSetInteger[]    the array of well sets
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSet[] array) throws IOException, 
                                                        ParserConfigurationException, 
                                                        TransformerException {
    	WellSetListXML setList = new WellSetListXML(array);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetInteger[]    the array of well sets
     * @return                       the XML formatted well sets
     */
    public String setToXMLAsString(WellSet[] array) {
    	WellSetListXML setList = new WellSetListXML(array);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    WellSetListXML    the well set list
     * @throws   IOException 
     */
    private void printXMLSet(WellSetListXML setList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellSetListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well set as an XML object.
     * @param    WellSetListXML    the well set list
     * @return                            the XML formatted set list
     */
    private String printXMLSetAsString(WellSetListXML setList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellSetListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    /*-------------------- Methods for Plate XML Output ---------------------*/
    
    /**
     * Prints the plate values in an XML format.
     * @param    Plate    the plate
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(Plate plate) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXML plateList = new PlateListXML(plate);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the plate values in an XML format.
     * @param    Plate    the plate
     * @return                   the XML formatted plates
     */
    public String plateToXMLAsString(Plate plate) {
    	PlateListXML plateList = new PlateListXML(plate);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the collection of plates in an XML format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(Collection<Plate> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXML plateList = new PlateListXML(collection);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the collection of plates in an XML format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @return                               the XML formatted plates 
     */
    public String plateToXMLAsString(Collection<Plate> collection) {
    	PlateListXML plateList = new PlateListXML(collection);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the array of plates in an XML format.
     * @param    PlateInteger[]    the array of plates
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void plateToXML(Plate[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	PlateListXML plateList = new PlateListXML(array);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the array of plates in an XML format.
     * @param    PlateInteger[]    the array of plates
     * @return                     the XML formatted plates
     */
    public String plateToXMLAsString(Plate[] array) {
    	PlateListXML plateList = new PlateListXML(array);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXML    the plate list
     * @throws   IOException 
     */
    private void printXMLPlate(PlateListXML plateList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(PlateListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXML    the plate list
     * @return                          the XML formatted plate list
     */
    private String printXMLPlateAsString(PlateListXML plateList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(PlateListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
        
    }
    
    /*-------------------- Methods for Stack XML Output ---------------------*/
    
    /**
     * Prints the plate stack in an XML format.
     * @param    Stack    the plate stack
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void stackToXML(Stack stack) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	StackListXML stackList = new StackListXML(stack);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the plate stack in an XML format.
     * @param    Stack    the plate stack
     * @return                   the XML formatted stacks 
     */
    public String stackToXMLAsString(Stack stack) {
    	StackListXML stackList = new StackListXML(stack);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the collection of plate stacks in an XML format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(Collection<Stack> collection) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXML stackList = new StackListXML(collection);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the collection of plate stacks in an XML format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @return                               the XML formatted stacks
     */
    public String stackToXMLAsString(Collection<Stack> collection) {
    	StackListXML stackList = new StackListXML(collection);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the array of plate stacks in an XML format.
     * @param    StackInteger[]    the array of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(Stack[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXML stackList = new StackListXML(array);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the array of plate stacks in an XML format.
     * @param    StackInteger[]    the array of plate stacks
     * @return                     the XML formatted stacks
     */
    public String stackToXMLAsString(Stack[] array) {
    	StackListXML stackList = new StackListXML(array);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the stack as an XML object.
     * @param    StackListXML    the stack list
     * @throws   IOException 
     * @throws   TransformerException 
     */
    public void printXMLStack(StackListXML stackList) throws IOException, 
    		TransformerException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(StackListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the stack list as an XML object.
     * @param    StackListXML    the stack list
     * @return                          the XML formatted stack list
     */
    public String printXMLStackAsString(StackListXML stackList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(StackListXML.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    /*-------------------- Methods for Setting Delimiter ---------------------*/
    
    /**
     * Sets the delimiter for the plate writer.
     * @param    String    the delimiter
     */
    public void setDelimiter(String delimiter) {
    	this.delimiter = delimiter;
    }
    
    /**
     * Returns the delimiter.
     * @return    the delimiter
     */
    public String getDelimiter() {
    	return this.delimiter;
    }
    
    /*----------------------- Helper Methods for Output ----------------------*/
    
    /**
     * Returns the row ID.
     * @return    row ID
     */
    private String rowString(int row) {

        String rowString = "";
        
        while (row >=  0) {
            rowString = (char) (row % ALPHA_BASE + 65) + rowString;
            row = (row  / ALPHA_BASE) - 1;
        }
        
        return rowString;
    }
	
    /**
     * Returns the row number for the plate type.
     * @param    int    the plate type
     * @return          the number of plate rows
     */
	private int parseRows(int type) {
		
		switch(type) {
		
		    case Plate.PLATE_6WELL: return Plate.ROWS_6WELL;
		
		    case Plate.PLATE_12WELL: return Plate.ROWS_12WELL;
		    
		    case Plate.PLATE_24WELL: return Plate.ROWS_24WELL;
		    
		    case Plate.PLATE_48WELL: return Plate.ROWS_48WELL;
		    
		    case Plate.PLATE_96WELL: return Plate.ROWS_96WELL;
		    
		    case Plate.PLATE_384WELL: return Plate.ROWS_384WELL;
		    
		    case Plate.PLATE_1536WELL: return Plate.ROWS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}
	
	/**
     * Returns the column number for the plate type.
     * @param    int    the plate type
     * @return          the number of plate columns
     */
    private int parseColumns(int type) {
		
		switch(type) {
		
		    case Plate.PLATE_6WELL: return Plate.COLUMNS_6WELL;
		
		    case Plate.PLATE_12WELL: return Plate.COLUMNS_12WELL;
		    
		    case Plate.PLATE_24WELL: return Plate.COLUMNS_24WELL;
		    
		    case Plate.PLATE_48WELL: return Plate.COLUMNS_48WELL;
		    
		    case Plate.PLATE_96WELL: return Plate.COLUMNS_96WELL;
		    
		    case Plate.PLATE_384WELL: return Plate.COLUMNS_384WELL;
		    
		    case Plate.PLATE_1536WELL: return Plate.COLUMNS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}

}
