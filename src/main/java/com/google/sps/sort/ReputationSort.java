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
import java.util.Collections;
import java.util.List;

// The alogrithm to sort Listings by Reputation
public final class ReputationSort {
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
  public static List<Listing> sortByReputation(List<Listing> listings) {
    List<Listing> sortedListings = new ArrayList<Listing>();
    
    for (Listing listingToAdd : listings) {
      int sortedSize = sortedListings.size();
      if (sortedSize == 0) {
        sortedListings.add(listingToAdd);
      } else {
        for (int i = 0; i < sortedSize; i++) {
          if (generateReputationScore(listingToAdd) < generateReputationScore(sortedListings.get(i))) {
            sortedListings.add(i, listingToAdd);
            break;
          } else {
            if (i == sortedListings.size() - 1) {
              sortedListings.add(listingToAdd);
            }
          }
        }
      }
    }

    Collections.reverse(sortedListings);

    for (int i = 0; i < sortedListings.size(); i++) {
      System.out.println("Listing " + i + " score: " + generateReputationScore(sortedListings.get(i)));
    }

    return sortedListings;
  }

  /**
   * Generate and return a reputation score from a listing entity)
   * 
   * @param listingEntity the entity of the listing to generate a score for
   * @return int representing a listing's reputationScore
   */
  public static int generateReputationScore(Entity listingEntity) {
    final double WEIGHT_1 = .20;
    final double WEIGHT_2 = .80;
    
    int upvotes = (int) new Long(
        (long) listingEntity.getProperty("upvotes")).intValue();
    int downvotes = (int) new Long(
        (long) listingEntity.getProperty("downvotes")).intValue();
    
    if (upvotes == 0) {
      return 0;
    }

    double upvotePercentage = upvotes / (upvotes + downvotes);

    int reputationScore = (int) ((upvotes * WEIGHT_1) 
        + ((upvotes * upvotePercentage) * WEIGHT_2));

    return reputationScore;
  }

  /**
   * Generate and return a reputation score from a listing entity)
   * 
   * @param listingEntity the entity of the listing to generate a score for
   * @return int representing a listing's reputationScore
   */
  public static int generateReputationScore(Listing listing) {
    final double WEIGHT_1 = .20;
    final double WEIGHT_2 = .80;
    
    int upvotes = (int) listing.getUpvotes();
    int downvotes = (int) listing.getDownvotes();

    if (upvotes == 0) {
      return 0;
    }

    double upvotePercentage = (double) upvotes / (upvotes + downvotes);
    int reputationScore = (int) ((upvotes * WEIGHT_1) 
        + ((upvotes * upvotePercentage) * WEIGHT_2));
    
    System.out.println("upvotes: " + upvotes + " downvotes: " + downvotes + " score: " + reputationScore + " perc: " + upvotePercentage);
    return reputationScore;
  }
}
