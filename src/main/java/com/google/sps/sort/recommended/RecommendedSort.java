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

package com.google.sps.sort.recommended;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.sps.data.DistanceMatrixOBJ;
import com.google.sps.data.Listing;
import com.google.sps.data.User;
import com.google.sps.sort.recommended.SortByUpvotedListings;
import com.google.sps.utility.AuthenticationUtility;
import com.google.sps.utility.ExcludeByRadius;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// The alogrithm to sort Listings by Recommended
public final class RecommendedSort {
  /**
   * Sorts the given List<Listing> based on a User's upvoted listings if they 
   *     created an account with us and based on the listings reputation and 
   *     location. 
   * 
   * @param datastore the DatastoreService that connects to the back end.
   * @param listings The List<Listing> to sort. 
   * @return a List<Listing> based on a User's upvoted listings or the listings 
   *     reputation and location.
   */
  public static List<Listing> sortByRecommended(DatastoreService datastore, 
      List<Listing> listings, UserService userService, String userLocation) throws Exception {
    List<Listing> sortedListings = new ArrayList<Listing>();
    // If the user is logged in then sort by upvoted listings first
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      Entity userEntity = AuthenticationUtility.getUserByEmail(datastore, 
          userEmail);

      // Removes the Listings in the List it returns from listings 
      sortedListings.addAll(SortByUpvotedListings.sortByUpvotedListings(
          datastore, listings, userEntity));
    }
    
    // If the unauthenticated user has not entered a location we show them the most recent listings.
    if (userLocation.equals("")) {
      sortedListings.addAll(sortByTime(listings));
    } else {
      sortedListings.addAll(sortByDistanceAndReputation(listings, userLocation));
    }

    return sortedListings;
  }

  /**
   *  Sorts listings based on a weighted sum of their distance score and their 
   *     reputation score.
   *
   *  @param listings List of listings to be sorted
   *  @param userLocation current location of user 
   *  @return list of listings that is sorted by their reputation and distance score 
   */
  public static List<Listing> sortByDistanceAndReputation(List<Listing> listings, String userLocation) 
      throws IOException {

    //Grab all locations for distance calculation  
    String[] locations = new String[listings.size()];
    for (int i = 0; i < listings.size(); i++) {
      locations[i] = listings.get(i).getLocation(); 
    }

    DistanceMatrixOBJ distances =  ExcludeByRadius.convertJsonToDMObject(
        ExcludeByRadius.distanceMatrixJsonURL(userLocation, listings));

    int[] distanceValues = distances.getIntegerDistanceValues();

    for (int i = 0; i < listings.size(); i++) {
      listings.get(i).generateReputationScore();
      listings.get(i).generateDistanceScore(distanceValues[i]);
      listings.get(i).generateReputationAndDistanceScore();
    }

    // Sorts listings in descending order based of their reputation and distance score
    Collections.sort(listings,
      (a, b) -> (-1 * a.reputationAndDistanceScore.compareTo(b.reputationAndDistanceScore))); 
    return listings;
  }
  
  /**
   * Sorts listings based off of how recently they have been posted
   *
   * @param listings list of listings to be sorted.
   * @return List of listings sorted by the time they were created.
   */
  public static List<Listing> sortByTime(List<Listing> listings) {
    Collections.sort(listings,
      (a, b) -> (-1 * a.timestamp.compareTo(b.timestamp)));

    return listings;
  }

}
