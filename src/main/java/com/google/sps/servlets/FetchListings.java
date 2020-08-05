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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.google.sps.data.Listing;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/** 
 * Servlet that creates Listings from Entities and returns the list of 
 *    Listings.
 */
@WebServlet("/fetch-listings")
public class FetchListings extends HttpServlet {

  // Based on the length of the String when no filters are checked (MIN) or 
  //     when all filters are checked and separated by "@" (MAX)
  static final int FILTER_MIN = 0;
  static final int FILTER_MAX = 31;
  // Based on the shortest filter type String length ("other".length = 5)
  static final int FILTER_MIN_LENGTH = 5;
  // Based on the length of the shortest/longest radius category 
  static final int RADIUS_MIN = 2;
  static final int RADIUS_MAX = 4;
  // Based on the length of the shortest/longest sort category 
  static final int SORT_MIN = 10;
  static final int SORT_MAX = 12;

  static final int LISTING_LIMIT = 50;

  /** 
   * Returns JSON which is a List of Listings associated with the user or an 
   *     error message if an exception is caught.
   *
   * @param request which contains data to retrieve listings
   * @param response listings in the form of json (List<Listings>) or an error 
   *     message in the form of JSON
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    // Get the parameters
    String typeFiltersString;
    try {
      typeFiltersString = ValidateInput.getUserString(request, 
        "type-filters", FILTER_MIN, FILTER_MAX);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String radiusFilter;
    try {
      radiusFilter = ValidateInput.getUserString(request, 
        "radius-filter", RADIUS_MIN, RADIUS_MAX, "");
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String sortBy;
    try {
      sortBy = ValidateInput.getUserString(request, 
        "sortBy", SORT_MIN, SORT_MAX, "recommended");
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    // Prepare to fetch entities from the backend
    Query queryListing = new Query("Listing");

    // Add a type filter for the Listings property if there are any filters
    filterQuery(typeFiltersString, "type", queryListing);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedQueryListings = datastore.prepare(queryListing);

    FetchOptions entitiesLimit = FetchOptions.Builder.withLimit(LISTING_LIMIT);
    List<Entity> listingEntities = preparedQueryListings.asList(entitiesLimit);

    // Turn Entities into Listings
    List<Listing> listings = new ArrayList<Listing>();
    for (Entity listingEntity : listingEntities) {
      // Entity listingEntity = listingEntities.get(i);
      listings.add(Listing.createListing(listingEntity));
    }

    // Sort the Listings based on sort parameter
    // The sorting algorithm will be given a List<Listing> and will return a 
    //     List<Listing>

    String jsonListings = new Gson().toJson(listings);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListings);
  }

  /**
   * Apply a filter to a query if there are some filters that have been checked 
   *     opposed to no filters checked or all of the filters being checked.
   * 
   * @param filtersString The string that contains filters separated by an "@"
   * @param property The property of the Entity to filter
   * @param query The query to add a filter to
   */
  private void filterQuery(String filtersString, String property, Query query) {
    int filtersStringLength = filtersString.length();
    boolean someTypeFiltersChecked = filtersStringLength > FILTER_MIN_LENGTH - 1
        && filtersStringLength < FILTER_MAX;
    // If no filters are checked or all of the filters have been checked then 
    //     don't add any filters
    if (someTypeFiltersChecked) {
      String[] typeFilters = filtersString.split("@");

      // If there are more than one filters use a CompositeFilter
      Filter filter;
      if (typeFilters.length > 1) {
        Collection<Filter> filterPredicates = new ArrayList<Filter>();
        for (String typeFilter : typeFilters) {
          filterPredicates.add(new FilterPredicate(property, 
              FilterOperator.EQUAL, typeFilter));
        }

        filter = new CompositeFilter(CompositeFilterOperator.OR,filterPredicates);
      // If there are only one filter use a FilterPredicate
      } else {
        filter = new FilterPredicate("type", 
            FilterOperator.EQUAL, typeFilters[0]);
      }
      query = query.setFilter(filter);
    } 
  }
}
