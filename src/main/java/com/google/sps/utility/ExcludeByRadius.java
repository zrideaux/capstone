// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
 
package com.google.sps.utility;


import com.google.sps.data.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner; 

/** 
 * File that takes in a user input origin location and a radius value and calculates the distance between  
 * the user and the listings that match their filter. If the distance between the user and the listing
 * is greater than the radius then the listing will be excluded from the list sent to be sorted.
 */ 
public class ExcludeByRadius{
  //API key used to access the DistanceMatrix api
  private final String API_KEY = "API_KEY";
  
  /**
   * Takes in a list of Listings that have had their filter applied and omits the Listings that are not 
   *   located inside the users requested radius. 
   * 
   * @return ArrayList  of desired listings that are within the radius set by the user.  
   * @param ArrayList of listings that are compatible with user.
   * @param String input of current users location.
   * @param int Integer value of radius slected in meters.
   */ 
  public ArrayList<Listing> removeListingsNotInRadius(ArrayList<Listing> listings, String userLocation, int radius)
      throws IOException{
    ArrayList<Listing> modifiedList = new ArrayList<>(); 

    //Base url 
    String apiCall="https://maps.googleapis.com/maps/api/distancematrix/json?"; 
    
    //Recieve a list of listings that are compatible with the search filter get their locations.
    String[] listingLocations = new String[listings.size()];
    for(int i = 0; i < listings.size(); i++){
      listingLocations[i] = listings.get(i).getLocation();
    }

    //Create URL from origin and Locations of other listings   
    String URL = distanceMatrixJsonUrl(userLocation, listingLocations, apiCall);
    System.out.println(URL);

    //Convert Json Distance Matrix OBject into Java object.  
    DistanceMatrixOBJ completeObject = convertJsonToDMObject(URL);

    //Cut list based on radius given.
    modifiedList = cutList(listings, completeObject, radius);

    return modifiedList;
  }
  
  /**
   * Builds complete url from user input origin location and an array of Strings holding 
   *     the locations of all the listings passed into the method.
   *
   * @param userLocation user input location
   * @param listingLocation listings location
   * @param baseURL base url of api 
   */ 
  private String distanceMatrixJsonUrl(String userLocation, String[] listingLocations, String baseURL) {
    String completeUrl = baseURL+"origins="+userLocation+"&destinations=";
    for(int i = 0; i < listingLocations.length; i++){
        completeUrl += listingLocations[i] + "|";
    }
    completeUrl += "&departure_time=now&API_KEY=" + API_KEY; 
    return completeUrl;
  }

  /**
   * Converts Distance Matrix JSON Object to Java object 
   *  
   * @param String Url for api call
   * @return parsable Java Object
   */ 
  private DistanceMatrixOBJ convertJsonToDMObject(String JsonObjURL)
    throws IOException {
    String jsonString = "";
    URL url = new URL(JsonObjURL);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
    conn.setRequestMethod("GET");
    conn.connect();
    int responsecode = conn.getResponseCode();
    
    //Checks if connection is a success  
    if(responsecode != 200) {
      throw new RuntimeException("HttpResponseCode: " +responsecode);
    } else {
      //Reads in information from api repsonse  
      Scanner fileReader = new Scanner(url.openStream());
      while(fileReader.hasNext()) {
        jsonString+=fileReader.nextLine();
      }
      System.out.println("\nJSON data in string format");
      System.out.println(jsonString);
      fileReader.close();
    
      //Converts json object to gson to be converted into java object
      Gson gson = new Gson();
      DistanceMatrixOBJ distance = gson.fromJson(jsonString, DistanceMatrixOBJ.class);
      
      //For debugging purposes
      double[] distances = distance.getDoubleDistanceValues();
      String[] locations = distance.getListingAddresses();
      String[] text = distance.getStringDistanceValues();
      String origin = distance.getOriginAddress();
      for(int i = 0; i < distances.length; i++) {
        System.out.println("The distance between " + origin + " and " + locations[i] +
          " is "+ text[i] +". That is " + (int)distances[i] +" meters.");
      }
       
      return distance;
    }
  }
   
  /**
   * Cuts list size based on how many of the listings fit in the radius
   *
   * @param ArrayList<Listings> of listings compatible with the radius 
   * @param DistanceMatrixOBJ containing all the distances to compare with radius
   * @param int value of radius in meters
   * @return list of listings that are within radius in no particular order
   */
  private static ArrayList<Listing> cutList(ArrayList<Listing> listings, DistanceMatrixOBJ distance, int radius) {
    double[] distancesInMeters = distance.getDoubleDistanceValues();
    String[] destinations = distance.getListingAddresses();
    ArrayList<Listing> returnList = new ArrayList<>();
    HashMap<String, Double> locationAndDistance = new HashMap<>();
      
    for(int i = 0; i < distancesInMeters.length; i++) {
      if(distancesInMeters[i] <= radius) {
        locationAndDistance.put(destinations[i], distancesInMeters[i]);
      }
    }
        
    for(Listing listing : listings) {
      if(locationAndDistance.containsKey(listing.getLocation())){
        returnList.add(listing);
      }
    }
      
    return returnList;
<<<<<<< HEAD
  }
=======
   }
>>>>>>> c13a2ccad79466cc0382fae4f08ae05f206726de
}
