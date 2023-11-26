package com.input;

import com.ErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.input.DataType.*;

/**
 * The Configuration class will be used when provided a Configuration file as an XML file. When provided a file location
 * of the configuration file, the main method of the class, parseConfigFile(), parses the configuration file containing
 * the node representations of dataID and event objects as well as the default output file location as a string,
 * extracts them from the file, and stores them in private attributes that can be access at any time by other parts of
 * the system via getters. The method parseConfigFile() and its helper functions follow section 3.2 of
 * the design document and requirements 10-15, 25, 26, and 32.
 *
 * @author Jeremiah Hockett
 * @editor Leah
 */

public class Configuration
{
    private List<Event> eventList;
    private List<DataID> dataIDList;
    private String defaultOutputLoc;

    public Configuration() {
        this.defaultOutputLoc = null;
        this.dataIDList = new ArrayList<>();
        this.eventList = new ArrayList<>();
    }

    public List<Event> getEventList()
    {
        return eventList;
    }
    public String getDefaultOutputLocation() {
        return defaultOutputLoc;
    }

    public List<DataID> getDataIDList(){
        return dataIDList;
    }


    /**
     * Parse the configuration file, extract the Event and DataID objects, and store then in a Configuration object
     * @param fileLoc - the file location of the configuration file, which is a xml file.
     * @return - a boolean value that is true if parsing was successful or false if otherwise
     */
    public boolean parseConfigFile(URL fileLoc) {
        boolean isEventsParsed;
        HashMap<String, DataID> dataIDMap;

        DocumentBuilderFactory configBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder configBuilder;
        Document configDoc = null;                     // Convert config file to parsable document

        try {
            configBuilder = configBuilderFactory.newDocumentBuilder();
            configDoc = configBuilder.parse(fileLoc.openStream());
        } catch (ParserConfigurationException | IOException | SAXException err){
            if (err instanceof ParserConfigurationException){
                ErrorHandler.logError("Parser failed to instantiate of documentBuilder object");
            } else {
                ErrorHandler.logError("Configuration file not found");
            }
            return false;
        }


        NodeList eventsParentNode = configDoc.getElementsByTagName("events");
        NodeList dataIDsParentNode = configDoc.getElementsByTagName("data_elements");

        // verify the <events> node and the <data_elements> node exist
        if (eventsParentNode.getLength() != 1 || dataIDsParentNode.getLength() != 1)
        {
            ErrorHandler.logError("Failure parsing configuration file: configuration file is invalid." +
                    "\nLoafr exiting...");
            return false;
        }


        // collect parent nodes from NodeLists
        Node eventRootNode = configDoc.getElementsByTagName("events").item(0);
        Node dataIDRootNode = configDoc.getElementsByTagName("data_elements").item(0);

        NodeList eventNodeList;
        NodeList dataIDNodeList;

        if (eventRootNode.getNodeType() == Node.ELEMENT_NODE){
            Element elem = (Element) eventRootNode;
            eventNodeList = elem.getElementsByTagName("event");      // Get events and dataID as nodes
        } else {
            ErrorHandler.logError("Failure parsing configuration file: configuration file is invalid." +
                    "\nLoafr exiting...");
            return false;
        }

        if (dataIDRootNode.getNodeType() == Node.ELEMENT_NODE){
            Element elem = (Element) dataIDRootNode;
            dataIDNodeList = elem.getElementsByTagName("data_element");
        } else {
            ErrorHandler.logError("No node in Config file labeled 'data_elements'");
            return false;
        }

        if (!eventRootNode.hasChildNodes() || !dataIDRootNode.hasChildNodes()){
            ErrorHandler.logError("Failure parsing configuration file: configuration file does not " +
                    "define any events or data types." +
                    "\nLoafr exiting...");
            return false;
        }
        if (!hasSubRootCorrectNodes(eventRootNode, "event")){
            ErrorHandler.logError("Events node has at least one child node that isn't an event node.");
            return false;
        }
        if(!hasSubRootCorrectNodes(dataIDRootNode, "data_element")){
            ErrorHandler.logError("Data_elements node has at least one child node that isn't an data element node.");
            return false;
        }

        dataIDMap = parseDataIDNodes(dataIDNodeList);
        if (null == dataIDMap)
            return false;

        isEventsParsed = parseEventNodes(eventNodeList, dataIDMap);
        if (!isEventsParsed)
            return false;

        NodeList defaultLocNodeList = configDoc.getElementsByTagName("default_output_location");
        if (defaultLocNodeList.getLength() != 1)
        {
            ErrorHandler.logError("Failure parsing configuration file: one default output file location " +
                    "is required.\nLoafr exiting...");
            return false;
        }

        Node defaultLocNode = defaultLocNodeList.item(0);
        Element elemDefaultLoc;
        if (defaultLocNode.getNodeType() == Node.ELEMENT_NODE) {
            elemDefaultLoc = (Element) defaultLocNode;
            defaultOutputLoc = elemDefaultLoc.getAttribute("file").trim();     // Get outputLoc out of node set defaultOutputLoc
            if (defaultOutputLoc.isEmpty()){
                ErrorHandler.logError("No specified default output file location.");
                return false;
            }

        } else {
            ErrorHandler.logError("XML file is structured improperly");
            return false;
        }

        return true;
    }

    /**
     * Convert string object dataType to enumerated DataType
     */
    private DataType inferType(String dataType){
        DataType type;
        switch (dataType) {
            case "integer": type = INTEGER;
                break;
            case "long": type = LONG;
                break;
            case "float": type = FLOAT;
                break;
            case "double": type = DOUBLE;
                break;
            case "boolean": type = BOOLEAN;
                break;
            case "string": type = STRING;
                break;
            case "char": type = CHAR;
                break;
            default:
                return null;
        }
        return type;
    }

    /**
     * Checks to see if a subRoot has correct event or data element nodes.
     * @param subRoot - an events or data_elements node that contains node representations of events or data elements
     * @param tagName - a string used to find the correct nodes
     * @return - a boolean value that is true if nodes are correct (valid nodes equal total amount of nodes) and false
     * if otherwise
     */
    private boolean hasSubRootCorrectNodes(Node subRoot, String tagName){
        int totalNodes = 0;
        int numValidNodes = 0;
        int length = subRoot.getChildNodes().getLength();
        NodeList allChildNodes;
        NodeList validChildNodes;

        if (subRoot.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) subRoot;
            allChildNodes = elem.getChildNodes();
            validChildNodes = elem.getElementsByTagName(tagName);
            for (int i = 0; i < length; i++) {
                if (allChildNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    totalNodes++;
                }
            }
            numValidNodes = validChildNodes.getLength();
            return totalNodes == numValidNodes;
        } else
            return false;
    }


    /**
     * Parse the list of DataID nodes and place them in a map for later in the parseConfigFile() method. If an error was
     * detected in DataIDNodeList, the error will be logged, and the method will return null.
     * @param dataIDNodeList - a list of nodes, where each node contains attributes of a DataID object.
     * @return a HashMap object containing all parsed DataID objects or null if error was detected.
     */
    private HashMap<String, DataID> parseDataIDNodes(NodeList dataIDNodeList){
        Node curNode;
        DataType tempType;
        String dataName;
        HashMap<String, DataID> tempDataIDMap = new HashMap<>();
        Element elemDataID;

        for (int i = 0; i < dataIDNodeList.getLength(); i++){
            curNode = dataIDNodeList.item(i);
            if (curNode.getNodeType() == Node.ELEMENT_NODE) {
                elemDataID = (Element) curNode;

                if (!elemDataID.hasAttribute("name")){                                   // Check if data element has no name
                    ErrorHandler.logError("Name of data element is missing");
                    return null;
                }

                dataName = elemDataID.getAttribute("name").trim();                          // Get dataID object attribute out of node

                tempType = inferType(elemDataID.getAttribute("type").trim());              // Get dataID object attribute out of node

                if (tempType == null){
                    ErrorHandler.logError("Data type is missing");
                    return null;
                }

                DataID newDataID = new DataID(dataName, tempType);

                tempDataIDMap.put(dataName,newDataID);                                          // Place dataID object in dataIDList
                this.dataIDList.add(newDataID);
            } else {
                ErrorHandler.logError("There are missing DataIDs in the configuration file.");
                return null;
            }
        }
        return tempDataIDMap;
    }

    /**
     * Parse all event nodes from a given NodeList of event nodes and a map of DataID objects. This private method must
     * execute after parseDataIDNodes() completes. If an error was detected in eventNodeList, the error will be logged,
     * and the method will return null.
     * @param eventNodeList - a NodeList of event nodes that are the parent of DataID nodes whose data type is elaborated
     *                      in nodes from dataIDMap
     * @param dataIDMap - a HashMap object containing dataID objects used to complete the DataID nodes for each event
     * @return - boolean value that is true if the parsing was successful and false otherwise
     */
    private boolean parseEventNodes(NodeList eventNodeList, HashMap<String, DataID> dataIDMap){
        ArrayList<DataID> curDataIDs;

//        NodeList eventList = eventNodeList.getChildNodes();
        if (eventNodeList.getLength() == 0){
            ErrorHandler.logError("There are no events in the configuration file.");
            return false;
        }
        for (int i = 0; i < eventNodeList.getLength(); i++){
            Node curNode = eventNodeList.item(i);
            NodeList dataIDNodes = curNode.getChildNodes();
            curDataIDs = new ArrayList<>();

            if (curNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elemEvent = (Element) curNode;
                String eventName;

                if (!elemEvent.hasAttribute("name")){
                    ErrorHandler.logError("Event has no appropriate name.");
                    return false;
                }
                eventName = elemEvent.getAttribute("name").trim();

                for (int j = 0; j < dataIDNodes.getLength(); j++){
                    Node dataNode = dataIDNodes.item(j);

                    if (dataNode.getNodeType() == Node.ELEMENT_NODE){
                        Element elemData = (Element) dataNode;

                        if (!elemData.hasAttribute("name")){                                   // Check if data element has no name
                            ErrorHandler.logError("Name of data element is missing in event " + eventName);
                            return false;
                        }
                        String dataName = elemData.getAttribute("name").trim();    // Get Event object attributes out of node and temp hashmap

                        if (!dataIDMap.containsKey(dataName)) {
                            ErrorHandler.logError("Data element is missing.");
                            return false;
                        }
                        curDataIDs.add(dataIDMap.get(dataName));
                    }
                }
                this.eventList.add(new Event(eventName, curDataIDs));
            } else {
                ErrorHandler.logError("There are no events in the configuration file.");
                return false;
            }
        }
        return true;
    }
}


