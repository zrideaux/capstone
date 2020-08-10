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

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.sps.utility.ListingConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public final class FilterQuery {

  static final HashMap<String, String> FILTERS = new HashMap<String, String>();

  /**
   * Populate the FILTERS HashMap constant
   */
  private static void initializeFilters() {
    FILTERS.put("1", "fundraiser");
    FILTERS.put("2", "petition");
    FILTERS.put("3", "event");
    FILTERS.put("4", "other");
  }

  /**
   * Apply a filter to a query if some filters have been checked as opposed to 
   *     no filters checked or all filters checked.
   * 
   * @param filtersString The string that contains filters separated by an "@"
   * @param property The property of the Entity to filter
   * @param query The query to add a filter to
   */
  public static void filterQuery(String filtersString, String property, 
      Query query) {
    int filtersStringLength = filtersString.length();

    // If no filters are checked (String length = FILTER_MIN) or all of the 
    //     filters have been checked (FILTER_MAX) then don't add any filters.
    boolean someTypeFiltersChecked = filtersStringLength > 
        ListingConstants.FILTER_MIN && filtersStringLength < 
        ListingConstants.FILTER_MAX;
    if (someTypeFiltersChecked) {
      // Adds values to the FILTERS HasMap constant if there are filters
      initializeFilters();

      String[] typeFilters = filtersString.split("@");

      // If there are more than one filters use a CompositeFilter
      Filter filter;
      if (typeFilters.length > 1) {
        Collection<Filter> filterPredicates = new ArrayList<Filter>();
        for (String typeFilter : typeFilters) {
          filterPredicates.add(new FilterPredicate(property, 
              FilterOperator.EQUAL, FILTERS.get(typeFilter)));
        }

        filter = new CompositeFilter(CompositeFilterOperator.OR,filterPredicates);
      // If there are only one filter use a FilterPredicate
      } else {
        filter = new FilterPredicate("type", 
            FilterOperator.EQUAL, FILTERS.get(typeFilters[0]));
      }
      query = query.setFilter(filter);
    } 
  }  
}