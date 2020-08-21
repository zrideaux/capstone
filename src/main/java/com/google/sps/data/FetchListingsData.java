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

import java.util.List;

/**Holds data needed for fetchListings. Can be edited as more information is needed*/
public class FetchListingsData {
  private List<Listing> listings;
  private String userLocation;

  public FetchListingsData(List<Listing> listings, String userLocation) {
    this.listings = listings;
    
    int commaIndex = userLocation.indexOf(",");
    userLocation = userLocation.replace(", USA", "").replaceAll("[0-9]", "");

    if (moreCommas(userLocation)) {
      this.userLocation = userLocation.substring(commaIndex + 1, userLocation.length());    
    } else {
      this.userLocation = userLocation;
    }

    
  }

  /**
   *  Checks to see if there are 2 or more commas in the string
   *
   * @param input String getting checked
   * @return true if 2 or more commas are present
   */
  private boolean moreCommas(String input) {
    int count = 0;
    
    for (int i  = 0; i < input.length(); i++) {
      if (input.charAt(i) == ',') {
        count++;
      }

      if(count == 2) {
        return true;  
      }
    }

    return false;
  }

}
