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
import java.util.Comparator;
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

    // Generate reputation scores for the listings to be sorted
    for (int i = 0; i < listings.size(); i++) {
      listings.get(i).generateReputationScore();
    }

    // Sort listings by their reputation score
    Collections.sort(listings, (a, b) -> a.reputationScore.compareTo(b.reputationScore));


    // Put the listings into descending order by their score
    Collections.reverse(listings);

    return listings;
  }
}
