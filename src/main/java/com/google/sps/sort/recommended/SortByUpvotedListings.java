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
import com.google.sps.comparator.RecommendedComparator;
import com.google.sps.data.Listing;
import com.google.sps.data.User;
import com.google.sps.utility.EntityUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public final class SortByUpvotedListings {
  /**
   * Returns a List<Listing> that were deemed recommended, and removes the 
   *     Listings in this list from the listings parameter.
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
    HashSet<String> userUpvotedListingKeyStrings = new HashSet<String>();
    EntityUtility.addEntityKeyStrings(datastore, User.DELIMITER,
          entity, userUpvotedListingKeyStrings, "upvotedListingKeys");
    
    // Since this HashSet will not be used by multiple threads, there is no 
    //     need to make it synchronized.
    // Similar users are users the current user shares an upvoted listing with
    HashSet<Entity> similarUsers = new HashSet<Entity>();
    accumulateSimilarUsers(datastore, entity, similarUsers, 
        userUpvotedListingKeyStrings);

    // Turn the User Entity into their upvoted listing Key Strings
    List<HashSet<String>> similarUsersUpvotedListingKeyStrings = new 
        ArrayList<HashSet<String>>();
    getSimilarUsersUpvotedListings(datastore, similarUsers, 
        similarUsersUpvotedListingKeyStrings);

    // Sort the list of non-mutual upvoted listing key strings
    RecommendedComparator recommendedComparator = new RecommendedComparator(
        userUpvotedListingKeyStrings);
    Collections.sort(similarUsersUpvotedListingKeyStrings, 
        recommendedComparator);

    return createRecommendedListings(listings,
        similarUsersUpvotedListingKeyStrings, userUpvotedListingKeyStrings);
  }

  /**
   * Accumulate all of the similar users or users that share an upvoted listing 
   *     with the current user.
   * Modifies the similarUsers param which will contain similar users (this 
   *     does not include the current user) and avoids repetition.
   * 
   * @param datastore The datastore that stores user/listing data.
   * @param entity The current user's Entity.
   * @param similarUsers The HashSet that will accumulate similar users.
   * @param userUpvotedListingKeyStrings A HashSet containing the current 
   *     user's upvoted listing Key Strings.
   */
  private static void accumulateSimilarUsers(DatastoreService datastore, 
      Entity entity, HashSet<Entity> similarUsers, 
      HashSet<String> userUpvotedListingKeyStrings) throws Exception {
    // Similar users are users the current user shares an upvoted listing with
    Iterator<String> upvotedListingKeyStringsIterator = 
        userUpvotedListingKeyStrings.iterator();
    while (upvotedListingKeyStringsIterator.hasNext()) {
      String entityKeyString = upvotedListingKeyStringsIterator.next();
      Entity upvotedListingEntity = EntityUtility.getEntityFromKey(datastore, 
          entityKeyString);

      // Add user to the HashSet and prevent any duplicates
      EntityUtility.addEntities(datastore, User.DELIMITER, upvotedListingEntity,
          similarUsers, "upvotedUserKeys");
    }

    // Removes current user from this HashSet
    similarUsers.remove(entity);
  }

  /**
   * Turn the similarUsers HashSet into a List of the user's upvoted listings 
   *     aka the similarUsersUpvotedListingKeyStrings param
   *
   * @param datastore The datastore that stores user/listing data.
   * @param similarUsers The HashSet that contains users that share an upvoted 
   *     listing with the current user.
   * @param similarUsersUpvotedListingKeyStrings The List containing similar 
   *     users' upvoted listing Key Strings.
   */
  private static void getSimilarUsersUpvotedListings(DatastoreService datastore,
      HashSet<Entity> similarUsers, 
      List<HashSet<String>> similarUsersUpvotedListingKeyStrings) 
      throws Exception {
    Iterator<Entity> similarUsersIterator = similarUsers.iterator();
    while (similarUsersIterator.hasNext()) {
      Entity similarUserEntity = similarUsersIterator.next();

      HashSet<String> upvotedListingKeyStrings = new HashSet<String>();
      // Adds the key strings of non-mutual upvoted listings
      EntityUtility.addEntityKeyStrings(datastore, User.DELIMITER,  
          similarUserEntity, upvotedListingKeyStrings, "upvotedListingKeys");
          
      if (upvotedListingKeyStrings.size() > 0) {
        similarUsersUpvotedListingKeyStrings.add(upvotedListingKeyStrings);
      }
    }
  }

  /**
   * Create the recommended Listings from similarUsersUpvotedListingKeyStrings 
   *     param and remove these Listings from the listings param. 
   * The recommended listings are non-mutual listings that appear in the 
   *     listings param.
   *
   * @param listings A prefiltered List of listings that contains all of the 
   *     listings that will be shown to the user.
   * @param similarUsersUpvotedListingKeyStrings A sorted List containing 
   *     similar users' upvoted listing Key Strings that will be turned into a 
   *     List<Listings> (recommended listings).
   * @param userUpvotedListingKeyStrings A HashSet containing the current 
   *     user's upvoted listing Key Strings.
   */
  private static List<Listing> createRecommendedListings(List<Listing> listings,
      List<HashSet<String>> similarUsersUpvotedListingKeyStrings, 
      HashSet<String> userUpvotedListingKeyStrings) {
    List<Listing> recommendedListings = new ArrayList<Listing>();

    for (HashSet<String> listingKeyStrings : 
        similarUsersUpvotedListingKeyStrings) {
      Iterator<Listing> listingsIterator = listings.iterator();
      while (listingsIterator.hasNext()) {
        Listing listing = listingsIterator.next();
        
        // Only add non-mutual listings that are in the listings param 
        boolean isInListings = listingKeyStrings.contains(listing.getKeyString());
        boolean isInUserUpvotedListings = userUpvotedListingKeyStrings.contains(
          listing.getKeyString());
        if (isInListings && !isInUserUpvotedListings) {
          recommendedListings.add(listing);
          listingsIterator.remove();
        }
      }
    }
    
    return recommendedListings;
  }
}
