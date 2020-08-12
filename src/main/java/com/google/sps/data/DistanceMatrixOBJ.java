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
package com.google.sps.data; 

import com.google.gson.annotations.*;
import com.google.gson.*;

/** Converts  JSON Distance Matrix Object into Java Object*/
public class DistanceMatrixOBJ {
  @SerializedName("rows")
  private Rows[] row;

  @SerializedName("destination_addresses")
  public String[] destination_addresses;

  @SerializedName("origin_addresses")
  public String[] origin_addresses;
  
  private static class Rows {
    @SerializedName("elements")
    public Elements[] elements;
  }
   
  private static class Elements {
    @SerializedName("distance")
    private Distance distance;
  }

  private static class Distance {
    @SerializedName("text")
    public String text;

    @SerializedName("value")
    public double value;
  }

  /**
   * Returns user input starting location.
   *  
   * @return user input location
   */
  public String getOriginAddress() {
    return origin_addresses[0];
  }

  /**
   * Returns a string array of all the listings' locations
   *
   * @return location of all listings in the object
   */
  public String[] getListingAddresses() {
    return destination_addresses;
  }

  /**
   * Returns an array of String values that show the users distance from the
   *    listings in kilometers.
   *
   * @return String array of values that represent the distance in kilometers 
   *    between user and listings i.e("980km")
   */
  public String[] getStringDistanceValues() {
    String[] listingDistanceTextValues = new String[row[0].elements.length];
    for(int i = 0; i < row[0].elements.length; i++) {
      listingDistanceTextValues[i] = row[0].elements[i].distance.text;
    }
    
    return listingDistanceTextValues;
  }
  
  /**
   * Returns a double array of values that show the users distance from the listing
   *    in meters.
   *
   * @return double array of values that represent the distance in meters 
   *   between user and listings i.e(980000.0)
   */
  public double[] getDoubleDistanceValues() {
    double[] listingDistanceValues = new double[row[0].elements.length];
    for(int i =0; i < row[0].elements.length; i++) {
      listingDistanceValues[i] = row[0].elements[i].distance.value;
    }
    
    return listingDistanceValues;
  }


  /**
   * Returns a Integer array of values that show the users distance from the listing
   *    in meters.
   *
   * @return Integer array of values that represent the distance in meters 
   *   between user and listings i.e(980000.0)
   */
  public int[] getIntegerDistanceValues() {
    int[] listingDistanceValues = new int[row[0].elements.length];
    
    for(int i =0; i < row[0].elements.length; i++) {
      listingDistanceValues[i] = (int) row[0].elements[i].distance.value;
    }

    return listingDistanceValues; 
  }
}
