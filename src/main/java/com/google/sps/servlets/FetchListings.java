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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Listing;
import com.google.sps.utility.ExcludeByRadius;
import com.google.sps.filter.FilterQuery;
import com.google.sps.sort.RecommendedSort;
import com.google.sps.sort.ReputationSort;
import com.google.sps.utility.ListingConstants;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

  static final HashMap<String, String> FILTERS = new HashMap<String, String>();


  // Based on the length of the String when no filters are checked (MIN) or 
  //     when all filters are checked and separated by "@" (MAX)
  static final int FILTER_MIN = 0;
  static final int FILTER_MAX = 7;
  // Based on the length of the shortest/longest radius category 
  static final int RADIUS_MIN = 10;
  static final int RADIUS_MAX = 101;
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
        "type-filters", ListingConstants.FILTER_MIN, 
        ListingConstants.FILTER_MAX);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    int radiusFilter;
    try {
      radiusFilter = ValidateInput.getUserNum(request, 
        "radius-filter", RADIUS_MIN, RADIUS_MAX, 10);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    int sortBy;
    try {
      sortBy = ValidateInput.getUserNum(request, 
        "sortBy", ListingConstants.SORT_MIN, ListingConstants.SORT_MAX);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String userLocation;
    try {
      userLocation = ValidateInput.getParameter(request, "location", "");  
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }

    // Prepare to fetch entities from the backend
    Query queryListing = new Query("Listing");

    // Add a type filter for the Listings property if there are any filters
    initializeFilters();
    FilterQuery.filterQuery(typeFiltersString, FILTERS, "type", queryListing);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedQueryListings = datastore.prepare(queryListing);

    FetchOptions entitiesLimit = FetchOptions.Builder.withLimit(
        ListingConstants.LISTING_LIMIT);
    List<Entity> listingEntities = preparedQueryListings.asList(entitiesLimit);

    // Turn Entities into Listings
    List<Listing> listings = new ArrayList<Listing>();
    for (Entity listingEntity : listingEntities) {
      listings.add(Listing.createListing(listingEntity));
    }

    // Sort the Listings based on sort parameter
    // The sorting algorithm will be given a List<Listing> and will return a 
    //     List<Listing>
    if (sortBy == 1) {
      UserService userService = UserServiceFactory.getUserService();
      listings = RecommendedSort.sortByRecommended(datastore, listings, 
          userService);
    } else if (sortBy == 2) {
      listings = ReputationSort.sortByReputation(listings);
    } else {
      // TODO call on LeastViewed sorting algorithm
    }

    if (!userLocation.equals("")) {
      listings = ExcludeByRadius.removeListingsNotInRadius(listings, userLocation, radiusFilter);  
    }
      
    String jsonListings = new Gson().toJson(listings);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListings);
  }

  /**
   * Populate the FILTERS HashMap constant
   */
  private static void initializeFilters() {
    FILTERS.put("1", "fundraiser");
    FILTERS.put("2", "petition");
    FILTERS.put("3", "event");
    FILTERS.put("4", "other");
  }
}
