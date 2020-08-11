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
import com.google.sps.data.User;
import com.google.sps.utility.EntityUtility;
import com.google.sps.utility.AuthenticationUtility;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
      List<Listing> listings, UserService userService) throws Exception {
    List<Listing> sortedListings = new ArrayList<Listing>();
    // If the user is logged in then sort by upvoted listings first
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      Entity userEntity = AuthenticationUtility.getUserByEmail(datastore, 
          userEmail);
    
      // Removes the Listings in the List it returns from listings 
      sortedListings.addAll(sortByUpvotedListings(datastore, listings, 
          userEntity));
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
   * @param entity An Entity that represents a user.
   * @return a List<Listing> that were deemed recommended;
   */
  public static List<Listing> sortByUpvotedListings(DatastoreService datastore,
      List<Listing> listings, Entity entity) throws Exception {
    List<Entity> upvotedListingEntities = EntityUtility.getEntities(
        User.DELIMITER, datastore, entity, "upvotedListingKeys");
    
    // Since this HashSet will not be used by multiple threads, there is no 
    //     need to make it synchronized.
    // Similar users are users the current user shares an upvoted listing with
    HashSet<Entity> similarUsers = new HashSet<Entity>();
    for (Entity upvotedListingEntity : upvotedListingEntities) {
      // Add user to the HashSet and prevent any duplicates
      addEntities(User.DELIMITER, datastore, upvotedListingEntity, similarUsers,
          "upvotedUserKeys");
    }

    List<HashSet<String>> similarUsersUpvotedListingKeyStrings = new 
        ArrayList<HashSet<String>>();
    for (Entity similarUserEntity : similarUsers.iterator()) {
      // Turn the User Entity into their upvoted listing Key Strings
      HashSet<Entity> upvotedListingKeyStrings = new HashSet<Entity>();
      addEntities(User.DELIMITER, datastore, similarUserEntity, 
          upvotedListingKeyStrings, "upvotedUserKeys");
      
      similarUsersUpvotedListingKeyStrings.add(upvotedListingKeyStrings)
    }

    // Sort the list of similar Users upvoted listing key strings
    

    // Iterate through the sorted List and keep the listings that appear in the 
    //     listings parameter. Also, remove the listing that appears in the 
    //     listings parameter.
    // This will also remove duplicates
    List<Listing> recommendedListings = new ArrayList<Listing>();
    for (HashSet<String> listingKeyStrings : similarUsersUpvotedListingKeyStrings) {
      Iterator<Listing> listingsIterator = listings.iterator();
      while (listingsIterator.hasNext()) {
        Listing listing = listingsIterator.next();
        if (listingKeyStrings.contains(listing.getKeyString())) {
          recommendedListings.add(listing);
          listingsIterator.remove();
        }
      }
    }
    return listings;
  }

  /**
   * Returns the recommended score from a User's upvoted listings and another 
   *     User's upvoted listings
   * Recommended Score: 
   *     - Determined based on the number of upvoted listings for User's share
   *
   * @param userUpvotedListings
   */
  public static int getRecommendedScore(List<Entity> userUpvotedListings, 
      HashSet<String> otherUpvotedListings) {
    int recommendedScore = 0; 
    for (Entity upvotedListing : userUpvotedListings) {
      String upvotedListingKeyString = KeyFactory.keyToString(upvotedListing.getKey());
      if (otherUpvotedListings.contains(upvotedListingKeyString)) {
        recommendedScore++;
      }
    }

    return recommendedScore;
  }

  /**
   * TODO
   */
  public static List<Listing> sortByRadiusAndReputation(List<Listing> listings) {
    // TODO
    return listings;
  }
}
