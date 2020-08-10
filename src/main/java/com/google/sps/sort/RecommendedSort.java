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

package com.google.sps.sort;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.sps.data.Listing;
import com.google.sps.utility.AuthenticationUtility;
import java.util.ArrayList;
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
      List<Listing> listings, UserService userService) {
    List<Listing> sortedListings = new ArrayList<Listing>();
    // If the user is logged in then sort by upvoted listings first
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      Entity userEntity = AuthenticationUtility.getUserByEmail(datastore, 
          userEmail);
    
      // Removes the Listings in the List it returns from listings 
      sortedListings.addAll(sortByUpvotedListings(listings, userEntity));
    } 
    sortedListings.addAll(sortByRadiusAndReputation(listings));

    return sortedListings;
  }

  /**
   * Returns a List<Listing> that were deemed recommended, and removes the 
   *     Listings in this list from listings.
   * Recommended:
   *     - A listing is deemed recommended if the most compatible User upvoted 
   *       it and is in listings.
   *
   * @param listings The List<Listing> to sort.
   * @param userEntity The Entity of the current user.
   * @return a List<Listing> that were deemed recommended;
   */
  public static List<Listing> sortByUpvotedListings(List<Listing> listings, 
      Entity userEntity) {
    // TODO
    return listings;
  }

  /**
   * TODO
   */
  public static List<Listing> sortByRadiusAndReputation(List<Listing> listings) {
    // TODO
    return listings;
  }
}
