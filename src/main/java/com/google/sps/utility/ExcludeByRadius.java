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
   * Builds complete url from user input origin location and an array of Strings holding 
   *     the locations of all the listings passed into the method.
   *
   * @param userLocation user input location
   * @param listingLocation listings location
   * @return Complete URL for call to API
   */ 
  public static String distanceMatrixJsonURL(String userLocation, List<Listing> listings) {
    String[] listingLocations = new String[listings.size()];
    for (int i = 0; i < listings.size(); i++) {
      listingLocations[i] = listings.get(i).getLocation().replace(" ", "+");
    }
    String baseURL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    userLocation = userLocation.replace(" ", "+");
    String completeURL = baseURL + "origins=" + userLocation + "&destinations=";
    for (int i = 0; i < listingLocations.length; i++) {
      listingLocations[i] = listingLocations[i].replace(" ", "").trim();
      completeURL += listingLocations[i] + "|";
    }
    completeURL += "&departure_time=now&key=" + API_KEY;
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
    //If the radius input is 100+ we change the value to 100,000 km (100 million m) if not multiply input by 1000.

    if (radius == 101) {
      radius = 100000000;
    } else {
      radius *= 1000;
    }
    
    double[] distancesInMeters = distance.getDoubleDistanceValues();
    String[] destinations = distance.getListingAddresses();
    ArrayList<Listing> returnList = new ArrayList<>();
    HashMap<String, Double> locationAndDistance = new HashMap<>();

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
      if (locationAndDistance.containsKey(listings.get(i).getLocation().replace(" ", "").toUpperCase().replaceAll("[0-9]", ""))) {
        System.out.println(listings.get(i).getLocation() + " Should be displayed \n");  
        returnList.add(listings.get(i));
      }
    }
      
    return returnList;
  }
}