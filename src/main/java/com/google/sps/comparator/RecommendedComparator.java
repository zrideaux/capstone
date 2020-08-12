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

package com.google.sps.comparator;

import java.util.Comparator; 
import java.util.HashSet;
import java.util.Iterator;

/** 
   * Used for sorting in descending order of the number of mutual and 
   *     for a tie breaker in descending order of duration.
  */
public class RecommendedComparator implements Comparator<HashSet<String>> {

  private final HashSet<String> userUpvotedListingKeyStrings;
  private final int userUpvotedListingsSize;

  public RecommendedComparator(HashSet<String> userUpvotedListingKeyStrings) {
    this.userUpvotedListingKeyStrings = userUpvotedListingKeyStrings;
    this.userUpvotedListingsSize = userUpvotedListingKeyStrings.size();
  }

  /**
   * Used for sorting in descending order the number of mutal upvoted listings 
   *     a listing has with this user's listings'.
   *
   * @param firstUpvotedListings A HashSet of listing key strings.
   * @param secondUpvotedListings A HashSet of listing key strings.
   * @return an int that states the ordering of these two listings.
   */
  @Override
  public int compare(HashSet<String> firstUpvotedListings, 
      HashSet<String> secondUpvotedListings) {
    int firstScore = getRecommendedScore(firstUpvotedListings);
    int secondScore = getRecommendedScore(secondUpvotedListings);
    return -1 * (firstScore - secondScore);
  }

  /**
   * Returns the recommended score from a User's upvoted listings and another 
   *     User's upvoted listings.
   * Recommended Score: 
   *     - Determined based on the number of upvoted listings both User's share.
   *
   * @param otherUpvotedListings Get the score for this HashSet of Listings
   * @return the recommended score.
   */
  private int getRecommendedScore(HashSet<String> otherUpvotedListings) {
    if (otherUpvotedListings.size() > this.userUpvotedListingsSize) {
      return getRecommendedScore(otherUpvotedListings, 
          this.userUpvotedListingKeyStrings);
    } else {
      return getRecommendedScore(this.userUpvotedListingKeyStrings, 
          otherUpvotedListings);
    }
  }

  /**
   * Returns the recommended score from a User's upvoted listings and another 
   *     User's upvoted listings.
   * Recommended Score: 
   *     - Determined based on the number of upvoted listings both User's share.
   *
   * @param firstUpvotedListings the upvoted listings HashSet to iterate through
   * @param secondUpvotedListings the upvoted listings HashSet to compare to
   * @return the recommended score.
   */
  public static int getRecommendedScore(HashSet<String> firstUpvotedListings, 
      HashSet<String> secondUpvotedListings) {
    int recommendedScore = 0; 
    Iterator<String> firstListingsIterator = firstUpvotedListings.iterator();
    while(firstListingsIterator.hasNext()) {
      String upvotedListingKeyString = firstListingsIterator.next();
      if (secondUpvotedListings.contains(upvotedListingKeyString)) {
        recommendedScore++;
      }
    }

    return recommendedScore;
  }
}
