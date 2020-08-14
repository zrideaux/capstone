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
import java.util.List;
import java.util.HashMap;
import java.util.Scanner; 

/** 
 * File that takes in a user input origin location and a radius value and calculates the distance between  
 * the user and the listings that match their filter. If the distance between the user and the listing
 * is greater than the radius then the listing will be excluded from the list sent to be sorted.
 */ 
public class ExcludeByRadius {
  //API key used to access the DistanceMatrix api
  private static final String API_KEY = secret.API_KEY;
  
  /**
   * Takes in a list of Listings that have had their filter applied and omits the Listings that are not 
   *   located inside the users requested radius. 
   *  
   * @param listings ArrayList of listings that are compatible with user.
   * @param userLocation input of current users location.
   * @param radius Integer value of radius slected in meters.
   * @return ArrayList of desired listings that are within the radius set by the user.
   */ 
  public static ArrayList<Listing> removeListingsNotInRadius(List<Listing> listings, String userLocation, int radius)
      throws IOException {
    ArrayList<Listing> modifiedList = new ArrayList<>(); 
    
    //Replaces spaces with + to make url valid
    
      
    //If the radius input is 100+ we change the value to 100,000 km (100 million m) if not multiply input by 1000.
    if (radius == 101) {
      radius = 100000000;
    } else {
      radius *= 1000;
    }

    
    //Recieve a list of listings that are compatible with the search filter get their locations.
    String[] listingLocations = new String[listings.size()];
    for (int i = 0; i < listings.size(); i++) {
      listingLocations[i] = listings.get(i).getLocation().replace(" ", "+");
    }

    //Create URL from origin and Locations of other listings   
    String URL = distanceMatrixJsonURL(userLocation, listingLocations);
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
   * @return Complete URL for call to API
   */ 
  public static String distanceMatrixJsonURL(String userLocation, String[] listingLocations) {
    String baseURL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    userLocation = userLocation.replace(" ", "+");
    String completeURL = baseURL+"origins="+userLocation+"&destinations=";
    for (int i = 0; i < listingLocations.length; i++) {
      listingLocations[i] = listingLocations[i].replace(" ", "");
      completeURL += listingLocations[i] + "|";
    }
    completeURL += "&departure_time=now&key=" + API_KEY;

    System.out.println(completeURL);

    return completeURL;
  }

  /**
   * Converts Distance Matrix JSON Object to Java object 
   *  
   * @param JsonObjURL Url for api call
   * @return parsable Java Object
   */ 
  public static DistanceMatrixOBJ convertJsonToDMObject(String JsonObjURL)
    throws IOException {
    String jsonString = "";
    URL url = new URL(JsonObjURL);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
    conn.setRequestMethod("GET");
    conn.connect();
    int responsecode = conn.getResponseCode();
    
    //Checks if connection is a success  
    if (responsecode != 200) {
      throw new RuntimeException("HttpResponseCode: " +responsecode);
    } else {
      //Reads in information from api repsonse  
      Scanner fileReader = new Scanner(url.openStream());
      while(fileReader.hasNext()) {
        jsonString+=fileReader.nextLine();
      }

      fileReader.close();
    
      //Converts json object to gson to be converted into java object
      Gson gson = new Gson();
      DistanceMatrixOBJ distance = gson.fromJson(jsonString, DistanceMatrixOBJ.class);
      
      //For debugging purposes
      double[] distances = distance.getDoubleDistanceValues();
      String[] locations = distance.getListingAddresses();
      String[] text = distance.getStringDistanceValues();
      String origin = distance.getOriginAddress();
      for (int i = 0; i < distances.length; i++) {
        System.out.println("The distance between " + origin + " and " + locations[i] +
          " is "+ text[i] +". That is " + (int)distances[i] +" meters.");
      }
       
      return distance;
    }
  }
   
  /**
   * Cuts list size based on how many of the listings fit in the radius
   *
   * @param listings ArrayList of listings compatible with the radius 
   * @param distance DistanceMatrixOBJ containing all the distances to compare with radius
   * @param radius integer value of radius in meters
   * @return list of listings that are within radius in no particular order
   */
  public static ArrayList<Listing> cutList(List<Listing> listings, DistanceMatrixOBJ distance, int radius) {
    double[] distancesInMeters = distance.getDoubleDistanceValues();
    String[] destinations = distance.getListingAddresses();
    ArrayList<Listing> returnList = new ArrayList<>();
    HashMap<String, Double> locationAndDistance = new HashMap<>();
      
    System.out.println("Radius is "+ radius + " meters");

    for (int i = 0; i < distancesInMeters.length; i++) {
      if (destinations[i].contains(", USA")) {
         destinations[i] = destinations[i].replace(", USA", "");
         destinations[i] = destinations[i].replace(" ", "");
      }
      destinations[i] = destinations[i].toUpperCase();

      if (distancesInMeters[i] <= radius) {
        locationAndDistance.put(destinations[i], distancesInMeters[i]);
      }
    }
        
    for (int i = 0; i < listings.size(); i++) { 
      if (locationAndDistance.containsKey(listings.get(i).getLocation().replace(" ", "").toUpperCase())) {
        System.out.println(listings.get(i).getLocation() + " Should be displayed \n");  
        returnList.add(listings.get(i));
      }
    }
      
    return returnList;
  }
}
