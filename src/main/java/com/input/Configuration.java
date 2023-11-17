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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.input.DataType.*;

public class Configuration
{
    private List<Event> eventList;
    private List<DataID> dataIDList;
    private String defaultOutputLoc;

    public Configuration(String defaultOutputLoc)
    {
        this.defaultOutputLoc = defaultOutputLoc;
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
     * @throws ParserConfigurationException - add desc here
     */
    public boolean parseConfigFile(String fileLoc) throws ParserConfigurationException{
        File configFile = new File(fileLoc);
        boolean isEventsParsed;
        HashMap<String, DataID> dataIDMap;

        if (fileLoc == null){
            ErrorHandler.logError("Location of file is null.");
            return false;
        }

        DocumentBuilderFactory configBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder configBuilder = configBuilderFactory.newDocumentBuilder();
        Document configDoc;                     // Convert config file to parsable document


        try {
            configDoc = configBuilder.parse(configFile);
        } catch (IOException | SAXException err){
            ErrorHandler.logError("Configuration file not found");
            return false;
        }

        NodeList eventNameNodeList = configDoc.getElementsByTagName("events");      // Get events and dataID as nodes
        NodeList dataIDNodeList = configDoc.getElementsByTagName("data_elements");

        if (eventNameNodeList.getLength() == 0 || dataIDNodeList.getLength() == 0){
            ErrorHandler.logError("No events or data elements in configuration file.");
            return false;
        }

        dataIDMap = parseDataIDNodes(dataIDNodeList);
        if (dataIDMap == null)
            return false;

        isEventsParsed = parseEventNodes(eventNameNodeList, dataIDMap);
        if (!isEventsParsed)
            return false;


        Node defaultLocNode = configDoc.getElementsByTagName("default_output_location").item(0);
        Element elemDefaultLoc;
        if (defaultLocNode.getNodeType() == defaultLocNode.ELEMENT_NODE) {
            elemDefaultLoc = (Element) defaultLocNode;

            if (elemDefaultLoc.isEqualNode(null)){
                ErrorHandler.logError("Default output log file location is null.");
                return false;
            }

            defaultOutputLoc = elemDefaultLoc.getAttribute("file");     // Get outputLoc out of node set defaultOutputLoc
        } else {
            ErrorHandler.logError("XML file is structured improperly");
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
            if (curNode.getNodeType() == curNode.ELEMENT_NODE) {
                elemDataID = (Element) curNode;

                if (!elemDataID.hasAttribute("name")){                                   // Check if data element has no name
                    ErrorHandler.logError("Name of data element is missing");
                    return null;
                }

                dataName = elemDataID.getAttribute("name");         // Get dataID object attribute out of node

                tempType = inferType(elemDataID.getAttribute("type"));              // Get dataID object attribute out of node

                if (tempType == null){
                    ErrorHandler.logError("Data type is missing");
                    return null;
                }

                tempDataIDMap.put(dataName, new DataID(dataName, tempType));          // Place dataID object in dataIDList
                this.dataIDList.add(new DataID(dataName, tempType));  // Should DataID objects have a attribute for whether it is a list or not
            } else {
                ErrorHandler.logError("XML file is structured improperly");
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
        ArrayList<DataID> curDataIDs = new ArrayList<>();
        for (int i = 0; i < eventNodeList.getLength(); i++){
            Node curNode = eventNodeList.item(i);
            NodeList dataIDNodes = curNode.getChildNodes();
            curDataIDs.clear();

            if (curNode.getNodeType() == curNode.ELEMENT_NODE) {
                Element elemEvent = (Element) curNode;
                String eventName;

                if (!elemEvent.hasAttribute("name")){
                    ErrorHandler.logError("Event has no appropriate name.");
                    return false;
                }
                eventName = elemEvent.getAttribute("name");

                for (int j = 0; j < dataIDNodes.getLength(); j++){
                    Node dataNode = dataIDNodes.item(j);

                    if (dataNode.getNodeType() == curNode.ELEMENT_NODE){
                        Element elemData = (Element) dataNode;

                        if (!elemData.hasAttribute("name")){                                   // Check if data element has no name
                            ErrorHandler.logError("Name of data element is missing");
                            return false;
                        }
                        String dataName = elemData.getAttribute("name");    // Get Event object attributes out of node and temp hashmap

                        if (!dataIDMap.containsKey(dataName)) {
                            ErrorHandler.logError("Data element is missing or incomplete");
                            return false;
                        }
                        curDataIDs.add(dataIDMap.get(dataName));
                    } else {
                        ErrorHandler.logError("XML file is structured improperly");
                        return false;
                    }

                    this.eventList.add(new Event(eventName, curDataIDs));
                }
            } else {
                ErrorHandler.logError("XML file is structured improperly");
                return false;
            }
        }
        return true;
    }
}


