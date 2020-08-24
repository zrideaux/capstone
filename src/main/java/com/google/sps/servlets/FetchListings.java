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
import com.google.sps.data.DistanceMatrixOBJ;
import com.google.sps.data.Listing;
import com.google.sps.data.FetchListingsData;
import com.google.sps.data.TrackingResponse;
import com.google.sps.utility.ExcludeByRadius;
import com.google.sps.filter.FilterQuery;
import com.google.sps.sort.recommended.RecommendedSort;
import com.google.sps.sort.ReputationSort;
import com.google.sps.utility.ListingConstants;
import com.google.sps.utility.UpdateListingUtility;
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
        "radius-filter", ListingConstants.RADIUS_MIN, 
        ListingConstants.RADIUS_MAX, 10);
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

    int timeToBack;
    try {
      timeToBack = ValidateInput.getParameter(request, "call", 0);
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
    UserService userService = UserServiceFactory.getUserService();
    List<Listing> listings;
    try {
      listings = entitiesToListings(datastore, listingEntities, userService);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }

    // Sort the Listings based on sort parameter
    // The sorting algorithm will be given a List<Listing> and will return a 
    //     List<Listing>
    if (sortBy == 1) {
      try {
        listings = RecommendedSort.sortByRecommended(datastore, listings, 
            userService, userLocation);
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      }
    } else if (sortBy == 2) {
      listings = ReputationSort.sortByReputation(listings);
    } else {
      // TODO call on LeastViewed sorting algorithm
    }

    String formattedUserLocation = "";
     
    if (!userLocation.equals("")) {   
      DistanceMatrixOBJ distance =  ExcludeByRadius.convertJsonToDMObject(
        ExcludeByRadius.distanceMatrixJsonURL(userLocation, listings));
     
      listings = ExcludeByRadius.cutList(listings, distance, radiusFilter);
      formattedUserLocation = distance.getOriginAddress();
    }

    FetchListingsData fetchListingsData = new FetchListingsData(listings, formattedUserLocation);

    TrackingResponse trackingListings = new TrackingResponse(timeToBack, 
        fetchListingsData);

    String jsonTrackingListings = new Gson().toJson(trackingListings);
    response.setContentType("application/json;");
    response.getWriter().println(jsonTrackingListings);
  }

  /**
   * Turns a List of listing Entities to a List of Listings.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param listingEntities a List of Entities to turn into Listings.
   * @param userService used to get a user's email and to determine if the user 
   *     is logged in.
   * @return a List<Listing> created from a List<Entity>.
   */
  public static List<Listing> entitiesToListings(DatastoreService datastore,
      List<Entity> listingEntities, UserService userService) {
    List<Listing> listings = new ArrayList<Listing>();

    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();

      // Determine which listings are owned by the user.
      for (Entity listingEntity : listingEntities) {
        listings.add(Listing.createListing(listingEntity, userEmail));
      }
    } else {
      for (Entity listingEntity : listingEntities) {
        listings.add(Listing.createListing(listingEntity));
      }
    }

    return listings;
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
