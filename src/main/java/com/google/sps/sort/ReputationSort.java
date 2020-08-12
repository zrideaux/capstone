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

import com.google.sps.data.Listing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// The alogrithm to sort Listings by Reputation
public final class ReputationSort {
  /**
   * Sorts the given List of Listings by using a reputation score based upon
   * the listings' existing downvotes and upvotes.
   *
   * @param listings The List of Listings to be sort.
   * @return a List of Listings sorted by their reputation score
   */
  public static List<Listing> sortByReputation(List<Listing> listings) {

    // Generate reputation scores for the listings to be sorted
    for (int i = 0; i < listings.size(); i++) {
      listings.get(i).generateReputationScore();
    }

    // Sort listings into descending order by their reputation score
    Collections.sort(listings,
        (a, b) -> (-1 * a.reputationScore.compareTo(b.reputationScore)));

    return listings;
  }
}
