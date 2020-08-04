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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.sps.data.Listing;
import com.google.sps.utility.ValidateInput;

/** 
 * Servlet that creates Listings from Entities and returns the list of 
 *    Listings.
 */
@WebServlet("/fetch-listings")
public class FetchListings extends HttpServlet {

  // Based on the lenght of the shortest/longest filter category
  static final int FILTER_MIN = 5;
  static final int FILTER_MAX = 9;
  // Based on the lenght of the shortest/longest radius category 
  static final int RADIUS_MIN = 2;
  static final int RADIUS_MAX = 4;
  // Based on the lenght of the shortest/longest sort category 
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
    String listingTypeFilter;
    try {
      listingTypeFilter = ValidateInput.getUserString(request, 
        "filters", FILTER_MIN, FILTER_MAX, "");
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String listingRadius;
    try {
      listingRadius = ValidateInput.getUserString(request, 
        "radius", RADIUS_MIN, RADIUS_MAX, "recommended");
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String listingSortBy;
    try {
      listingSortBy = ValidateInput.getUserString(request, 
        "sort", SORT_MIN, SORT_MAX, "recommended");
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    // Filter the Listings in the backend
    Query queryListing = new Query("Listing");
    if (listingTypeFilter.length() > 0) {
      FilterPredicate filterListings = new FilterPredicate("type", 
          FilterOperator.EQUAL, listingTypeFilter);
      queryListing = queryListing.setFilter(filterListings);
    }
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedQueryListings = datastore.prepare(queryListing);

    FetchOptions entitiesLimit = FetchOptions.Builder.withLimit(LISTING_LIMIT);
    List<Entity> listingEntities = preparedQueryListings.asList(entitiesLimit);

    // Turn Entities into Listings
    List<Listing> listings = new ArrayList<Listing>();
    for (Entity listingEntity : listingEntities) {
      listings.add(Listing.createListing(listingEntity));
    }

    // Sort the Listings based on sort parameter
    // The sorting algorithm will be given a List<Listing> and will return a 
    //     List<Listing>

    String jsonListings = new Gson().toJson(listings);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListings);
  }
}
