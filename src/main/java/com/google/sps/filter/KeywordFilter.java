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

package com.google.sps.filter;

import com.google.appengine.api.datastore.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public final class KeywordFilter {
  /**
   * Filter a list of entites to include only listings whose tags or name
   * match keywords specified in the search bar.
   *
   * @param listingEntities a list of listing entities to filter through
   * @param keywordFiltersString a string of keywords to compare to listings
   * @return a list of entities that has been filtered by keyword
   */
  public static List<Entity> filterByKeyword(List<Entity> listingEntities,
      String keywordFiltersString) {

    List<Entity> updatedListingEntities = new ArrayList<Entity>();
    
    // Make a set of the keywords entered into the search bar
    HashSet<String> keywordFiltersSet = new HashSet<String>(
        Arrays.asList(keywordFiltersString.toLowerCase().split("\\W+")));

    // For each of the remaining entities, check if any tags or words in the
    //    listing name match with the keywords
    for (int i = 0; i < listingEntities.size(); i++) {
      // Make a set of tags and words in the name
      String listingTags = (String) listingEntities.get(i).getProperty("tags");
      String listingName = (String) listingEntities.get(i).getProperty("name");

      HashSet<String> matches = new HashSet<>(
          Arrays.asList(listingTags.toLowerCase().split("\\W+")));
      matches.addAll(Arrays.asList(listingName.toLowerCase().split("\\W+")));

      // Get the intersection of the name/tags and the entered keywords
      System.out.println("Matches before intersection: " + matches.toString());
      matches.retainAll(keywordFiltersSet);
      System.out.println("Intersection: " + matches.toString());

      // If the intersection is not the null set, include the current entity
      if (matches.size() > 0) {
        updatedListingEntities.add(listingEntities.get(i));
      }
    }

    return updatedListingEntities;
  }  
}
